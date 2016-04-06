package com.example.danil.canvasanimationsample.water;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.example.danil.canvasanimationsample.R;
import com.example.danil.canvasanimationsample.water.views.LiquidDropTextView;
import com.example.danil.canvasanimationsample.water.views.LiquidFrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaterFragment extends Fragment implements View.OnTouchListener {

    @Bind(R.id.scale_top_block)
    LiquidDropTextView topBlock;
    @Bind(R.id.bottom_water_portion_view)
    LiquidFrameLayout waterContainer;
    @Bind(R.id.fall_view)
    View fallView;

    @OnClick(R.id.fab)
    void onFabClick(){
        waterContainer.executeAnimator();
    }
    private int y;
    private boolean isAnimatingNotEnd;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private boolean mInitialized = false;

    public WaterFragment() {
    }

    public static WaterFragment newInstance() {
        WaterFragment fragment = new WaterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.water_fragment, container, false);
        ButterKnife.bind(this, rootView);
        rootView.setOnTouchListener(this);
        return rootView;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        if (isAnimatingNotEnd) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // запоминаем положение начального касания одна переменная для дельты другая постоянная
                y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // пересчитываем размеры вьюх в соотвествии с положением пальца
                int deltaY = ((int) event.getY()) - y;
                y = ((int) event.getY());
                topBlock.setLayoutParams(new FrameLayout.LayoutParams(topBlock.getWidth(),
                        (topBlock.getHeight() + deltaY) < 0 ? 0 : topBlock.getHeight() + deltaY));
                topBlock.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                fallAnimation();
                break;
        }
        return true;
    }

    private void fallAnimation() {
        isAnimatingNotEnd = true;
        fallView.getLayoutParams().height = topBlock.getHeight();
        fallView.invalidate();

        float endPoint = waterContainer.getY() + LiquidFrameLayout.ANIMATION_OFFSET;
        float scale = (float) topBlock.getHeight() / topBlock.getWidth();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleTop = ObjectAnimator.ofFloat(topBlock, View.SCALE_X, scale);
        scaleTop.setDuration(400);
        scaleTop.setInterpolator(new DecelerateInterpolator());
        ValueAnimator animator = ObjectAnimator.ofFloat(fallView, View.TRANSLATION_Y, 0, endPoint);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                topBlock.setScaleX(1);
                topBlock.setLayoutParams(new FrameLayout.LayoutParams(topBlock.getWidth(), 0));
                topBlock.invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimatingNotEnd = false;
                waterContainer.execute(fallView.getHeight());
                fallView.setLayoutParams(new FrameLayout.LayoutParams(fallView.getWidth(), 0));
                fallView.invalidate();
            }
        });
        animatorSet.play(animator).after(scaleTop);
        animatorSet.start();

    }
}