//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>
package com.walton.filebrowser.business.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.util.Constants;

/**
 *
 * Provide adapter to listview.
 *
 *
 *
 * @date 2011-11-01
 *
 * @version 1.0.0 *
 *
 * @author 何远超 (heyc@biaoqi.com.cn).
 *
 * @category List mode of radio mode adapter
 */
public class DataAdapter extends BaseAdapter {
    // load listview
    private LayoutInflater layoutInflater;
    private static final String TAG ="DataAdapter";
    // ListView item
    private List<BaseData> list;
    private Handler handler;
    private int itemHeight = 0;

    private static class ItemViewHolder {
        // Folder icon or file type icon, such as music icon
        ImageView iconImage;
        // Folder or file name, can also said equipment name or sharing
        // equipment ip
        TextView nameText;
        // File format, mainly describe the file extension
        TextView formatText;
        // Other description information, such as file size
        TextView descriptionText;
    }

    public DataAdapter(Activity activity, Handler handler, List<BaseData> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list = new ArrayList<BaseData>();
        }
        layoutInflater = LayoutInflater.from(activity);
        this.handler = handler;
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
        ItemViewHolder holder = null;
        // Line uninitialized
        if (convertView == null) {
            // Through the flater initialization line view
            convertView = layoutInflater.inflate(
                    R.layout.radio_selection_mode_list_item, null);
            convertView.setOnHoverListener(new MyOnHoverListener());
            // And will view 3 is view quoted on the tag
            holder = new ItemViewHolder();
            holder.iconImage = (ImageView) convertView
                    .findViewById(R.id.iconImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.formatText = (TextView) convertView
                    .findViewById(R.id.formatText);
            holder.descriptionText = (TextView) convertView
                    .findViewById(R.id.descriptionText);
            convertView.setTag(holder);
            // If the line has initialization, directly from the tag attributes
            // for child views reference
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        // Get line data (model)
        BaseData item = list.get(position);
        if (item != null) {
            holder.iconImage.setBackgroundResource(item.getIcon());
            int type = item.getType();
            if (Constants.FILE_TYPE_RETURN == type) {
                holder.nameText.setText(R.string.back);
            } else {
                holder.nameText.setText(item.getName());
            }
            if (Constants.FILE_TYPE_DIR == type) {
                holder.formatText.setText(R.string.folder);
            } else {
                holder.formatText.setText(item.getFormat());
            }
            if (item.getDescription() != null
                    && !(Constants.FILE_TYPE_RETURN == type)) {
                holder.descriptionText.setText(item.getDescription());
            } else {
                holder.descriptionText.setText("");
            }
            if (position == Constants.LIST_MODE_DISPLAY_NUM) {
                itemHeight = (int) convertView.getY() / Constants.LIST_MODE_DISPLAY_NUM;
            }
        }
        return convertView;
    }

    class MyOnHoverListener implements OnHoverListener {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            if (itemHeight == 0) {
                itemHeight = v.getHeight();
            }
            switch (what) {
            // Mouse Enter view
            case MotionEvent.ACTION_HOVER_ENTER:
                int pos = (int) v.getY() / itemHeight;
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ADAPTER_POSITION, pos);
                Message msg = new Message();
                msg.what = Constants.UPDATE_LISTVIEW_FOCUS;
                msg.setData(bundle);
                handler.sendMessage(msg);
                break;
            }
            return false;
        }
    }
}