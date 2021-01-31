package com.walton.filebrowser.ui.gridvideo;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.video.MediaError;
import com.mstar.android.media.MMediaPlayer;
import com.walton.filebrowser.util.Tools;
import android.os.Handler;

/**
 * MWPlayer Factory class
 * @author jason
 */
public class MWPlayerFactory {
    private static final String TAG = "MWPlayerFactory";
    private static final int KEY_PARAMETER_SET_MUTIL_VIDEO_PLAY = 2054;
    private static final String netParameter = "set_multiple_player";
    private static MWPlayerFactory mMWPlayerFactory = null;
    private IMWPlayerListener mMWPlayerListener;
    private Context mContext;
    private static Handler mHandler = null;
    private MWPlayerFactory() {
    }

    /**
     * single instance
     * @return MWPlayerFactory object
     */
    public static MWPlayerFactory getInstance() {
        if (null == mMWPlayerFactory) {
            mMWPlayerFactory = new MWPlayerFactory();
        }
        return mMWPlayerFactory;

    }
    /**
     * create MMediaPlayer with surfaceview and video path
     * @param context  android context {@link Context}
     * @param isFirstVideo weather is new the first player
     * @param path the video path
     * @param surfaceView the surfaceview to play video
     * @param mWPlayerListener Player listener
     * @return
     */
    public synchronized MMediaPlayer creatMWPlayerWithSurfaceView(final Context context,
            final boolean isFirstVideo, final String path, SurfaceView surfaceView,
            final IMWPlayerListener mWPlayerListener , final int position) {
        // close the built-in music service of android
        Log.d(TAG, "creatMWPlayerWithSurfaceView" + "   isFirstVideo:"
                + isFirstVideo + ",  path:" + path + ",  surfaceview:"
                + surfaceView+", position:"+position);
        if (isFirstVideo) {
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "pause");
            context.sendBroadcast(i);
            //Tools.setSWDetileModeOn(true);
        }
        this.mContext = context;
        this.mMWPlayerListener = mWPlayerListener;
        final MMediaPlayer mMMediaPlayer = new MMediaPlayer();
        final Uri mUri = Uri.parse(path);
        Map<String, String> tmpmHeaders = new HashMap<String, String>();
        if (Tools.isNetPlayback(path) && tmpmHeaders != null) {
            tmpmHeaders.put(netParameter, "1");
        }
        final Map<String, String> mHeaders = tmpmHeaders;
        mMMediaPlayer.setOnErrorListener(new MMediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int framework_err,
                    int impl_err) {
                Log.e(TAG, "onError: " + framework_err + "," + impl_err + "  mMMediaPlayer = "+mMMediaPlayer);
                String errorMsg = changeToErrorMsg(framework_err, impl_err);
                mWPlayerListener.onVideoPlayFaild(errorMsg);
                releaseErrorPlayer(mMMediaPlayer);
                return true;
            }
        });
        mMMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        mMMediaPlayer.setOnPreparedListener(new MMediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        Log.d(TAG,
                                "onPrepared getVideoWidth: "+ mp.getVideoWidth()
                                        + ", getVideoHeight "+ mp.getVideoHeight()+",position:"+position);
                        mMMediaPlayer.start();
                        mWPlayerListener.onAttachedVideoFrame();
                    }
                });
        mMMediaPlayer
                .setOnCompletionListener(new MMediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        Log.i(TAG, "OnCompletion");
                        if (mMMediaPlayer.getDuration()>100) {
                            mMMediaPlayer.seekTo(100);
                            mMMediaPlayer.start();
                        } else {
                            String errorMsg = "This video's duration is not beyond 100ms";
                            mWPlayerListener.onVideoPlayFaild(errorMsg);
                            releaseErrorPlayer(mMMediaPlayer);
                        }
                    }
                });
        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG, "surfaceDestroyed position:"+position);
                Log.w(TAG,"why surfaceDestroyed:"+Log.getStackTraceString(new Throwable()));
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "surfaceCreated position:"+position);
                //ensure MediaPlayer prepare after surface created for avoid IllegalStateException

                try {
                    mMMediaPlayer.setDisplay(surfaceHolder);
                } catch (IllegalStateException ex) {
                    Log.w(TAG, "IllegalStateException Unable to open content: "
                            + mUri, ex);
                    mWPlayerListener.onVideoPlayFaild("can't open this file");
                    releaseErrorPlayer(mMMediaPlayer);
                }
                if (MWPlayerManager.getInstance().mSurfaceCreatedTAG.containsKey(position)) {
                    Log.i(TAG, "had surfaceCreated at this position and so return");
                    return ;
                } else {
                    Log.i(TAG, "has never surfaceCreated at this position");
                    MWPlayerManager.getInstance().mSurfaceCreatedTAG.put(position,1);
                }
                try {
                    //don't use this inteface for IllegalStateException
                    if (Tools.isNetPlayback(path) && mHeaders != null) {
                        Log.i(TAG,"netvideo setDataSource with headers");
                        mMMediaPlayer.setDataSource(context, mUri ,mHeaders);
                    } else {
                        mMMediaPlayer.setDataSource(context, mUri);
                        mMMediaPlayer.setParameter(KEY_PARAMETER_SET_MUTIL_VIDEO_PLAY, 1);
                    }
                    //mMMediaPlayer.setDataSource(path);
                   /* if (isFirstVideo) {
                        Log.d(TAG, "new first player , MMediaPlayer.setParameter(4001, 1) , MMediaPlayer ="+mMMediaPlayer);
                        mMMediaPlayer.setParameter(
                                KEY_PARAMETER_SET_MUTIL_VIDEO_PLAY, 1);
                    }*/
                    mMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMMediaPlayer.setScreenOnWhilePlaying(true);
                    mMMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    Log.d(TAG, "IOException");
                    mWPlayerListener.onVideoPlayFaild("can't open this file");
                    releaseErrorPlayer(mMMediaPlayer);
                } catch (IllegalArgumentException ex) {
                    Log.w(TAG,
                            "IllegalArgumentException Unable to open content: "
                                    + mUri, ex);
                    mWPlayerListener.onVideoPlayFaild("can't open this file");
                    releaseErrorPlayer(mMMediaPlayer);
                } catch (IllegalStateException ex) {
                    Log.w(TAG, "IllegalStateException Unable to open content: "
                            + mUri, ex);
                    mWPlayerListener.onVideoPlayFaild("can't open this file");
                    releaseErrorPlayer(mMMediaPlayer);
                } catch (SecurityException ex) {
                    Log.w(TAG, "SecurityException Unable to open content: "
                            + mUri, ex);
                    mWPlayerListener.onVideoPlayFaild("can't open this file");
                    releaseErrorPlayer(mMMediaPlayer);
                } catch (Exception ex) {
                    Log.w(TAG, "Exception Unable to open content: " + mUri, ex);
                    mWPlayerListener.onVideoPlayFaild("can't open this file");
                    releaseErrorPlayer(mMMediaPlayer);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
                Log.i(TAG, "surfaceChanged");
            }
        });

        return mMMediaPlayer;
    }

    // The following is a series of the player listener in callback
    MMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            Log.d(TAG, "onVideoSizeChanged width: " + width + ", height "
                    + height);
        }
    };

    /**
     * changed error value to error message tip to user
     * @param framework_err error int value
     * @param implError error int value
     * @return the error string info message
     */
    private String changeToErrorMsg(int framework_err, int implError) {
        String strMessage = "";
        switch (framework_err) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                strMessage = mContext.getResources().getString(R.string.video_media_error_server_died);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                strMessage = processErrorUnknown(framework_err, implError);
                break;
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                strMessage = mContext.getResources().getString(R.string.video_media_error_not_valid);
                break;
            default:
                strMessage = mContext.getResources().getString(R.string.video_media_other_error_unknown);
                break;
        }
        return strMessage;
    }

    // Unknown error handling
    private String processErrorUnknown(int what, int extra) {
        int strID = R.string.video_media_error_unknown;
        switch (extra) {
            case MediaError.ERROR_MALFORMED:
                strID = R.string.video_media_error_malformed;
                break;
            case MediaError.ERROR_IO:
                strID = R.string.video_media_error_io;
                break;
            case MediaError.ERROR_UNSUPPORTED:
                strID = R.string.video_media_error_unsupported;
                break;
            case MediaError.ERROR_FILE_FORMAT_UNSUPPORT:
                strID = R.string.video_media_error_format_unsupport;
                break;
            case MediaError.ERROR_NOT_CONNECTED:
                strID = R.string.video_media_error_not_connected;
                break;
            case MediaError.ERROR_AUDIO_UNSUPPORT:
                strID = R.string.video_media_error_audio_unsupport;
                break;
            case MediaError.ERROR_VIDEO_UNSUPPORT:
                strID = R.string.video_media_error_video_unsupport;
                break;
            case MediaError.ERROR_DRM_NO_LICENSE:
                strID = R.string.video_media_error_no_license;
                break;
            case MediaError.ERROR_DRM_LICENSE_EXPIRED:
                strID = R.string.video_media_error_license_expired;
                break;

            default:
                //usb storage off
                strID = R.string.video_media_other_error_unknown;
                break;
        }
        return mContext.getResources().getString(strID);
    }

    /**
     * release player's resource for play error
     * @param player
     */
    private void releaseErrorPlayer(final MMediaPlayer player){
        Log.i(TAG, "releaseErrorPlayer start...");
        new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    player.stop();
                    player.release();
                    Log.i(TAG, "releaseErrorPlayer end...");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
