package com.medias.perfectpitch.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medias.perfectpitch.R;

public class ContentsViewItem extends RelativeLayout {
    private ImageView image;
    private TextView title;
    private TextView subtitle;
    private RelativeLayout viewBound;

    public ContentsViewItem(Context context) {
        super(context);
        initialize(context);
    }

    public ContentsViewItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentsViewItem, 0, 0);
        String titleText = a.getString(R.styleable.ContentsViewItem_titleText);
        String subtitleText = a.getString(R.styleable.ContentsViewItem_subtitleText);
        ColorStateList titleTextColor = a.getColorStateList(R.styleable.ContentsViewItem_titleTextColor);
        ColorStateList subTitleTextColor = a.getColorStateList(R.styleable.ContentsViewItem_titleTextColor);
        int backgroundResourceId = a.getResourceId(R.styleable.ContentsViewItem_background, R.drawable.sel_background_accent);
        int imageResourceId = a.getResourceId(R.styleable.ContentsViewItem_image, -1);
        a.recycle();

        initialize(context);

        if (titleText != null)
            title.setText(titleText);

        if (subtitleText != null)
            subtitle.setText(subtitleText);

        if (titleTextColor != null)
            title.setTextColor(titleTextColor);
        else
            title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        if (subTitleTextColor != null)
            subtitle.setTextColor(subTitleTextColor);
        else
            subtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));

        image.setBackgroundResource(backgroundResourceId);

        if (imageResourceId != -1)
            image.setImageResource(imageResourceId);
    }

    @Override
    public void setPressed(boolean pressed) {
        image.setPressed(pressed);
        viewBound.setPressed(pressed);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                return true;
            case MotionEvent.ACTION_UP:
                setPressed(false);
                boolean inBounds = (event.getX() >= 0 && event.getY() >= 0 && event.getX() <= this.getWidth() && event.getY() <= this.getHeight());
                if (inBounds)
                    return performClick();
        }
        return false;
    }

    public void setTitleText(String titleText) {
        title.setText(titleText);
        invalidate();
    }

    public void setSubtitleText(String subTitleText) {
        subtitle.setText(subTitleText);
        invalidate();
    }

    public void setImageBackground(int resId) {
        image.setBackgroundResource(resId);
        invalidate();
    }

    public void setImage(int resId) {
        image.setImageResource(resId);
        invalidate();
    }

    private void initialize(Context context) {
        View.inflate(context, R.layout.component_view_item, this);
        image = (ImageView) findViewById(R.id.image_view);
        viewBound = (RelativeLayout) findViewById(R.id.view_bound);
        title = (TextView) findViewById(R.id.text_title);
        subtitle = (TextView) findViewById(R.id.text_subtitle);
    }

}
