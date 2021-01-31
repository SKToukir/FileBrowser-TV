package com.walton.filebrowser.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.walton.filebrowser.FileFilter.MyFileFilter;
import com.walton.filebrowser.FocusTest;
import com.walton.filebrowser.R;
import com.walton.filebrowser.load.ImageLoader;
import com.walton.filebrowser.managers.FileManager;
import com.walton.filebrowser.model.FileModel;
import com.walton.filebrowser.util.ApkUtil;
import com.walton.filebrowser.util.StorageSpaceCount;
import com.walton.filebrowser.util.TypeUtils;
import com.walton.filebrowser.util.UiUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private List<FileModel> mDatas;
    private File[] files;
    private final LayoutInflater mInflater;
    private HashSet<CheckBox> mCbs;
    private HashMap<Integer, Boolean> mCheckedCbs;
    private int mState;

    public GridViewAdapter(Context context, List<FileModel> data, File[] fileList, HashMap<Integer, Boolean> mCheckedCbs) {

        this.files = fileList;
        mDatas = data;
        mCbs = new HashSet<CheckBox>();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mCheckedCbs = mCheckedCbs;
        UiUtils.getContext().registerReceiver(new GetSelectStateReceiver(), new IntentFilter("com.walton.filebrowser.getstate"));
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_files, parent, false);
            convertView.setTag(new ViewHolder(convertView));
            Intent intent = new Intent();
            intent.setAction("com.walton.filebrowser.datachange");
            UiUtils.getContext().sendBroadcast(intent);

        }

        viewHolder = (ViewHolder) convertView.getTag();
        bindViewData(position, viewHolder);


        return convertView;
    }

    private void bindViewData(int position, ViewHolder viewHolder) {

        String title = files[position].getName();
        viewHolder.titleTv.setText(title);
        File file = files[position];


        if (mCheckedCbs.size() > 0) {
            if (mCheckedCbs.get(position) != null && mCheckedCbs.get(position)) {
                viewHolder.mCbChecked.setChecked(true);
            } else {
                viewHolder.mCbChecked.setChecked(false);
            }
        }
        //Handling that some checkboxes are not selected
        if (mState == 1) {
            viewHolder.mCbChecked.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mCbChecked.setVisibility(View.INVISIBLE);
        }

        if(file.isDirectory()){
            //根据不同的文件类型，设置不同的图片图标
            File[] chlidfiles = file.listFiles(new MyFileFilter());
            boolean isEmpty = false;
            if(chlidfiles==null || chlidfiles.length==0){
                isEmpty = true;
            }
            viewHolder.imgFiles.setImageBitmap(null);
//            if(file.canRead()){
//                if(isEmpty){
//                    viewHolder.imgFiles.setImageResource(R.drawable.file_icon_folder_h);
//                }else{
//                    viewHolder.imgFiles.setImageResource(R.drawable.file_icon_folder_h);
//                }
//            }else{
//                if(isEmpty){
//                    viewHolder.imgFiles.setImageResource(R.drawable.file_icon_folder_h);
//                }else{
//                    viewHolder.imgFiles.setImageResource(R.drawable.file_icon_folder_h);
//                }
//            }
            viewHolder.imgFiles.setImageResource(R.drawable.file_icon_folder_h);
        }

        else {
            String fileName = file.getName().toLowerCase();

            if(TypeUtils.isImage(fileName)){
//                    mImageLoader.loadImage(mIvIcon);
                viewHolder.imgFiles.setBackgroundDrawable(null);
                Glide.with(viewHolder.imgFiles.getContext())
                        .load(file)
                        .placeholder(R.drawable.file_icon_picture)
                        .into(viewHolder.imgFiles);
            }else if(TypeUtils.isAudio(fileName)){
                viewHolder.imgFiles.setImageResource(R.drawable.file_icon_mp3);
            }else if(TypeUtils.isVideo(fileName)){
                viewHolder.imgFiles.setImageResource(R.drawable.video_default);
//                Glide.with(viewHolder.imgFiles.getContext())
//                        .load(file)
//                        .placeholder(R.drawable.video_default)
//                        .into(viewHolder.imgFiles);
            }else if(TypeUtils.isTxt(fileName)){
                viewHolder.imgFiles.setImageResource(R.drawable.file_icon_txt);
            }else if(fileName.endsWith(".apk")){
                //获取apk图标
                Drawable apkIcon = ApkUtil.loadApkIcon(UiUtils.getContext(), file.getAbsolutePath());
                if(apkIcon==null){

                    viewHolder.imgFiles.setBackgroundResource(R.mipmap.ic_apk_icon);
                }else{
                    viewHolder.imgFiles.setImageDrawable(apkIcon);
                }
            }else {
//                viewHolder.imgFiles.setBackgroundResource(R.drawable.file_icon_default);
                viewHolder.imgFiles.setImageResource(R.drawable.file_icon_default);
            }

        }

//        setIcon(files[position], viewHolder);

    }

    public void update(File[] update) {
        this.files = update;
        this.notifyDataSetChanged();
    }

    public HashSet<CheckBox> getmCbs() {
        return mCbs;
    }

    class ViewHolder {
        View itemView;
        ImageView imgFiles;
        RelativeLayout fl_main_layout;
        TextView titleTv;
        private CheckBox mCbChecked;

        public ViewHolder(View view) {
            this.itemView = view;
            this.fl_main_layout = view.findViewById(R.id.fl_main_layout);
            this.imgFiles = view.findViewById(R.id.imgFiles);
            this.titleTv = (TextView) view.findViewById(R.id.txtFileTile);
            this.mCbChecked = view.findViewById(R.id.cb_checked);
            mCbs.add(mCbChecked);
        }
    }

