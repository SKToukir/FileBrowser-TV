package com.walton.filebrowser.ui.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.ui.media.util.LogUtil;
import com.walton.filebrowser.ui.music.MusicPlayerActivity;
import com.walton.filebrowser.ui.photo.ImagePlayerActivity;
import com.walton.filebrowser.ui.video.VideoPlayerActivity;
import com.walton.filebrowser.ui.video.NetVideoPlayActivity;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.PlaylistTool;

public class LocalDataBrowser {

    private static final String TAG = "LocalDataBrowser";

    private Activity activity;

    private Handler handler;

    private LocalDataManager localDataManager;

    protected List<BaseData> data;

    private int activityType;

    // Focus the current position
    protected int focusPosition;

    // Click confirm focus position media types
    private int mediaType;

    public static int hasCancelVideoTaskStateInLocalData=Constants.GRID_TASK_NOT_CANCELED;
    private RefreshUIListener refreshUIListener = new RefreshUIListener() {

        @Override
        public void onFinish(List<BaseData> list, int currentPage,
                             int totalPage, int position) {

            focusPosition = position;
            if (data != null) {
                // data clear.
                data.clear();
            }
            data.addAll(list);

            // send refresh UI event.
            Message msg = handler.obtainMessage();
            msg.what = Constants.UPDATE_LOCAL_DATA;

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.BUNDLE_PAGE, currentPage);
            bundle.putInt(Constants.BUNDLE_TPAGE, totalPage);
            bundle.putInt(Constants.BUNDLE_INDEX, position);
            msg.setData(bundle);
            handler.sendMessage(msg);

        }

        @Override
        public void onOneItemAdd(List<BaseData> list, int currentPage,
                                 int totalPage, int position) {
        }

        @Override
        public void onScanCompleted() {
        }

        @Override
        public void onFailed(int code) {
            Message message = handler.obtainMessage();
            // Refresh disk equipment
            if (code == Constants.UPDATE_DISK_DEVICE) {
                message.what = Constants.UPDATE_PROGRESS_INFO;
                message.arg2 = Constants.PROGRESS_TEXT_SHOW;
                message.arg1 = R.string.loading_usb_device;
            }

            handler.sendMessage(message);
        }
    };
