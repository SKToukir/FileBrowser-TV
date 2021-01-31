package com.walton.filebrowser.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.walton.filebrowser.managers.MusicManager;


public class MusicService extends Service implements IMusicService {
    private static final String TAG = "MusicService";
    private MediaPlayer mMediaPlayer;

    private MediaPlayer.OnCompletionListener mOnCompletionListener;

    @Override
    public void onCreate() {
        super.onCreate();
        MusicManager.init(this);
        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setOnCompletionListener(mp -> {
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(mp);
            }
        });
        sendBroadcast(new Intent("com.clearcrane.musicplayer.intent.action.serviceoncreate"));
    }

    @Override
    public void play(String url) {
        try {
            Log.d(TAG, "play: " + url);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if (isPlaying()) mMediaPlayer.pause();
    }

    @Override
    public void resume() {
        if (!isPlaying()) mMediaPlayer.start();
    }

    @Override
    public void stop() {
        if (isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    @Override
    public void restart() {
        mMediaPlayer.seekTo(0);
    }

    @Override
    public void setPosition(int position) {
        mMediaPlayer.seekTo(position);
    }

    @Override
    public int getPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void setOnCompleteListener(MediaPlayer.OnCompletionListener listener) {
        mOnCompletionListener = listener;
    }

    @Override
    public int volumeUp() {
        AudioManager audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audio == null) {
            return -1;
        }
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume >= maxVolume) {
            return currentVolume;
        }
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + 1, 0);

        return audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int volumeDown() {
        AudioManager audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audio == null) {
            return -1;
        }
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int minVolume = 0;
        if (currentVolume <= minVolume) {
            return currentVolume;
        }
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 0);

        return audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int getVolume() {
        AudioManager audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audio == null) {
            return -1;
        }
        return audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void setVolume(int volume) {
        AudioManager audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audio == null) {
            return;
        }
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
