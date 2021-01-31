package com.walton.filebrowser.ui.media.util;

import android.util.Log;

public class LogUtil {

	/**
	 * true:打开log false:关闭所有的日志
	 */
	private static boolean OPEN_LOG = true;

	/**
	 * true : 打开debug 日志 false:关闭debug日志
	 */
	private static boolean DEBUG = true;
	/**
	 * TAG 名称
	 */
	private static String tag = "jdk";//LogUtil.class.getSimpleName();
	private String mClassName;
	private static LogUtil log;
	// private static final String USER_NAME = "@tool@";

	private LogUtil() {
		// mClassName = name;
	}

	/**
	 * Get The Current Function Name
	 *
	 * @return Name
	 */
	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			mClassName = st.getFileName();
			StringBuffer sb = new StringBuffer();
			//sb.append("\n");
			//sb.append("| Thread : ");
			//sb.append(Thread.currentThread().getName());
			//sb.append("\n");
			sb.append("| [");
			sb.append(st.getMethodName());
			sb.append(" (");
			sb.append(st.getFileName());
			sb.append(":");
			sb.append(st.getLineNumber());
			sb.append(")]");
			sb.append(" ");
			sb.append("%s");
			sb.append("\n");
			return sb.toString();
		}
		return null;
	}

	private String getClassName() {
		return mClassName;
	}

	public static void setTag(String tag) {
		LogUtil.tag = tag;
	}

	public static void i(Object str) {
		print(Log.INFO, str);
	}

	public static void d(Object str) {
		print(Log.DEBUG, str);
	}

	public static void v(Object str) {
		print(Log.VERBOSE, str);
	}

	public static void w(Object str) {
		print(Log.WARN, str);
	}

	public static void e(Object str) {
		print(Log.ERROR, str);
	}

	/**
	 * 用于区分不同接口数据 打印传入参数
	 *
	 * @param index
	 * @param str
	 */

	private static void print(int index, Object str) {
		if (!OPEN_LOG) {
			return;
		}
		if (log == null) {
			log = new LogUtil();
		}
		String name = log.getFunctionName();
		String logMsg = str.toString();
		if (name != null) {
			logMsg = String.format(name, str.toString());
		}

		// Close the debug log When DEBUG is false
		if (!DEBUG) {
			if (index <= Log.DEBUG) {
				return;
			}
		}
//		StringBuilder sb = new StringBuilder(tag);
//		sb.append("-");
//		sb.append(log.getClassName());
//		String tag = sb.toString();
		switch (index) {
			case Log.VERBOSE:
				Log.v(tag, logMsg);
				break;
			case Log.DEBUG:
				Log.d(tag, logMsg);
				break;
			case Log.INFO:
				Log.i(tag, logMsg);
				break;
			case Log.WARN:
				Log.w(tag, logMsg);
				break;
			case Log.ERROR:
				Log.e(tag, logMsg);
				break;
			default:
				break;
		}
	}
}