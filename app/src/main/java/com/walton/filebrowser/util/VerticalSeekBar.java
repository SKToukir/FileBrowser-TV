package com.walton.filebrowser.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.walton.filebrowser.ui.video.VideoPlayerViewHolder;

public class VerticalSeekBar extends SeekBar {
	private int height = 150, width = 29;

	private float xPos = -1, yPos = -1;
	
	private VideoPlayerViewHolder mHolder;

	@SuppressWarnings("unused")
	private int top = -1, bottom = -1, left = -1, right = -1;

	public VerticalSeekBar(Context context) {
		super(context, null);

	}

	public VerticalSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs, com.android.internal.R.attr.seekBarStyle);

	}

	public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setHolder(VideoPlayerViewHolder holder) {
		mHolder = holder;
	}

	public interface d {
		public void get(int x);
	}

	/**
	 * A callback that notifies clients when the progress level has been
	 * changed. This includes changes that were initiated by the user through a
	 * touch gesture or arrow key/trackball as well as changes that were
	 * initiated programmatically.
	 */
	public interface OnSeekBarChangeListener {

		/**
		 * Notification that the progress level has changed. Clients can use the
		 * fromUser parameter to distinguish user-initiated changes from those
		 * that occurred programmatically.
		 * 
		 * @param seekBar
		 *            The SeekBar whose progress has changed
		 * @param progress
		 *            The current progress level. This will be in the range
		 *            0..max where max was set by
		 *            {@link ProgressBar#setMax(int)}. (The default value for
		 *            max is 100.)
		 * @param fromUser
		 *            True if the progress change was initiated by the user.
		 */
		void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

		/**
		 * Notification that the user has started a touch gesture. Clients may
		 * want to use this to disable advancing the seekbar.
		 * 
		 * @param seekBar
		 *            The SeekBar in which the touch gesture began
		 */
		void onStartTrackingTouch(SeekBar seekBar);

		/**
		 * Notification that the user has finished a touch gesture. Clients may
		 * want to use this to re-enable advancing the seekbar.
		 * 
		 * @param seekBar
		 *            The SeekBar in which the touch gesture began
		 */
		void onStopTrackingTouch(SeekBar seekBar);
	}

	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		int h = MeasureSpec.getSize(heightMeasureSpec);
		height = h;
		this.setMeasuredDimension(width, height);

	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldw, oldh);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		left = l;
		right = r;
		top = t;
		bottom = b;
	}

	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-height, 0);
		super.onDraw(c);
		this.setMax(10);
	}

	public boolean onTouchEvent(MotionEvent event) {
		xPos = event.getX();
		yPos = event.getY() + this.getTop();
		float progress = 0;
		if((this.getBottom() - yPos) * 2 > this.getBottom() - this.getTop()){
			progress = (this.getBottom() - yPos)
					/ (this.getBottom() - this.getTop() - 30);
		}else{
			progress = (this.getBottom() - yPos - 30)
					/ (this.getBottom() - this.getTop());
		}
		float offset;

		Log.d("onTouchEvent", "xPos= " + xPos);
		Log.d("onTouchEvent", "yPos= " + yPos);
		Log.d("onTouchEvent", "this.getBottom()= " + this.getBottom());
		Log.d("onTouchEvent", "this.getTop()= " + this.getTop());
		Log.d("onTouchEvent", "progress= " + progress);
		offset = (-(progress) * (this.getBottom() - this.getTop())) + 20;

		Log.d("onTouchEvent", "offset= " + (int) offset);
		int mProgress = (int) (10 * progress);
		Log.d("onTouchEvent", "Progress = " + mProgress);
		this.setProgress(mProgress);
		//this.setThumbOffset((int) offset);
		if(mHolder != null){
			mHolder.setVoice(mProgress);
		}
		return true;
	}

}
