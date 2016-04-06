package com.example.danil.canvasanimationsample.path;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danil.canvasanimationsample.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PathFragment extends Fragment {

    @Bind(R.id.path_view)
    PathView pathView;

    @OnClick(R.id.fab)
    void onFabClick() {
        if (pathView.isStart()) {
            pathView.stop();
        } else {
            pathView.start();
        }
    }

    public PathFragment() {
    }

    public static PathFragment newInstance() {
        PathFragment fragment = new PathFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.path_fragment, container, false);
        ButterKnife.bind(this, rootView);
//        pathView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        return rootView;
    }
}