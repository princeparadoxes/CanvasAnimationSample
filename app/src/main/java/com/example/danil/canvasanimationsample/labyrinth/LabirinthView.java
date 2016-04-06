package com.example.danil.canvasanimationsample.labyrinth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Danil on 29.03.2016.
 */
public class LabirinthView extends View {
    private int iter = 0;
    public float x = 0, y = 0, w = 16, h = 16, innerRadius = 10, outerRadius = 90;
    boolean[] visited = new boolean[(int) (w * h)];
    private ShapeDrawable shapeDrawable;

    public LabirinthView(Context context) {
        super(context);
    }

    public LabirinthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabirinthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        makeShapeDrawable();
        if (shapeDrawable != null) {
            shapeDrawable.draw(canvas);
        }
//        this.mCanvas = mCanvas;
//        noCrash();
    }

    private void makeShapeDrawable() {
        Shape shape = new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                paint.setStrokeWidth(2);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
//                drawX(canvas, paint, 1);
//                drawY(canvas, paint, 1);
                noCrash(canvas, paint);
            }
        };
        shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    public boolean canMove() {
        return this.canMoveX() || this.canMoveY();
    }

    public boolean canMoveX() {
//        int x = this.x;
//        int y = this.y;
//        int w = this.w;
//        int h = this.h;
        boolean[] visited = this.visited;
        int cm1 = (int) (y * w + ((w + x - 1) % w));
        int cp1 = (int) (y * w + ((x + 1) % w));
        return !(visited[cm1] && visited[(cp1)]);
    }

    public boolean canMoveY() {
//        int x = this.x;
//        int y = this.y;
//        int w = this.w;
//        int h = this.h;
//        boolean[] visited = this.visited;
        int c = (int) (y * w + x);
        return !((y == 0 || visited[(int) (c - w)]) && (y == (h - 1) || visited[(int) (c + w)]));
    }

    public boolean drawX(Canvas canvas, Paint paint, int step) {
//        float x = this.x;
//        float y = this.y;
//        float w = this.w;
//        float h = this.h;
//        boolean[] visited = this.visited;
        float new_x = (w + x + step) % w;
        int c = (int) (y * h + new_x);
        if (visited[c]) return false;
        float r_h = (this.outerRadius - this.innerRadius);
        float i_r = this.innerRadius;
        float cur_r = i_r + (y / h) * r_h;
        float x_r = (float) ((x / w) * Math.PI * 2);
        float nx_r = (float) ((new_x / w) * Math.PI * 2);

        Path path = new Path();
        RectF rectF = new RectF(getWidth() / 2 - cur_r, getHeight() / 2 - cur_r,
                getWidth() / 2 + cur_r, getHeight() / 2 + cur_r);
        path.arcTo(rectF, x_r, nx_r, step < 0);
        canvas.drawPath(path, paint);
        this.visited[c] = true;
        this.x = new_x;
//        invalidate();
        return true;
    }

    public boolean drawY(Canvas canvas, Paint paint, int step) {
//        float x = this.x;
//        float y = this.y;
//        float w = this.w;
//        float h = this.h;
//        boolean[] visited = this.visited;
        float new_y = y + step;
        int c = (int) (new_y * h + x);
        if (new_y < 0 || new_y == h || visited[c]) return false;
        float r_h = (this.outerRadius - this.innerRadius);
        float i_r = this.innerRadius;
        float cur_r = i_r + (y / h) * r_h;
        float new_r = i_r + (new_y / h) * r_h;
        int x_r = (int) ((x / w) * Math.PI * 2);
        Path path = new Path();
        float moveToX = (float) Math.cos(x_r) * cur_r + getWidth() / 2;
        float moveToY = (float) Math.sin(x_r) * cur_r + getHeight() / 2;
        float lineToX = (float) Math.cos(x_r) * new_r + getWidth() / 2;
        float lineToY = (float) Math.sin(x_r) * new_r + getHeight() / 2;
        path.moveTo(moveToX, moveToY);
        path.lineTo(lineToX, lineToY);
        canvas.drawPath(path, paint);
        this.visited[c] = true;
        this.y = new_y;
//        invalidate();
        return true;
    }

    private void drawStuff(Canvas c, Paint paint) {
        for (int j = 0; j < 10; j++) {
            if (!canMove()) {
                int nx = (int) (Math.random() * w);
                int ny = (int) (Math.random() * h);
                if (true || !visited[(int) (ny * w + nx)]) {
                    x = nx;
                    y = ny;
                    visited[(int) (ny * w + nx)] = true;
                }
            }
            if (canMoveX() && Math.random() < 0.6) {
                int dir = (Math.random() - 0.5 < 0 ? -1 : 1);
                boolean n = drawX(c, paint, dir) || drawX(c, paint, -dir);
                return;
            }
            if (canMoveY() && Math.random() < 0.3) {
                int dir = (Math.random() - 0.5 < 0 ? -1 : 1);
                if (!drawY(c, paint, dir)) {
                    dir = -dir;
                    drawY(c, paint, dir);
                }
                if (Math.random() > 0.5) drawY(c, paint, dir);
                return;
            }
        }
    }

    private void noCrash(Canvas canvas, Paint paint) {
        iter += 1;
        if (iter > 250) clear(canvas);
        if (iter > 150) return;
        canvas.save();
        canvas.translate(100, 100);
        drawStuff(canvas, paint);
        canvas.restore();
    }

    private void clear(Canvas canvas) {
        iter = 0;
        x = (int) (Math.random() * w);
        visited[(int) x] = true;
        canvas.clipRect(0, 0, 200, 200);
    }

//    var e = document.getElementById("foo")
//    Foo=e
//    Foo.iter=0
//    Foo.ctx=e.getContext("2d")
//    Foo.onclick=
//
//
//    clear
//    clear()

//    e.style.left=document.body.clientWidth/2-100
//    e.style.top=document.body.clientHeight/2-100
//
//    setInterval(noCrash, 16)
//
//    window.onresize=
//
//    function(ev) {
//        e.style.left = document.body.clientWidth / 2 - 100
//        e.style.top = document.body.clientHeight / 2 - 100
//        noCrash()
//    }

}
