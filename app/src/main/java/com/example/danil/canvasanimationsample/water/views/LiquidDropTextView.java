package com.example.danil.canvasanimationsample.water.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.TextView;

public class LiquidDropTextView extends TextView {
    private ShapeDrawable mShapeDrawable;
    private Path mPath = new Path();

    public LiquidDropTextView(Context context) {
        super(context);
    }

    public LiquidDropTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        makeShapeDrawable();
        if (mShapeDrawable != null) {
            mShapeDrawable.draw(canvas);
        }
    }

    private void makeShapeDrawable() {
        Shape shape = new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(getResources().getColor(android.R.color.white));
                mPath.reset();
                mPath.moveTo(-5, getHeight() + 5);
                mPath.lineTo(-5, 0);
                mPath.addArc(new RectF(-5, -100, getWidth() + 5, getHeight()), 180, -180);
                mPath.lineTo(getWidth() + 5, getHeight() + 5);
                mPath.lineTo(0.0f, getHeight() + 5);
                mPath.close();
                canvas.drawPath(mPath, paint);
            }
        };
        mShapeDrawable = new ShapeDrawable(shape);
        mShapeDrawable.setBounds(0, 0, getWidth(), getHeight());
    }
}