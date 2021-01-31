package com.walton.filebrowser.ui.video;

import android.content.Context;
import android.util.Log;
import com.walton.filebrowser.R;
import com.mstar.android.media.AudioTrackInfo;
import com.mstar.android.media.MMediaPlayer;
import android.media.Metadata;

import java.lang.reflect.Method;

/**
 * @author jason-f.zhang
 * @version 0.1
 *
 */
public class AudioTrackManager {
    private static final String TAG = "AudioTrackManager";
    private static AudioTrackManager mAudioTrackManager = null ;

    private AudioTrackManager(){}
    /**
     * get the {@link AudioTrackManager} instances with singleton patteren
     * @return AudioTrackManager
     */
    public static AudioTrackManager getInstance(){
        if(null == mAudioTrackManager){
            mAudioTrackManager = new AudioTrackManager();
        }
        return mAudioTrackManager ;
    }

    // Audio track Settings option value
    private static String audioTrackSettingOptTextOne[] = null;
    private static String audioTrackSettingOptTextTwo[] = null;

    /**
     * @return Initialization track Settings option value.
     */
    public static String[] initAudioTackSettingOpt(Context context, int id) {
        if (id == 1) {
            if (audioTrackSettingOptTextOne == null) {
                audioTrackSettingOptTextOne = context.getResources()
                        .getStringArray(R.array.audio_track_setting_opt);
            }
            return audioTrackSettingOptTextOne;
        } else {
            if (audioTrackSettingOptTextTwo == null) {
                audioTrackSettingOptTextTwo = context.getResources()
                        .getStringArray(R.array.audio_track_setting_opt);
            }
            return audioTrackSettingOptTextTwo;
        }
    }

    /**
     * @param index
     *            Subscript index value
     */
    public static String getAudioTackSettingOpt(int index, int id) {
        if (id == 1) {
            if (index >= 0 && index < 6) {
                return audioTrackSettingOptTextOne == null ? null : audioTrackSettingOptTextOne[index];
            }
        } else {
            if (index >= 0 && index < 6) {
                return audioTrackSettingOptTextOne == null ? null : audioTrackSettingOptTextTwo[index];
            }
        }
        return null;
    }

    /**
     * @ param index subscript index value. @ param value to set to value.
     */
    public static void setAudioTackSettingOpt(Context context, int index,
                                              final String value, int id) {
        Log.i(TAG, "setAudioTackSettingOpt index:" + index + " value:" + value + " id:" + id);
        if (id == 1) {
            if (index >= 0 && index < 7) {
                if (value != null) {
                    audioTrackSettingOptTextOne[index] = value;
                } else {
                    audioTrackSettingOptTextOne[index] = context.getResources()
                            .getString(R.string.video_size_unknown);
                }
            }
        } else {
            if (index >= 0 && index < 7) {
                if (value != null) {
                    audioTrackSettingOptTextTwo[index] = value;
                } else {
                    audioTrackSettingOptTextTwo[index] = context.getResources()
                            .getString(R.string.video_size_unknown);
                }
            }
        }
    }
    /**
     * clear the AudioTrack's setting info
     */
    public static void destroyAudioTrackOption(){
        audioTrackSettingOptTextOne = null ;
        audioTrackSettingOptTextTwo = null ;
    }

    /**
     * For track information.
     *!need player is in play state
     * @param typeIsAudio
     * @return
     */

