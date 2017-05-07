package com.medias.perfectpitch.observable;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.SparseIntArray;

import com.medias.perfectpitch.BR;
import com.medias.perfectpitch.R;
import com.medias.perfectpitch.data.Preferences;
import com.medias.perfectpitch.sound.Synth;

import java.util.Random;

public class Play extends BaseObservable {
    private static final int AD_INTERVAL = 100;
    private static final int DEMO_LIMIT = 4;
    private static final int FULL_LIMIT = 12;
    private static final int OCTAVE_MID = 1;
    private static final int MAX_LEVEL_SCORE = 200;

    private Context context;
    private Synth synth;
    private PlayStates state;
    private Random random = new Random();
    private SparseIntArray sequence = new SparseIntArray();

    private boolean loading;
    private String message;
    private boolean[] activeMap = new boolean[12];
    private int namesId;
    private boolean repeated;
    private int score;
    private int release;
    private boolean result;
    private int randomNoteId;
    private int limit;
    private int maxScore;
    private int unlockedMax;
    private Synth.Sequence direction;

    public Play(Context context) {
        this.context = context;
        synth = new Synth(context);
        sequence.append(unlockedMax, release);
        activeMap[unlockedMax] = true;
        this.state = PlayStates.BEGIN;
        updateState();
    }

    public void loadPreferences() {
        limit = Preferences.get(context, Preferences.TWELVE_NOTES_PURCHASED) == Preferences.TRUE ? FULL_LIMIT : DEMO_LIMIT;
        direction = Preferences.get(context, Preferences.AUTO_PLAY_ORDER) == 0 ? Synth.Sequence.FORWARD : Synth.Sequence.BACKWARD;
        maxScore = MAX_LEVEL_SCORE * (limit - 1);

        release = context.getResources().getIntArray(R.array.release_values)[Preferences.get(context, Preferences.RELEASE_ID)];
        for (int i = 0; i < sequence.size(); i++) {
            sequence.put(sequence.keyAt(i), release);
        }

        synth.setTone(Preferences.get(context, Preferences.TONE_ID));
        setName(Preferences.get(context, Preferences.NOTE_NAMING_ID));
    }

    @Bindable
    public boolean isCorrect() {
        return state == PlayStates.COMPLETE || state == PlayStates.CORRECT;
    }

    @Bindable
    public boolean isIncorrect() {
        return state == PlayStates.INCORRECT;
    }

    @Bindable
    public String getScore() {
        return String.valueOf((score % MAX_LEVEL_SCORE == 0 && state == PlayStates.CORRECT) || state == PlayStates.COMPLETE ? MAX_LEVEL_SCORE : score % MAX_LEVEL_SCORE);
    }

    @Bindable
    public int getProgress() {
        return Integer.parseInt(getScore()) + 5;
    }

    public void setScore(int score) {
        this.score = score;
        notifyPropertyChanged(BR.score);
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= unlockedMax; i++) {
            int index = direction == Synth.Sequence.FORWARD ? i : unlockedMax - i;
            builder.append(i > 0 && i < unlockedMax ? ", " : i == unlockedMax ? " and " : "");
            builder.append(getNames()[index]);
        }
        return message.replace("@notes", builder.toString()).replace("@correct", getNames()[randomNoteId]);
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public String[] getNames() {
        return context.getResources().getStringArray(R.array.names_values)[namesId].split(",");
    }

