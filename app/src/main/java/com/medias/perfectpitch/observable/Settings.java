package com.medias.perfectpitch.observable;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.medias.perfectpitch.BR;
import com.medias.perfectpitch.R;
import com.medias.perfectpitch.activity.InstrumentActivity;
import com.medias.perfectpitch.data.Preferences;
import com.medias.perfectpitch.fragment.NavigationFragment;

public class Settings extends BaseObservable {
    public static final int SETTINGS_VIEW = 0;
    public static final int TONES_VIEW = 1;
    public static final int NAMES_VIEW = 2;
    public static final int RELEASE_VIEW = 3;
    public static final int ORDER_VIEW = 4;

    private Context context;
    private int selectedToneId;
    private int selectedNamingId;
    private int selectedReleaseId;
    private boolean synthSettings;
    private int displayedChild;
    private int selectedAutoPlayOrderId;

    public Settings(Context context) {
        this.context = context;
        setSelectedNaming(Preferences.get(context, Preferences.NOTE_NAMING_ID));
        setSelectedRelease(Preferences.get(context, Preferences.RELEASE_ID));
        setSynthSettings(((Class) ((Activity) context).getIntent().getExtras().get(NavigationFragment.INTENT_SENDER)) == InstrumentActivity.class);
        setSelectedTone(Preferences.get(context, synthSettings ? Preferences.SYNTH_TONE_ID : Preferences.TONE_ID));
        setAutoPlayOrder(Preferences.get(context, Preferences.AUTO_PLAY_ORDER));
        setDisplayedChild(SETTINGS_VIEW);
    }

    @Bindable
    public String getSelectedTone() {
        return context.getResources().getStringArray(R.array.tones)[selectedToneId];
    }

    @Bindable
    public String getSelectedNaming() {
        return context.getResources().getStringArray(R.array.names)[selectedNamingId];
    }

    @Bindable
    public String getSelectedRelease() {
        return context.getResources().getStringArray(R.array.release)[selectedReleaseId];
    }

    @Bindable
    public String getSelectedAutoPlayOrder() {
        return context.getResources().getStringArray(R.array.play_order)[selectedAutoPlayOrderId];
    }

    @Bindable
    public int getSelectedAutoPlayOrderId() {
        return selectedAutoPlayOrderId;
    }

    @Bindable
    public int getSelectedToneId() {
        return selectedToneId;
    }

    @Bindable
    public int getSelectedNamingId() {
        return selectedNamingId;
    }

    @Bindable
    public int getSelectedReleaseId() {
        return selectedReleaseId;
    }

    @Bindable
    public boolean isSynthSettings() {
        return synthSettings;
    }

    @Bindable
    public int getDisplayedChild() {
        return displayedChild;
    }

    public boolean isTonesPurchased() {
        return Preferences.get(context, Preferences.TONES_PURCHASED) == Preferences.TRUE;
    }

    public void setSelectedTone(int selectedToneId) {
        this.selectedToneId = selectedToneId;
        Preferences.set(context, synthSettings ? Preferences.SYNTH_TONE_ID : Preferences.TONE_ID, selectedToneId);
        notifyPropertyChanged(BR.selectedTone);
    }

    public void setSelectedNaming(int selectedNamingId) {
        this.selectedNamingId = selectedNamingId;
        Preferences.set(context, Preferences.NOTE_NAMING_ID, selectedNamingId);
        notifyPropertyChanged(BR.selectedNaming);
    }

    public void setSelectedRelease(int selectedReleaseId) {
        this.selectedReleaseId = selectedReleaseId;
        Preferences.set(context, Preferences.RELEASE_ID, selectedReleaseId);
        notifyPropertyChanged(BR.selectedRelease);
    }

    public void setAutoPlayOrder(int selectedAutoPlayOrderId) {
        this.selectedAutoPlayOrderId = selectedAutoPlayOrderId;
        Preferences.set(context, Preferences.AUTO_PLAY_ORDER, selectedAutoPlayOrderId);
        notifyPropertyChanged(BR.selectedAutoPlayOrder);
    }

    public void setSynthSettings(boolean synthSettings) {
        this.synthSettings = synthSettings;
        notifyPropertyChanged(BR.synthSettings);
    }

    public void setDisplayedChild(int displayedChild) {
        this.displayedChild = displayedChild;
        notifyPropertyChanged(BR.displayedChild);
    }
}
