package com.walton.filebrowser.ui.video;


import android.util.Log;

import android.view.SurfaceHolder;
import android.content.Context;
import com.mstar.android.media.MMediaPlayer;
import com.mstar.android.media.SubtitleTrackInfo;
import com.walton.filebrowser.R;

public class SubtitleManager {
    // innerSubtitle total tracks number
    public static int mVideoSubtitleNo = 0;
    // externalSubtitlt total tracks number
    public static int mExtSubtitleNo = 0;

    public static int mInnerSubtitleBase = 0;

    public static int mExtSubtitleBase = 0;

    public static int mSelectedSubTrackLast = 0;
    // Subtitles options value
    private static String[] subTitleSettingOptTextOne = null;
    private static String[] subTitleSettingOptTextTwo = null;
    //Subtitle language type
    private static String[] subtitleLanguageTypeOne = null;
    private static String[] subtitleLanguageTypeTwo = null;
    private static SubtitleManager mSubtitleManager = null;
    public static final int MAX_SUBTITLE_SETTING_OPTION = 11;
    private SubtitleManager() {
        // TODO Auto-generated constructor stub
    }
    public static SubtitleManager getInstance(){
        if(null == mSubtitleManager){
            mSubtitleManager = new SubtitleManager() ;
        }
        return mSubtitleManager ;
    }
    /**
     * Settings you want to play the subtitles encoding, a video can have
     * multiple subtitles such as English subtitles, Chinese subtitles.
     *
     * @param track
     */
    public void setSubtitleTrack(MMediaPlayer mMMediaPlayer,int track) {
        if (mMMediaPlayer != null) {
            mMMediaPlayer.setSubtitleTrack(track);
        }
    }

