package com.walton.filebrowser.util;

import android.content.Context;
import android.widget.Toast;

public class ToastFactory {
	private static Context context = null;

	private static Toast toast = null;

	/**
	 * @param context
	 *            使用时的上下文
	 * @param hint
	 *            在提示框中需要显示的文本
	 * @return 返回一个不会重复显示的toast
	 * 
	 * */

	public static Toast getToast(Context context, String hint, int gravity) {
		if (ToastFactory.context == context) {
			toast.cancel();
			System.out.println("not create");
		} else {
			System.out.println("create toast");
			ToastFactory.context = context;
		}
		toast = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
		toast.setGravity(gravity, 0, 0);
		return toast;
	}
}
