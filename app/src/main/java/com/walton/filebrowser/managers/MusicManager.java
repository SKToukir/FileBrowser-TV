package com.walton.filebrowser.managers;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


import com.walton.filebrowser.service.IMusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2018/5/8.
 * <p>
 * MusicManager的作用是管理音乐播放的状态，比如播放列表等，并且和外界相连
 */

public class MusicManager implements IMusicManager {
    private static final String TAG = "MusicManager";
    private static MusicManager sManager;
    private IMusicService mService;
    private List<Music> mPlayList = new ArrayList<>();
    private int mCurrentPosition;
    private OnProgressListener mProgressListener;

    private MusicManager(IMusicService service) {
        mService = service;
        mService.setOnCompleteListener(mp -> playNext());
    }

    public static void init(IMusicService service) {
        if (sManager == null) {
            sManager = new MusicManager(service);
        }
    }

    public static IMusicManager getInstance() {
        if (sManager == null) {
            throw new NullPointerException();
        }
        return sManager;
    }

    @Override
    public void setPlayList(List<Music> musicList) {
        if (musicList == null) {
            return;
        }
        Log.d(TAG, "setPlayList: " + musicList);
        mPlayList.clear();
        mPlayList.addAll(musicList);
    }

    @Override
    public List<Music> getPlayList() {
        return mPlayList;
    }

    @Override
    public void setCurrentPlaying(int position) {
        mService.play(mPlayList.get(position).url);
    }

    @Override
    public Music getCurrentPlaying() {
        return mPlayList.get(mCurrentPosition);
    }

    @Override
    public void startPlay(int position) {
        if (position > mPlayList.size() - 1 || position < 0) {
            return;
        }
        Music play = mPlayList.get(position);
        Log.d(TAG, "startPlay: " + play);
        mService.play(play.url);
    }

    @Override
    public void setProgress(int progress) {
        mService.setPosition(progress);
    }

    @Override
    public int getDuration() {
        return mService.getDuration();
    }

    @Override
    public int getProgress() {
        return mService.getPosition();
    }

    @Override
    public void forward(int second) {
        mService.setPosition(mService.getPosition() + second * 1000);
    }

    @Override
    public void backward(int second) {
        mService.setPosition(mService.getPosition() - second * 1000);
    }

    @Override
    public int volumeUp() {
        return mService.volumeUp();
    }

    @Override
    public int volumeDown() {
        return mService.volumeDown();
    }

    @Override
    public int getVolume() {
        return mService.getVolume();
    }

    @Override
    public void setVolume(int volume) {
        mService.setVolume(volume);
    }

    @Override
    public void setPlayMode(int mode) {
        // TODO: 2018/5/10
    }

    @Override
    public void playNext() {
        mCurrentPosition = mCurrentPosition >= mPlayList.size() - 1 ? 0 : mCurrentPosition + 1;
        Music next = mPlayList.get(mCurrentPosition);
        Log.d(TAG, "playNext: " + next);
        mService.play(next.url);
    }

    @Override
    public void playPrevious() {
        Log.d(TAG, "playPrevious: ");
        mCurrentPosition = mCurrentPosition <= 0 ? mPlayList.size() - 1 : mCurrentPosition - 1;
        mService.play(mPlayList.get(mCurrentPosition).url);
    }

    @Override
    public void playOrPause() {
        if (isPlaying()) {
            mService.pause();
        } else {
            mService.resume();
        }
    }


    @Override
    public boolean isPlaying() {
        return mService.isPlaying();
    }

    @Override
    public void stopPlaying() {
        if (isPlaying()){
            mService.stop();
        }
    }

    @Override
    public void setOnProgressListener(OnProgressListener listener) {
        mProgressListener = listener;
        mHandler.postDelayed(mProgressTask, 500);
    }

    private Handler mHandler = new Handler();
    private Runnable mProgressTask = new Runnable() {
        @Override
        public void run() {
            mProgressListener.onProgress(mPlayList.get(mCurrentPosition), mService.getPosition(), mService.getDuration());
            mHandler.postDelayed(this, 500);
        }
    };

    public static class Music {
        public String name;
        public String url;
        public String artist;
        public String author;
        public String album;
        public String publishYear;
        public byte[] albumCover;

        protected Music() {
        }

        public Music(String name, String url, String artist) {
            this.name = checkNotNull(name);
            this.url = checkNotNull(url);
            this.artist = checkNotNull(artist);
        }

        public Music(String name, String url, String artist, String author, String album, String publishYear, byte[] albumCover) {
            this.name = checkNotNull(name);
            this.url = checkNotNull(url);
            this.artist = checkNotNull(artist);
            this.author = checkNotNull(author);
            this.album = checkNotNull(album);
            this.publishYear = checkNotNull(publishYear);
            this.albumCover = albumCover;
        }

        private static String checkNotNull(String obj) {
            return TextUtils.isEmpty(obj) ? "UNKNOWN" : obj;
        }

        @SuppressWarnings("ImplicitArrayToString")
        @Override
        public String toString() {
            return "Music{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", artist='" + artist + '\'' +
                    ", author='" + author + '\'' +
                    ", album='" + album + '\'' +
                    ", publishYear='" + publishYear + '\'' +
                    ", albumCover=" + albumCover +
                    '}';
        }
    }
}
