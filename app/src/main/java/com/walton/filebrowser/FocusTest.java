package com.walton.filebrowser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.walton.filebrowser.adapter.GridViewAdapter;
import com.walton.filebrowser.adapter.MenuListAdapter;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.dialog.EditDialog;
import com.walton.filebrowser.dialog.NormalDialog;
import com.walton.filebrowser.dialog.OnEditDialogListener;
import com.walton.filebrowser.dialog.OnNormalDialogButtonClickListener;
import com.walton.filebrowser.interfaces.OnUsbSateChanged;
import com.walton.filebrowser.managers.ThreadManager;
import com.walton.filebrowser.model.FileModel;
import com.walton.filebrowser.model.SourceCatItem;
import com.walton.filebrowser.receiver.USBStateReceiver;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.ui.main.LocalDataBrowser;
import com.walton.filebrowser.ui.media.activity.BaseActivity;
import com.walton.filebrowser.ui.media.util.FileUtil;
import com.walton.filebrowser.ui.media.util.LogUtil;
import com.walton.filebrowser.ui.music.MusicPlayerActivity;
import com.walton.filebrowser.ui.photo.ImagePlayerActivity;
import com.walton.filebrowser.ui.video.VideoPlayerActivity;
import com.walton.filebrowser.util.ApkUtil;
import com.walton.filebrowser.util.ConcurrentTotalFileSize;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.FilePath;
import com.walton.filebrowser.util.FileUtils;
import com.walton.filebrowser.util.GridViewTV;
import com.walton.filebrowser.util.SystemUtil;
import com.walton.filebrowser.util.ToastHelper;
import com.walton.filebrowser.util.Tools;
import com.walton.filebrowser.util.TypeUtils;
import com.walton.filebrowser.util.UiUtils;
import com.walton.filebrowser.view.NumberProgressBar;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import com.walton.filebrowser.wheellib.ScrollChoice;

public class FocusTest extends BaseActivity implements OnUsbSateChanged, View.OnFocusChangeListener {

    final static String TAG = FocusTest.class.getSimpleName();
    public static String ROOT_PATH = "";
    public static List<File> mNowTask = new ArrayList<File>();
    public static List<FileModel> fileModels = new ArrayList<>();
    public static String totalStorage, freeStorage, usbName;
    public static int progressValue;
    public static boolean isActivityFirstTimeLoad = true;
    public static List<BaseData> subListData = new ArrayList<>();
    public static String selectedCatItemName;
    public static List<BaseData> backupMediaItems;
    static String dabradabra;

    // data container
    public MediaContainerApplication mediaContainer = null;
    public String mCurrentDirectory;
    public MainUpView mainUpView1;
    public String key = "";
    public List<BaseData> listOfMedia;
    int index = 0;
    Intent intent = null;
    // local usb disk data browsing class
    private LocalDataBrowser localDataBrowser = null;
    // Click confirm focus position media types
    private int mediaType;
    /**
     * Determine whether to copy or paste, the default is to copy
     */
    private boolean mIsCopy = true;
    private SharedPreferences mSharedPre;
    private boolean mIsSelecting = false;
    private NormalDialog mNormalDialog;
    private AlertDialog mDeleteDialog;
    private EditDialog mEditDialog;
    private File mRootFile;

    //    private GetDataRunnable mDataRunnable;
    private Stack<Integer> lastSelectedItem = new Stack<>();
    private TextView txtFilePath;
    private String selectedFileName;
    private MenuListAdapter mMenuAdapter;
    //    private SimpleAdapter mMenuAdapter;
    private List<Map<String, Object>> mMenuData;
    private RelativeLayout mRlvMenu;
    private ListView mMenu;
    private boolean mCancleCopy = false;
    private View pastView;
    private AlertDialog mProgressDialog;
    private NumberProgressBar mProgeressBar;
    private long mTotalSize;
    private CopyRunable mRunable;
    private List<SourceCatItem> sourceCatItemList = new ArrayList<>();
    private TextView txtCircularViewOne, txtCircularViewTwo, txtCircularViewThree, txtCircularViewFour, txtCircularViewFive, txtCircularViewSix, txtCircularViewSeven;
    private HashMap<Integer, Boolean> mCheckedCbs;
    private File[] files;
    private FileModel fileModel;
    private GridViewAdapter mAdapter;
    private TextView textview_device_name;
    private View mOldView;
    private GridViewTV gridView;
    // 延时请求初始位置的item.
    Handler mFirstHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            gridView.setDefualtSelect(0);
        }
    };
    private USBStateReceiver usbStateReceiver;
    private LinearLayout layoutSystemStorage;
    private TextView txtSysFreeSpace, txtSelectedCategory, tv_count, txtUsbNameNew, txtNull;
    private List<String> categoryItems1, categoryPhotosList, categoryVideoList, categoryAudioList, categoryDocsList, categoryAppsList, categoryZipList;
    private boolean isWheelFocus;
    //    private CircularListView circularListView;
    private int gridViewSelectedItem;
    private AVLoadingIndicatorView loadingIndecator;
    private Intent arguments;
    private List<BaseData> sourceData = new ArrayList<BaseData>();
    private ArrayList<BaseData> tmpArray = new ArrayList<BaseData>();
    private ProgressBar progressBar;
    // Empty layout views
    private LinearLayout layoutEmptyPage;
    private Button btnCreate;
    private boolean isBusyScroller = false;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == Constants.BROWSER_LOCAL_DATA) {
                excuteGetAllFiles();
            }
        }
    };
    private BaseData bs;
    private boolean isSameDirectory;
    private int mCurrentProgress = 0;
    private boolean mPageDown;
    private String l, tempCurrentDirectory, tempRootFile;
    private long lastScrollTime;
    private Comparator<BaseData> comparator = new Comparator<BaseData>() {

        @Override
        public int compare(BaseData lData, BaseData rData) {

            String lName = lData.getName();
            String rName = rData.getName();
            if (lName != null && rName != null) {
                Collator collator = Collator.getInstance(Locale.ENGLISH);
                return collator.compare(lName.toLowerCase(),
                        rName.toLowerCase());

            } else {
                Log.e("comparator", "lName != null && rName != null is false");
                return 0;
            }

        }
    };
    private File lastCopyFile;
    /**
     * 粘贴文件
     */
    public HashMap<Integer, Boolean> getmCheckedCbs() {
        return mCheckedCbs;
    }

    @Override
    public void updateList(boolean flag) {

    }

    @Override
    public void fill(File file) {

    }

    @Override
    public Handler getHandler() {
        return null;
    }

//    private
//    void refreshCircular() {
////        if (mIsAdapterDirty) {
////            new Handler().post(new Runnable() {
////                @Override
////                public void run() {
////                    circularListView.scrollFirstItemToCenter();
////                    mIsAdapterDirty = false;
////                }
////            });
////        }
//
//        TextView centerView = (TextView) circularListView.getCentralChild();
//
//        if (centerView != null) {
//            centerView.setTextColor(getResources().getColor(R.color.colorAccent));
//            centerView.setTypeface(Typeface.DEFAULT_BOLD);
//        }
//        for (int i = 0; i < circularListView.getChildCount(); i++) {
//            TextView view = (TextView) circularListView.getChildAt(i);
//            if (view != null && view != centerView) {
//                view.setTextColor(getResources().getColor(R.color.sky_blue_light));
//
//                view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//            }
//        }
//    }

    @Override
    public void getFiles(String path) {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initUI() {

        txtCircularViewOne = findViewById(R.id.txtCircularViewOne);
        txtCircularViewTwo = findViewById(R.id.txtCircularViewTwo);
        txtCircularViewThree = findViewById(R.id.txtCircularViewThree);
        txtCircularViewFour = findViewById(R.id.txtCircularViewFour);
        txtCircularViewFive = findViewById(R.id.txtCircularViewFive);
        txtCircularViewSix = findViewById(R.id.txtCircularViewSix);
        txtCircularViewSeven = findViewById(R.id.txtCircularViewSeven);


        progressBar = findViewById(R.id.pbarHome);

        layoutEmptyPage = findViewById(R.id.layoutEmptyPage);
        layoutEmptyPage.setVisibility(View.GONE);

        btnCreate = findViewById(R.id.btnCreate);

        tv_count = findViewById(R.id.tv_count);

        txtUsbNameNew = findViewById(R.id.txtUsbNameNew);
        txtUsbNameNew.setText(arguments.getStringExtra("usb_name"));

//        circularListView = findViewById(R.id.circularListView);
//        circularListView.setOnFocusChangeListener(this);

        txtSelectedCategory = findViewById(R.id.txtSelectedCategory);

        txtSysFreeSpace = findViewById(R.id.txtSysFreeSpace);

        layoutSystemStorage = findViewById(R.id.layoutSystemStorage);


        mMenu = findViewById(R.id.lv_menu);
        mMenu.setNextFocusUpId(R.id.lv_menu);
        mMenu.setNextFocusDownId(R.id.lv_menu);
        mMenu.setNextFocusLeftId(R.id.lv_menu);
        mMenu.setNextFocusRightId(R.id.lv_menu);

        txtFilePath = findViewById(R.id.txtFilePath);
        txtFilePath.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtFilePath.setSelected(true);
        txtFilePath.setSingleLine(true);
//        txtFilePath.setText("Oxfam says 8 men as rich as half the world. | Govt may set threshold for probe into deposits. | At least 32 dead after Turkish plane hits village.");
//        txtUsbName = view.findViewById(R.id.txtUsbName);
//        txtUsbName.setText(key.substring(0, 1).toUpperCase() + key.substring(1));


        gridView = findViewById(R.id.gridView);
        gridView.setOnFocusChangeListener(this);
//        gridView.setNextFocusLeftId(R.id.circularListView);

        mainUpView1 = findViewById(R.id.mainUpView1);
        mainUpView1.setVisibility(View.GONE);
        mainUpView1.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
        bridget.setTranDurAnimTime(0);
        // 设置移动边框的图片.
        mainUpView1.setUpRectResource(R.drawable.app_item_bg);
        // 移动方框缩小的距离.
        mainUpView1.setDrawUpRectPadding(new Rect(3, 3, 3, 3));

        setDataToSourceCatList();

        txtCircularViewFour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtCircularViewFour.setTypeface(null, Typeface.NORMAL);
                    txtCircularViewFour.setAlpha(1.0f);
//                    zoomIn(v);
                } else {
                    txtCircularViewFour.setTypeface(null, Typeface.NORMAL);
                    txtCircularViewFour.setAlpha(0.5f);
//                    zoomOut(v);
                }
            }
        });

        gridView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

