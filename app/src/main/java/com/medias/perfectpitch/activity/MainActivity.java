package com.medias.perfectpitch.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.medias.perfectpitch.R;
import com.medias.perfectpitch.data.Preferences;
import com.medias.perfectpitch.databinding.ActivityMainBinding;
import com.medias.perfectpitch.utils.Animations;
import com.medias.perfectpitch.utils.Typefaces;
import com.medias.perfectpitch.utils.UiHelper;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000;
    private static final int REQUEST_CODE = 1024;

    private ActivityMainBinding binding;
    private Intent googlePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);

        binding.viewAnimator.setInAnimation(Animations.fadeIn(this));
        binding.viewAnimator.setOutAnimation(Animations.fadeOut(this));

        UiHelper.overrideTextFonts(binding.layoutMain, Typefaces.ptSans(this));
        UiHelper.overrideTextFonts(binding.layoutTitle, Typefaces.ptSans(this));

        googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        splash();
    }

    private void splash() {
        binding.imageIcon.startAnimation(Animations.zoomIn(this));
        binding.layoutTitle.startAnimation(Animations.fadeIn(this));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_DURATION);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.viewAnimator.showNext();
                            if (Preferences.getString(getApplicationContext(), Preferences.USER_MAIL).matches(""))
                                startActivityForResult(googlePicker, REQUEST_CODE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Preferences.setString(this, Preferences.USER_MAIL, accountName);
        }
    }

    public void onSynthClicked(View view) {
        startActivity(new Intent(this, InstrumentActivity.class));
    }

    public void onStartClicked(View view) {
        startActivity(new Intent(this, PlayActivity.class));
    }

    public void onExpansionsClicked(View view) {
        startActivity(new Intent(this, ExpansionActivity.class));
    }

}
