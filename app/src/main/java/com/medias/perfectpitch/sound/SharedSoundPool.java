package com.medias.perfectpitch.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;
import java.util.Locale;

class SharedSoundPool {
    private static final String COLLECTION_PATH = "sound/%s/%d";
    private static final String COLLECTION_FILE_PATH = "sound/%s/%d/%s";
    private static final int MAX_STREAMS = 4;
    private static SharedSoundPool currentInstance;
    private SoundPool soundPool;

    public static final String CATEGORY_SPEECH = "speech";
    public static final String CATEGORY_INSTRUMENT = "instrument";

    public static SharedSoundPool getInstance() {
        if (currentInstance == null) {
            currentInstance = new SharedSoundPool();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                currentInstance.soundPool = new SoundPool.Builder().setMaxStreams(MAX_STREAMS).build();
            } else {
                currentInstance.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
            }
        }
        return currentInstance;
    }

    public int[] load(Context context, String category, int collectionId) {
        try {
            String[] files = context.getAssets().list(String.format(Locale.US, COLLECTION_PATH, category, collectionId));
            int[] soundIds = new int[files.length];
            for (int i = 0; i < soundIds.length; i++) {
                soundIds[i] = soundPool.load(context.getAssets().openFd(String.format(Locale.US, COLLECTION_FILE_PATH, category, collectionId, files[i])), 1);
            }
            return soundIds;
        } catch (IOException e) {
            //
        }
        return null;
    }

    public void unload(Context context, int[] soundIds) {
        for (int i = 0; i < soundIds.length; i++) {
            soundPool.unload(soundIds[i]);
        }
    }

    public int play(int soundId) {
        return soundPool.play(soundId, 1, 1, 0, 0, 1);
    }

    public void stop(int streamId) {
        soundPool.stop(streamId);
    }

    public void setVolume(int streamId, float volume) {
        soundPool.setVolume(streamId, volume, volume);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (soundPool != null) {
            soundPool.release();
        }
    }

    private SharedSoundPool() {

    }
}
