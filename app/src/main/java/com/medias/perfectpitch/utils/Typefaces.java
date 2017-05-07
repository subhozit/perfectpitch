package com.medias.perfectpitch.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Typefaces {

    private static Typeface ptSans;
    private static Typeface ptSansBold;

    private Typefaces() {

    }

    public static Typeface ptSans(Context activity) {
        if (ptSans == null)
            ptSans = Typeface.createFromAsset(activity.getAssets(), "font/PT_Sans-Narrow-Regular.ttf");
        return ptSans;
    }

    public static Typeface ptSansBold(Context activity) {
        if (ptSansBold == null)
            ptSansBold = Typeface.createFromAsset(activity.getAssets(), "font/PT_Sans-Narrow-Bold.ttf");
        return ptSansBold;
    }

}
