package com.medias.perfectpitch.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.medias.perfectpitch.R;
import com.medias.perfectpitch.databinding.ActivityInstrumentBinding;
import com.medias.perfectpitch.observable.Instrument;
import com.medias.perfectpitch.utils.Animations;
import com.medias.perfectpitch.utils.Typefaces;
import com.medias.perfectpitch.utils.UiHelper;


public class InstrumentActivity extends AppCompatActivity implements View.OnTouchListener {

    private ActivityInstrumentBinding binding;
    private Instrument observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trns_from_top, R.anim.trns_to_bottom);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_instrument);
        observable = new Instrument(this);
        binding.setObservable(observable);
        binding.setHandler(this);

        UiHelper.overrideTextFonts(binding.layoutMain, Typefaces.ptSans(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        observable.setLoading(true);
        binding.imageLoader.startAnimation(Animations.rotate(this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                observable.loadSounds();
                observable.setLoading(false);
                binding.imageLoader.setAnimation(null);
            }
        }).start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        UiHelper.setPressed(view, event);
        int indexInParent = UiHelper.indexInParent(view);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                observable.strike(indexInParent);
                return true;
            case MotionEvent.ACTION_UP:
                observable.release(indexInParent);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onPlusPressed(View view) {
        observable.shiftUp();
    }

    public void onMinusPressed(View view) {
        observable.shiftDown();
    }
}
