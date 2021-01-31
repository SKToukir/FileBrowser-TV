package com.walton.filebrowser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.util.StorageSpaceCount;
import com.walton.filebrowser.util.USB;

import java.util.ArrayList;
import java.util.List;

public class UsbItemAdapter extends BaseAdapter {

    private static final String TAG = "UsbItemAdapter";

    private List<BaseData> list = new ArrayList<>();

    private LayoutInflater mInflater;

    private int[] pockets_image = {R.drawable.sys_pocket_one, R.drawable.sys_pocket_four, R.drawable.sys_pocket_three};//,R.drawable.sys_pocket_four};
    private int[] pockets_round_image = {R.drawable.circle_back_one, R.drawable.circle_back_four, R.drawable.circle_back_three};//,R.drawable.circle_back_four};

    private Context mContext;

    private int pocketIndex;

    public UsbItemAdapter(List<BaseData> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UsbViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.usb_item, parent, false);
            convertView.setTag(new UsbViewHolder(convertView));
        }
        viewHolder = (UsbViewHolder) convertView.getTag();
        bindViewData(position, viewHolder);
        return convertView;
    }

    private void bindViewData(int position, UsbViewHolder viewHolder) {
        BaseData usb = list.get(position);
        Log.d(TAG, "bindViewData: USB name " + usb.getName());
        if (position % pockets_image.length == 0) {

            viewHolder.rlItemHolder.setBackgroundResource(pockets_image[0]);

//            viewHolder.rlItemHolder.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlItemHolder.setBackgroundResource(pockets_image[0]);
//                }
//            });

            viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[0]);

//            viewHolder.rlPocketRound.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[0]);
//                }
//            });
        } else if (position % pockets_image.length == 1) {

            viewHolder.rlItemHolder.setBackgroundResource(pockets_image[1]);

//            viewHolder.rlItemHolder.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlItemHolder.setBackgroundResource(pockets_image[1]);
//                }
//            });

            viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[1]);

//            viewHolder.rlPocketRound.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[1]);
//                }
//            });
        } else if (position % pockets_image.length == 2) {

            viewHolder.rlItemHolder.setBackgroundResource(pockets_image[2]);

//            viewHolder.rlItemHolder.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlItemHolder.setBackgroundResource(pockets_image[2]);
//                }
//            });
            viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[2]);
//            viewHolder.rlPocketRound.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[2]);
//                }
//            });
        } else if (position % pockets_image.length == 3) {

            viewHolder.rlItemHolder.setBackgroundResource(pockets_image[3]);

//            viewHolder.rlItemHolder.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlItemHolder.setBackgroundResource(pockets_image[3]);
//                }
//            });

            viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[3]);

//            viewHolder.rlPocketRound.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.rlPocketRound.setBackgroundResource(pockets_round_image[3]);
//                }
//            });
        }

        Log.d(TAG, "bindViewData: Position:" + position);
        if (position == 0) {

            viewHolder.txt_storage_name.setText("Local Storage");

//            viewHolder.txt_storage_name.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.txt_storage_name.setText("Local Storage");
//                }
//            });

            viewHolder.imgUsbDisk.setImageResource(R.drawable.system_storage);
            viewHolder.imgUsbDisk.setScaleType(ImageView.ScaleType.FIT_CENTER);

//            viewHolder.imgUsbDisk.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.imgUsbDisk.setImageResource(R.drawable.system_storage);
//                    viewHolder.imgUsbDisk.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                }
//            });

        } else {

            viewHolder.txt_storage_name.setText(usb.getName());
            Log.d(TAG, "run: What the hell is hapenning " + usb.getName());

//            viewHolder.txt_storage_name.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.txt_storage_name.setText(usb.getName());
//                    Log.d(TAG, "run: What the hell is hapenning "+usb.getName());
//                }
//            });

            viewHolder.imgUsbDisk.setImageResource(R.drawable.usb);
            viewHolder.imgUsbDisk.setScaleType(ImageView.ScaleType.FIT_CENTER);

//            viewHolder.imgUsbDisk.post(new Runnable() {
//                @Override
//                public void run() {
//                    viewHolder.imgUsbDisk.setImageResource(R.drawable.usb);
//                    viewHolder.imgUsbDisk.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                }
//            });
        }

        if (Integer.parseInt(usb.getStorageUses()) > 85){
            viewHolder.pbarSystem.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progressbar_low_space));
        }else {
            viewHolder.pbarSystem.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_bar_horizontal));
        }

        viewHolder.pbarSystem.setSecondaryProgress(100);
        viewHolder.pbarSystem.setProgress(Integer.parseInt(usb.getStorageUses()));

//        viewHolder.pbarSystem.post(new Runnable() {
//            @Override
//            public void run() {
//                viewHolder.pbarSystem.setSecondaryProgress(100);
//                viewHolder.pbarSystem.setProgress(Integer.parseInt(usb.getStorageUses()));
//            }
//        });

        viewHolder.txtSystemStorageSizeAvilable.setText(usb.getStorageFree() + " free of " + usb.getStorageTotal());

//        viewHolder.txtSystemStorageSizeAvilable.post(new Runnable() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void run() {
//                viewHolder.txtSystemStorageSizeAvilable.setText(usb.getStorageFree() + " free of " + usb.getStorageTotal());
//            }
//        });

    }

    class UsbViewHolder {

        private ImageView imgUsbDisk;
        private ProgressBar pbarSystem;
        private RelativeLayout rlSysPocket, rlItemHolder, rlPocketRound;
        private TextView txt_storage_name, txtSystemStorageSizeAvilable;

        public UsbViewHolder(View view) {
            imgUsbDisk = view.findViewById(R.id.imgUsbDisk);
            pbarSystem = view.findViewById(R.id.pbarSystem);
            rlSysPocket = view.findViewById(R.id.rlSysPocket);
            rlItemHolder = view.findViewById(R.id.rlItemHolder);
            rlPocketRound = view.findViewById(R.id.rlPocketRound);
            txt_storage_name = view.findViewById(R.id.txt_storage_name);
            txtSystemStorageSizeAvilable = view.findViewById(R.id.txtSystemStorageSizeAvilable);
        }
    }
}
