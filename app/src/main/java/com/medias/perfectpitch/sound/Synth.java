package com.medias.perfectpitch.sound;

import android.content.Context;
import android.util.SparseIntArray;

public class Synth {
    public static final int LENGTH = 37;
    public static final int MIDDLE_C = 12;

    private Context context;
    private int[] soundIds;
    private int transpose;
    private int toneId = -1;
    private int decayTime = 300;
    private SharedSoundPool sharedSoundPool;
    private SparseIntArray streamIds = new SparseIntArray();

    public Synth(Context context) {
        this.context = context;
        sharedSoundPool = SharedSoundPool.getInstance();
    }

    public void play(final int noteId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                streamIds.append(absoluteId(noteId), sharedSoundPool.play(soundIds[noteId]));
            }
        }).start();
    }

    public void play(final int noteId, final int duration) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int streamId = sharedSoundPool.play(soundIds[absoluteId(noteId)]);
                    Thread.sleep(duration);
                    decay(streamId);
                    sharedSoundPool.stop(streamId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void play(final SparseIntArray seq, final Sequence direction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < seq.size(); i++) {
                        int index = direction == Sequence.FORWARD ? i : seq.size() - 1 - i;
                        int streamId = sharedSoundPool.play(soundIds[absoluteId(seq.keyAt(index))]);
                        Thread.sleep(seq.valueAt(index));
                        decay(streamId);
                        sharedSoundPool.stop(streamId);
                    }
                } catch (InterruptedException e) {
                    //
                }
            }
        }).start();
    }

    public void stop(final int noteId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int streamId = streamIds.get(absoluteId(noteId));
                    streamIds.delete(absoluteId(noteId));
                    decay(streamId);
                    sharedSoundPool.stop(streamId);
                } catch (InterruptedException e) {
                    //
                }
            }
        }).start();
    }

    public void setTone(final int toneId) {
        if (this.toneId != toneId) {
            if (soundIds != null) {
                sharedSoundPool.unload(context, soundIds);
            }
            soundIds = sharedSoundPool.load(context, SharedSoundPool.CATEGORY_INSTRUMENT, toneId);
            this.toneId = toneId;
        }
    }

    public void setDecayTime(int decayTime) {
        this.decayTime = decayTime;
    }

    public void setTranspose(int transpose) {
        this.transpose = transpose;
    }

    public void setOctave(int octave) {
        this.transpose = 12 * octave;
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (soundIds != null) {
            sharedSoundPool.unload(context, soundIds);
        }
    }

    private void decay(int streamId) throws InterruptedException {
        int sleepTime = decayTime / 10;
        for (float x = 0; x >= 0; x -= 0.1) {
            sharedSoundPool.setVolume(streamId, x * x * x);
            Thread.sleep(sleepTime);
        }
    }

    private int absoluteId(int noteId) {
        return noteId + transpose;
    }

    public enum Sequence {
        FORWARD, BACKWARD
    }
}