//                            gridView.setFocusable(true);
//                            gridView.requestFocus();

                            try {
                                gridView.setSelection(gridViewSelectedItem);
                                mainUpView1.setFocusView(getViewByPosition(gridViewSelectedItem), 1.0f);
                            } catch (Exception e) {
                                Log.d(TAG, "run: " + e.getMessage());
                                gridViewSelectedItem = 0;
                                gridView.setSelection(gridViewSelectedItem);
                                mainUpView1.setFocusView(getViewByPosition(gridViewSelectedItem), 1.0f);
                            }
                        }
                    }, 10);

                } else {
                    gridView.post(new Runnable() {
                        @Override
                        public void run() {
                            mainUpView1.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        initMenu();

        new Thread(() -> {
            String freeSpace = freeStorage + " available of " + totalStorage;
            txtSysFreeSpace.setText(freeSpace);
            progressBar.setSecondaryProgress(100);
            progressBar.setProgress(FocusTest.progressValue);

            if (FocusTest.progressValue > 85) {
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_low_space));
            } else {
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_horizontal));
            }

        }).start();
    }

    private void zoomIn(View view) {
        if (view != null) {
            view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start();
        }
    }

    private void zoomOut(View view) {
        if (view != null) {
            view.animate().scaleX(1).scaleY(1).setDuration(200).start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_test);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loadingIndecator = findViewById(R.id.avi);
        loadingIndecator.setVisibility(View.VISIBLE);

        arguments = getIntent();

        initUI();
        Log.d(TAG, "onCreate: Hurrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        mediaContainer = MediaContainerApplication.getInstance();

        usbStateReceiver = new USBStateReceiver(this);
        registerReceiver(usbStateReceiver, usbStateReceiver.getIntentFilter());
        mSharedPre = getSharedPreferences("config", MODE_PRIVATE);

        mCurrentDirectory = arguments.getStringExtra("rootDir");
        key = arguments.getStringExtra("key");

        /*
         * Below ROOT_PATH is for musicplayer playlist. Playlist genarate by this path
         * */
        FocusTest.ROOT_PATH = mCurrentDirectory + "/";
        dabradabra = mCurrentDirectory;

        startScan(FocusTest.ROOT_PATH);

        mRootFile = new File(mCurrentDirectory);
        txtFilePath.setText(mRootFile.getAbsolutePath());

        mCheckedCbs = new HashMap<>();


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * 这里注意要加判断是否为NULL.
                 * 因为在重新加载数据以后会出问题.
                 */
                if (view != null) {
                    mainUpView1.setFocusView(view, mOldView, 1.0f);
                    gridViewSelectedItem = position;
                }
                mOldView = view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint({"WrongConstant", "SdCardPath"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: "+mCurrentDirectory);
                if (mIsSelecting) {
                    CheckBox cb = view.findViewById(R.id.cb_checked);
                    if (cb.getVisibility() == View.VISIBLE) {
                        cb.setChecked(!cb.isChecked());
                        mCheckedCbs.put(position, cb.isChecked());
                        return;
                    }
                }

                if (view != null) {
                    mainUpView1.setFocusView(view, mOldView, 1.0f);
                }
                mOldView = view;

                selectedFileName = files[position].getName();

                if (files[position].isDirectory()) {

                    lastSelectedItem.push(position);

                    FocusTest.ROOT_PATH = mCurrentDirectory + "/";

                    //startScan(FocusTest.ROOT_PATH);

                    if (files.length > 0) {

                        loadingIndecator.smoothToShow();


                        mCurrentDirectory = files[position].getAbsolutePath();
                        /*
                         * Below ROOT_PATH is for musicplayer playlist. Playlist genarate by this path
                         * */

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mainUpView1.setVisibility(View.GONE);
                            }
                        });

//                        runOnUiThread(() -> excuteClickedItemFiles());
                        excuteClickedItemFiles();

                    } else {
                        //getActivity().runOnUiThread(() -> excuteGetAllFiles());
                    }
                } else {
                    String name = files[position].getName();
                    if (TypeUtils.isVideo(name)) {

                        Log.d(TAG, "onItemClick: " + position);
                        startPlayer(Constants.FILE_TYPE_VIDEO, position);

                    }
                    if (TypeUtils.isImage(name)) {

                        startPlayer(Constants.FILE_TYPE_PICTURE, position);

                    }
                    if (TypeUtils.isTxt(name)) {
                        try {
                            ApkUtil.openFile(getApplicationContext(), files[position]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (TypeUtils.isAudio(name)) {

                        startPlayer(Constants.FILE_TYPE_SONG, position);
                    }
                    if (name.endsWith(".apk")) {


//                        eventManager.share(new ArrayList<File>().addAll(files));

                        try {
                            ApkUtil.installApk(getApplicationContext(), files[position].getPath(), files[position]);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mFirstHandler.sendMessageDelayed(mFirstHandler.obtainMessage(), 188);
    }

    private void startPlayer(int type, int position) {

//        boolean hasMedia = false;
//        // check whether has the specified type of media
//        hasMedia = MediaContainerApplication.getInstance().hasMedia(type);
//
//
//        listOfMedia = new ArrayList<>();
//        listOfMedia.clear();
//
//        for (int u = 0; u < files.length; u++) {
//            if (TypeUtils.isAudio(files[u].getName())) {
//                Log.d(TAG, "startPlayer: Audio file" + u);
//                bs = new BaseData();
//                bs.setName(files[u].getName());
//                bs.setPath(files[u].getPath());
//                listOfMedia.add(bs);
//            } else if (TypeUtils.isVideo(files[u].getName())) {
//                Log.d(TAG, "startPlayer: Video file" + u);
//                bs = new BaseData();
//                bs.setName(files[u].getName());
//                bs.setPath(files[u].getPath());
//                listOfMedia.add(bs);
//            }
//        }
//        if (listOfMedia.size() > 0) {
//            for (int i = 0; i < listOfMedia.size(); i++) {
//                if (files[position].getPath().replace(" ", "%20").toLowerCase().equalsIgnoreCase(listOfMedia.get(i).getPath().replace(" ", "%20").toLowerCase())) {
//                    index = i;
//                }
//            }
//        }
//
//        Log.d(TAG, "startPlayer: Hurrrrrrrrrrrraaaaaaaaaaaahhhhhhhhhh " + index);
//
//
////        int index = getMediaFile(type, position);
//        Log.d(TAG, "startPlayer, index : " + index + " position:" + position + " " + "PATH:" + files[position].getPath() + " FILES:" + files.length + " " + " " + "Media Files:" + listOfMedia.size());
//
//        MediaContainerApplication.getInstance().clearMediaSongList();
//        MediaContainerApplication.getInstance().putMediaData(Constants.OPTION_STATE_SONG, listOfMedia);
//
//        Intent intent = null;
//
//        // Start video player
//        if (Constants.FILE_TYPE_VIDEO == type) {
//            intent = new Intent(FocusTest.this, VideoPlayerActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//            // Start Image Player
//        } else if (Constants.FILE_TYPE_PICTURE == type) {
//            intent = new Intent(FocusTest.this, ImagePlayerActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        } else if (Constants.FILE_TYPE_SONG == type) {
//            intent = new Intent(FocusTest.this, MusicPlayerActivity.class);
//        }
//
////        if (!Constants.bReleasingPlayer) {
////            intent = new Intent(this, VideoPlayerActivity.class);
////            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////        } else {
////            String sMessage = getResources().getString(R.string.busy_tip);
////            Toast toast = Toast.makeText(this, sMessage, Toast.LENGTH_SHORT);
////            toast.setGravity(Gravity.CENTER, 0, 0);
////            toast.show();
////            return;
////        }
//        // the current broadcast media index
//        Log.d(TAG, "index00===" + index);
//        intent.putExtra(Constants.BUNDLE_INDEX_KEY, index);
//
//        if (loadingIndecator.getVisibility() == View.VISIBLE) {
//            loadingIndecator.smoothToHide();
//            loadingIndecator.setVisibility(View.GONE);
//        }
//
//        startActivity(intent);

        new StartPlayerBackGround().execute(type, position);

    }

    protected int getMediaFile(int type, int position) {
        Log.d(TAG, "getMediaFile: POS:" + position);
        subListData.clear();
        // Obtain all media files
        ArrayList<BaseData> mediaFiles = new ArrayList<BaseData>();
        // get all the data
        List<BaseData> allFiles = new ArrayList<BaseData>();
        // file type switch in all types mode

        if (type < 0) {
            Log.d("jdk", "type===" + type);
            allFiles.addAll(getUIDataList(Constants.OPTION_STATE_ALL));
            mediaFiles.addAll(getMediaFileList(-type));

            // file type switch in pictures, music, or video mode
        } else {
            allFiles.addAll(getUIDataList(Constants.OPTION_STATE_ALL));
            mediaFiles.addAll(getMediaFileList(type));
        }
        subListData.addAll(mediaFiles);
        // Index conversion
        int index = position;

        int size = allFiles.size();
        Log.d(TAG, "getMediaFile: Size:549=" + size + " and index:" + index);
        if (index >= 0 && index < size) {
            BaseData bd = allFiles.get(index);
            String path = bd.getPath();//files[position].getPath();
            Log.d(TAG, "getMediaFile: AllFiles Size:" + allFiles.size());
            Log.d(TAG, "getMediaFile: Path:" + path);
            index = 0;
            // Calculating the current click the media file subscript
            for (BaseData item : mediaFiles) {
                if (path.equals(item.getPath())) {
                    Log.d("jdk", "path===" + path + ",item.getPath()=" + item.getPath());
                    return index;
                } else {
                    index++;
                }
            }
        }

        return 0;
    }

    public final synchronized List<BaseData> getUIDataList(final int type) {
        List<BaseData> local = new ArrayList<BaseData>();
        // Add all folders data
        local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_DIR));

        if (type == Constants.OPTION_STATE_ALL) {
            // Add all file data
            local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_FILE));

            // Add all designated type of media files
        } else {
            local.addAll(getMediaFileList(type));
        }

        Log.d(TAG, "getUIDataList: DataListSize: " + local.size());
        return local;
    }

    public final synchronized ArrayList<BaseData> getMediaFileList(
            final int type) {
        ArrayList<BaseData> local = new ArrayList<BaseData>();
        Log.d(TAG, "getMediaFileList: type " + type);
        switch (type) {
            case Constants.OPTION_STATE_PICTURE:
                // Add all pictures data
                local.addAll(mediaContainer
                        .getMediaData(Constants.FILE_TYPE_PICTURE));

                break;

            case Constants.OPTION_STATE_SONG:
                // Add all the music data
                local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_SONG));

                break;

            case Constants.OPTION_STATE_VIDEO:
                // Add all the video data
                local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_VIDEO));

                break;

            default:
                break;
        }
        Log.d(TAG, "getMediaFileList: " + local.size());
        return local;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDataToSourceCatList() {


        categoryItems1 = new ArrayList<>();
        categoryItems1.add(Constants.DOCS);
        categoryItems1.add(Constants.APPS);
        categoryItems1.add(Constants.ZIP);
        categoryItems1.add(Constants.ALL);
        categoryItems1.add(Constants.PHOTOS);
        categoryItems1.add(Constants.VIDEOS);
        categoryItems1.add(Constants.AUDIO);

        categoryPhotosList = new ArrayList<>();
        categoryPhotosList.add(Constants.APPS);
        categoryPhotosList.add(Constants.ZIP);
        categoryPhotosList.add(Constants.ALL);
        categoryPhotosList.add(Constants.PHOTOS);
        categoryPhotosList.add(Constants.VIDEOS);
        categoryPhotosList.add(Constants.AUDIO);
        categoryPhotosList.add(Constants.DOCS);


        categoryVideoList = new ArrayList<>();
        categoryVideoList.add(Constants.ZIP);
        categoryVideoList.add(Constants.ALL);
        categoryVideoList.add(Constants.PHOTOS);
        categoryVideoList.add(Constants.VIDEOS);
        categoryVideoList.add(Constants.AUDIO);
        categoryVideoList.add(Constants.DOCS);
        categoryVideoList.add(Constants.APPS);

        categoryAudioList = new ArrayList<>();
        categoryAudioList.add(Constants.APPS);
        categoryAudioList.add(Constants.PHOTOS);
        categoryAudioList.add(Constants.VIDEOS);
        categoryAudioList.add(Constants.AUDIO);
        categoryAudioList.add(Constants.DOCS);
        categoryAudioList.add(Constants.APPS);
        categoryAudioList.add(Constants.ZIP);

        categoryDocsList = new ArrayList<>();
        categoryDocsList.add(Constants.PHOTOS);
        categoryDocsList.add(Constants.VIDEOS);
        categoryDocsList.add(Constants.AUDIO);
        categoryDocsList.add(Constants.DOCS);
        categoryDocsList.add(Constants.APPS);
        categoryDocsList.add(Constants.ZIP);
        categoryDocsList.add(Constants.ALL);

        categoryAppsList = new ArrayList<>();
        categoryAppsList.add(Constants.VIDEOS);
        categoryAppsList.add(Constants.AUDIO);
        categoryAppsList.add(Constants.DOCS);
        categoryAppsList.add(Constants.APPS);
        categoryAppsList.add(Constants.ZIP);
        categoryAppsList.add(Constants.ALL);
        categoryAppsList.add(Constants.PHOTOS);

        categoryZipList = new ArrayList<>();
        categoryZipList.add(Constants.AUDIO);
        categoryZipList.add(Constants.DOCS);
        categoryZipList.add(Constants.APPS);
        categoryZipList.add(Constants.ZIP);
        categoryZipList.add(Constants.ALL);
        categoryZipList.add(Constants.PHOTOS);
        categoryZipList.add(Constants.VIDEOS);


//
//
//        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//        listAdapter.addAll(categoryItems1);
//        Display display = getWindowManager().getDefaultDisplay();
//        circularListView.setRadius(200);
//        circularListView.setAdapter(listAdapter);
//        circularListView.setNextFocusRightId(R.id.gridView);
//
////        new Handler().post(() -> {
////            circularListView.scrollFirstItemToCenter();
////        });
//
//
//        circularListView.setCircularListViewContentAlignment(CircularListViewContentAlignment.Right);
//
//        circularListView.setCircularListViewListener(new CircularListViewListener() {
//            @Override
//            public void onCircularLayoutFinished(CircularListView circularListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                refreshCircular();
//            }
//        });
//
//        circularListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (circularListView.getCentralPosition() == position) {
//
//                    if (circularListView.hasFocus()) {
//
//                        excuteGetAllFiles();
//
//                        lastSelectedItem.empty();
//
//                        mCurrentDirectory = mRootFile.getAbsolutePath();
//                        txtSelectedCategory.setText((String) circularListView.getSelectedItem());
//                        txtFilePath.setText(mRootFile.getAbsolutePath());
//                        txtFilePath.setSelected(true);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private List<FileModel> getFileModels(String path) {

        fileModels.clear();

        selectedCatItemName = txtCircularViewFour.getText().toString().trim();


        File directory = new File(path);
        files = directory.listFiles();

        if (files != null && files.length > 0) {
            files = FileUtils.sort(files, selectedCatItemName);

            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                fileModel = new FileModel();
                fileModel.setFileName(files[i].getName());
                fileModel.setFilePath(files[i].getPath());

                fileModels.add(fileModel);
            }
        }


        return fileModels;
    }

    private void excuteGetAllFiles() {

        Observable<List<FileModel>> fileObservable = Observable.fromCallable(() -> getFileModels(mRootFile.getAbsolutePath()));
        fileObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FileModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<FileModel> fileModelsList) {
                        mAdapter = new GridViewAdapter(getApplicationContext(), fileModelsList, files, mCheckedCbs);
                        gridView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onError(Throwable e) {
                        isBusyScroller = false;
                        loadingIndecator.smoothToHide();
                    }

                    @Override
                    public void onComplete() {

                        isBusyScroller = false;

                        loadingIndecator.setVisibility(View.GONE);

                        mAdapter.update(files);

                        isBusyScroller = false;

                        if (files.length == 0) {
                            mainUpView1.setVisibility(View.GONE);

                            layoutEmptyPage.setVisibility(View.VISIBLE);

                            txtCircularViewFour.setFocusable(true);
                            txtCircularViewFour.requestFocus();

                        } else {
                            layoutEmptyPage.setVisibility(View.GONE);

                            txtCircularViewFour.setFocusable(true);
                            txtCircularViewFour.requestFocus();

//                            gridView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    gridView.setSelection(0);
//                                    gridView.setFocusable(true);
//                                    gridView.requestFocus();
//
//                                    mainUpView1.setFocusView(getViewByPosition(0), 1.0f);
//                                }
//                            });

                        }

                    }
                });
    }

    private void excuteClickedItemFiles() {

        fileModels.clear();

        gridViewSelectedItem = 0;

        Observable<List<FileModel>> fileObservable = Observable.fromCallable(() -> getFileModels(mCurrentDirectory));
        fileObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FileModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<FileModel> fileModelsList) {
                        mAdapter = new GridViewAdapter(getApplicationContext(), fileModelsList, files, mCheckedCbs);
                        gridView.setAdapter(mAdapter);
                        mAdapter.update(files);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        loadingIndecator.setVisibility(View.GONE);


                        txtFilePath.append("/" + selectedFileName);
                        txtFilePath.setSelected(true);

                        if (files.length == 0) {
                            mainUpView1.setVisibility(View.GONE);
                            layoutEmptyPage.setVisibility(View.VISIBLE);

                            txtCircularViewFour.setFocusable(true);
                            txtCircularViewFour.requestFocus();
                        } else {
                            gridView.post(new Runnable() {
                                @Override
                                public void run() {
                                    gridView.setSelection(0);
                                    gridView.setFocusable(true);
                                    gridView.requestFocus();
                                    mainUpView1.setFocusView(getViewByPosition(gridViewSelectedItem), 1.0f);
                                }
                            });
                            layoutEmptyPage.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public View getViewByPosition(int pos) {
        final int firstListItemPosition = gridView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + gridView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return gridView.getAdapter().getView(pos, null, gridView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return gridView.getChildAt(childIndex);
        }

    }

    @Override
    public void onFocusChange(View v, boolean b) {
       /* switch (v.getId()) {
            case R.id.circularListView:
//                if (b) {
//                    gridViewSelectedItem = 0;
//                    TextView centerView = (TextView) circularListView.getCentralChild();
//                    isWheelFocus = true;
//                    centerView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//
//                } else {
//                    TextView centerView = (TextView) circularListView.getCentralChild();
//                    isWheelFocus = false;
//
//                    centerView.setTextColor(ContextCompat.getColor(this, R.color.sky_blue));
//
//                }
                break;
        }*/
    }

    @Override
    public void onUsbSateChange(boolean isConnected, String path) {

        Log.d(TAG, "OnUsbStateChange: " + isConnected + " " + path);


        try {
            if (!isConnected) {
                //ToastHelper.newInstance(this).show("Removed");
                closeFragment();
            } else {
                closeFragment();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {

    }

    private void excuteCopyFile() {
        mRunable = new CopyRunable();
        //异步复制文件
        ThreadManager.getInstance().getmLongThreadPool().excute(mRunable);

    }

    /**
     * 创建新文件夹
     */
    private void createNewFolder() {
        mEditDialog = new EditDialog(this, "New Folder", "New Folder");

        mEditDialog.setmOnEditDialogListener(new OnEditDialogListener() {
            @Override
            public void onOkButtonClick(String name) {
                Log.d(TAG, "onOkButtonClick: " + mCurrentDirectory + "/" + name);
                if (FileUtil.isFileExist(mCurrentDirectory + "/" + name)) {
                    Log.d(TAG, "onOkButtonClick: Already");
                    ToastHelper.newInstance(UiUtils.getContext()).show("File already exists!");
                } else {
                    FileUtils.createFolder(mCurrentDirectory, name);
                    String mCurrentDir = mCurrentDirectory;
                    File updateFile = new File(mCurrentDir);
                    update(updateFile);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gridView.setSelection(0);
                            gridView.setFocusable(true);
                            gridView.requestFocus();
                        }
                    }, 200);
                    mEditDialog.dismiss();
                }
            }
        });
        mEditDialog.show();
    }

    public void cutFile() {
        File[] currentSeletcted = getCurrentSeletcted();
        if (currentSeletcted == null) {
            int selectedItemPosition = gridView.getSelectedItemPosition();
            if (selectedItemPosition < 0) {
                return;
            }
            File file = getmFileList()[selectedItemPosition];
            mNowTask.add(file);
            return;
        }
        for (int i = 0; i < currentSeletcted.length; i++) {
//            添加到复制任务中
            mNowTask.add(currentSeletcted[i]);
        }
    }

    /**
     * 进入多选状态
     */
    public void selectSomeBroadCast() {
        //多选操作
        mIsSelecting = true;
        selectSome();
        //发广播通知
        Intent intent = new Intent("com.walton.filebrowser.getstate");
        intent.putExtra("state", 1);
        sendBroadcast(intent);
    }

    public void selectSome() {
        if (mAdapter.getmCbs() != null) {
            //让所有的CheckBox都显示
            for (CheckBox cb : mAdapter.getmCbs()
            ) {
                cb.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 退出多选选状态
     */
    public void unSelectSome() {
        //让所有的CheckBox都显示
        for (CheckBox cb : mAdapter.getmCbs()) {
            cb.setVisibility(View.INVISIBLE);
            cb.setChecked(false);
        }
        mCheckedCbs.clear();
    }

    /**
     * 复制文件
     */
    private void copyFile() {
        File[] currentSeletcted = getCurrentSeletcted();
        if (currentSeletcted == null) {
            int selectedItemPosition = gridView.getSelectedItemPosition();
            if (selectedItemPosition < 0) {
                return;
            }
            File file = getmFileList()[selectedItemPosition];
            mNowTask.add(file);
            return;
        }
        for (int i = 0; i < currentSeletcted.length; i++) {
//            添加到复制任务中
            mNowTask.add(currentSeletcted[i]);
        }
    }

    /**
     * Get the current selected item
     *
     * @return Get all currently selected files
     */
    private File[] getCurrentSeletcted() {
        int length = getmFileList().length;
        List<File> listFiles = new ArrayList<File>();
        //先判断用户是否选中
        //获取选中的集合
        HashMap<Integer, Boolean> checkeds = getmCheckedCbs();
        if (checkeds == null || checkeds.size() == 0) {
            return null;
        }
        File[] files = getmFileList();
        Set<Integer> keySet = checkeds.keySet();
        int j = 0;
        for (int i = 0; i < files.length; i++) {
            if (keySet.contains(i) && checkeds.get(i) != null && checkeds.get(i)) {
                listFiles.add(files[i]);
            }
        }
        return listFiles.toArray(new File[listFiles.size()]);
    }

    public File[] getmFileList() {
        return files;
    }

    public boolean dispathcKeyEvent(KeyEvent event) {
        if (mRlvMenu.getVisibility() == View.VISIBLE) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    if (mMenu.getSelectedItemPosition() == 0) {
                        mMenu.setSelection(0);
                        return true;
                    }
                    mPageDown = false;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (mMenu.getSelectedItemPosition() == mMenu.getChildCount() - 1) {
                        mMenu.setSelection(0);
                        return true;
                    }
                    mPageDown = true;
                }
            }
        } else {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if (isWheelFocus) {

                        }
                    } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (isWheelFocus) {
                        }
                    }
                }
            }
        }
        return true;
    }

    private void renameFile() {
        final int selectedItemPosition = gridView.getSelectedItemPosition();
        if (selectedItemPosition < 0) {
            ToastHelper.newInstance(UiUtils.getContext()).show("No item selected!");
            return;
        }
        final File[] files = getmFileList();
        String fileName = files[selectedItemPosition].getName();
        final File file = files[selectedItemPosition];

        mEditDialog = new EditDialog(this, "Rename File", fileName);


        mEditDialog.setmOnEditDialogListener(name -> {
            boolean res = FileUtils.renameFile(file, name);
            if (!res) {
                return;
            }
            String mCurrentDir = mCurrentDirectory;
            File updateFile = new File(mCurrentDir);
            update(updateFile);
            mEditDialog.dismiss();
        });
        mEditDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        txtFilePath.setText(mRootFile.getAbsolutePath());
//        txtFilePath.setSelected(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Called");
        subListData.clear();
        fileModels.clear();
        mNowTask.clear();
        unregisterReceiver(usbStateReceiver);
    }

    private void update(final File file) {
        if (file == null) {
            return;
        }
        layoutEmptyPage.setVisibility(View.GONE);

        files = FileUtils.sort(file.listFiles(), txtCircularViewFour.getText().toString().trim());

        mAdapter.update(files);
    }

    public void showMenu() {
        if (mRlvMenu.getVisibility() == View.VISIBLE) {
            Log.d("Loooo", "HideMenu " + gridViewSelectedItem);
            mRlvMenu.setVisibility(View.GONE);
            if (files.length > 0) {
                gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        gridView.setFocusable(true);
                        gridView.requestFocus();
                        gridView.setSelection(gridViewSelectedItem);
                        mainUpView1.setFocusView(getViewByPosition(gridViewSelectedItem), 1.0f);
                    }
                });
            } else {
                txtCircularViewFour.setFocusable(true);
                txtCircularViewFour.requestFocus();
            }
        } else {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mMenu.setFocusable(true);
                    mMenu.requestFocus();
                }
            });

            Log.d("Loooo", "ShowMenu " + gridViewSelectedItem);

            if (mNowTask.size() <= 0) {
                mMenuAdapter.setTaskAvailable(false);
            } else {
                mMenuAdapter.setTaskAvailable(true);
            }

            if (files.length == 0) {
                mMenuAdapter.setFolderIsEmpty(true);
                if (mNowTask.size() > 0) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mMenu.setSelection(1);
                        }
                    });
                } else {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mMenu.setSelection(4);
                        }
                    });
                }
            } else {
                mMenuAdapter.setFolderIsEmpty(false);
                mMenu.setSelection(0);
            }

            mMenu.setAdapter(mMenuAdapter);

            mRlvMenu.setVisibility(View.VISIBLE);
        }
    }

    private void initMenu() {
        mMenuData = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("iv_menu", R.drawable.select_all);
        map1.put("tv_menu", "Multiple selection");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("iv_menu", R.drawable.rename);
        map2.put("tv_menu", "Rename");
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("iv_menu", R.drawable.copy);
        map3.put("tv_menu", "Copy");
//        HashMap<String, Object> map4 = new HashMap<>();
//        map4.put("iv_menu", R.drawable.cut);
//        map4.put("tv_menu", "Cutting");
        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("iv_menu", R.drawable.paste);
        map5.put("tv_menu", "Paste");

        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("iv_menu", R.drawable.delete);
        map6.put("tv_menu", "Delete");
        HashMap<String, Object> map7 = new HashMap<>();
        map7.put("iv_menu", R.drawable.new_folder);
        map7.put("tv_menu", "New");


//        mMenuData.add(map4);
        mMenuData.add(map3);
        mMenuData.add(map5);
        mMenuData.add(map1);
        mMenuData.add(map6);
        mMenuData.add(map7);
        mMenuData.add(map2);

        mMenu.setTag("menu");
        mMenu.setOnFocusChangeListener(this);
        mMenu.setItemsCanFocus(true);
        mRlvMenu = findViewById(R.id.rl_menu);


        mMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    //Determine if you need to skip the pasted function
                    String category_task = mSharedPre.getString("category_task", "");
                    Log.d(TAG, "onItemSelected: " + category_task);
                    if (mNowTask.size() <= 0 && TextUtils.isEmpty(category_task)) {
                        if (files.length > 0) {
                            if (mPageDown) {
                                mMenu.setSelection(i + 1);
                            } else {
                                mMenu.setSelection(i - 1);
                            }
                        } else {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mMenu.setSelection(4);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (mMenuAdapter == null) {
            mMenuAdapter = new MenuListAdapter(this, mMenuData);
        }
        mMenu.setAdapter(mMenuAdapter);

        mMenu.setOnItemClickListener((adapterView, view1, i, l) -> {

            switch (i) {
                case 111:
                    hideSideMenu();
                    mSharedPre.edit().putString("category_task", "").apply();
                    mIsCopy = false;
                    mNowTask.clear();
                    cutFile();
                    if (mNowTask.size() > 0) {
                        ToastHelper.newInstance(this).show(R.string.clipbrd);
                    } else {
                        ToastHelper.newInstance(this).show(R.string.clipbrd_fialed);
                    }
                    unSelectSome();
                    exitSelectmode();
                    break;
                case 0:
                    mSharedPre = getSharedPreferences("config", MODE_PRIVATE);
                    mIsCopy = true;
                    mNowTask.clear();
                    copyFile();
                    hideSideMenu();
                    if (mNowTask.size() > 0) {
                        ToastHelper.newInstance(UiUtils.getContext()).show(R.string.clipbrd);
                    } else {
                        ToastHelper.newInstance(UiUtils.getContext()).show(R.string.clipbrd_fialed);
                    }
                    unSelectSome();
                    exitSelectmode();
                    break;
                case 1:
                    hideSideMenu();
                    pasteFile();
                    break;
                case 2:
                    hideSideMenu();
                    if (files.length > 0)
                        selectSomeBroadCast();
                    else
                        ToastHelper.newInstance(this).show("Nothing To Select");
                    break;
                case 3:
                    hideSideMenu();
                    if (files.length > 0)
                        deleteFolder();
                    else
                        ToastHelper.newInstance(this).show("Nothing To Select");
                    break;
                case 4:
                    hideSideMenu();
                    createNewFolder();
                    break;
                case 5:
                    hideSideMenu();
                    if (files.length > 0)
                        renameFile();
                    else
                        ToastHelper.newInstance(this).show("Nothing To Select");
                    break;
            }
        });
    }

    private boolean pasteFileContainsDirectory() {
        mCancleCopy = false;
        mIsCopy = true;
        String category_task = mSharedPre.getString("category_task", "");
        if (!TextUtils.isEmpty(category_task)) {
            String[] split = category_task.split(";");
            for (int i = 0; i < split.length - 1; i++) {
                Log.d(TAG, "pasteFileContainsDirectory: " + split[i]);
                File f = new File(split[i]);
                if (!f.isFile()) {
                    mIsCopy = false;
                }
            }
        }
        mSharedPre.edit().putString("category_task", "").apply();
        return mIsCopy;
    }

    private void hideSideMenu() {
        if (mRlvMenu.getVisibility() == View.VISIBLE) {
            mRlvMenu.setVisibility(View.GONE);
            gridView.setFocusable(true);
            gridView.requestFocus();
            return;
        }
    }

    private void exitSelectmode() {
        //发广播通知
        Intent intent = new Intent("com.walton.filebrowser.getstate");
        intent.putExtra("state", 0);
        sendBroadcast(intent);
        mIsSelecting = false;
    }

    /**
     * 删除文件
     */
    private void deleteFolder() {

        //获取当前文件的集合
        String title = null;
        final File[] files = getmFileList();
        int selectedItemPosition = gridView.getSelectedItemPosition();
        final File[] currentSeletcted = getCurrentSeletcted();
        if (selectedItemPosition >= 0) {
            File file = files[selectedItemPosition];
            title = "Are you sure you want to delete：" + file.getName();// + " What??";
        }
        if (currentSeletcted != null && currentSeletcted.length > 0) {
            title = "Are you sure you want to delete this?" + currentSeletcted.length;// + "Item?？";
        }
        mNormalDialog = new NormalDialog(this, title);
        mNormalDialog.setmListener(new OnNormalDialogButtonClickListener() {
            @Override
            public void onOkButtonClick() {
                //异步删除
                int selectedItemPosition = gridView.getSelectedItemPosition();
                File file = null;
                if (selectedItemPosition >= 0) {
                    file = files[selectedItemPosition];

                }
                new DeleteFileTask().execute(file);

//                deleteFilesss(file);
            }

            @Override
            public void onCancleButtonClick() {

            }
        });
        mNormalDialog.show();
    }

    public void back() {
        if (mRlvMenu.getVisibility() == View.VISIBLE) {
            mRlvMenu.setVisibility(View.GONE);
            if (layoutEmptyPage.getVisibility() != View.VISIBLE) {
                gridView.setFocusable(true);
                gridView.requestFocus();
            } else {
                txtCircularViewFour.setFocusable(true);
                txtCircularViewFour.requestFocus();
            }
            return;
        }

        if (mIsSelecting) {

            mNormalDialog = new NormalDialog(this, "Whether to exit the multi-select mode");
            mNormalDialog.setmListener(new OnNormalDialogButtonClickListener() {
                @Override
                public void onOkButtonClick() {
                    unSelectSome();
                    exitSelectmode();
                }

                @Override
                public void onCancleButtonClick() {
                    mNormalDialog.dismiss();
                }
            });
            mNormalDialog.show();
            return;
        }

        if (mCurrentDirectory.startsWith("/")) {

            tempCurrentDirectory = mCurrentDirectory.substring(1);
        } else {
            tempCurrentDirectory = mCurrentDirectory;
        }

        if (mRootFile.getAbsolutePath().startsWith("/")) {
            tempRootFile = mRootFile.getAbsolutePath().substring(1);
        } else {
            tempRootFile = mRootFile.getAbsolutePath();
        }

//        if (mRootFile.getAbsolutePath().substring(0,1).equals("/")){
//            tempRootFile = mRootFile.getAbsolutePath().substring(1);
//        }

        if (tempCurrentDirectory.equals(mRootFile.getAbsolutePath().substring(1))) {

            if (getApplicationContext() != null) {
                isActivityFirstTimeLoad = true;
                mainUpView1.setVisibility(View.GONE);
                Log.d("FragmentDisplay", "Already in root directory");
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction trans = manager.beginTransaction();
//                trans.remove(getApplicationContext());
//                trans.commit();
//                manager.popBackStack();
                finish();
            }
            return;
        }
        try {
            l = String.valueOf(lastSelectedItem.pop());
            gridViewSelectedItem = Integer.parseInt(l);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取父级目录并返回
        final File parentDir = new File(mCurrentDirectory).getParentFile();

        //更新导航
        String dir = txtFilePath.getText().toString();
        Log.d("FragmentDisplay", "directory:" + dir);
        final int index = dir.lastIndexOf("/");
        String substring = dir.substring(0, index);
        txtFilePath.setText(substring);
        txtFilePath.setSelected(true);

        new Handler().postDelayed(() -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    update(parentDir);

                    if (parentDir == null) {
                        return;
                    }

                    mCurrentDirectory = parentDir.getAbsolutePath();
                    Log.d("lastitem", Integer.parseInt(l) + 1 + "/" + files.length);

                    gridView.setFocusable(true);
                    gridView.requestFocus();

                    mainUpView1.setVisibility(View.VISIBLE);
                }
            });
            gridView.setSelection(Integer.parseInt(l));
            mainUpView1.setFocusView(getViewByPosition(Integer.parseInt(l)), 1.0f);
        }, 100);
    }

    private void pasteFile() {
        //不取消
        mCancleCopy = false;
        String category_task = mSharedPre.getString("category_task", "");
        if (!TextUtils.isEmpty(category_task)) {
            String[] split = category_task.split(";");
            for (int i = 0; i < split.length - 1; i++) {
                mNowTask.add(new File(split[i]));
            }
            String flag = split[split.length - 1];
            if (flag.equals("0")) {
                mIsCopy = false;
            } else if (flag.equals("1")) {
                mIsCopy = true;
            }
        }
        mSharedPre.edit().putString("category_task", "").apply();

        isSameDirectory = false;
        File destFile = new File(mCurrentDirectory);
        for (int i = 0; i < mNowTask.size(); i++) {
            Log.d(TAG, "copyFolder: " + mNowTask.get(i).getPath() + "\n" + destFile.getPath() + "\n" + dabradabra);

            if (mNowTask.get(i).isDirectory()) {
                File newFolder = new File(destFile, mNowTask.get(i).getName());
                if (mNowTask.get(i).getPath().equalsIgnoreCase(destFile.getPath())) {
                    isSameDirectory = true;
                    mNowTask.remove(i);
                } else if (newFolder.exists()) {
                    mNowTask.remove(i);
                    ToastHelper.newInstance(UiUtils.getContext()).show("Folder already exists!");
                }
            }
        }

        if (isSameDirectory || mNowTask.size() == 0) {
            ToastHelper.newInstance(UiUtils.getContext()).show("Folder can't paste in the same directory!");
            mNowTask.clear();
            return;
        }


        mProgressDialog = new AlertDialog.Builder(this).create();
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pastView = UiUtils.inflate(R.layout.pastfile_dialog);
        Button btnCancle = pastView.findViewById(R.id.btn_cancle);
        mProgeressBar = pastView.findViewById(R.id.pb);
        //获取用户要拷贝文件的总大小
        mTotalSize = 0;

        //计算机一下当前文件的大小
        for (int i = 0; i < mNowTask.size(); i++) {
            if (mNowTask.get(i).isDirectory()) {
                try {
                    mTotalSize += new ConcurrentTotalFileSize()
                            .getTotalSizeOfFilesInDir(mNowTask.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mTotalSize += mNowTask.get(i).length();
            }
        }

        //做一个转换
        double total = (double) mTotalSize / 1024 / 1024;

        mProgeressBar.setMax((int) total);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置取消复制标志位true
                mCancleCopy = true;
                //从线程池中移除该任务
                ThreadManager.getInstance().getmLongThreadPool().cancle(mRunable);
                mProgressDialog.dismiss();
            }
        });

        mProgressDialog.setView(pastView);
        mProgressDialog.show();
        //Perform a replication task
        excuteCopyFile();

    }


    public void copyFolder(File srcFile, File destFile)
            throws IOException {

        Log.d(TAG, "copyFolderTest: Current Directory" + mCurrentDirectory + " \nFile Name Src: " + destFile.getPath());
        // 判断该File是文件夹还是文件
        if (srcFile.isDirectory()) {
            if (mCurrentDirectory.contains(srcFile.getName())){
                ToastHelper.newInstance(this).show("Can't paste folder under same root directory!");
            }else {
                // 文件夹
                File newFolder = new File(destFile, srcFile.getName());
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
                // 获取该File对象下的所有文件或者文件夹File对象
                File[] fileArray = srcFile.listFiles();
                Log.d(TAG, "copyFolder: " + String.valueOf(fileArray.length));
                for (File file : fileArray) {
                    if (mCancleCopy) {
                        return;
                    }
                    Log.d(TAG, "copyFolderTest--: " + file.getPath() + " \nDestFile: " + newFolder.getPath());
                    copyFolder(file, newFolder);
                }
            }
        } else {
            // 文件
            File newFile = new File(destFile, srcFile.getName());
            //如果该目录下存在该文件，创建一个副本。
            if (newFile.exists()) {
                //获取文件名字没有扩展名
                String fileNameNoEx = FileUtils.getFileNameNoEx(newFile.getName());
                //获取当前文件全路径
                String absolutePath = newFile.getAbsolutePath();
                //替换当前的名字,创建一个文件副本
                absolutePath = absolutePath.replace(fileNameNoEx, fileNameNoEx + SystemUtil.getDate().replace("_", ""));
                newFile = new File(absolutePath);
                Log.d(TAG, "copyFolder: " + absolutePath);
            }
            if (!mCancleCopy) {
                copyFile(srcFile, newFile);
            }
        }
    }

    /**
     * 复制文件
     *
     * @param srcFile
     * @param newFile
     * @throws IOException
     */
    public void copyFile(File srcFile, File newFile) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        //如果是媒体文件，更新到数据库
        FileUtils.updateDataBase(newFile.getAbsolutePath());
        try {
            bis = new BufferedInputStream(new FileInputStream(
                    srcFile));
            bos = new BufferedOutputStream(
                    new FileOutputStream(newFile));

            byte[] bys = new byte[1024];
            int len;
            while ((len = bis.read(bys)) != -1 && !mCancleCopy) {
                bos.write(bys, 0, len);
                //记录当前已经文件的大小
                mCurrentProgress += len;
                final double temp = (double) mCurrentProgress / 1024 / 1024;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgeressBar.setProgress((int) temp);
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mNormalDialog != null && mNormalDialog.isShowing()) {
            mNormalDialog.cancel();
            mNormalDialog = null;
        }
        if (mEditDialog != null && mEditDialog.isShowing()) {
            mEditDialog.cancel();
            mEditDialog = null;
        }
    }


    public void closeFragment() {
        finish();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "dispatchKeyEvent: " + event.getAction());
        if (mRlvMenu.getVisibility() == View.VISIBLE) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    if (mMenu.getSelectedItemPosition() == 0) {
                        mMenu.setSelection(0);
                        return true;
                    }
                    if (files.length == 0 && mNowTask.size() > 0) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (mMenu.getSelectedItemPosition() == 1) {
                                    mMenu.setSelection(4);

                                } else {
                                    mMenu.setSelection(1);
                                }
                            }
                        });
                        return true;
                    } else if (files.length == 0 && mNowTask.size() <= 0) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mMenu.setSelection(4);
                            }
                        });
                        return true;
                    }
                    mPageDown = false;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