    /**
     * Set up additional caption text.
     *
     * @param Uri
     */
    public void setSubtitleDataSource(final MMediaPlayer mMMediaPlayer,final String Uri) {
        if (mMMediaPlayer != null) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    mMMediaPlayer.setSubtitleDataSource(Uri);
                    }
                }).start();
        }
    }

    /**
     * Open subtitle.
     */
    public void onSubtitleTrack(MMediaPlayer mMMediaPlayer) {
        if (mMMediaPlayer != null) {
            mMMediaPlayer.onSubtitleTrack();
        }
    }

    /**
     * close subtitle.
     */
    public void offSubtitleTrack(MMediaPlayer mMMediaPlayer) {
        if (mMMediaPlayer != null) {
            mMMediaPlayer.offSubtitleTrack();
        }
    }

    /**
     * Get subtitles data to a string, the string coding unified for utf-8.
     *
     * @return
     */
    public String getSubtitleData(MMediaPlayer mMMediaPlayer) {
        String str = "";
        if (mMMediaPlayer != null) {
            return mMMediaPlayer.getSubtitleData();
        }
        return str;
    }

    /**
     * Set the drawing of subtitles SurfaceHolder.
     *
     * @param sh
     */
    public void setSubtitleDisplay(MMediaPlayer mMMediaPlayer,SurfaceHolder sh) {
        if (mMMediaPlayer != null && sh != null) {
            mMMediaPlayer.setSubtitleDisplay(sh);
        }
    }

    /**
     * synchronize subtitle and video.
     *!need player is in play state
     * @param time
     * @return
     */
    public int setSubtitleSync(MMediaPlayer mMMediaPlayer,int time) {
        if (null != mMMediaPlayer) {
            return mMMediaPlayer.setSubtitleSync(time);
        }
        return 0;
    }
    /**
     * For a concrete SubtitleTrackInfo object of subtitles.
     *
     * @param subtitlePosition
     * !need player is in play state
     * @return
     */
    public SubtitleTrackInfo getSubtitleTrackInfo(MMediaPlayer mMMediaPlayer,int subtitlePosition) {
        if (null != mMMediaPlayer) {
            return mMMediaPlayer.getSubtitleTrackInfo(subtitlePosition);
        }
        return null;
    }

    /**
     * get subtitle info.
     *  !need player is in play state
     * @return
     */
    public SubtitleTrackInfo getAllSubtitleTrackInfo(MMediaPlayer mMMediaPlayer) {
        if (null != mMMediaPlayer) {
            return mMMediaPlayer.getAllSubtitleTrackInfo();
        }
        return null ;
    }


    /**
     * @return Initialization subtitle Settings related option value.
     */
    public static String[] initSubtitleSettingOpt(Context context, int id) {
        if (id == 1) {
            if (subTitleSettingOptTextOne == null) {
                Log.i("SubtitleManager","subTitleSettingOptTextOne is null");
                subTitleSettingOptTextOne = context.getResources()
                        .getStringArray(R.array.subtitle_setting_opt);
            }
            return subTitleSettingOptTextOne;
        } else {
            if (subTitleSettingOptTextTwo == null) {
                Log.i("SubtitleManager","subTitleSettingOptTextTwo is null");
                subTitleSettingOptTextTwo = context.getResources()
                        .getStringArray(R.array.subtitle_setting_opt);
            }
            return subTitleSettingOptTextTwo;
        }
    }

    /**
     * get designated position subtitles options value.
     *
     * @ param index subscript index value.
     */
    public static String getSubtitleSettingOptValue(int index, int id) {
        if (id == 1) {
            if (subTitleSettingOptTextOne != null && index >= 0 && index < MAX_SUBTITLE_SETTING_OPTION) {
                return subTitleSettingOptTextOne[index];
            }
        } else {
            if (subTitleSettingOptTextTwo != null && index >= 0 && index < MAX_SUBTITLE_SETTING_OPTION) {
                return subTitleSettingOptTextTwo[index];
            }
        }
        return null;
    }

    /**
     * Settings specified position subtitles options value.
     *
     * @ param index subscript index value. @ param value to set to value.
     */
    public static void setSubtitleSettingOpt(int index, final String value,
            int id) {
        Log.i("SubtitleManager","setSubtitleSettingOpt index:"+index+",value:"+value+",id:"+id);
        if (id == 1) {
            if (subTitleSettingOptTextOne != null && index >= 0 && index < MAX_SUBTITLE_SETTING_OPTION) {
                subTitleSettingOptTextOne[index] = value;
            } else {
                Log.i("SubtitleManager","subTitleSettingOptTextOne may be null");
            }
        } else {
            if (subTitleSettingOptTextTwo != null && index >= 0 && index < MAX_SUBTITLE_SETTING_OPTION) {
                subTitleSettingOptTextTwo[index] = value;
            } else {
                Log.i("SubtitleManager","subTitleSettingOptTextTwo may be null");
            }
        }
    }

    public static void destroySubtitleSettingOptOne(){
        subTitleSettingOptTextOne = null ;
    }

    public static void destroySubtitleSettingOptTwo(){
        subTitleSettingOptTextTwo = null ;
    }

    public static void destroySubtitleSettingOpt(){
        subTitleSettingOptTextOne = null ;
        subTitleSettingOptTextTwo = null ;
    }

    public static void setSubtitleLanguageType(String[] types, int id) {
        if (id == 1) {
            subtitleLanguageTypeOne = types;
        } else {
            subtitleLanguageTypeTwo = types;
        }
    }

    public static String[] getSubtitleLanguageTypes(int id) {
        if (id == 1) {
            return subtitleLanguageTypeOne;
        } else {
            return subtitleLanguageTypeTwo;
        }
    }

    public static String getSubtitleLanguage(int index, int id) {
        if (id == 1) {
            if (subtitleLanguageTypeOne != null) {
                int size = subtitleLanguageTypeOne.length;
                if (index < size && index >= 0) {
                    return subtitleLanguageTypeOne[index];
                }
            }
        } else {
            if (subtitleLanguageTypeTwo != null) {
                int size = subtitleLanguageTypeTwo.length;
                if (index < size && index >= 0) {
                    return subtitleLanguageTypeTwo[index];
                }
            }
        }
        return null;
    }

    public static int getSubtitleLanguageIndex(String value, int id) {
        if (id == 1) {
            if (subtitleLanguageTypeOne != null) {
                int size = subtitleLanguageTypeOne.length;
                for (int i = 0; i < size; i++) {
                    if (subtitleLanguageTypeOne[i].equals(value)) {
                        return i;
                    }
                }
            }
        } else {
            if (subtitleLanguageTypeTwo != null) {
                int size = subtitleLanguageTypeTwo.length;
                for (int i = 0; i < size; i++) {
                    if (subtitleLanguageTypeTwo[i].equals(value)) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }
}
