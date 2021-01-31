//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2012 - 2015 MStar Semiconductor, Inc. All rights reserved.
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


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.ui.main.FileBrowserActivity;
import com.walton.filebrowser.ui.main.ImageDownLoader;
import com.walton.filebrowser.ui.main.ImageDownLoader.onImageLoaderListener;


/**
 *
 * Provide adapter to gridview.
 *
 * @date 2015-01-01
 *
 * @version 1.0.0 *
 *
 * @author andrew.wang
 *
 */
public class GridAdapter extends BaseAdapter {

    private final static String TAG="GridAdapter";

    private LayoutInflater mInflater;

    private List<BaseData> gridOnePage = new ArrayList<BaseData>();

    private Hashtable position2Url = new Hashtable();

    private Handler handler;

    private int itemHeight = 0;

    private int itemWidth = 0;

    private final static int maxPictureSize = 5242880;

    private ImageDownLoader mImageDownLoader;

    public GridAdapter(Context context,Handler handler,List<BaseData>gridOnePage) {
        this.mInflater = LayoutInflater.from(context);
        this.gridOnePage=gridOnePage;
        this.handler=handler;
        mImageDownLoader = new ImageDownLoader(context);
    }
    public int getCount() {
        if (gridOnePage==null) return 0;
        return gridOnePage.size();
    }
    public Object getItem(int arg0) {
        return null;
    }
    public long getItemId(int arg0) {
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gridview_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setOnHoverListener(new GridViewOnHoverListener());
            convertView.setTag(holder);
        } else {
                holder = (ViewHolder) convertView.getTag();
        }
        Drawable drawable =null;
        String url =gridOnePage.get(position).getPath();
        int flag4HasCacheOrNot =0;
        if(url != null && url.length()>0){
            position2Url.put(position,url);
        }
        if (gridOnePage.get(position).getType() == Constants.FILE_TYPE_VIDEO) {
            Drawable tmpDrawable = mImageDownLoader.getDrawableFromMemCache(url);
            if (tmpDrawable!=null) {
                holder.img.setImageDrawable(tmpDrawable);
                flag4HasCacheOrNot=1;
            } else {
                drawable = FileBrowserActivity.mFileTypeDrawable.get(3);
                holder.img.setImageDrawable(drawable);
            }

        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_FILE) {

            drawable = FileBrowserActivity.mFileTypeDrawable.get(0);
            holder.img.setImageDrawable(drawable);

        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_PICTURE) {
            Drawable tmpDrawable = mImageDownLoader.getDrawableFromMemCache(url);
            if (tmpDrawable!=null) {
                holder.img.setImageDrawable(tmpDrawable);
                flag4HasCacheOrNot=1;

            } else {
                drawable = FileBrowserActivity.mFileTypeDrawable.get(1);
                holder.img.setImageDrawable(drawable);

            }

        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_SONG) {
            Drawable tmpDrawable = mImageDownLoader.getDrawableFromMemCache(url);
            if (tmpDrawable!=null) {
                holder.img.setImageDrawable(tmpDrawable);
                flag4HasCacheOrNot=1;

            } else {
                drawable = FileBrowserActivity.mFileTypeDrawable.get(2);
                holder.img.setImageDrawable(drawable);

            }

        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_DIR) {

            drawable = FileBrowserActivity.mFileTypeDrawable.get(4);
            holder.img.setImageDrawable(drawable);


        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_RETURN) {

            drawable = FileBrowserActivity.mFileTypeDrawable.get(5);
            holder.img.setImageDrawable(drawable);

        }
        holder.title.setText((String) gridOnePage.get(position).getName());
        final ImageView imageView = holder.img;
        if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_VIDEO && 0==flag4HasCacheOrNot) {
            mImageDownLoader.downloadImage(position,url,Constants.GRID_POSITION_IS_VIDEO, new onImageLoaderListener() {
               @Override
               public void onImageLoader(Drawable drawableResult, int positionResult ,String urlResult) {
                   String positionPresentUrl = (String)position2Url.get(positionResult);
                   if (imageView != null && drawableResult != null
                       && urlResult.equals(positionPresentUrl)) {
                       imageView.setImageDrawable(drawableResult);
                   } else
                        Log.e(TAG,"onImageLoader failed:"+urlResult);
                }
           });
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_SONG && 0==flag4HasCacheOrNot) {
            mImageDownLoader.downloadImage(position,url,Constants.GRID_POSITION_IS_MUSIC, new onImageLoaderListener() {
               @Override
               public void onImageLoader(Drawable drawableResult, int positionResult ,String urlResult) {
                   String positionPresentUrl = (String)position2Url.get(positionResult);
                   if (imageView != null && drawableResult != null
                       && urlResult.equals(positionPresentUrl)){
                       imageView.setImageDrawable(drawableResult);
                   } else
                        Log.e(TAG,"onImageLoader failed:"+urlResult);

                }
            });
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_PICTURE && 0==flag4HasCacheOrNot) {
            String tmpPath = gridOnePage.get(position).getPath();
            File tmpFile = new File(tmpPath);
            long tmpLength = tmpFile.length();
            if (tmpLength<=maxPictureSize  ) {
               mImageDownLoader.downloadImage(position,url,Constants.GRID_POSITION_IS_PICTURE, new onImageLoaderListener() {
                  @Override
                  public void onImageLoader(Drawable drawableResult, int positionResult ,String urlResult) {
                      String positionPresentUrl = (String)position2Url.get(positionResult);
                      if (imageView != null && drawableResult != null
                          && urlResult.equals(positionPresentUrl)){
                          imageView.setImageDrawable(drawableResult);
                      } else
                          Log.e(TAG,"onImageLoader failed:"+urlResult);
                  }
              });
            }
        }
        return convertView;
    }

    public final class ViewHolder {
        public ImageView img;
        public TextView title;
        //public TextView info;
    }
    public void cancelTask(boolean isShutDown){
        mImageDownLoader.cancelTask(isShutDown);
    }

    public class GridViewOnHoverListener implements OnHoverListener {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            if (0==itemHeight) {
                itemHeight = v.getHeight();
            }
            if(0==itemWidth ){
                itemWidth = v.getWidth();
            }
            switch (what) {
            case MotionEvent.ACTION_HOVER_ENTER:
                 int x = (int) v.getX()/itemWidth;
                 int y = (int) v.getY() / itemHeight;
                 Bundle bundle = new Bundle();
                 int pos=y*Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM+x;
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