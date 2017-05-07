package com.medias.perfectpitch.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.medias.perfectpitch.R;

public class Animations {
    private Animations() {

    }

    public static Animation zoomIn(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_zoom_in);
    }

    public static Animation zoomOut(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_zoom_out_large);
    }

    public static Animation toBottom(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_to_bottom);
    }

    public static Animation growShrink(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_zoom_out_large);
    }

    public static Animation rotate(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.rotate);
    }

    public static Animation rotateCopy(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.rotate_copy);
    }

    public static Animation fromLeft(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_from_left);
    }

    public static Animation toRight(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_to_right);
    }

    public static Animation toLeft(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_to_left);
    }

    public static Animation fromRight(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.trns_from_right);
    }

    public static Animation fadeIn(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.fade_in);
    }

    public static Animation fadeOut(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.fade_out);
    }

}