//                    if (mMenu.getSelectedItemPosition() == mMenu.getChildCount() - 1) {
//                        mMenu.setSelection(0);
//                        return true;
//                    }
                    if (files.length == 0 && mNowTask.size() > 0) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (mMenu.getSelectedItemPosition() == 1) {
                                    mMenu.setSelection(4);

                                } else {
                                    mMenu.setSelection(1);
                                }
                            }
                        });
                        return true;
                    } else if (files.length == 0 && mNowTask.size() <= 0) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mMenu.setSelection(4);
                            }
                        });
                        return true;
                    }
                    mPageDown = true;
                }
            }
        } else {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if (isWheelFocus) {

                        }
                    } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (isWheelFocus) {
                        }
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "KeyCode:" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                if (FocusTest.pictureViewFragment != null && FocusTest.pictureViewFragment.isVisible()) {
//                    FocusTest.pictureViewFragment.back();
//                    Log.d(TAG, "KeyCode: first call");
//                } else {
                if (this != null) {// && fileDisplayFragment.isVisible()) {
                    back();
                    Log.d(TAG, "KeyCode: second call");
                } else {
                    Log.d(TAG, "KeyCode: finish call");
                    back();
                }
//                }
                return true;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
////                if (fileDisplayFragment != null && fileDisplayFragment.isVisible()) {
////                    return true;sd
////                }
//                return true;
////                break;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
////                if (fileDisplayFragment != null && fileDisplayFragment.isVisible()) {
////                    return true;
////                }
////                break;
//                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (gridView.isFocused()) {
                    return true;
                }
                if (txtCircularViewFour.isFocused()) {
                    Log.d(TAG, "onKeyDown: UP");
                    // for up
                    if (!isBusyScroller) {
                        isBusyScroller = true;
                        loadingIndecator.setVisibility(View.VISIBLE);
                        lastScrollTime = System.currentTimeMillis();
                        updateView(txtCircularViewFive.getText().toString().trim());
                        Log.d(TAG, "onKeyDown: DOWN");
                    } else {
                        Log.d(TAG, "onKeyDown: Busy");
                    }
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (gridView.isFocused()) {
                    return true;
                }
                if (txtCircularViewFour.isFocused()) {
                    if (!isBusyScroller) {
                        isBusyScroller = true;
                        loadingIndecator.setVisibility(View.VISIBLE);
                        lastScrollTime = System.currentTimeMillis();
                        updateView(txtCircularViewThree.getText().toString().trim());
                        Log.d(TAG, "onKeyDown: DOWN");
                    } else {
                        Log.d(TAG, "onKeyDown: Busy");
                    }
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mRlvMenu.getVisibility() == View.VISIBLE) {
                    return true;
                }

                if (gridView.isFocused()) {
                    txtCircularViewFour.setFocusable(true);
                    txtCircularViewFour.setFocusableInTouchMode(true);
                    txtCircularViewFour.requestFocus();

                    Log.d(TAG, "onKeyDown: LEFT");
                }


                return true;
            case KeyEvent.KEYCODE_MENU:
                if (this != null)// && fileDisplayFragment.isVisible())
                    showMenu();
                return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void updateView(String previousItem) {
        Log.d(TAG, "updateView: " + previousItem);
        switch (previousItem) {
            case "All":
                generateView(categoryItems1);
                break;
            case "Photos":
                generateView(categoryPhotosList);
                break;
            case "Videos":
                generateView(categoryVideoList);
                break;
            case "Audio":
                generateView(categoryAudioList);
                break;
            case "Docs":
                generateView(categoryDocsList);
                break;
            case "Apps":
                generateView(categoryAppsList);
                break;
            case "Zip":
                generateView(categoryZipList);
                break;
        }

    }

    private void generateView(List<String> categoryList) {

        txtCircularViewOne.setText(categoryList.get(0));
        txtCircularViewTwo.setText(categoryList.get(1));
        txtCircularViewThree.setText(categoryList.get(2));
        txtCircularViewFour.setText(categoryList.get(3));
        txtCircularViewFive.setText(categoryList.get(4));
        txtCircularViewSix.setText(categoryList.get(5));
        txtCircularViewSeven.setText(categoryList.get(6));

        //TODO
        // update gridview data

        lastSelectedItem.empty();

        gridViewSelectedItem = 0;

        excuteGetAllFiles();

        mCurrentDirectory = mRootFile.getAbsolutePath();
        txtSelectedCategory.setText(txtCircularViewFour.getText().toString());
        txtFilePath.setText(mRootFile.getAbsolutePath());
        txtFilePath.setSelected(true);

    }

    public final void startScan(final String path) {
        Log.d(TAG, "startScan: " + path);
        // If the path is empty is directly to return
        if (path == null || path.length() == 0) {
            return;
        }
        // Startup thread scanning
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    // Only directory to scan
                    if (file.isDirectory()) {
                        File[] ff = file.listFiles();
                        if (file.list() != null && file.list().length > 0) {
                            // Under the current directory lists all documents
                            // (folder)
                            scan(file.listFiles());
                        } else {
                            // Clear all cache data
                            mediaContainer.clearAll();
                            Log.d(TAG, "run: No data");

                        }

                    }
                } catch (Exception e) {
                    Log.i("SCAN", "********scan**fail**********");
                }
            }
        }).start();

    }

    private void scan(final File[] files) {

        // all files
        List<BaseData> localFile = new ArrayList<BaseData>();
        // all folders
        List<BaseData> localFolder = new ArrayList<BaseData>();
        // all pictures
        List<BaseData> localPicture = new ArrayList<BaseData>();
        // all musics
        List<BaseData> localSong = new ArrayList<BaseData>();
        // all videos
        List<BaseData> localVideo = new ArrayList<BaseData>();

        try {
            // Under the current directory lists all documents (folder) list
            for (File f : files) {
                // Obtain filename
                String name = f.getName();
                BaseData file = new BaseData();
                // Setting absolute path
                file.setPath(f.getAbsolutePath());
                // Set parent directory path
                file.setParentPath(f.getParent());
                // Set up files (folders) name
                file.setName(name);

                // Scanning to folder
                if (f.isDirectory()) {
                    file.setType(Constants.FILE_TYPE_DIR);
                    localFolder.add(file);
                    // Scanning to file
                } else {
                    // Obtain and set the file extension
                    int pos = name.lastIndexOf(".");
                    String extension = "";
                    if (pos > 0) {
                        extension = name.toLowerCase().substring(pos + 1);
                        file.setFormat(extension);
                    }
                    String formatSize = Tools.formatSize(BigInteger.valueOf(f
                            .length()));
                    // setting file size
                    file.setSize(formatSize);
                    file.setDescription(formatSize);
                    // Set the file modification time
                    file.setModifyTime(f.lastModified());

                    // According to the file name to judgment document type, set up
                    // different types of files
                    if (check(name, getResources().getStringArray(R.array.photo_filter))) {
                        file.setType(Constants.FILE_TYPE_PICTURE);

                        localPicture.add(file);

                        // music file
                    } else if (check(name,
                            getResources().getStringArray(R.array.audio_filter))) {
                        file.setType(Constants.FILE_TYPE_SONG);

                        localSong.add(file);
                        Log.d(TAG, "scan: " + localSong.size());
                        // video file
                    } else if (check(name,
                            getResources().getStringArray(R.array.video_filter))) {
                        file.setType(Constants.FILE_TYPE_VIDEO);

                        localVideo.add(file);

                        // playlist file
                    } else if (check(name,
                            getResources().getStringArray(R.array.playlist_filter))) {
                        file.setType(Constants.FILE_TYPE_MPLAYLIST);
                        // other file
                    } else {
                        file.setType(Constants.FILE_TYPE_FILE);
                    }
                    // Save all data
                    localFile.add(file);
                }
            }
        } catch (Exception e) {

        }
        // Clear all cache data
        mediaContainer.clearAll();
        // All documents in order to list
        if (localFile.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_FILE, localFile);
        }
        // For all folders sorting
        if (localFolder.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_DIR, localFolder);
        }
        // For all image list sorting
        if (localPicture.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_PICTURE, localPicture);
        }
        // For all music list sorting
        if (localSong.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_SONG, localSong);
        }
        // For all video list sorting
        if (localVideo.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_VIDEO, localVideo);
        }
    }

    /*
     * Through the filename judgment is what types of documents..
     */
    public boolean check(final String name, final String[] extensions) {
        for (String end : extensions) {
            // Name never to null, without exception handling
            if (name.toLowerCase().endsWith(end)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Cache Data.
     */
    public void putAllToCache(final int type, final List<BaseData> src) {
        LogUtil.d("type=" + type + ",BaseData.size=" + src.size());
        // sort data
//        Collections.sort(src, comparator);
        // cache to memory
        mediaContainer.putMediaData(type, src);
    }

    @SuppressLint("StaticFieldLeak")
    public class StartPlayerBackGround extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
            listOfMedia = new ArrayList<>();
            listOfMedia.clear();
            runOnUiThread(() -> loadingIndecator.smoothToShow());
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... voids) {

            int type = voids[0];
            int position = voids[1];

            MediaContainerApplication.getInstance().clearAll();

            if (Constants.FILE_TYPE_SONG == type) {
                for (int u = 0; u < files.length; u++) {
                    if (TypeUtils.isAudio(files[u].getName())) {
                        bs = new BaseData();
                        bs.setName(files[u].getName());
                        bs.setPath(files[u].getPath());
                        Log.d(TAG, "startPlayer: Audio file" + u);
                        listOfMedia.add(bs);
                    }
                }
            } else if (Constants.FILE_TYPE_VIDEO == type) {
                for (int u = 0; u < files.length; u++) {
                    if (TypeUtils.isVideo(files[u].getName())) {
                        bs = new BaseData();
                        bs.setName(files[u].getName());
                        bs.setPath(files[u].getPath());
                        Log.d(TAG, "startPlayer: Audio file" + u);
                        listOfMedia.add(bs);
                    }
                }
            } else if (Constants.FILE_TYPE_PICTURE == type) {
                for (int u = 0; u < files.length; u++) {
                    if (TypeUtils.isImage(files[u].getName())) {
                        bs = new BaseData();
                        bs.setName(files[u].getName());
                        bs.setPath(files[u].getPath());
                        Log.d(TAG, "startPlayer: Audio file" + u);
                        listOfMedia.add(bs);
                    }
                }
            }

            if (listOfMedia.size() > 0) {
                for (int i = 0; i < listOfMedia.size(); i++) {
                    if (files[position].getPath().replace(" ", "%20").toLowerCase().equalsIgnoreCase(listOfMedia.get(i).getPath().replace(" ", "%20").toLowerCase())) {
                        index = i;
                    }
                }

                Log.d(TAG, "startPlayer: Hurrrrrrrrrrrraaaaaaaaaaaahhhhhhhhhh " + index);

                for (int i = 0; i < listOfMedia.size(); i++) {
                    Log.d(TAG, "doInBackground: --- " + listOfMedia.get(i).getPath());
                }

//        int index = getMediaFile(type, position);
                Log.d(TAG, "startPlayer, index : " + index + " position:" + position + " " + "PATH:" + files[position].getPath() + " FILES:" + files.length + " " + " " + "Media Files:" + listOfMedia.size());

                if (Constants.FILE_TYPE_SONG == type) {

                    MediaContainerApplication.getInstance().putMediaData(Constants.FILE_TYPE_SONG, listOfMedia);
                } else if (Constants.FILE_TYPE_VIDEO == type) {

                    MediaContainerApplication.getInstance().putMediaData(Constants.FILE_TYPE_VIDEO, listOfMedia);
                } else if (Constants.FILE_TYPE_PICTURE == type) {

                    MediaContainerApplication.getInstance().putMediaData(Constants.FILE_TYPE_PICTURE, listOfMedia);
                }

                if (Constants.FILE_TYPE_VIDEO == type) {
                    intent = new Intent(FocusTest.this, VideoPlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    // Start Image Player
                } else if (Constants.FILE_TYPE_PICTURE == type) {
                    intent = new Intent(FocusTest.this, ImagePlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                } else if (Constants.FILE_TYPE_SONG == type) {
                    intent = new Intent(FocusTest.this, MusicPlayerActivity.class);
                }
            }
            return index;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            // the current broadcast media index
            Log.d(TAG, "index00===" + aVoid);
            intent.putExtra(Constants.BUNDLE_INDEX_KEY, aVoid);

            if (loadingIndecator.getVisibility() == View.VISIBLE) {
                loadingIndecator.smoothToHide();
                loadingIndecator.setVisibility(View.GONE);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, 300);

        }
    }

    class CopyRunable implements Runnable {
        @Override
        public void run() {
            String dest = mCurrentDirectory;
            final File destFile = new File(dest);

            if (mNowTask != null && mNowTask.size() > 0) {
                for (int i = 0; i < mNowTask.size() && !mCancleCopy; i++) {
                    try {
                        copyFolder(mNowTask.get(i), destFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //如果是剪切则删除之前的文件
            if (!mIsCopy) {
                for (int i = 0; i < mNowTask.size(); i++) {
                    File file = mNowTask.get(i);
                    if (file.isFile()) {
                        file.delete();
                    } else {
                        FileUtils.deleteFolder(file);
                    }
                }
            }

//                复制完成，设置为0
            mTotalSize = 0;
            mCurrentProgress = 0;
            //复制成功后，清除当前任务
            mNowTask.clear();


            //关闭复制文件进度对话框
            UiUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                    ToastHelper.newInstance(UiUtils.getContext()).show(R.string.copySuccess);
                    //刷新展示界面
                    String mCurrentDir = mCurrentDirectory;
                    File updateFile = new File(mCurrentDir);
                    update(updateFile);

                    gridView.setSelection(0);
                    gridView.setFocusable(true);
                    gridView.requestFocus();
                }
            });
        }
    }

    private class DeleteFileTask extends AsyncTask<File, Void, Integer> {
        @Override
        protected Integer doInBackground(File... params) {
            File[] currentSeletcted = getCurrentSeletcted();
            File file = params[0];
            if (currentSeletcted != null && currentSeletcted.length > 0) {
                for (int i = 0; i < currentSeletcted.length; i++) {
                    if (currentSeletcted[i].isFile()) {
                        currentSeletcted[i].delete();
                        FileUtils.updateDataBase(currentSeletcted[i].getAbsolutePath(), null, true);

                    } else {
                        FileUtils.deleteFolder(currentSeletcted[i]);
                    }
                }
                return 0;
            } else {
                if (file != null) {
                    if (file.isFile()) {
                        file.delete();
                        //Update the database
                        FileUtils.updateDataBase(file.getAbsolutePath(), null, true);
                    } else {
                        FileUtils.deleteFolder(file.getAbsoluteFile());
                    }
                }
                return 1;
            }
        }


        @Override
        protected void onPostExecute(Integer flag) {

            ToastHelper.newInstance(getApplicationContext()).show(R.string.delSuccess);
            String mCurrentDir = mCurrentDirectory;
            File updateFile = new File(mCurrentDir);
            update(updateFile);
            if (mDeleteDialog != null && mDeleteDialog.isShowing()) {
                mDeleteDialog.dismiss();
            }

            gridView.setSelection(0);
            gridView.setFocusable(true);
            gridView.requestFocus();


            new Handler().postDelayed(() -> {
                if (mIsSelecting) {
                    //Exit multi-select mode
                    exitSelectmode();
                    unSelectSome();
                }
                if (files.length == 0) {
                    mainUpView1.setVisibility(View.GONE);
                    ToastHelper.newInstance(getApplicationContext()).show(R.string.nullDir);
//                    circularListView.requestFocus();

                    layoutEmptyPage.setVisibility(View.VISIBLE);

                    txtCircularViewFour.setFocusable(true);
                    txtCircularViewFour.requestFocus();

                } else {
                    mainUpView1.setVisibility(View.VISIBLE);
                    mainUpView1.setFocusView(getViewByPosition(0), 1.0f);
                }
            }, 100);
        }
    }
}

