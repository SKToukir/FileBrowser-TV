package com.walton.filebrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.walton.filebrowser.adapter.GridViewAdapter;
import com.walton.filebrowser.interfaces.OnUsbSateChanged;
import com.walton.filebrowser.model.FileModel;
import com.walton.filebrowser.receiver.USBStateReceiver;
import com.walton.filebrowser.util.FileUtils;
import com.walton.filebrowser.util.GridViewTV;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class USBFilesActivity extends Activity implements OnUsbSateChanged {

    private static final String TAG = USBFilesActivity.class.getSimpleName();

    private MainUpView mainUpView1;
    private View mOldView;
    private GridViewAdapter mAdapter;
    private GridViewTV gridView;
    private int mSavePos = -1;
    private int mCount = 50;

    private USBStateReceiver usbStateReceiver;
    public static String PATH = "";
    public static String KEY = "";
    private FileModel fileModel;
    private List<FileModel> fileModels = new ArrayList<>();
    private File[] files;
    private TextView txtUsbName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_usbfiles);
        usbStateReceiver = new USBStateReceiver(this);
        registerReceiver(usbStateReceiver, usbStateReceiver.getIntentFilter());

        init();

        mainUpView1.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
        bridget.setTranDurAnimTime(200);
        // 设置移动边框的图片.
        mainUpView1.setUpRectResource(R.drawable.white_light_10);
        // 移动方框缩小的距离.
        mainUpView1.setDrawUpRectPadding(new Rect(20, 20, 20, 20));
        // 加载数据.
        getFiles();

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 这里注意要加判断是否为NULL.
                 * 因为在重新加载数据以后会出问题.
                 */
                if (view != null) {
                    mainUpView1.setFocusView(view, mOldView, 1.2f);
                }
                mOldView = view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"Clicked"+String.valueOf(position),Toast.LENGTH_LONG).show();
                mFindhandler.removeCallbacksAndMessages(null);
                mSavePos = position; // 保存原来的位置(不要按照我的抄，只是DEMO)
                mFindhandler.sendMessageDelayed(mFindhandler.obtainMessage(), 111);
                Toast.makeText(getApplicationContext(), PATH+"/"+fileModels.get(position).getFileName(), Toast.LENGTH_LONG).show();
            }
        });
        mFirstHandler.sendMessageDelayed(mFirstHandler.obtainMessage(), 188);
    }

    // 延时请求初始位置的item.
    Handler mFirstHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            gridView.setDefualtSelect(2);
        }
    };
    // 更新数据后还原焦点框.
    Handler mFindhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mSavePos != -1) {
                gridView.requestFocusFromTouch();
                gridView.setSelection(mSavePos);
            }
        }
    };


    @SuppressLint({"WrongConstant", "SetTextI18n"})
    private void init() {

        gridView = findViewById(R.id.gridView);
        mainUpView1 = findViewById(R.id.mainUpView1);

        txtUsbName= findViewById(R.id.txtUsbName);
        txtUsbName.setText(KEY.substring(0, 1).toUpperCase() + KEY.substring(1));

//        mAdapter = new GridViewAdapter(this, fileModels,files);

    }

    private void getFiles(){

        Log.d("Files", KEY+" Path: " + PATH);
        File directory = new File(PATH);
        files = directory.listFiles();

        if (files != null && files.length>0){
            files = FileUtils.sort(files,FocusTest.selectedCatItemName);
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                fileModel = new FileModel();
                fileModel.setFileName(files[i].getName());
                fileModel.setFilePath(files[i].getPath());
               // fileModel.setFileType(files[i].getName().substring(files[i].getName().lastIndexOf(".")));

                fileModels.add(fileModel);

                Log.d("Files", "FileName:" + files[i].getName());
            }
        }

    }

    @Override
    public void onUsbSateChange(boolean isConnected, String path) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbStateReceiver);
    }
}
