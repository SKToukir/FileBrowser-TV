package com.walton.filebrowser.wheellib;

public interface CircularListViewListener {
    void onCircularLayoutFinished(CircularListView circularListView,
                                  int firstVisibleItem,
                                  int visibleItemCount,
                                  int totalItemCount);
}
