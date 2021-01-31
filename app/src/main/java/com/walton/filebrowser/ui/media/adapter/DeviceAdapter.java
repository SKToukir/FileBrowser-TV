package com.walton.filebrowser.ui.media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.media.view.ReflectItemView;
import java.util.List;
import java.util.Map;

public class DeviceAdapter extends BaseAdapter {
    private static final String TAG = "DeviceAdapter";
    private LayoutInflater mInflater;
    private List<Map<String, String>> groupmap;
    private Context context;

    public DeviceAdapter(List<Map<String, String>> groupmap,
                         Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.groupmap = groupmap;
        /*for (Map<String, String> map : groupmap) {
            for (Entry<String, String> entry : map.entrySet()) {
                Log.i(TAG, "groupmap entry.key=" + entry.getKey());
                Log.i(TAG, "groupmap entry.value=" + entry.getValue());
            }
        }*/
    }

    @Override
    public int getCount() {
        return groupmap.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*for (Map<String, String> map : groupmap) {
            for (Entry<String, String> entry : map.entrySet()) {
                Log.i(TAG, "groupmap entry.key=" + entry.getKey());
                Log.i(TAG, "groupmap entry.value=" + entry.getValue());
            }
        }*/
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_item, null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_device_icon);
            holder.mTextView = (TextView) convertView.findViewById(R.id.tv_device_name);
            holder.mReflectLayout = (ReflectItemView) convertView.findViewById(R.id.reflect_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String mountType = groupmap.get(position).get("mountType");
        if (mountType != null) {
            int type = Integer.parseInt(mountType);
            switch (type) {
                case 0:
//                    holder.mImageView.setImageResource(R.drawable.device_usb);
                    holder.mReflectLayout.setBackgroundResource(R.drawable.selector_type_bg);
                    String str = groupmap.get(position).get("mountPath").toString();
                    String name[] = str.split("\\/");
                    holder.mTextView.setText(name[name.length - 1]);
                    break;
                case 2:
//                    holder.mImageView.setImageResource(R.drawable.device_sd);
                    holder.mReflectLayout.setBackgroundResource(R.drawable.selector_type_bg);
                    holder.mTextView.setText(R.string.sdcard);
                    break;
                default:
//                    holder.mImageView.setImageResource(R.drawable.default_sound_back);
                    holder.mReflectLayout.setBackgroundResource(R.drawable.selector_type_bg);
                    holder.mTextView.setText(R.string.sdcard);
                    break;
            }
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        private ReflectItemView mReflectLayout;
    }

}
