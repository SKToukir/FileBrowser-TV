package com.walton.filebrowser.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewTV extends GridView {

    public GridViewTV(Context context) {
        this(context, null);
    }

    public GridViewTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public boolean isInTouchMode() {
        return !(hasFocus() && !super.isInTouchMode());
    }

    private void init(Context context, AttributeSet attrs) {
        this.setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (this.getSelectedItemPosition() != -1) {
            if (i + this.getFirstVisiblePosition() == this.getSelectedItemPosition()) {// This is the last item to be refreshed
                return childCount - 1;
            }
            if (i == childCount - 1) {// This is the last item that needs to be refreshed
                return this.getSelectedItemPosition() - this.getFirstVisiblePosition();
            }
        }
        return i;
    }

    public void setDefualtSelect(int pos) {
        requestFocusFromTouch();
        setSelection(pos);
    }


}