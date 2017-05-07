package com.medias.perfectpitch.utils;

import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UiHelper {

    public static boolean inBounds(View v, MotionEvent event) {
        return (event.getX() >= 0 && event.getY() >= 0 && event.getX() <= v.getWidth() && event.getY() <= v.getHeight());
    }

    public static int getFocusedChildIndex(View parent, MotionEvent event) {
        ViewGroup p = (ViewGroup) parent;
        int idx = (int) event.getX() / (p.getWidth() / p.getChildCount());
        return idx < 0 ? 0 : (idx >= p.getChildCount() ? p.getChildCount() - 1 : idx);
    }

    public static View getFocusedChild(View parent, MotionEvent event) {
        ViewGroup p = (ViewGroup) parent;
        return p.getChildAt(getFocusedChildIndex(parent, event));
    }

    public static int indexInParent(View child) {
        ViewGroup p = (ViewGroup) child.getParent();
        for (int idx = 0; idx < p.getChildCount(); idx++) {
            if (child == p.getChildAt(idx))
                return idx;
        }
        return -1;
    }

    public static List<View> getChildren(View parent) {
        List<View> children = new ArrayList<>();
        ViewGroup p = (ViewGroup) parent;
        for (int idx = 0; idx < p.getChildCount(); idx++) {
            children.add(p.getChildAt(idx));
        }
        return children;
    }

    public static void setPressed(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.setPressed(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                view.setPressed(false);
                break;
        }
    }

    public static void overrideTextFonts(View v, Typeface typeface) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideTextFonts(child, typeface);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeface);
            }
        } catch (Exception e) {
        }
    }

    private UiHelper() {

    }
}
