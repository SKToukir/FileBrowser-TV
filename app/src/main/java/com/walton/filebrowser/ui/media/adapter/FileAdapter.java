package com.walton.filebrowser.ui.media.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.media.activity.BaseActivity;
import com.walton.filebrowser.ui.media.util.FileUtil;
import com.walton.filebrowser.ui.media.util.ImageWorker;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * data adapter
 *
 * @author liu_tianbao Provide data for the list or thumbnail
 */
public class FileAdapter extends BaseAdapter {
    private final static String TAG = "LocalPlayer--FileAdapter";
    private ImageWorker mImageWorker;
    public List<File> list;
    private int layout = 0;
    public static final int IMAGE = 1;
    public static final int VIDEO = 2;
    LayoutInflater inflater;
    BaseActivity context;
    public static Boolean isChecBoxShow = false;
    public static Map<Integer, Boolean> isSelectedMap = new HashMap<Integer, Boolean>();

    public FileAdapter(BaseActivity context, List<File> list, int layout,
                       Handler handler, Boolean isChecBoxShow) {
        this.list = list;
        this.layout = layout;
        inflater = LayoutInflater.from(context);
        this.context = context;
        mImageWorker = new ImageWorker(context);
        this.isChecBoxShow = isChecBoxShow;
        initSelectedMapData();
    }

    /**
     * Init the checkbox selected data map value to false.
     */
    public void initSelectedMapData() {
        for (int len = 0; len < list.size(); len++) {
            isSelectedMap.put(len, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addFile(File file) {
        this.list.add(file);
    }

    public void addDir(int location, File file) {
        this.list.add(location, file);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<File> getFiles() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return getMyView(convertView, position);
    }

    public class ViewHolder {
        TextView tvFileName;
        ImageView ivFileIcon;
    }

    public static Map<Integer, Boolean> getIsSelectedMap() {
        return isSelectedMap;
    }

    public static void setIsSelected(Map<Integer, Boolean> isSelectedMap) {
        FileAdapter.isSelectedMap = isSelectedMap;
    }

    private View getMyView(View convertView, final int position) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_view_item, null);
            holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_filename);
            holder.ivFileIcon = (ImageView) convertView.findViewById(R.id.iv_fileicon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File file = list.get(position);
        String f_type = FileUtil.getMIMEType(file, context);
        if (file.isFile()) {
            if ("audio/*".equals(f_type)) {
                holder.ivFileIcon.setImageResource(R.drawable.file_type_audio);
            } else if ("video/*".equals(f_type)) {
                mImageWorker.setLoadingImage(R.drawable.file_type_video);
                mImageWorker.loadImage(file.getAbsolutePath(), ImageWorker.FILETYPE.VIDEO, holder.ivFileIcon);
            } else if ("apk/*".equals(f_type)) {
                mImageWorker.setLoadingImage(R.drawable.file_type_apk);
                mImageWorker.loadImage(file.getAbsolutePath(), ImageWorker.FILETYPE.APK, holder.ivFileIcon);
            } else if (f_type.contains("image")) {
                mImageWorker.setLoadingImage(R.drawable.file_type_picture);
                mImageWorker.loadImage(file.getAbsolutePath(), ImageWorker.FILETYPE.IMAGE, holder.ivFileIcon);
            } else {
                holder.ivFileIcon.setImageResource(R.drawable.file_type_file);
            }
        } else {
            holder.ivFileIcon.setImageResource(R.drawable.file_type_folder);
        }
        holder.tvFileName.setText(file.getName());
        return convertView;
    }
}