private String pathUsb;
    public LocalDataBrowser(Activity activity, Handler handler,
                            List<BaseData> data) {
        this.activity = activity;
        this.handler = handler;
        this.data = data;
    }


    /**
     * into the folder for data.
     *
     * @param type
     *            media types
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void browser(int index, int type) {
        Log.d(TAG, "browser: index "+ index + " "+ type);
        this.activityType = type;
        if (localDataManager == null) {
            localDataManager = new LocalDataManager(activity, refreshUIListener);
        }
        localDataManager.browser(index, type);

    }

    /**
     * complete turn the page operation
     */
    protected void refresh(int operateType) {

        // finish turn the page, 0 for flip over mark
        if (operateType == Constants.KEYCODE_PAGE_UP
                || operateType == Constants.TOUCH_PAGE_UP) {
            // page up
            if (FileBrowserActivity.mListOrGridFlag==Constants.GRIDVIEW_MODE) {
                Message msg = handler.obtainMessage();
                msg.what = Constants.GRID_CANCEL_TASK;
                msg.arg1 = Constants.GRID_CANCEL_TASK_NO_NEED_PLAY;
                handler.sendMessage(msg);
            }
            localDataManager.getCurrentPage(-1, 0);

        } else if (operateType == Constants.KEYCODE_PAGE_DOWN
                || operateType == Constants.TOUCH_PAGE_DOWN) {
            // page down
            if (FileBrowserActivity.mListOrGridFlag==Constants.GRIDVIEW_MODE) {
                Message msg = handler.obtainMessage();
                msg.what = Constants.GRID_CANCEL_TASK;
                msg.arg1 = Constants.GRID_CANCEL_TASK_NO_NEED_PLAY;
                handler.sendMessage(msg);
            }
            localDataManager.getCurrentPage(1, 0);

            // filter files
        } else if (operateType == Constants.OPTION_STATE_ALL
                || operateType == Constants.OPTION_STATE_SONG
                || operateType == Constants.OPTION_STATE_VIDEO
                || operateType == Constants.OPTION_STATE_PICTURE) {
            this.activityType = operateType;
            Log.d(TAG, "LocalDataBrowser activityType:" + activityType);

            localDataManager.getCurrentPage(0, operateType);

        }

    }

    /**
     * receive to disk loading or remove radio again when the interface shown on
     * disk data.
     *
     * @param path
     *            disk mount the absolute path.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void updateUSBDevice(final String path) {
        localDataManager.showUSBDevice(path);
    }
    public int getLocalDataBrowserState(){
        if (null == localDataManager)
            return -1;
        int tmpState = localDataManager.getLocalDataBrowserState();
        return tmpState;
    }
    protected void switchViewMode(){
        localDataManager.switchViewMode=1;
        localDataManager.onFinish();
    }
    /**
     * According to the parameter start designated player.
     *
     * @param type
     *            media types.
     * @param position
     *            focus position.
     */
    protected void startPlayer(final int type, final int position) {
        Log.i(TAG, type+" *****startPlayer****" + position);
        if (Constants.FILE_TYPE_MPLAYLIST == type) {
            BaseData bd = localDataManager.getCommonFile(position);
            if (bd == null) {
                return;
            }
            Log.v("LocalDataBrowser", "play playlist file:"+bd.getPath());
            LogUtil.d("play playlist file:"+bd.getPath());
            PlaylistTool ptool =new PlaylistTool();
            ArrayList<BaseData> pathList = ptool.parsePlaylist(bd.getPath());
            if (pathList != null) {
                String path = pathList.get(0).getPath();
                if (path != null && localDataManager.check(path,
                        activity.getResources().getStringArray(R.array.photo_filter))) {
                    MediaContainerApplication.getInstance().clearPhotoList();
                    MediaContainerApplication.getInstance().setAllPhotoList(pathList);
                    Intent intent = new Intent(activity,ImagePlayerActivity.class);
                    // The current click position
                    int index = localDataManager.getMediaFile(Constants.FILE_TYPE_SONG, position);
                    // the current broadcast media index
                    intent.putExtra(Constants.BUNDLE_INDEX_KEY, index);

                    activity.startActivity(intent);
                    return;
                }
            }
            Intent intent = null;
            intent = new Intent(activity, NetVideoPlayActivity.class);
            intent.putExtra(Constants.BUNDLE_FILETYPE_KEY, type);
            intent.putExtra(Constants.BUNDLE_BASEDATA_KEY, bd);
            activity.startActivity(intent);
            return;
        }
        boolean hasMedia = false;
        // check whether has the specified type of media
        hasMedia = MediaContainerApplication.getInstance().hasMedia(type);
        if (hasMedia) {
            // The current click position
            int index = 0;
            if (activityType != type
                    && activityType == Constants.OPTION_STATE_ALL) {
                index = localDataManager.getMediaFile(-type, position);
            } else {
                index = localDataManager.getMediaFile(type, position);
            }
            Log.d(TAG, "startPlayer, index : " + index);

            // intent for start activity
            Intent intent = null;
            // Start picture player
            if (Constants.FILE_TYPE_PICTURE == type) {
                if (!Constants.bReleasingPlayer) {
                    intent = new Intent(activity, ImagePlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                } else {
                    String sMessage = activity.getString(R.string.busy_tip);
                    Toast toast = Toast.makeText(activity, sMessage, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                // Start music player
            } else if (Constants.FILE_TYPE_SONG == type) {
                intent = new Intent(activity, MusicPlayerActivity.class);



                // Start video player
            } else if (Constants.FILE_TYPE_VIDEO == type) {
                //modify because of mantis 0893708
                if (!Constants.bReleasingPlayer) {
                    intent = new Intent(activity, VideoPlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                } else {
                    String sMessage = activity.getString(R.string.busy_tip);
                    Toast toast = Toast.makeText(activity, sMessage, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

            }
            // the current broadcast media index
            Log.d("jdk", "index00===" + index);
            intent.putExtra(Constants.BUNDLE_INDEX_KEY, index);

            // boot media player
            if(Constants.FILE_TYPE_PICTURE == type){
                if(Constants.isExit){
                    activity.startActivity(intent);
                }else{
                    focusPosition = position;
                    handler.sendEmptyMessageDelayed(Constants.START_MEDIA_PLAYER, 500);
                }
                return;
            }
            activity.startActivity(intent);
        } else {
            Log.d(TAG, "Does not has specified type : " + type + " of media.");
        }
    }

    public void startPlayer(){
        startPlayer(mediaType, focusPosition);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void pressReturn(){
        String description = data.get(0).getDescription();
        // return TOP page
        if (Constants.RETURN_TOP.equals(description)) {
            handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);

            // previous directory level directory
        } else if (Constants.RETURN_LOCAL.equals(description)) {
            Message message = handler.obtainMessage();
            message.what = Constants.UPDATE_PROGRESS_INFO;
            message.arg1 = R.string.loading_local_resource;
            message.arg2 = Constants.PROGRESS_TEXT_SHOW;
            handler.sendMessage(message);

            browser(0, activityType);
        }
    }

    /**
     * processing key events.
     *
     * @ param keyCode
     *                key value.
     * @ param position
     *                response button ListView focus position.
     * @ return key event handling complete returning true,otherwise returns false.
     */
    protected boolean processKeyDown(int keyCode, int position) {
        Log.d(TAG, "processKeyDown: keyCode "+keyCode+" "+position);
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // ocus in the first "return", click on the previous page
            Log.d(TAG, "processKeyDown: ENTER");
            //if (position == Constants.POSITION_0 && !Tools.getUSBMountedPath().equals(LocalDataManager.ROOT_PATH)) {
            if (position == -2) {
//                String description = data.get(0).getDescription();
//                // return TOP page
//                if (Constants.RETURN_TOP.equals(description)) {
//                    handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);
//                    Log.d(TAG, "processKeyDown: Return Top");
//                    // previous directory level directory
//                } else if (Constants.RETURN_LOCAL.equals(description)) {
//                    Message message = handler.obtainMessage();
//                    message.what = Constants.UPDATE_PROGRESS_INFO;
//                    message.arg1 = R.string.loading_local_resource;
//                    message.arg2 = Constants.PROGRESS_TEXT_SHOW;
//                    handler.sendMessage(message);
//
//                    browser(-2, activityType);
//
//                    Log.d(TAG, "processKeyDown: Return Local");
//                }
                Message message = handler.obtainMessage();
                message.what = Constants.UPDATE_PROGRESS_INFO;
                message.arg1 = R.string.loading_local_resource;
                message.arg2 = Constants.PROGRESS_TEXT_SHOW;
                handler.sendMessage(message);

                browser(-2, activityType);

                Log.d(TAG, "processKeyDown: Return Local");

            } else {
                BaseData di;
                mediaType = 0;

                if (position < data.size()) {
                    di = data.get(position);
                    mediaType = di.getType();
                    Log.e(TAG, "processKeyDown, positon : " + position + " type "+mediaType + " "+di.getFormat());
                } else {
                    Log.e(TAG, "processKeyDown, positon : " + position + "type "+mediaType);
                }

                // media files on click confirm key activate a player
                if (Constants.FILE_TYPE_PICTURE == mediaType
                        || Constants.FILE_TYPE_SONG == mediaType
                        || Constants.FILE_TYPE_VIDEO == mediaType
                        || Constants.FILE_TYPE_MPLAYLIST == mediaType) {
                    // start player
                    if (Constants.GRIDVIEW_MODE == FileBrowserActivity.mListOrGridFlag) {
                        focusPosition= position;
                        Message msg = handler.obtainMessage();
                        msg.what = Constants.GRID_CANCEL_TASK;
                        msg.arg1 = Constants.GRID_CANCEL_TASK_NEED_PLAY;
                        handler.sendMessage(msg);
                        Log.d(TAG, "processKeyDown: Message: "+msg.toString());
                    }
                    else
                        startPlayer(mediaType, position);

                    // catalog click confirm button to enter catalog level
                    // directory level directory
                } else if (Constants.FILE_TYPE_DIR == mediaType) {
                    Message msg = handler.obtainMessage();
                    msg.what = Constants.UPDATE_PROGRESS_INFO;
                    msg.arg1 = R.string.loading_local_resource;
                    msg.arg2 = Constants.PROGRESS_TEXT_SHOW;
                    handler.sendMessage(msg);
                    Log.d(TAG, "processKeyDown: what"+msg);
                    browser(position, activityType);

                    // Not media file and folder is not when
                } else {
                    Message message = handler.obtainMessage();
                    message.what = Constants.UPDATE_EXCEPTION_INFO;
                    message.arg1 = Constants.UNSUPPORT_FORMAT;
                    handler.sendMessage(message);
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (Constants.LISTVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
                if (focusPosition == Constants.POSITION_0) {
                    // page up
                    refresh(Constants.KEYCODE_PAGE_UP);
                } else {
                    focusPosition = position;
                }
                return true;

            } else if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                if (focusPosition<Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM && focusPosition>=0) {
                    // page up
                    refresh(Constants.KEYCODE_PAGE_UP);
                } else {
                    focusPosition = position;
                }
                return true;

            }


        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (Constants.LISTVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
                if (focusPosition == Constants.POSITION_9) {
                    // page down
                    refresh(Constants.KEYCODE_PAGE_DOWN);
                } else {
                    focusPosition = position;
                }
                return true;
            } else if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                int lastRowStartPosition =Constants.GRID_MODE_DISPLAY_NUM-Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM-1;
                if (focusPosition<=Constants.GRID_MODE_DISPLAY_NUM && focusPosition>=lastRowStartPosition) {
                    // page down
                    refresh(Constants.KEYCODE_PAGE_DOWN);
                } else {
                    focusPosition = position;
                }
                return true;
            }
        }
        return false;
    }
}
