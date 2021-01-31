package com.walton.filebrowser.managers;


import androidx.annotation.IntDef;

import java.util.List;

/**
 * Created by jjy on 2018/5/8.
 * <p>
 * MusicManager的作用是管理音乐播放的状态，比如播放列表等，并且和外界相连
 */

public interface IMusicManager {
    /**
     * 设置播放列表
     *
     * @param musicList
     */
    void setPlayList(List<MusicManager.Music> musicList);

    List<MusicManager.Music> getPlayList();

    /**
     * 保存当前播放的音乐
     *
     * @param position 当前播放的音乐的序号
     */
    void setCurrentPlaying(int position);

    void playOrPause();

    boolean isPlaying();

    void stopPlaying();
    /**
     * 获取当前播放的音乐
     *
     * @return
     */
    MusicManager.Music getCurrentPlaying();

    /**
     * 开始播放
     *
     * @param position 音乐列表的序号
     */
    void startPlay(int position);

    /**
     * 设置播放进度
     *
     * @param progress
     */
    void setProgress(int progress);

    /**
     * 获取当前播放进度
     *
     * @return
     */
    int getProgress();

    int getDuration();

    /**
     * 快进
     *
     * @param second 快进秒数
     */
    void forward(int second);

    void backward(int second);

    void playNext();

    void playPrevious();

    void setOnProgressListener(OnProgressListener listener);

    interface OnProgressListener {
        void onProgress(MusicManager.Music music, int progress, int duration);
    }

    /**
     * 提高音量
     *
     * @return 当前音量；或-1表示已经到最大音量；或-2其他错误
     */
    int volumeUp();

    /**
     * 降低音量
     *
     * @return 当前音量；或-1表示已经到最小音量；或-2其他错误
     */
    int volumeDown();

    int getVolume();

    void setVolume(int volume);

    /**
     * 设置播放模式
     *
     * @param mode 模式
     */
    void setPlayMode(@PlayMode int mode);

    @IntDef({ORDERED, SHUFFLE, SINGLE})
    public @interface PlayMode {
    }

    int ORDERED = 0;
    int SHUFFLE = 1;
    int SINGLE = 2;
}
