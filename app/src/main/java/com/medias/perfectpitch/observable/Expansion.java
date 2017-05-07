package com.medias.perfectpitch.observable;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.medias.perfectpitch.BR;
import com.medias.perfectpitch.R;
import com.medias.perfectpitch.data.Preferences;

public class Expansion extends BaseObservable {

    private Context context;
    private boolean twelveNotesPurchased;
    private boolean tonesPurchased;

    public Expansion(Context context) {
        this.context = context;
        setTwelveNotesPurchased(Preferences.get(context, Preferences.TWELVE_NOTES_PURCHASED) == Preferences.TRUE);
        setTonesPurchased(Preferences.get(context, Preferences.TONES_PURCHASED) == Preferences.TRUE);
    }

    @Bindable
    public String getTwelveNotesSubtitle() {
        return twelveNotesPurchased ? context.getResources().getString(R.string.expansion_installed) :
                context.getResources().getString(R.string.expansion_twelve_notes_info);
    }

    @Bindable
    public String getTonesSubtitle() {
        return tonesPurchased ? context.getResources().getString(R.string.expansion_installed) :
                context.getResources().getString(R.string.expansion_tones_info);
    }

    @Bindable
    public boolean isTwelveNotesPurchased() {
        return twelveNotesPurchased;
    }

    @Bindable
    public boolean isTonesPurchased() {
        return tonesPurchased;
    }

    public String getEmail() {
        return Preferences.getString(context, Preferences.USER_MAIL);
    }

    public void setTwelveNotesPurchased(boolean twelveNotesPurchased) {
        this.twelveNotesPurchased = twelveNotesPurchased;
        if (twelveNotesPurchased) {
            Preferences.set(context, Preferences.TWELVE_NOTES_PURCHASED, Preferences.TRUE);
            notifyPropertyChanged(BR.twelveNotesSubtitle);
        }
    }

    public void setTonesPurchased(boolean instrumentsPurchased) {
        this.tonesPurchased = instrumentsPurchased;
        if (instrumentsPurchased) {
            Preferences.set(context, Preferences.TONES_PURCHASED, Preferences.TRUE);
            notifyPropertyChanged(BR.tonesSubtitle);
        }
    }
}
