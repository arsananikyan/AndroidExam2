package com.picsarttraining.androidexam2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Arsen on 06.04.2016.
 */
public class CustomGridView extends View {
    private static final float TOUCH_MOVE_THRESHOLD = 10f;
    private static final long CLICK_DURATION_LIMIT_MS = 200;
    private static final long LONG_CLICK_DURATION_LIMIT_MS = 200;


    private static final int COLUMNS_COUNT = 3;
    private static final int ROWS_COUNT = 3;

    private PointF actionDownPoint;
    private int screenWidth;
    private int screenHeight;
    private Paint gridPaint;
    private Paint circlePaint;
    private Circle selectedCircle;
    private Random random;
    private ArrayList<Circle> circleList;
    boolean isClick = true;

    public CustomGridView(Context context) {
        super(context);

    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circleList = new ArrayList<>();

        gridPaint = new Paint();
        gridPaint.setColor(Color.BLACK);
        gridPaint.setStrokeWidth(3);

        circlePaint = new Paint();
        random = new Random();
        actionDownPoint = new PointF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < COLUMNS_COUNT - 1; i++)
            canvas.drawLine((i + 1) * screenWidth / COLUMNS_COUNT, 0, (i + 1) * screenWidth / COLUMNS_COUNT, screenHeight, gridPaint);
        for (int i = 0; i < ROWS_COUNT - 1; i++)
            canvas.drawLine(0, (i + 1) * screenHeight / ROWS_COUNT, screenWidth, (i + 1) * screenHeight / ROWS_COUNT, gridPaint);

        for (Circle circle : circleList) {
            circlePaint.setColor(circle.color);
            canvas.drawCircle(circle.x, circle.y, circle.radius, circlePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenHeight = getMeasuredHeight();
        screenWidth = getMeasuredWidth();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        final int action = event.getActionMasked();

        boolean handled = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionDownPoint.set(event.getX(), event.getY());
                selectedCircle = findCircleIn(event.getX(), event.getY());
                handled = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                handled = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float dist = (float) Math.hypot(x - actionDownPoint.x, y - actionDownPoint.y);
                if (dist > TOUCH_MOVE_THRESHOLD) {
                    if (selectedCircle != null) {
                        selectedCircle.x = x;
                        selectedCircle.y = y;
                    }
                    isClick = false;
                } else {
                    handled = true;
                    isClick = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getEventTime() - event.getDownTime() <= CLICK_DURATION_LIMIT_MS && isClick) {
                    x = event.getX();
                    y = event.getY();
                    if (selectedCircle != null) {
                        circleList.remove(selectedCircle);
                        break;
                    }
                    addRandomCircleIn(x, y);
                    handled = true;
                } else {
                    handled = false;
                }
                break;

            default:
                break;
        }

        invalidate();
        return handled;
    }

    private void addRandomCircleIn(float x, float y) {
        int randomColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        float randomRadius = (float) random.nextInt(50) + 50;
        circleList.add(new Circle(x, y, randomRadius, randomColor));
    }

    private Circle findCircleIn(float x, float y) {
        //We need to find Circle with the biggest z-index
        for (int i=circleList.size()-1;i>=0;i--) {
            if (Math.hypot(circleList.get(i).x - x, circleList.get(i).y - y) < circleList.get(i).radius) {
                return circleList.get(i);
            }
        }
        return null;
    }

    public static class Circle {
        private float x;
        private float y;
        private int color;
        private float radius;

        public Circle(float x, float y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }

    public static class Image
    {
        private float x;
        private float y;
        private Bitmap bmp;
        public Image(float x, float y, Bitmap bmp)
        {
            this.x = x;
            this.y = y;
            this.bmp = bmp;
        }
    }
}
