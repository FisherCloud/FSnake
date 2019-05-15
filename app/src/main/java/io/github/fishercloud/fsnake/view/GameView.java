package io.github.fishercloud.fsnake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Random;

import io.github.fishercloud.fsnake.Control;
import io.github.fishercloud.fsnake.model.Food;
import io.github.fishercloud.fsnake.model.Grid;
import io.github.fishercloud.fsnake.model.Point;
import io.github.fishercloud.fsnake.model.Snake;
import io.github.fishercloud.fsnake.utils.LogUtil;

/**
 * 游戏的主界面
 */

public class GameView extends View {
    private boolean isFailed = false;
    private Paint paint = new Paint();

    private Grid grid;
    private Snake snake;
    private Food food;
    private int count = 0;

    public int getCount() {
        return count;
    }

    private Control control = Control.UP;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        grid = new Grid();//创建格子对象，画格子时候使用
        snake = new Snake();//创建一个蛇对象。这时候蛇对象是空的，我们需要初始化一个值
        food = new Food(grid.getGridSize() / 2, grid.getGridSize() / 2);//创建一个食物对象
        Point Point = new Point(grid.getGridSize() / 2, grid.getGridSize() / 2);
        snake.getSnake().add(Point);//定义一个中心点 ，添加到蛇身上
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFailed) {
            paint.setTextSize(50);
            paint.setColor(Color.BLACK);
            canvas.drawText("游戏结束", (getRootView().getWidth() >> 1) - 150, (getRootView().getHeight() >> 1) - 100, paint);
            canvas.drawText("您一共吃到了" + count + "个食物", (getRootView().getWidth() >> 1) - 200, getRootView().getHeight() >> 1, paint);
            LogUtil.d("游戏结束");
            return;
        }
        if (grid != null) {
            paint.setColor(Color.RED);
            drawGrid(canvas);
        }
        if (snake != null) {
            paint.setColor(Color.GREEN);
            drawSnake(canvas);
        }
        if (food != null) {
            paint.setColor(Color.RED);
            drawFood(canvas);
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("得分:" + count, (getRootView().getWidth() >> 1) - 150, (getRootView().getHeight() >> 1) + 100, paint);
    }

    private void drawSnake(Canvas canvas) {
        List<Point> snakes = snake.getSnake();
        for (Point point : snakes) {
            int startX = grid.getOffset() + grid.getGridWidth() * point.getX();
            int stopX = startX + grid.getGridWidth();
            int startY = grid.getOffset() + grid.getGridWidth() * point.getY();
            int stopY = startY + +grid.getGridWidth();
            canvas.drawRect(startX, startY, stopX, stopY, paint);
        }
    }

    private void drawFood(Canvas canvas) {
        int x = grid.getOffset() + grid.getGridWidth() * food.getX();
        int x2 = grid.getGridWidth() + x;
        int y = grid.getOffset() + grid.getGridWidth() * food.getY();
        int y2 = grid.getGridWidth() + y;
        canvas.drawRect(x, y, x2, y2, paint);
    }

    private void drawGrid(Canvas canvas) {
        //画竖线
        for (int i = 0; i <= grid.getGridSize(); i++) {
            int startX = grid.getOffset() + grid.getGridWidth() * i;
            int stopX = startX;
            int startY = grid.getOffset();//+grid.getGridWidth() * i
            int stopY = startY + grid.getLineLength();//
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
        //画横线
        for (int i = 0; i <= grid.getGridSize(); i++) {
            int startX = grid.getOffset();//+grid.getGridWidth() * i
            int stopX = startX + grid.getLineLength();

            int startY = grid.getOffset() + grid.getGridWidth() * i;
            int stopY = startY;
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    /**
     * 返回值判断程序输赢
     *
     * @return
     */
    public boolean refreshView() {
        boolean isAdd = false;
        List<Point> pointList = snake.getSnake();
        Point point = pointList.get(0);
        LogUtil.i("point = " + point);
        Point pointNew = null;
        if (control == Control.LEFT) {
            pointNew = new Point(point.getX() - 1, point.getY());
        } else if (control == Control.RIGHT) {
            pointNew = new Point(point.getX() + 1, point.getY());
        } else if (control == Control.UP) {
            pointNew = new Point(point.getX(), point.getY() - 1);
        } else if (control == Control.DOWN) {
            pointNew = new Point(point.getX(), point.getY() + 1);
        }

        if (isEat(point)) {
            Random random = new Random();
            food = new Food(random.nextInt(grid.getGridSize()), random.nextInt(grid.getGridSize()));
            isAdd = true;
            count++;
        } else {
            isAdd = false;
        }

        if (pointNew != null) {
            pointList.add(0, pointNew);
            if (!isAdd) {
                pointList.remove(pointList.get(pointList.size() - 1));
            }
        }

        if (isFailed(point)) {
            isFailed = true;
            invalidate(); // 触发视图的绘制刷新
            return true;
        }
        invalidate();
        //此处只是刷新页面
        //刷新页面会重新绘制
        return false;
    }

    private boolean isEat(Point point) {
        LogUtil.i("point.get(x) = " + point.getX());
        if (point.getX() == food.getX() && point.getY() == food.getY()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFailed(Point point) {
        LogUtil.i("point.getX() = " + point.getX());
        if (point.getY() == 0 && control == Control.UP) {
            return true;
        } else if (point.getX() == 0 && control == Control.LEFT) {
            return true;
        } else if (point.getY() == grid.getGridSize() - 1 && control == Control.DOWN) {
            return true;
        } else if (point.getX() == grid.getGridSize() - 1 && control == Control.RIGHT) {
            return true;
        }
        return false;
    }


    int x;
    int y;

    @Override
    public boolean onTouchEvent(MotionEvent event) { //通过手势来改变蛇体运动方向
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        LogUtil.e("x =" + x + " y = " + y + " action() = " + action);
        // TODO Auto-generated method stub
        if (action == KeyEvent.ACTION_DOWN) {
            //每次Down事件，都置为Null
            x = (int) (event.getX());
            y = (int) (event.getY());
        }
        if (action == KeyEvent.ACTION_UP) {
            //每次Down事件，都置为Null
            int x = (int) (event.getX());
            int y = (int) (event.getY());

//            新建一个滑动方向，
            Control control = null;
            // 滑动方向x轴大说明滑动方向为 左右
            if (Math.abs(x - this.x) > Math.abs(y - this.y)) {
                if (x > this.x) {
                    control = Control.RIGHT;
                    LogUtil.i("右划");
                }
                if (x < this.x) {
                    control = Control.LEFT;
                    LogUtil.i("左划");
                }
            } else {
                if (y < this.y) {
                    control = Control.UP;
                    LogUtil.i("上划");
                }
                if (y > this.y) {
                    control = Control.DOWN;
                    LogUtil.i("下划");
                }
            }

            if (this.control == Control.UP || this.control == Control.DOWN) {
                if (control == Control.UP || Control.UP == Control.DOWN) {
                    LogUtil.i("已经是上下移动了，滑动无效");
                } else {
                    this.control = control;
                }
            } else if (this.control == Control.LEFT || this.control == Control.RIGHT) {
                if (control == Control.LEFT || Control.UP == Control.RIGHT) {
                    LogUtil.i("已经是左右移动了，滑动无效");
                } else {
                    this.control = control;
                }
            }
        }
        //Log.e(TAG, "after adjust mSnakeDirection = " + mSnakeDirection);
        return super.onTouchEvent(event);
    }
}
