package com.walton.filebrowser.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.walton.filebrowser.FocusTest;
import com.walton.filebrowser.R;
import com.walton.filebrowser.adapter.UsbItemAdapter;
import com.walton.filebrowser.business.adapter.GridAdapter;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.service.UsbService;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.operators.flowable.FlowableAmb;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class RootActivity extends Activity {

    private static final String TAG = RootActivity.class.getSimpleName();

    private static final int READ_EXTERNAL_STORAGE_CODE = 101;

    // gridview tv adapter
    public static UsbItemAdapter adapter = null;

    // control container class
    private UsbBrowserViewHolder holder = null;

    // listView data source
    public List<BaseData> sourceData = new ArrayList<BaseData>();

    private ArrayList<BaseData> tmpArray = new ArrayList<BaseData>();

    // local usb disk data browsing class
    private LocalDataBrowser localDataBrowser = null;

    private int m_dataSource = Constants.BROWSER_LOCAL_DATA;

    private int translationY_focus = -45, translationY_un_focus = 0;

    private float x_focus = 15f, y_focus = 50f, x_un_focus = 15f, y_un_focus = 0f;

    String pathUsb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        if (!PermissionUtils.getInstance(this).isPermissionGranted()){
            checkAppPermission();
        }

        startService(new Intent(this, UsbService.class));

        holder = new UsbBrowserViewHolder(this, handler);
        holder.findViews();


        localDataBrowser = new LocalDataBrowser(this, handler, sourceData);
        adapter = new UsbItemAdapter(sourceData, this);

        handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);

        holder.gridViewUsb.setAdapter(adapter);
        holder.gridViewUsb.setOnItemSelectedListener(onItemSelectedListener);
        holder.gridViewUsb.setOnItemClickListener(onItemClickLIstener);

    }

    private void checkAppPermission() {

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
            //申请READ_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        try {
            if (requestCode == READ_EXTERNAL_STORAGE_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.length > 0) {
                    // Permission Granted
                    PermissionUtils.getInstance(this).permissionGranted(true);
                } else {
                    PermissionUtils.getInstance(this).permissionGranted(false);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, " Exception :: " + e);
        }

    }


    /************************************************************************
     * Handler event handling area
     ************************************************************************/
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void handleMessage(Message message) {
            if (message.what == Constants.BROWSER_LOCAL_DATA) {
                Log.d(TAG, "handleMessage: Yes called BROWSER LOCAL DATA");
                localDataBrowser.browser(-1, Constants.OPTION_STATE_ALL);
            } else if (message.what == Constants.UPDATE_LOCAL_DATA) {
                Log.d(TAG, "handleMessage: Yes called UPDATE " + sourceData.size());
                tmpArray.clear();
                tmpArray.addAll(sourceData);

                adapter.notifyDataSetChanged();

                Log.d(TAG, "handleMessage: Yes called UPDATE Adapter " + adapter.getCount());
            }
        }
    };

    private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (view != null) {
                holder.setGridViewFocused(true, position);
                RelativeLayout txt = holder.getViewByPosition(position).findViewById(R.id.rlSysPocket);
                txt.animate().x(x_focus).y(y_focus);
                txt.animate().translationY(translationY_focus);

                Log.d(TAG, "onItemSelected: selected " + position);

                for (int i = 0; i < tmpArray.size(); i++) {
                    if (i != position) {
                        int finalI = i;
                        RelativeLayout txts = holder.getViewByPosition(finalI).findViewById(R.id.rlSysPocket);
                        txts.animate().x(x_un_focus).y(y_un_focus);
                        txts.animate().translationY(translationY_un_focus);
                        Log.d(TAG, "onItemSelected: " + finalI);
                    }
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private OnItemClickListener onItemClickLIstener = (parent, view, position, id) -> {

        Log.d(TAG, "onItemClick: path: "+ tmpArray.get(position).getPath());

        pathUsb = tmpArray.get(position).getPath();

        TextView txtUsbName = holder.getViewByPosition(position).findViewById(R.id.txt_storage_name);
        String uName = txtUsbName.getText().toString();

        FocusTest.progressValue = Integer.parseInt(tmpArray.get(position).getStorageUses());
        FocusTest.freeStorage = tmpArray.get(position).getStorageFree();
        FocusTest.totalStorage = tmpArray.get(position).getStorageTotal();

        Intent bundle = new Intent(RootActivity.this, FocusTest.class);
        bundle.putExtra("rootDir", tmpArray.get(position).getPath());
        bundle.putExtra("key", tmpArray.get(position).getKey());

        bundle.putExtra("usb_name", uName);

        bundle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(bundle);

    };

    private BroadcastReceiver discChangeReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // disk loading
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                // current for Local data browsing disk device list page
                Log.i(TAG, "action.equals(Intent.ACTION_MEDIA_MOUNTED)");
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    // update device list
                    localDataBrowser.updateUSBDevice(null);
                }
                // disk unloading
            } else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                Log.i(TAG, "action.equals(Intent.ACTION_MEDIA_EJECT)");
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    Uri uri = intent.getData();
                    String devicePath = uri.getPath();

                    slideDownPacket(devicePath);

                    localDataBrowser.updateUSBDevice(devicePath);
                }
            }
        }
    };

    private void slideDownPacket(String path) {
        int itemPos = 0;
        for (int i = 0; i < tmpArray.size(); i++){
            if (path.equalsIgnoreCase(tmpArray.get(i).getPath())){
                itemPos = i;
                break;
            }
        }
        int finalItemPos = itemPos;
        new Thread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout txts = holder.getViewByPosition(finalItemPos).findViewById(R.id.rlSysPocket);
                txts.animate().x(x_un_focus).y(y_un_focus);
                txts.animate().translationY(translationY_un_focus);
            }
        }).start();
    }

    public int getCurrentDataSource() {
        return m_dataSource;
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");

        registerReceiver(discChangeReceiver, filter);

        super.onResume();
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        unregisterReceiver(discChangeReceiver);
    }
}