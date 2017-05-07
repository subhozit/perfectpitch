package com.medias.perfectpitch.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medias.perfectpitch.R;
import com.medias.perfectpitch.utils.UiHelper;

public class OptionsList extends LinearLayout implements View.OnTouchListener {
    private Context context;
    private ColorStateList itemTextColor;
    private TextView header;
    private LinearLayout layoutContainer;
    private AdapterView.OnItemClickListener itemClickListener;
    private int selected;

    public OptionsList(Context context) {
        super(context);
        this.context = context;
    }

    public OptionsList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OptionsList, 0, 0);
        String headerText = a.getString(R.styleable.OptionsList_headerText);
        ColorStateList headerTextColor = a.getColorStateList(R.styleable.OptionsList_headerTextColor);
        CharSequence[] items = a.getTextArray(R.styleable.OptionsList_items);
        itemTextColor = a.getColorStateList(R.styleable.OptionsList_itemTextColor);
        selected = a.getInt(R.styleable.OptionsList_selected, 0);
        a.recycle();

        initialize();

        if (headerText != null)
            header.setText(headerText);
        if (headerTextColor != null)
            header.setTextColor(headerTextColor);

        for (int i = 0; i < items.length; i++) {
            TextView tv = new TextView(context);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(8, 8, 8, 8);
            tv.setText(items[i]);
            tv.setBackgroundResource(R.drawable.sel_background_dark);
            tv.setOnTouchListener(this);
            if (itemTextColor != null)
                tv.setTextColor(itemTextColor);
            layoutContainer.addView(tv);
        }

        setSelected(selected);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        UiHelper.setPressed(v, event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int position = UiHelper.indexInParent(v) - 2;
                itemClickListener.onItemClick(null, this, position, 0);
                return true;
        }
        return false;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSelected(int position) {
        for (int i = 2; i < layoutContainer.getChildCount(); i++) {
            if (i - 2 == position)
                ((TextView) layoutContainer.getChildAt(i)).setTextColor(ContextCompat.getColor(context, R.color.white));
            else
                ((TextView) layoutContainer.getChildAt(i)).setTextColor(itemTextColor);
        }
        invalidate();
    }

    private void initialize() {
        View.inflate(context, R.layout.component_list, this);
        header = (TextView) findViewById(R.id.text_title);
        layoutContainer = (LinearLayout) findViewById(R.id.layout_container);
    }

}
