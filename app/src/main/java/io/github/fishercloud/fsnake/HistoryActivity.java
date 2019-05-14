package io.github.fishercloud.fsnake;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.fishercloud.fsnake.model.Score;
import io.github.fishercloud.fsnake.sqlite.DatabaseHelper;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter simple_adapter;
    private SQLiteDatabase database;
    private List<Map<String, Object>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        database = new DatabaseHelper(HistoryActivity.this).getReadableDatabase();
        listView = findViewById(R.id.listView);
        dataList = new ArrayList<Map<String, Object>>();
        refreshScore();
    }

    public void refreshScore() {
        //如果dataList已经有的内容，全部删掉
        //并且更新simp_adapter
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simple_adapter.notifyDataSetChanged();
        }

        //从数据库读取信息
        Cursor cursor = database.query("scores", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Integer score = cursor.getInt(cursor.getColumnIndex("score"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("h_score", "得分："+score);
            map.put("h_date", date);
            dataList.add(map);
        }

        simple_adapter = new SimpleAdapter(this, dataList, R.layout.item,
                new String[]{"h_score", "h_date"}, new int[]{
                R.id.h_score, R.id.h_date});
        listView.setAdapter(simple_adapter);
    }
}
