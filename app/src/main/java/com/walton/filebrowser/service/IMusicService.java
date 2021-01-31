package com.walton.filebrowser.service;

import android.media.MediaPlayer;

/**
 * Created by jjy on 2018/5/8.
 * <p>
 * MusicService的作用是控制音乐的播放，很纯粹
 */

public interface IMusicService {
    void play(String url);

    void pause();

    void resume();

    void stop();

    void restart();

    void setPosition(int position);

    int getPosition();

    int getDuration();

    void setOnCompleteListener(MediaPlayer.OnCompletionListener listener);

    /**
     * 提高音量
     *
     * @return 当前音量；或-1表示失败
     */
    int volumeUp();

    /**
     * 降低音量
     *
     * @return 当前音量；或-1表示失败
     */
    int volumeDown();

    int getVolume();

    void setVolume(int volume);

    boolean isPlaying();
}
