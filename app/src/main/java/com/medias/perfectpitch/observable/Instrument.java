package com.medias.perfectpitch.observable;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.medias.perfectpitch.BR;
import com.medias.perfectpitch.R;
import com.medias.perfectpitch.data.Preferences;
import com.medias.perfectpitch.sound.Synth;

public class Instrument extends BaseObservable {
    private Context context;
    private Synth synth;
    private int toneId;
    private boolean loading;
    private int beginId = Synth.MIDDLE_C;
    private boolean[] visibilityMap = new boolean[Synth.LENGTH];

    public Instrument(Context context) {
        this.context = context;
        this.synth = new Synth(context);
        for (int i = Synth.LENGTH - 1; i >= Synth.MIDDLE_C; i--) {
            visibilityMap[i] = true;
        }
    }

    @Bindable
    public String getTone() {
        return context.getResources().getStringArray(R.array.tones)[toneId];
    }

    @Bindable
    public boolean getLoading() {
        return loading;
    }

    public void setTone(int toneId) {
        this.toneId = toneId;
        notifyPropertyChanged(BR.tone);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    public boolean[] getVisibilityMap() {
        return visibilityMap;
    }

    public void strike(int noteId) {
        synth.play(noteId);
    }

    public void release(int noteId) {
        synth.stop(noteId);
    }

    public void shiftUp() {
        if (beginId < Synth.LENGTH - Synth.MIDDLE_C - 1) {
            visibilityMap[beginId++] = false;
            notifyPropertyChanged(BR.visibilityMap);
        }
    }

    public void shiftDown() {
        if (beginId > 0) {
            visibilityMap[--beginId] = true;
            notifyPropertyChanged(BR.visibilityMap);
        }
    }

    public void loadSounds() {
        setTone(Preferences.get(context, Preferences.SYNTH_TONE_ID));
        synth.setTone(toneId);
    }


}
