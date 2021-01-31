package com.walton.filebrowser.ui.media.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.media.view.CustomProgressDialog;
import com.walton.filebrowser.ui.media.bean.GroupInfo;
import com.walton.filebrowser.ui.media.util.FileUtil;
import com.walton.filebrowser.ui.media.util.FilterType;
import com.walton.filebrowser.ui.media.util.MountInfo;
import com.walton.filebrowser.ui.media.util.PreferenceUtil;
import com.walton.filebrowser.ui.media.util.ToastUtil;
import com.walton.filebrowser.ui.media.view.ReflectItemView;
import com.walton.filebrowser.ui.media.adapter.DeviceAdapter;
import com.walton.filebrowser.ui.media.adapter.FileAdapter;
import com.walton.filebrowser.ui.media.adapter.FileAdapter.ViewHolder;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaActivity extends BaseActivity implements OnFocusChangeListener {
    public final String TAG = "MediaActivity";

    final static String MOUNT_LABLE = "mountLable";

    final static String MOUNT_TYPE = "mountType";

    final static String MOUNT_PATH = "mountPath";

    final static String MOUNT_NAME = "mountName";

    final static int DELETE_POSITION = 0;
    final static int SEARCH_POSITION = 1;
    final static int ORDER_POSITION = 2;

    private final static int DELETE_SUCCEED_DIALOG = 0x21;
    private final static int DELETE_ASK_DIALOG = 0x22;

    private LayoutInflater mInflater;

    /**
     * 挂载设备
     */
    private LinearLayout ll_mounted_equipment;
    private GridView gvMountedDevice;
    private ImageView ivDeviceIcon;
    private TextView tvDeviceName;
    private LinearLayout ll_choose_type;
    private ReflectItemView ibTypeAll;
    private ReflectItemView ibTypeMoive;
    private ReflectItemView ibTypeMusic;
    private ReflectItemView ibTypeImage;
    /**
     * 文件管理功能组件
     */
    private LinearLayout ll_bottom_menu;
    private CheckBox lv_item_checkbox;
    private Boolean isCheckBoxSelected = false;
    private int isSelectedNumber = 0;
    private RelativeLayout rl_check_all;
    private CheckBox cb_check_all;

    /**
     * 设备类型节点集合
     */
    List<GroupInfo> groupList;
    /**
     * 设备子节点集合
     */
    List<Map<String, String>> childList;
    /**
     * 存放点击位置集合
     */
    List<Integer> intList;
    int clickPos = 0;
    /**
     * 文件列表的点击位置
     */
    private int myPosition = 0;
    FileUtil util;
    int menu_item = 0;
    /**
     * 操作列表
     */
    ListView list;

    /**
     * 选中的文件列表
     */
    List<String> selectList = null;
    private String directorys = "";
    private String parentPath = "";
    /**
     * 索引显示
     */
    TextView numInfo;
    int clickCount = 0;
    /**
     * 设备列表类别位置
     */
    int groupPosition = -1;

    /**
     * 所选存储设备标识
     */
    private int selectedEquipment = 0;

    List<Map<String, String>> groupmap;

    /**
     * 提示
     */
    private TextView tvNotice;

    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            mHandler.removeMessages(0);
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessageDelayed(msg, 1000);
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                ToastUtil.showToast(context, Toast.LENGTH_SHORT,
                        getString(R.string.install_equi));
            }
            if (action.equals(Intent.ACTION_MEDIA_REMOVED)
                    || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
                ToastUtil.showToast(context, Toast.LENGTH_SHORT,
                        getString(R.string.uninstall_equi));
            }
        }
    };

    private static final String ACTION_VIDEO = "android.intent.action.localvideo";
    private static final String ACTION_PICTURE = "android.intent.action.localpicture";
    private static final String ACTION_MUSIC = "android.intent.action.localmusic";
    private static final String ACTION_FILE = "android.intent.action.localfile";
    private int intFromWhere = 0;
    private static final int FROM_VIDEO = 1;
    private static final int FROM_MUSIC = 2;
    private static final int FROM_PICTURE = 3;
    private static final int FROM_FILE = 4;
    private TextView tvDevNotice;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        List<File> updateList;

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    refreshList();
                    break;
            }

        }

    };

    /**
     * @Description:refresh data after files sorted or searched.
     */
    private void refresh(List<File> updateList) {
        showCheckBox(false);
        if (updateList.size() == 0 || updateList == null) {
            Toast.makeText(this, R.string.no_files, Toast.LENGTH_SHORT).show();
        } else {
            adapter.list = updateList;
            adapter.initSelectedMapData();
            adapter.notifyDataSetChanged();
            listFile = updateList;
            /*if (listView.getVisibility() == View.VISIBLE) {
                adapter = new FileAdapter(MediaActivity.this, listFile, listlayout, handler, false);
                listView.setAdapter(adapter);
            } else if (gridView.getVisibility() == View.VISIBLE) {
                adapter = new FileAdapter(MediaActivity.this, listFile, gridlayout, handler, false);
                gridView.setAdapter(adapter);
            }*/
        }
    }

    private void refreshList() {
        getMountEquipmentList();
        ll_choose_type.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.INVISIBLE);
        ll_mounted_equipment.setVisibility(View.VISIBLE);
        //showBtn.setVisibility(View.INVISIBLE);
        intList.clear();
        numInfo.setText("");
        pathTxt.setText("");
        typeTxt.setText("");
        dividerIv.setVisibility(View.GONE);
        updateGridViewNumColumns(childList);
        DeviceAdapter adapter = new DeviceAdapter(childList, MediaActivity.this);
        gvMountedDevice.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mIntent = getIntent();
        if (mIntent != null) {
            String strAction = mIntent.getAction();
            if (strAction != null) {
                if (strAction.equals(ACTION_VIDEO)) {
                    intFromWhere = FROM_VIDEO;
                } else if (strAction.equals(ACTION_MUSIC)) {
                    intFromWhere = FROM_MUSIC;
                } else if (strAction.equals(ACTION_PICTURE)) {
                    intFromWhere = FROM_PICTURE;
                } else if (strAction.equals(ACTION_FILE)) {
                    intFromWhere = FROM_FILE;
                } else {
                    intFromWhere = 0;
                }
            }
        }
        FilterType.filterType(MediaActivity.this);
        setContentView(R.layout.activity_main);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        selectList = new ArrayList<String>();
        init();
        registerUSB();
    }

    @Override
    protected void loadData() {

    }

    /**
     * @Description:show the listview or gridview item checkbox image
     */
    private void showCheckBox(Boolean flag) {
        if (flag) {
            adapter.isChecBoxShow = true;
            rl_check_all.setVisibility(View.VISIBLE);

        } else {
            adapter.isChecBoxShow = false;
            rl_check_all.setVisibility(View.GONE);

        }
        adapter.list = listFile;
        adapter.notifyDataSetChanged();

    }

    /**
     * 初始化视图
     */
    private void init() {
        //showBtn = (ImageButton) findViewById(R.id.ib_showstyle);
        //showBtn.setVisibility(View.INVISIBLE);
        int count = PreferenceUtil.getPrefInt(this, "currShowType", 1);
        //showBtn.setImageResource(showTypeArray[count]);
        currShowType = count;
        intList = new ArrayList<Integer>();
        listFile = new ArrayList<File>();
        gridlayout = R.layout.grid_view_item;
        listlayout = R.layout.list_view_item;
//        listView = (ListView) findViewById(R.id.lv_content);
//        gridView = (GridView) findViewById(R.id.gv_content);
//
//        tvNotice = (TextView) findViewById(R.id.tv_notice);
//        gvMountedDevice = (GridView) findViewById(R.id.gv_mounted_equipment);
//
//        pathTxt = (TextView) findViewById(R.id.tv_path);
//        numInfo = (TextView) findViewById(R.id.tv_count);
//        typeTxt = (TextView) findViewById(R.id.tv_file_type);
//        dividerIv = (ImageView) findViewById(R.id.iv_divider);
//
//        ll_mounted_equipment = (LinearLayout) this
//                .findViewById(R.id.ll_mounted_equipment);
//        ll_mounted_equipment.setVisibility(View.VISIBLE);
//        ll_choose_type = (LinearLayout) this.findViewById(R.id.ll_choose_type);
//        ibTypeAll = (ReflectItemView) this.findViewById(R.id.ib_all);
//        ibTypeMoive = (ReflectItemView) this.findViewById(R.id.ib_moive);
//        ibTypeMusic = (ReflectItemView) this.findViewById(R.id.ib_music);
//        ibTypeImage = (ReflectItemView) this.findViewById(R.id.ib_image);
//        tvDevNotice = (TextView) findViewById(R.id.tv_dev_notice);
//        rl_check_all = (RelativeLayout) this.findViewById(R.id.rl_check_all);
//        cb_check_all = (CheckBox) this.findViewById(R.id.cb_check_all);

        cb_check_all.setOnCheckedChangeListener(ch_check_all_Listener);
        initChooseTypeView();
        getsdList();
    }

    private OnCheckedChangeListener ch_check_all_Listener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean check) {

            Map<Integer, Boolean> isSelectedMap = new HashMap<Integer, Boolean>();
            for (int index = 0; index < listFile.size(); index++) {
                isSelectedMap.put(index, check);
            }

            adapter.setIsSelected(isSelectedMap);
            adapter.notifyDataSetChanged();
            if (check) {
                isSelectedNumber = isSelectedMap.size();
            } else {
                isSelectedNumber = 0;
            }
            Log.i("nongweiyi", "isSelectedNumber=" + isSelectedNumber
                    + " check=" + check);
        }
    };

    /**
     * 初始化文件类型选择视图
     */
    private void initChooseTypeView() {
        /*ibTypeAll.setOnFocusChangeListener(this);
        ibTypeMoive.setOnFocusChangeListener(this);
        ibTypeMusic.setOnFocusChangeListener(this);
        ibTypeImage.setOnFocusChangeListener(this);*/
    }

    /**
     * Obtain mount list 获得挂载列表
     */
    public void getsdList() {
        getMountEquipmentList();
        updateGridViewNumColumns(childList);
        DeviceAdapter adapter = new DeviceAdapter(childList,
                MediaActivity.this);
        gvMountedDevice.setAdapter(adapter);
        gvMountedDevice.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int position, long id) {
                selectedEquipment = position;
                ll_choose_type.setVisibility(View.VISIBLE);
                ll_mounted_equipment.setVisibility(View.INVISIBLE);
                switch (intFromWhere) {
                    case FROM_VIDEO:
                        Log.i(TAG, "FROM_VIDEO");
                        currfilterType = 1;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                        break;
                    case FROM_MUSIC:
                        Log.i(TAG, "FROM_MUSIC");
                        currfilterType = 2;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                        break;
                    case FROM_PICTURE:
                        Log.i(TAG, "FROM_PICTURE");
                        currfilterType = 3;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                        break;
                    case FROM_FILE:
                        Log.i(TAG, "ibTypeAll is clicked");
                        currfilterType = 0;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                        break;
                }

                ibTypeAll.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "ibTypeAll is clicked");
                        currfilterType = 0;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                    }
                });
                ibTypeMoive.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "ibTypeMoive is clicked");
                        currfilterType = 1;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                    }
                });
                ibTypeMusic.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "ibTypeMusic is clicked");
                        currfilterType = 2;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                    }
                });
                ibTypeImage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "ibTypeImage is clicked");
                        currfilterType = 3;
                        ll_choose_type.setVisibility(View.INVISIBLE);
                        //showBtn.setVisibility(View.VISIBLE);
                        updateByType(position);
                    }
                });
                // First time enter,selected all by default
                ibTypeAll.requestFocus();
            }

            private void updateByType(int position) {
                String path = childList.get(position).get(MOUNT_PATH);
                mountSdPath = path;
                arrayFile.clear();
                arrayDir.clear();
                directorys = path;
                int count = PreferenceUtil.getPrefInt(MediaActivity.this, "currShowType", 1);
                if (count == 0) {
                    gridView.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    gridView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }
                listFile.clear();
                clickPos = 0;
                myPosition = 0;
                geDdirectory(directorys);
                intList.add(position);
                updateList(true);
            }
        });
        gvMountedDevice.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (view == null) {
                    view = mInflater.inflate(R.layout.device_item, null);
                }
                ivDeviceIcon = (ImageView) view.findViewById(R.id.iv_device_icon);
                tvDeviceName = (TextView) view.findViewById(R.id.tv_device_name);
                //showOnFocusAnimation(ivDeviceIcon);
                //showOnFocusAnimation(tvDeviceName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * File list sorting 文件列表排序
     */
    @Override
    public void updateList(boolean flag) {
        if (flag) {
            // for broken into the directory contains many files,click again
            // error
            listFile.clear();
            Log.i(TAG, "updateList");
            //showBtn.setOnClickListener(clickListener);

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            progress = new CustomProgressDialog(MediaActivity.this);
            progress.show();

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            waitThreadToIdle(thread);
            thread = new MyThread();
            thread.setStopRun(false);
            progress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    thread.setStopRun(true);
                    if (keyBack) {
                        intList.add(clickPos);
                    } else {
                        clickPos = myPosition;
                        currentFileString = preCurrentPath;
                        Log.v("\33[32m Main1", "onCancel" + currentFileString
                                + "\33[0m");
                        intList.remove(intList.size() - 1);
                    }
                    ToastUtil.showToast(MediaActivity.this,
                            Toast.LENGTH_SHORT, getString(R.string.cause_anr));
                }
            });
            thread.start();
        } else {
            adapter.notifyDataSetChanged();
            fill(new File(currentFileString));
        }

    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    private void registerUSB() {
        IntentFilter usbFilter = new IntentFilter();
        usbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        usbFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        usbFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        usbFilter.addDataScheme("file");
        registerReceiver(usbReceiver, usbFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(usbReceiver);
        super.onDestroy();
        Log.d("jdk", "onDestroy===");
        Process.killProcess(Process.myPid()); // psqiu
    }

    private void getMountEquipmentList() {
        String[] mountType = getResources().getStringArray(R.array.mountType);
        MountInfo info = new MountInfo(this);
        groupList = new ArrayList<GroupInfo>();
        childList = new ArrayList<Map<String, String>>();
        GroupInfo group = null;
        for (int j = 0; j < mountType.length; j++) {
            group = new GroupInfo();
            for (int i = 0; i < info.index; i++) {
                if (info.type[i] == j) {
                    if (info.path[i] != null) { // && (info.path[i].contains("/mnt"))  psqiu：避免SD卡不显示
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(MOUNT_TYPE, String.valueOf(info.type[i]));
                        map.put(MOUNT_PATH, info.path[i]);
                        map.put(MOUNT_LABLE, "");
                        map.put(MOUNT_NAME, info.partition[i]);
                        childList.add(map);
                    }
                }
            }
            if (childList.size() > 0) {
                tvDevNotice.setText(R.string.my_device);
                group.setChildList(childList);
                group.setName(mountType[j]);
                groupList.add(group);
            } else {
                tvDevNotice.setText(R.string.choose_equipment);
            }
        }
    }

    // for broken into the directory contains many files,click again error
    class MyThread extends MyThreadBase {
        public void run() {
            util = new FileUtil(MediaActivity.this, currfilterType,
                    currentFileString);
            listFile = util.getFiles(currSortType, "local");
            currentFileString = util.currentFilePath;
            handler.sendEmptyMessage(UPDATE_LIST);
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public synchronized void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LIST:
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                    if (listView.getVisibility() == View.VISIBLE) {
                        adapter = new FileAdapter(MediaActivity.this, listFile,
                                listlayout, handler, false);
                        listView.setAdapter(adapter);
                        listView.setOnItemSelectedListener(itemSelect);
                        listView.setOnItemClickListener(ItemClickListener);
                    } else if (gridView.getVisibility() == View.VISIBLE) {
                        adapter = new FileAdapter(MediaActivity.this, listFile,
                                gridlayout, handler, false);
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(ItemClickListener);
                        gridView.setOnItemSelectedListener(itemSelect);
                        gridView.setOnKeyListener(itemKeyListener);
                    }
                    fill(new File(currentFileString));
                    if (listFile.size() <= 0) {
                        tvNotice.setVisibility(View.VISIBLE);
                        switch (currfilterType) {
                            case 0:
                                tvNotice.setText(R.string.no_files);
                                break;
                            case 1:
                                tvNotice.setText(R.string.no_video);
                                break;
                            case 2:
                                tvNotice.setText(R.string.no_music);
                                break;
                            case 3:
                                tvNotice.setText(R.string.no_picture);
                                break;
                        }

                    }
                    break;
            }
        }
    };

    /**
     * 文件项选中事件
     */
    OnItemSelectedListener itemSelect = new OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (parent.equals(listView) || parent.equals(gridView)) {
                myPosition = position;
            }
            numInfo.setText((position + 1) + "/" + listFile.size());
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnKeyListener itemKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    }
                    if (gridView.getSelectedItemPosition() > 0 && gridView.getCount() > 1) {
                        gridView.setSelection(gridView.getSelectedItemPosition() - 1);
                    }
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    }
                    if (gridView.getSelectedItemPosition() < gridView.getCount() - 1 && gridView.getCount() > 1) {
                        gridView.setSelection(gridView.getSelectedItemPosition() + 1);
                    }
                    return true;
                default:
                    return false;
            }
        }
    };

    /**
     * 文件项点击事件
     */
    private OnItemClickListener ItemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            ViewHolder vHollder = (ViewHolder) v.getTag();
            /*if (vHollder.iv_item_select.getVisibility() == View.VISIBLE) {
                vHollder.iv_item_select.toggle();
                FileAdapter.getIsSelectedMap().put(position,
                        vHollder.iv_item_select.isChecked());
                if (vHollder.iv_item_select.isChecked()) {
                    isSelectedNumber++;
                } else {
                    isSelectedNumber--;
                }

            } else {*/
            if (listFile.size() > 0) {
                if (position >= listFile.size()) {
                    position = listFile.size() - 1;
                }
                // for chmod the file
                //chmodFile(listFile.get(position).getPath());
                if (listFile.get(position).isDirectory()
                        && listFile.get(position).canRead()) {
                    intList.add(position);
                    clickPos = 0;
                } else {
                    clickPos = position;
                    // OpenFiles.open(CustomApplication.getContext(),listFile.get(position));
                    // listFile.get(clickPos));
                }
                myPosition = clickPos;
                arrayFile.clear();
                arrayDir.clear();
                // for broken into the directory contains many files,click
                // again
                // error
                preCurrentPath = currentFileString;
                keyBack = false;
                getFiles(listFile.get(position).getPath());
            }
            //}

        }

    };

    /**
     * Obtain a collection of files directory 获得目录下文件集合
     *
     * @param path
     */
    private void geDdirectory(String path) {
        directorys = path;
        parentPath = path;
        currentFileString = path;
    }

    @Override
    /**
     * Depending on the file directory path judgment do:
     * Go to the directory the file: Open the file system application
     * 根据文件路径判断执行的操作目录:进入目录 文件:系统应用打开文件
     * @param path
     */
    public void getFiles(String path) {
        if (path == null)
            return;
        openFile = new File(path);
        if (openFile.exists()) {
            if (openFile.isDirectory()) {
                currentFileString = path;
                updateList(true);
            } else {
                super.openFile(this, openFile);
            }
        } else {
            refreshList();
        }
    }

    @Override
    protected void initUI() {

    }


    @Override
    /**
     * Populate the list of files to the data container
     * 将文件列表填充到数据容器中
     * @param fileroot
     */
    public void fill(File fileroot) {
        try {
            if (clickPos >= listFile.size()) {
                clickPos = listFile.size() - 1;
            }
            //System.out.println("lll:path=" + fileroot.getPath());
            if (currfilterType == 0) {
                typeTxt.setText(R.string.alls);
            } else if (currfilterType == 1) {
                typeTxt.setText(R.string.videos);
            } else if (currfilterType == 2) {
                typeTxt.setText(R.string.musics);
            } else if (currfilterType == 3) {
                typeTxt.setText(R.string.pictures);
            }
            dividerIv.setVisibility(View.VISIBLE);
            pathTxt.setText(fileroot.getPath());
            numInfo.setText((clickPos + 1) + "/" + listFile.size());
            if (!fileroot.getPath().equals(directorys)) {
                parentPath = fileroot.getParent();
                currentFileString = fileroot.getPath();
            } else {
                currentFileString = directorys;
            }

            if (listFile.size() == 0) {
                numInfo.setText(0 + "/" + 0);
            }

            if (clickPos >= 0) {
                if (listView.getVisibility() == View.VISIBLE) {
                    listView.requestFocus();
                    listView.setSelection(clickPos);
                } else if (gridView.getVisibility() == View.VISIBLE) {
                    gridView.requestFocus();
                    gridView.setSelection(clickPos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            /*case KeyEvent.KEYCODE_MENU:
                if (listView.getVisibility() == View.VISIBLE
                        || gridView.getVisibility() == View.VISIBLE) {
                    gv_bottomMenu.setVisibility(View.VISIBLE);
                    gv_bottomMenu.requestFocus();
                    return true;
                }*/
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                super.onKeyDown(KeyEvent.KEYCODE_ENTER, event);
                return true;
            case KeyEvent.KEYCODE_BACK:
               if (adapter.isChecBoxShow) {
                    adapter.isChecBoxShow = false;
                    adapter.notifyDataSetChanged();
                    rl_check_all.setVisibility(View.GONE);
                    cb_check_all.setChecked(false);
                } else {
                    keyBack = true;
                    tvNotice.setVisibility(View.GONE);
                    String newName = pathTxt.getText().toString();
                    // 当前目录是根目录
                    if (newName.equals("")) {
                        //showBtn.setVisibility(View.INVISIBLE);
                        if (ll_choose_type.getVisibility() == View.VISIBLE) {
                            ll_choose_type.setVisibility(View.INVISIBLE);
                            ll_mounted_equipment.setVisibility(View.VISIBLE);
                            DeviceAdapter adapter = new DeviceAdapter(childList,
                                    MediaActivity.this);
                            gvMountedDevice.setAdapter(adapter);
                            System.out.println("lll:selectedEquipment="
                                    + selectedEquipment);
                            gvMountedDevice.setSelection(selectedEquipment);
                        } else {
                            SharedPreferences share = getSharedPreferences(
                                    "OPERATE", SHARE_MODE);
                            share.edit().clear().commit();
                            Intent cmd = new Intent(
                                    "com.unionman.android.music.musicservicecommand");
                            cmd.putExtra("command", "stop");
                            this.sendBroadcast(cmd);
                            onBackPressed();
                            /*
                             * clickCount++; if (clickCount == 1) {
                             * ToastUtil.showToast(MediaActivity.this,
                             * Toast.LENGTH_SHORT, getString(R.string.quit_app)); }
                             * else if (clickCount == 2) { SharedPreferences share =
                             * getSharedPreferences( "OPERATE", SHARE_MODE);
                             * share.edit().clear().commit(); Intent cmd = new
                             * Intent(
                             * "com.unionman.android.music.musicservicecommand");
                             * cmd.putExtra("command", "stop");
                             * this.sendBroadcast(cmd); onBackPressed(); }
                             */
                        }
                    } else {
                        clickCount = 0;
                        if (!currentFileString.equals(directorys)) {
                            arrayDir.clear();
                            arrayFile.clear();
                            getFiles(parentPath);
                        } else {
                            pathTxt.setText("");
                            numInfo.setText("");
                            typeTxt.setText("");
                            dividerIv.setVisibility(View.GONE);
                            //showBtn.setOnClickListener(null);
                            int count = PreferenceUtil.getPrefInt(this,
                                    "currShowType", 1);
                            //showBtn.setImageResource(showTypeArray[count]);
                            currShowType = count;
                            currSortType = 0;
                            // filterBut.setOnClickListener(null);
                            // filterBut.setImageResource(filterArray[0]);
                            gridView.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            listFile.clear();
                            getsdList();
                        }
                        // 点击的父目录位置
                        int pos = -1;
                        if (intList.size() <= 0) {
                            groupPosition = 0;
                            intList.add(0);
                        }

                        pos = intList.size() - 1;
                        if (pos >= 0) {
                            if (listView.getVisibility() == View.VISIBLE) {
                                clickPos = intList.get(pos);
                                myPosition = clickPos;
                                intList.remove(pos);
                            } else if (gridView.getVisibility() == View.VISIBLE) {
                                clickPos = intList.get(pos);
                                myPosition = clickPos;
                                intList.remove(pos);
                            } else if (intFromWhere == 0) {
                                ll_mounted_equipment.setVisibility(View.GONE);
                                //showBtn.setVisibility(View.GONE);
                                ll_choose_type.setVisibility(View.VISIBLE);
                                ll_choose_type.requestFocus();
                                switch (currfilterType) {
                                    case 0:
                                        ibTypeAll.requestFocus();
                                        break;
                                    case 1:
                                        ibTypeMoive.requestFocus();
                                        break;
                                    case 2:
                                        ibTypeMusic.requestFocus();
                                        break;
                                    case 3:
                                        ibTypeImage.requestFocus();
                                        break;
                                }
                            } else {
                                //showBtn.setVisibility(View.INVISIBLE);
                                ll_choose_type.setVisibility(View.INVISIBLE);
                                ll_mounted_equipment.setVisibility(View.VISIBLE);
                                DeviceAdapter adapter = new DeviceAdapter(
                                        childList, MediaActivity.this);
                                gvMountedDevice.setAdapter(adapter);
                            }
                        }
                    }
                    return true;
                }

        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

    }

    private void updateGridViewNumColumns(List<Map<String, String>> childList) {
        int num = 0;
        int width = 0;
        for (Map<String, String> map : childList) {
            num++;
            width = width + 380;
        }
        gvMountedDevice.setNumColumns(num);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) gvMountedDevice.getLayoutParams();
        linearParams2.width = width;
        linearParams2.height = 450;
        gvMountedDevice.setLayoutParams(linearParams2);
    }
}