    public void setName(int nameId) {
        this.namesId = nameId;
        notifyPropertyChanged(BR.names);
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public boolean[] getActiveMap() {
        return activeMap;
    }

    @Bindable
    public boolean isSkip() {
        return state == PlayStates.BEGIN && score < Preferences.get(context, Preferences.HIGHEST_SCORE);
    }

    @Bindable
    public boolean isNext() {
        return state != PlayStates.COMPLETE && state != PlayStates.ASK;
    }

    @Bindable
    public boolean isOptions() {
        return state == PlayStates.ASK;
    }

    public void skipLevel() {
        setScore(score + MAX_LEVEL_SCORE);
        state = (score == maxScore) ? PlayStates.COMPLETE : state;
        updateState();
    }

    public boolean isBreak() {
        return (state == PlayStates.CORRECT && score % AD_INTERVAL == 0 && limit <= DEMO_LIMIT);
    }

    public void replay() {
        synth.play(randomNoteId, release);
    }

    public void nextState() {
        if (state == PlayStates.BEGIN) {
            state = PlayStates.REHEARSE;
        } else if (state == PlayStates.REHEARSE) {
            state = PlayStates.ASK;
        } else if (state == PlayStates.ASK) {
            state = result ? PlayStates.CORRECT : PlayStates.INCORRECT;
        } else if (state == PlayStates.CORRECT) {
            state = (score == (limit - 1) * MAX_LEVEL_SCORE) ? PlayStates.COMPLETE : (score % MAX_LEVEL_SCORE == 0) ? PlayStates.BEGIN : PlayStates.ASK;
        } else if (state == PlayStates.INCORRECT) {
            state = PlayStates.REHEARSE;
        }
        updateState();
    }

    private void updateState() {
        if (state == PlayStates.BEGIN) {
            setMessage(score == 0 ? context.getResources().getString(R.string.play_begin) : context.getResources().getString(R.string.play_unlocked));
            unlockedMax++;
            sequence.append(unlockedMax, release);
            activeMap[unlockedMax] = true;

            if (score > Preferences.get(context, Preferences.HIGHEST_SCORE))
                Preferences.set(context, Preferences.HIGHEST_SCORE, score);
        } else if (state == PlayStates.REHEARSE) {
            setMessage(context.getResources().getString(R.string.play_rehearse));
            synth.setOctave(OCTAVE_MID);
            synth.play(sequence, direction);
        } else if (state == PlayStates.ASK) {
            setMessage(context.getResources().getString(R.string.play_ask));
            synth.play(getRandomNoteId(), release);
        } else if (state == PlayStates.CORRECT) {
            setMessage(context.getResources().getString(R.string.play_correct));
            setScore(score + 5);
        } else if (state == PlayStates.INCORRECT) {
            setMessage(context.getResources().getString(R.string.play_incorrect));
            setScore((score / MAX_LEVEL_SCORE) * MAX_LEVEL_SCORE);
        } else if (state == PlayStates.COMPLETE) {
            Preferences.set(context, Preferences.HIGHEST_SCORE, maxScore);
            setMessage(limit == FULL_LIMIT ? context.getResources().getString(R.string.play_complete) : context.getResources().getString(R.string.play_demo_complete));
        }
        notifyPropertyChanged(BR._all);
    }

    private int getRandomNoteId() {
        int noteId;
        do {
            noteId = random.nextInt(unlockedMax + 1);
        } while (repeated && noteId == randomNoteId);
        repeated = (noteId == randomNoteId);
        randomNoteId = noteId;
        if (score % MAX_LEVEL_SCORE < 50) {
            synth.setOctave(OCTAVE_MID);
        } else if (score % MAX_LEVEL_SCORE < 100) {
            synth.setOctave(random.nextInt(OCTAVE_MID + 1));
        } else if (score % MAX_LEVEL_SCORE < 150) {
            synth.setOctave(random.nextInt(OCTAVE_MID + 1) + 1);
        } else {
            synth.setOctave(random.nextInt(OCTAVE_MID + 2));
        }
        return noteId;
    }

    public void guess(int noteId) {
        //noteId = randomNoteId; //DEBUG
        result = (noteId == randomNoteId);
        nextState();
    }

    private enum PlayStates {
        BEGIN,
        REHEARSE,
        ASK,
        CORRECT,
        INCORRECT,
        COMPLETE
    }
}
