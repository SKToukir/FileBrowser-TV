package com.walton.filebrowser.ui.video;


import android.content.Context;
import android.util.Log;


import java.util.ArrayList;

/**
 * Created by nate.luo on 15-8-6.
 */
public class TaskThread extends Thread {
    private static final String TAG = "TaskThread";
    private Context mContext = null;
    private ArrayList<Runnable> mEventQueue = new ArrayList<Runnable>();
    private boolean mExited = false;
    private boolean mShouldExit = false;

    public TaskThread(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void run() {
        Log.i(TAG, "TaskThread run");
        setName("TaskThread " + getId());

        guardedRun();
    }

    public void queueEvent(Runnable r) {
        // Log.i(TAG, "queueEvent Runnable:" + r);
        if (r == null) {
            throw new IllegalArgumentException("r must not be null");
        }
        //mEventQueue.add(r);
        addAndRemoveEvent(true,r);
    }
    public synchronized Runnable addAndRemoveEvent(boolean tag,Runnable r) {
        if (true == tag ) {
            mEventQueue.add(r);
        } else {
            return mEventQueue.remove(0);
        }
        return null;
    }
    public void clearEvent() {
        mEventQueue.clear();
    }

    public void requestExit() {
        mShouldExit = true;
    }

    private void guardedRun() {
        Runnable event = null;
        while (!mShouldExit) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mContext == null) {
                return;
            }

            if (!mEventQueue.isEmpty()) {
                //event = mEventQueue.remove(0);
                event = addAndRemoveEvent(false,null);
                if (event != null) {
                    event.run();
                    event = null;
                } else {
                    Log.i(TAG,"even is null");
                }
            }
        }
    }

}