    public AudioTrackInfo getAudioTrackInfo(MMediaPlayer mMMediaPlayer,final boolean typeIsAudio) {
        if (null != mMMediaPlayer) {
            Log.i(TAG, "***getAudioTrackInfo**");
            try {
                return mMMediaPlayer.getAudioTrackInfo(typeIsAudio);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Settings you want to play the audio track, data from getAudioTrackInfo
     * return values.
     *
     * @param track
     */

    public void setAudioTrack(MMediaPlayer mMMediaPlayer,int track) {
        if (mMMediaPlayer != null) {
            mMMediaPlayer.setAudioTrack(track);
        }
    }
    //!need player is in play state
    public int getAudioTrackNumber(MMediaPlayer mMMediaPlayer,final boolean typeIsAudio) {
        AudioTrackInfo audioTrackInfo = getAudioTrackInfo(mMMediaPlayer, typeIsAudio);
        if (audioTrackInfo != null) {
            int codecID = audioTrackInfo.getCodecID();
            return codecID;
        }
        return -1;
    }

    public int getAudioTrackCount(MMediaPlayer mMMediaPlayer) {
        Log.i(TAG, "getAudioTrackCount: (mMMediaPlayer == null) = " + (mMMediaPlayer == null));
        if (mMMediaPlayer == null)
            return 1;

        Metadata data = mMMediaPlayer.getMetadata(true, true);
        Log.i(TAG, "getAudioTrackCount: (data != null) = " + (data != null));
        int totalTrackNum = 1;
        if (data != null) {
            if (data.has(Metadata.TOTAL_TRACK_NUM)) {
                totalTrackNum = data.getInt(Metadata.TOTAL_TRACK_NUM);
                Log.i(TAG, "getAudioTrackCount: totalTrackNum = " + totalTrackNum);
            }
            try {
                Class clz = Class.forName("android.media.Metadata");
                Method get = clz.getDeclaredMethod("recycleParcel");
                // http://hcgit:8080/#/q/bac5dbcffd9773356a969254d9c8733a419df8e4
                data.recycleParcel();
                Log.i(TAG, "recycleParcel");
            } catch (Exception e) {
                Log.i(TAG, "Can't find android.media.Metadata API recycleParcel !");
                e.printStackTrace();
            }
        }
        return totalTrackNum;
    }

    public int getCurrentAudioTrackId(MMediaPlayer mMMediaPlayer) {
        Log.i(TAG, "getCurrentAudioTrackId");
        if (mMMediaPlayer == null)
            return 1;
        Metadata data = mMMediaPlayer.getMetadata(true, true);
        int currentAudioTrackId = 0;
        if (data != null) {
            int CurrentTrackId = 0;
            int count = 0;
            if (data.has(Metadata.TOTAL_TRACK_NUM)) {
                count = data.getInt(Metadata.TOTAL_TRACK_NUM);
            }
            if (data.has(Metadata.CURRENT_AUDIO_TRACK_ID)) {
                CurrentTrackId = data.getInt(Metadata.CURRENT_AUDIO_TRACK_ID);
                if (count != 0) {
                    currentAudioTrackId =  CurrentTrackId % count;
                }
            }
            try {
                Class clz = Class.forName("android.media.Metadata");
                Method get = clz.getDeclaredMethod("recycleParcel");
                // http://hcgit:8080/#/q/bac5dbcffd9773356a969254d9c8733a419df8e4
                data.recycleParcel();
                Log.i(TAG, "recycleParcel");
            } catch (Exception e) {
                Log.i(TAG, "Can't find android.media.Metadata API recycleParcel !");
                e.printStackTrace();
            }
        }
        return currentAudioTrackId;
    }

    public int getCurrentAudioChannelNum(MMediaPlayer mMediaPlayer) {
        Log.i(TAG, "getCurrentAudioChannelNum: Metadata.CURRENT_AUDIO_CHANNEL_COUNT : "
                + Metadata.CURRENT_AUDIO_CHANNEL_COUNT);
        if (mMediaPlayer == null) {
            return 0;
        }
        Metadata data = mMediaPlayer.getMetadata(true, true);
        int num = 0;
        if (data != null) {
            if (data.has(Metadata.CURRENT_AUDIO_CHANNEL_COUNT)) {
                num = data.getInt(Metadata.CURRENT_AUDIO_CHANNEL_COUNT);
                Log.i(TAG, "getCurrentAudioChannelNum: data != null and num = " + num);
            }
        }
        Log.i(TAG, "getCurrentAudioChannelNum: ChannelNum: " + num);
        return num;
    }

    public String getCurrentAudioCodecType(MMediaPlayer mMediaPlayer) {
        Log.i(TAG, "getCurrentAudioCodecType: ");
        if (mMediaPlayer == null) {
            return "";
        }
        Metadata data = mMediaPlayer.getMetadata(true, true);
        String codecType = "";
        if (data != null) {
            if (data.has(Metadata.AUDIO_CODEC)) {
                codecType = data.getString(Metadata.AUDIO_CODEC);
                Log.i(TAG, "getCurrentAudioCodecType: data != null and codecType = " + codecType);
            }
        }
        return codecType;
    }

    //!need player is in play state
    public String getAudioTrackName(Context context, MMediaPlayer mMMediaPlayer,int viewId) {
        Log.i(TAG, "getAudioTrackName: ");
        AudioTrackInfo audioTrackInfo = getAudioTrackInfo(mMMediaPlayer,false);
        String strTitle = null;
        if (audioTrackInfo != null) {
            strTitle = audioTrackInfo.getTitle();
        }
        Log.i(TAG, "getAudioTrackName: strTitle : " + strTitle);
        if (strTitle == null
                || strTitle.equals(context.getResources().getString(R.string.video_size_unknown))) {
            String audio = getAudioTackSettingOpt(0, viewId);
            int numberAudioTrack = Integer.parseInt(audio.substring(
                    audio.length() - 1, audio.length())) ;
            strTitle = "Audio "+numberAudioTrack;
        }
        return strTitle;
    }

    //!need player is in play state
    public int getAudioTrackDuration(MMediaPlayer mMMediaPlayer) {
        AudioTrackInfo audioTrackInfo = getAudioTrackInfo(mMMediaPlayer,false);
        if (audioTrackInfo != null) {
            int duration = audioTrackInfo.getTotalPlayTime();
            if (duration > 0)
                return duration;
        }
        return -1;
    }
    //!need player is in play state
    public String getAudioTrackLanguage(Context context,MMediaPlayer mMMediaPlayer) {
        AudioTrackInfo audioTrackInfo = getAudioTrackInfo(mMMediaPlayer,false);
        Log.i(TAG, "*******AudioTracklanguage*****" + audioTrackInfo);
        if (audioTrackInfo != null) {
            String language = audioTrackInfo.getAudioLanguageType();
            Log.i(TAG, "*******AudioTracklanguage*****" + language);
            if (language != null)
                return language;
        }
        return context.getResources().getString(R.string.video_size_unknown);
    }
}
