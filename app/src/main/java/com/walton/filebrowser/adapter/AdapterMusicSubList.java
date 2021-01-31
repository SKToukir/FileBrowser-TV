package com.walton.filebrowser.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.ui.music.EqualizerView;
import com.walton.filebrowser.ui.music.MusicPlayerActivity;
import com.walton.filebrowser.util.MusicUtils;
import com.walton.filebrowser.util.ToastHelper;
import com.walton.filebrowser.util.UiUtils;

import java.util.List;
import java.util.logging.Handler;

public class AdapterMusicSubList extends RecyclerView.Adapter<AdapterMusicSubList.MyViewHolder> {

    private static final String TAG = "AdapterMusicSubList";

    private Context mContext;
    private final LayoutInflater mInflater;
    private List<BaseData> baseDataList;
    private OnItemCallBack onItemCallBack;

    public AdapterMusicSubList(Context context, List<BaseData> list) {
        this.mContext = context;
        this.baseDataList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_sub_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        bindViewData(position, holder);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return baseDataList.size();
    }

//    @Override
//    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        AdapterMusicSubList.ViewHolder viewHolder = null;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.music_sub_item, viewGroup, false);
//            convertView.setTag(new AdapterMusicSubList.ViewHolder(convertView));
//        }
//
//        viewHolder = (AdapterMusicSubList.ViewHolder) convertView.getTag();
//        bindViewData(i, viewHolder);
//
//
//        return convertView;
//    }

    private void bindViewData(int i, AdapterMusicSubList.MyViewHolder viewHolder) {


        String title = baseDataList.get(i).getName();
        viewHolder.txtContentName.setText(title);

        viewHolder.imgCurrentPlayingEq.post(new Runnable() {
            @Override
            public void run() {
                if (MusicPlayerActivity.currentPosition == i) {
                    viewHolder.imgCurrentPlayingEq.setVisibility(View.VISIBLE);
                    viewHolder.imgCurrentPlayingEq.animateBars();
                    viewHolder.itemView.animate().scaleX(1.2f).scaleY(1.2f).start();
                } else {
                    viewHolder.itemView.animate().scaleX(1.0f).scaleY(1.0f).start();
                    viewHolder.imgCurrentPlayingEq.stopBars();
                    viewHolder.imgCurrentPlayingEq.setVisibility(View.GONE);
                }
            }
        });

        Bitmap bm = createAlbumThumbnail(baseDataList.get(i).getPath());

        if (bm == null) {
            bm = MusicUtils.getArtwork(mContext, baseDataList.get(i).getId(), baseDataList.get(i).getAlbum(), false);
            if (bm == null) {
                bm = MusicUtils.getDefaultArtwork(mContext);
            }
        }

        viewHolder.imgMusic.setImageBitmap(bm);

        viewHolder.itemView.setOnClickListener(new RecyclerClick(i));
        viewHolder.itemView.setOnFocusChangeListener(new MyFocusChange(i,
                viewHolder));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private EqualizerView imgCurrentPlayingEq;
        private ImageView imgMusic;
        private TextView txtContentName;
        private RelativeLayout fl_main_layout;
//        private MainUpView mainUpViewSubList;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            this.fl_main_layout = itemView.findViewById(R.id.fl_main_layout);
            this.imgCurrentPlayingEq = view.findViewById(R.id.eq);
            this.imgMusic = view.findViewById(R.id.imgMusic);
            this.txtContentName = view.findViewById(R.id.txtMusicItem);

//            mainUpViewSubList = itemView.findViewById(R.id.mainUpViewSubList);
//            mainUpViewSubList.setVisibility(View.GONE);
//            mainUpViewSubList.setEffectBridge(new EffectNoDrawBridge());
//            EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpViewSubList.getEffectBridge();
//            bridget.setTranDurAnimTime(0);
//            // 设置移动边框的图片.
////            mainUpViewSubList.setUpRectResource(R.drawable.focused_border);
//            // 移动方框缩小的距离.
//            mainUpViewSubList.setDrawUpRectPadding(new Rect(3, 3, 3, 3));
        }

    }


    private Bitmap createAlbumThumbnail(String filePath) {
        Log.i(TAG, "-------- createAlbumThumbnail ---------   filePath:" + filePath);
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            byte[] art = retriever.getEmbeddedPicture();
            Log.i(TAG, "art:" + art);
            if (art != null) {
                Log.i(TAG, "art.length:" + art.length);
            }
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
        } catch (IllegalArgumentException ex) {
            Log.i(TAG, "IllegalArgumentException");
        } catch (RuntimeException ex) {
            Log.i(TAG, "RuntimeException");
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }

    private class MyFocusChange implements View.OnFocusChangeListener {

        int position;
        MyViewHolder holder;

        public MyFocusChange(int position, MyViewHolder holder) {
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
//            if (hasFocus) {
////                holder.fl_main_layout.animate().scaleX(1.15f).scaleY(1.15f).start();
//                holder.mainUpViewSubList.setVisibility(View.VISIBLE);
//                holder.mainUpViewSubList.setFocusView(holder.fl_main_layout,1.0f);
//            } else {
////                holder.fl_main_layout.animate().scaleX(1f).scaleY(1f).start();
//                    holder.mainUpViewSubList.setVisibility(View.GONE);
//            }
            if (onItemCallBack != null) {
                onItemCallBack.onFocusChange(v, hasFocus, position);
            }
        }

    }

    private class RecyclerClick implements View.OnClickListener {

        int position;

        public RecyclerClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onItemCallBack != null) {
                onItemCallBack.onItemClick(v, position);
            }
        }

    }

    public void setOnItemCallBack(OnItemCallBack onItemCallBack) {
        this.onItemCallBack = onItemCallBack;
    }

    public interface OnItemCallBack {
        public void onFocusChange(View v, boolean hasFocus, int posiotion);

        public void onItemClick(View v, int position);
    }

}
