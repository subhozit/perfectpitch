package com.medias.perfectpitch.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.medias.perfectpitch.R;
import com.medias.perfectpitch.databinding.ActivitySettingsBinding;
import com.medias.perfectpitch.observable.Settings;
import com.medias.perfectpitch.utils.Animations;
import com.medias.perfectpitch.utils.Typefaces;
import com.medias.perfectpitch.utils.UiHelper;
import com.medias.perfectpitch.widget.OptionsList;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ActivitySettingsBinding binding;
    private Settings observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trns_from_top, R.anim.trns_to_bottom);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        observable = new Settings(this);
        binding.setHandler(this);
        binding.setObservable(observable);
        binding.viewAnimator.setInAnimation(Animations.fromLeft(this));
        binding.viewAnimator.setOutAnimation(Animations.toRight(this));

        UiHelper.overrideTextFonts(binding.viewAnimator, Typefaces.ptSans(this));
    }

    @Override
    public void onBackPressed() {
        if (observable.getDisplayedChild() == Settings.SETTINGS_VIEW) {
            this.finish();
        } else {
            observable.setDisplayedChild(Settings.SETTINGS_VIEW);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionsList optionsList = (OptionsList) view;
        optionsList.setSelected(position);
        if (optionsList == binding.listTones) {
            observable.setSelectedTone(position);
        } else if (optionsList == binding.listNames) {
            observable.setSelectedNaming(position);
        } else if (optionsList == binding.listRelease) {
            observable.setSelectedRelease(position);
        } else if (optionsList == binding.listOrder) {
            observable.setAutoPlayOrder(position);
        }
        binding.viewAnimator.setInAnimation(Animations.fromRight(this));
        binding.viewAnimator.setOutAnimation(Animations.toLeft(this));

        observable.setDisplayedChild(Settings.SETTINGS_VIEW);
    }

    public void onToneClicked(View view) {
        if (observable.isTonesPurchased()) {
            observable.setDisplayedChild(Settings.TONES_VIEW);
        } else {
            startActivity(new Intent(this, ExpansionActivity.class));
        }
    }

    public void onNamesClicked(View view) {
        binding.viewAnimator.setInAnimation(Animations.fromLeft(this));
        binding.viewAnimator.setOutAnimation(Animations.toRight(this));
        observable.setDisplayedChild(Settings.NAMES_VIEW);
    }

    public void onReleaseClicked(View view) {
        binding.viewAnimator.setInAnimation(Animations.fromLeft(this));
        binding.viewAnimator.setOutAnimation(Animations.toRight(this));
        observable.setDisplayedChild(Settings.RELEASE_VIEW);
    }

    public void onOrderClicked(View view) {
        binding.viewAnimator.setInAnimation(Animations.fromLeft(this));
        binding.viewAnimator.setOutAnimation(Animations.toRight(this));
        observable.setDisplayedChild(Settings.ORDER_VIEW);
    }

}

