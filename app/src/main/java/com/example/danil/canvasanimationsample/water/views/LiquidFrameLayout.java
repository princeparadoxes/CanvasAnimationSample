package com.example.danil.canvasanimationsample.water.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.danil.canvasanimationsample.misc.DimenTools;

import java.util.ArrayList;
import java.util.Random;

public class LiquidFrameLayout extends FrameLayout {
    public static final int ANIMATION_OFFSET = 100;
    public static final int COUNT_REPEATS = 12;
    public static final int COUNT_FRAMES = 40;
    public static final int HALF_COUNT_FRAMES = COUNT_FRAMES / 2;
    public static final int ANIMATION_TIME = HALF_COUNT_FRAMES * 3;
    private ShapeDrawable shapeDrawable;
    private Path path = new Path();
    private ArrayList<Float> initialValues;
    private ArrayList<Float> values;
    private ArrayList<Float> tempArray;
    private FrameLayout mainView;
    private Handler updateHandler;
    private boolean isAnimated = false;
    private boolean isLastIteration = false;
    private static final int COUNT_SECTORS = 9;
    int repeat = 1;

    public LiquidFrameLayout(Context context) {
        super(context);
        setWillNotDraw(false);
        prepareWater();
    }

    public LiquidFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        prepareWater();
    }

    private void prepareWater() {
        mainView = this;
        initialValues = new ArrayList<>();
        values = new ArrayList<>();
        tempArray = new ArrayList<>();
        for (int i = 0; i < COUNT_SECTORS; i++) {
            initialValues.add(0f);
            values.add(0f);
            tempArray.add(0f);
        }
        updateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mainView.invalidate();
            }
        };

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        makeShapeDrawable();
        if (shapeDrawable != null) {
            shapeDrawable.draw(canvas);
        }
    }

    private void makeShapeDrawable() {
        Shape shape = new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(getResources().getColor(android.R.color.white));
                path.reset();
                path.moveTo(-5, values.get(0) + ANIMATION_OFFSET);
                float width = (getWidth() / COUNT_SECTORS) * 1.5f;
                PointF point;
                for (int i = 0; i < COUNT_SECTORS - 1; i++) {
                    point = new PointF(width * i, values.get(i));
                    PointF next = new PointF(width * (i + 1), values.get(i + 1));
                    path.quadTo(point.x, point.y + 100, (next.x + point.x) / 2, (point.y + next.y) / 2 + ANIMATION_OFFSET);
                }
                path.lineTo(getWidth(), -5);
                path.lineTo(0, -5);
                path.lineTo(0.0f, values.get(0));
                canvas.drawPath(path, paint);
            }
        };
        shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    public void execute(int height) {
        final float dp = DimenTools.dpFromPx(getContext(), height);
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (!isAnimated) {
                    isAnimated = true;
                    isLastIteration = false;
                    for (int i = 0; i < COUNT_SECTORS; i++) {
                        values.set(i, 0f);
                        tempArray.set(i, 0f);
                    }
                    float multiplier = (float) (0.05 * dp);
                    multiplier = multiplier > 10 ? 10 : multiplier;
//                    multiplier = multiplier < 2 ? 2 : multiplier;
                    initialValues.set(0, (float) (new Random().nextInt(1) - 1) * multiplier);
                    initialValues.set(1, (float) (new Random().nextInt(2) + 4) * multiplier);
                    initialValues.set(2, (float) (new Random().nextInt(1) - 3) * multiplier);
                    initialValues.set(3, (float) (new Random().nextInt(2) - 8) * multiplier);
                    initialValues.set(4, (float) (new Random().nextInt(1) - 3) * multiplier);
                    initialValues.set(5, (float) (new Random().nextInt(2) + 4) * multiplier);
                    initialValues.set(7, (float) (new Random().nextInt(1) - 1) * multiplier);
                    for (int k = 0; k < 6; k++) {
                        if (k != 0) {
                            for (int i = 0; i < 20; i++) {
                                for (int j = 0; j < initialValues.size(); j++) {
                                    values.set(j, values.get(j) - tempArray.get(j) / 20);
                                }
                                updateHandler.sendEmptyMessage(0);
                                try {
                                    Thread.sleep(3);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        for (int i = 0; i < 20; i++) {
                            for (int j = 0; j < initialValues.size(); j++) {
                                if (!isLastIteration) {
                                    if (k != 5) {
                                        values.set(j, values.get(j) + initialValues.get(j) / 20);
                                    }
                                }
                            }
                            updateHandler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(3);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        for (int j = 0; j < initialValues.size(); j++) {
                            tempArray.set(j, initialValues.get(j));
                            initialValues.set(j, ((initialValues.get(j) * -1) / 4) * ((4 - k) < 0 ? 0 : (4 - k)));
                        }
                    }
                    isAnimated = false;
                }
            }
        };
        thread.start();
    }

    public void executeAnimator() {
        isLastIteration = false;
        repeat = 0;
        for (int i = 0; i < COUNT_SECTORS; i++) {
            values.set(i, 0f);
            tempArray.set(i, 0f);
        }
        initialValues.set(0, (float) (new Random().nextInt(1) - 1) * 10);
        initialValues.set(1, (float) (new Random().nextInt(2) + 4) * 10);
        initialValues.set(2, (float) (new Random().nextInt(1) - 3) * 10);
        initialValues.set(3, (float) (new Random().nextInt(2) - 8) * 10);
        initialValues.set(4, (float) (new Random().nextInt(1) - 3) * 10);
        initialValues.set(5, (float) (new Random().nextInt(2) + 4) * 10);
        initialValues.set(7, (float) (new Random().nextInt(1) - 1) * 10);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setRepeatCount(COUNT_REPEATS * 2);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                repeat++;
                for (int j = 0; j < initialValues.size(); j++) {
                    tempArray.set(j, initialValues.get(j));
                    initialValues.set(j, ((initialValues.get(j) * -1) / 4) * ((4 - repeat) < 0 ? 0 : (4 - repeat)));
                }
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentFraction = animation.getAnimatedFraction();
                if (repeat % 2 != 0 && repeat != 0) {
                    for (int j = 0; j < initialValues.size(); j++) {
                        values.set(j, tempArray.get(j) * currentFraction);
                    }
                    invalidate();
                }
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentFraction = animation.getAnimatedFraction();
                if (repeat % 2 == 0 && repeat != COUNT_REPEATS - 1) {
                    for (int j = 0; j < initialValues.size(); j++) {
                        values.set(j, tempArray.get(j) * currentFraction);
                    }
                    invalidate();
                }
            }
        });
        animator.start();
    }
}