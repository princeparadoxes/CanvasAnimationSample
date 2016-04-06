package com.example.danil.canvasanimationsample.labyrinth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.example.danil.canvasanimationsample.R;
import com.example.danil.canvasanimationsample.water.views.LiquidDropTextView;
import com.example.danil.canvasanimationsample.water.views.LiquidFrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LabirinthFragment extends Fragment {

    @Bind(R.id.labirinth_view)
    LabirinthView labirinthView;

    @OnClick(R.id.fab)
    void onFabClick() {
        labirinthView.invalidate();
    }

    public LabirinthFragment() {
    }

    public static LabirinthFragment newInstance() {
        LabirinthFragment fragment = new LabirinthFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.labirinth_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}