//    public void setIcon(File file, ViewHolder holder) {
//
//
//        String extension;
//        Drawable drawable = null;
//
//        try {
//
//            extension = fileManager.getExtension(file.getAbsolutePath());
//
//            if (file.isFile()) {
//
//                switch (extension) {
//
//                    case ".apk":
//                        if (ApkUtil.loadApkIcon(UiUtils.getContext(), file.getAbsolutePath()) == null) {
//                            drawable = UiUtils.getDrawalbe(R.mipmap.ic_launcher);
//                        } else {
//                            drawable = ApkUtil.loadApkIcon(UiUtils.getContext(), file.getAbsolutePath());
//                        }
//                        break;
//
//                    case ".c":
//                    case ".cpp":
//                    case ".doc":
//                    case ".docx":
//                    case ".exe":
//                    case ".h":
//                    case ".html":
//                    case ".java":
//                    case ".log":
//                    case ".txt":
//                    case ".pdf":
//                    case ".ppt":
//                    case ".xls":
//                    case ".xlsx":
//                        drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_file);
//                        break;
//
//                    case ".3ga":
//                    case ".aac":
//                    case ".mp3":
//                    case ".m4a":
//                    case ".ogg":
//                    case ".wav":
//                    case ".wma":
//                        drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_audio);
//                        break;
//
//                    case ".3gp":
//                    case ".avi":
//                    case ".mpg":
//                    case ".mpeg":
//                    case ".mp4":
//                    case ".mkv":
//                    case ".webm":
//                    case ".wmv":
//                    case ".vob":
////                        drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_video);
//
//                        Glide.with(mContext).load(file.getAbsolutePath())
//                                .thumbnail(0.5f)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(holder.imgFiles);
//
//
//                        return;
//
//                    case ".ai":
//                    case ".bmp":
//                    case ".exif":
//                    case ".gif":
//                    case ".jpg":
//                    case ".jpeg":
//                    case ".png":
//                    case ".svg":
//
//                        Glide.with(mContext).load(file.getAbsolutePath())
//                                .thumbnail(0.5f)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(holder.imgFiles);
//                        return;
//
//                    case ".rar":
//                    case ".zip":
//                    case ".ZIP":
//                        drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_compressed);
//                        break;
//
//                    default:
//                        drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_error);
//                        break;
//                }
//
//            } else if (file.isDirectory()) {
//                drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_folder);
//            } else drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_error);
//
//        } catch (Exception e) {
//            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_error);
//        }
//
//        drawable = DrawableCompat.wrap(drawable);
//
//        holder.imgFiles.setImageDrawable(drawable);
//    }

    public class GetSelectStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mState = intent.getIntExtra("state", 1000);
        }
    }
}