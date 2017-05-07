package com.medias.perfectpitch.activity;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.medias.perfectpitch.R;
import com.medias.perfectpitch.databinding.ActivityPlayBinding;
import com.medias.perfectpitch.observable.Play;
import com.medias.perfectpitch.utils.Animations;
import com.medias.perfectpitch.utils.Typefaces;
import com.medias.perfectpitch.utils.UiHelper;

public class PlayActivity extends AppCompatActivity {
    private Play observable;
    private ActivityPlayBinding binding;
    private InterstitialAd interstitialAd;
    private int[][] noteMap = {{-1, 1, 3, -1, 6, 8, 10}, {0, 2, 4, 5, 7, 9, 11}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trns_from_top, R.anim.trns_to_bottom);

        observable = new Play(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        binding.setObservable(observable);
        binding.setHandler(this);
        binding.progressBar.startAnimation(Animations.rotate(this));

        UiHelper.overrideTextFonts(binding.layoutMain, Typefaces.ptSans(this));
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        observable.setLoading(true);
        binding.imageLoader.startAnimation(Animations.rotate(this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                observable.loadPreferences();
                observable.setLoading(false);
                binding.imageLoader.setAnimation(null);
            }
        }).start();
    }

    public void onNextPressed(View view) {
        observable.nextState();
    }

    public void onSkipPressed(View view) {
        observable.skipLevel();
        binding.imageSymbol.setImageResource(R.drawable.ic_checked);
        binding.imageSymbol.startAnimation(Animations.zoomOut(this));
    }

    public void onReplayPressed(View view) {
        observable.replay();
    }

    public void onAnswerPressed(View view) {
        int id = view.getParent() == binding.layoutOptionsTop ? noteMap[0][UiHelper.indexInParent(view)] : noteMap[1][UiHelper.indexInParent(view)];
        observable.guess(id);

        if (observable.isBreak()) {
            //interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("94CD06DD9E2B819366D02B26D1B3606B").build());
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }

        binding.imageSymbol.setImageResource(observable.isCorrect() ? R.drawable.ic_checked : observable.isIncorrect() ? R.drawable.ic_unchecked : null);
        binding.imageSymbol.startAnimation(Animations.zoomOut(this));
    }
}
