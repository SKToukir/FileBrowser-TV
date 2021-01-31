package com.walton.filebrowser.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.walton.filebrowser.R;

import java.util.ArrayList;

/**
 * For all ListView of play list dialog.
 */
public class ListDataAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder holder;
    private ArrayList<String> optionData;

    public ListDataAdapter(Context context, ArrayList<String> optionData) {
        try {
            mInflater = LayoutInflater.from(context);
            this.optionData = optionData;
        } catch (Exception e) {
        }
    }

    public int getCount() {
        return optionData.size();
    }

    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_data_list_adapter,
                    null);
            holder = new ViewHolder();
            holder.settingOptionTextView = (TextView) convertView
                    .findViewById(R.id.ListNameTextView);
            convertView.setTag(holder);
        } else {
            holder.settingOptionTextView = (TextView) convertView
                    .findViewById(R.id.ListNameTextView);
        }
        holder.settingOptionTextView.setText(optionData.get(position));
//        holder.settingOptionTextView.setSelected(true);
        return convertView;
    }

    static class ViewHolder {
        TextView settingOptionTextView;
    }
}