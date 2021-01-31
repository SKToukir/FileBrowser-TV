package com.walton.filebrowser.ui.main;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.util.GridViewTV;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class UsbBrowserViewHolder {

    private static final String TAG = "UsbBrowserViewHolder";

    private Activity rootActivity;
    
    private Handler handler;

    /************************************************************************
     * Control and layout definition area
     ************************************************************************/

    protected GridViewTV gridViewUsb;

    protected AVLoadingIndicatorView avi;

    public UsbBrowserViewHolder(Activity activity, Handler handler){
        this.rootActivity = activity;
        this.handler = handler;
    }


    protected void findViews(){
        gridViewUsb = rootActivity.findViewById(R.id.gridViewUsb);
        avi = rootActivity.findViewById(R.id.avi);
    }

    protected void setGridViewFocused(boolean focusable, int position){
        if (focusable){
            gridViewUsb.setFocusable(true);
            gridViewUsb.requestFocus();
            gridViewUsb.setSelection(position);
        }else {
            gridViewUsb.setFocusable(false);
        }
    }

    public View getViewByPosition(int pos) {
        final int firstListItemPosition = gridViewUsb.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + gridViewUsb.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return gridViewUsb.getAdapter().getView(pos, null, gridViewUsb);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return gridViewUsb.getChildAt(childIndex);
        }

    }
    
    protected void showLoading(boolean flag){
        if (flag){
            avi.setVisibility(View.VISIBLE);
        }else {
            avi.setVisibility(View.GONE);
        }
    }

}
