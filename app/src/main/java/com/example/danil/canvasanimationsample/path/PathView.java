package com.example.danil.canvasanimationsample.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Danil on 30.03.2016.
 */
public class PathView extends View {

    private static final int LEFT = 0;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int FRAMES_PER_SECOND = 60;
    private static final float DISTANCE_PER_SECOND = 180;


    private boolean isStart;
    private int initPadding = 10;
    private float distance = DISTANCE_PER_SECOND / FRAMES_PER_SECOND;
    private int direction = DOWN;
    private int leftPadding;
    private int rightPadding;
    private int topPadding;
    private int bottomPadding;
    private float x;
    private float y ;

    Path path = new Path();
    Paint paint = new Paint();

    long startTime;

    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        leftPadding = initPadding * 2;
        rightPadding = initPadding;
        topPadding = initPadding;
        bottomPadding = initPadding;
        x = initPadding;
        y = initPadding;

        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
        path.reset();
        path.moveTo(x, y);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
        direction = DOWN;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStart) {
            switch (direction) {
                case LEFT:
                    if (leftPadding > x - distance) {
                        if (leftPadding == x) {
                            y = y + distance;
                            leftPadding = leftPadding + initPadding;
                            direction = DOWN;
                        } else {
                            x = leftPadding;
                        }

                    } else {
                        x = x - distance;
                    }
                    break;
                case UP:
                    if (topPadding > y - distance) {
                        if (topPadding == y) {
                            x = x - distance;
                            topPadding = topPadding + initPadding;
                            direction = LEFT;
                        } else {
                            y = topPadding;
                        }

                    } else {
                        y = y - distance;
                    }
                    break;
                case RIGHT:
                    if (canvas.getWidth() - rightPadding < x + distance) {
                        if (canvas.getWidth() - rightPadding == x) {
                            y = y - distance;
                            rightPadding = rightPadding + initPadding;
                            direction = UP;
                        } else {
                            x = canvas.getWidth() - rightPadding;
                        }

                    } else {
                        x = x + distance;
                    }
                    break;
                case DOWN:
                    if (canvas.getHeight() - bottomPadding < y + distance) {
                        if (canvas.getHeight() - bottomPadding == y) {
                            x = x + distance;
                            bottomPadding = bottomPadding + initPadding;
                            direction = RIGHT;
                        } else {
                            y = canvas.getHeight() - bottomPadding;
                        }

                    } else {
                        y = y + distance;
                    }
                    break;
            }
            path.lineTo(x, y);
            canvas.drawPath(path, paint);
            canvas.save();
            path.moveTo(x, y);

            this.postInvalidateDelayed(1000 / FRAMES_PER_SECOND);
        } else {
            canvas.drawColor(Color.WHITE);
        }
    }

    public void start() {
        isStart = true;
        invalidate();
    }

    public void stop() {
        isStart = false;
        init();
        this.postInvalidateDelayed(0);
    }

    public boolean isStart() {
        return isStart;
    }
}
