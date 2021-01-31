package com.walton.filebrowser.focus;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ListView;

public class FocusListView extends ListView {

	public FocusListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		int lastSelectItem = getSelectedItemPosition();
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if (gainFocus) {
			if (lastSelectItem < 0) {
				lastSelectItem = 0;
			}
			setSelection(lastSelectItem);
		}
	}

}