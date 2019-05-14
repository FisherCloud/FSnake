package io.github.fishercloud.fsnake;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;

import io.github.fishercloud.fsnake.model.Score;
import io.github.fishercloud.fsnake.sqlite.DatabaseHelper;
import io.github.fishercloud.fsnake.utils.LogUtil;
import io.github.fishercloud.fsnake.view.GameView;

public class MainActivity extends AppCompatActivity {
    private static final int WHAT_REFRESH = 200;
    private int time = 200;// 每次刷新时间间隔

    private int count = 0;
    private GameView gameView;
    private Score score;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (WHAT_REFRESH == msg.what) {
                count++;
                if (count % 20 == 0) {
                    if (time - 10 > 0) {
                        time = time - 10;
                    }
                }
                boolean isFailed = gameView.refreshView();
                if (!isFailed) {//没有结束继续发线程
                    sendControlMessage();
                } else {
                    // 结束告诉程序你输了
                    Toast.makeText(MainActivity.this, "游戏结束！您一共吃到了" + gameView.getCount() + "个食物", Toast.LENGTH_SHORT).show();
                    SQLiteDatabase database = new DatabaseHelper(MainActivity.this).getReadableDatabase();
                    score = new Score(gameView.getCount(), new Date().toLocaleString());
                    ContentValues values = new ContentValues();
                    values.put("score", score.getScore());
                    values.put("date", score.getDate());
                    database.insert("scores", null, values);
                    database.close();
                    LogUtil.d(score);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        gameView.setClickable(true);
        setContentView(gameView);
        sendControlMessage();
    }

    private void sendControlMessage() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WHAT_REFRESH);
            }
        }, time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true; // true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
    }

    /**
     * 菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.history_item:
                Toast.makeText(this, "历史记录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
}