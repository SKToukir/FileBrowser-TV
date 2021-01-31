package com.walton.filebrowser.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.walton.filebrowser.R;

import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MenuListAdapter extends ArrayAdapter<Map<String, Object>> implements View.OnClickListener {

    private List<Map<String, Object>> map1;
    private Context mContext;
    private SharedPreferences mSharedPre;
    private boolean isTaskAvailable;
    private boolean isFolderIsEmpty;

    public MenuListAdapter(Context context, List<Map<String, Object>> objects) {
        super(context, R.layout.item_menu, objects);
        this.mContext = context;
        this.map1 = objects;
        mSharedPre = mContext.getSharedPreferences("config", MODE_PRIVATE);
    }

    public void setTaskAvailable(boolean taskAvailable) {
        isTaskAvailable = taskAvailable;
    }

    public void setFolderIsEmpty(boolean folderIsEmpty) {
        isFolderIsEmpty = folderIsEmpty;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, Object> obj = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_menu, parent, false);
            viewHolder.tv_menu = convertView.findViewById(R.id.tv_menu);
            viewHolder.iv_menu = convertView.findViewById(R.id.iv_menu);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Log.d("Position", "getView: " + position);

        viewHolder.tv_menu.setText((String) obj.get("tv_menu"));
        viewHolder.iv_menu.setImageResource((int) obj.get("iv_menu"));

        if (isFolderIsEmpty) {
            if (!isTaskAvailable) {
                if (position == 1) {
                    result.setAlpha(0.2f);
                } else if (position == 4) {
                    result.setAlpha(1.0f);
                } else {
                    result.setAlpha(0.2f);
                }
            } else {
                if (position == 1) {
                    result.setAlpha(1.0f);
                } else if (position == 4) {
                    result.setAlpha(1.0f);
                } else {
                    result.setAlpha(0.2f);
                }
            }

        } else {
            if (!isTaskAvailable) {
                if (position == 1) {
                    result.setAlpha(0.2f);
                } else {
                    result.setAlpha(1.0f);
                }
            } else {
                result.setAlpha(1.0f);
            }
        }
        return result;
    }

    @Override
    public void onClick(View view) {

    }

    public static class ViewHolder {
        TextView tv_menu;
        ImageView iv_menu;
    }
}
