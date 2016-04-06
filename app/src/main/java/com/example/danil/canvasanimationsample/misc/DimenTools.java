package com.example.danil.canvasanimationsample.misc;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class DimenTools
{
    static public float dpFromPx(Context context, float px)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    static public float pxFromDp(Context context, float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    static public float pxByDensity(Context context, int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

    static public float spFromPx(Context contex, float px)
    {
        return px / contex.getResources().getDisplayMetrics().scaledDensity;
    }

    static public boolean isTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    static public Point displaySize(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    static public int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getActionBarHeight(Context context)
    {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}