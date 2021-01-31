package com.walton.filebrowser.ui.media.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.ui.media.activity.BaseActivity;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FileUtil {
	private static final String TAG = "LocalPlayer--FileUtil";

	// associated with page
	private static BaseActivity mx;

	// the type of filter
	private int filterCount;

	// current path
	public String currentFilePath;

	// catalog collection
	private List<File> fileDir = new ArrayList<File>();

	// file collection
	private List<File> file = new ArrayList<File>();

	// pictrun file collection
	private List<File> photoList = null;

	// audio file collection
	private List<File> musicList = null;

	// video file collection
	private List<File> videoList = null;

	// text index
	private int Num = 0;

	// temp length
	private int tempLength = 0;

	// new add edittext
	private EditText myEditText;

	/**
	 * @param activity
	 */
	public FileUtil(BaseActivity activity) {
		mx = activity;
	}

	/**
	 * @param activity
	 * @param filterCount
	 *            // the type of filter
	 * @param arrayDir
	 * @param arrayFile
	 * @param currentFileString
	 */
	public FileUtil(BaseActivity activity, int filterCount,
                    List<File> arrayDir, List<File> arrayFile, String currentFileString) {
		mx = activity;
		this.filterCount = filterCount;
		this.fileDir = arrayDir;
		this.file = arrayFile;
		this.currentFilePath = currentFileString;
	}

	/**
	 * Constructor
	 * 
	 * @param activity
	 * @param filterCount
	 * @param currentFileString
	 */
	public FileUtil(BaseActivity activity, int filterCount,
                    String currentFileString) {
		mx = activity;
		fileDir.clear();
		file.clear();
		this.filterCount = filterCount;
		this.currentFilePath = currentFileString;
	}

	public static boolean isFileExist(String pathName) {
		File fileToCheck = new File(pathName);
		if (fileToCheck.exists() && fileToCheck.isDirectory()) {
			return true;
		}
		return false;
	}

	/**
	 * Determine the file type based on the file suffix
	 * 
	 * @param f
	 *            //file
	 * @return //Custom file types
	 */
	public static String getMIMEType(File f, BaseActivity mea) {
		mx = mea;
		String type = "";
		String fName = f.getName();
		// Get file extension
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toUpperCase();
		// read the audio condition
		// CNcomment: 读取音频条件
		SharedPreferences shareAudio = mea.getSharedPreferences("AUDIO",
				Context.MODE_PRIVATE);
		String strAudio = shareAudio.getString(end, "");
		// read the video condition
		// CNcomment: 读取视频条件
		SharedPreferences shareVideo = mea.getSharedPreferences("VIDEO",
				Context.MODE_PRIVATE);
		String strVideo = shareVideo.getString(end, "");
		// read picturn condition
		// CNcomment: 读取图片条件
		SharedPreferences shareImage = mea.getSharedPreferences("IMAGE",
				Context.MODE_PRIVATE);
		String strImage = shareImage.getString(end, "");
		if (!strAudio.equals("")) {
			type = "audio/*";
		} else if (!strVideo.equals("")) {
			if (strVideo.equals("ISO")) {
				type = "video/iso";
			} else {
				type = "video/*";
			}
		} else if (!strImage.equals("")) {
			if (strImage.equalsIgnoreCase("GIF")) {
				type = "image/gif";
			} else {
				type = "image/*";
			}

		} else if (end.toLowerCase().equals("apk")) {
			type = "apk/*";
		} else {
			type = "*/*";
		}
		return type;
	}

	public static String getMIMEType(String fileName, BaseActivity mea) {
		mx = mea;
		String type = "";
		String fName = fileName;

		// get file extension

		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toUpperCase();
		// read audio condition
		SharedPreferences shareAudio = mea.getSharedPreferences("AUDIO",
				Context.MODE_PRIVATE);
		String strAudio = shareAudio.getString(end, "");
		// read video condition
		SharedPreferences shareVideo = mea.getSharedPreferences("VIDEO",
				Context.MODE_PRIVATE);
		String strVideo = shareVideo.getString(end, "");
		// read picturn condition
		SharedPreferences shareImage = mea.getSharedPreferences("IMAGE",
				Context.MODE_PRIVATE);
		String strImage = shareImage.getString(end, "");
		if (!strAudio.equals("")) {
			type = "audio";
		} else if (!strVideo.equals("")) {
			type = "video";
		} else if (!strImage.equals("")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "apk";
		} else {
			// If you can not directly open, out of the list of software to the
			// user to select
		}
		type += "/*";
		return type;
	}
	/**
	 * get file type by file
	 * @param seletedFile
	 * @return fileType
	 */
	public static String getFileType(File seletedFile) {
		if(seletedFile.isFile()){
			String nameStr[]=seletedFile.getName().split("\\.");
			return nameStr[nameStr.length-1];
		}else if(seletedFile.isDirectory()){
			return MediaContainerApplication.getInstance().getResources().getString(R.string.directory);
		}else{
			return MediaContainerApplication.getInstance().getResources().getString(R.string.unknow);
		}

	}
	/**
	 * get file's last modified time.
	 * 
	 * @param file
	 * @return last modified time
	 */
	public static String getFileLastModified(File file) {
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar date = Calendar.getInstance();
		  date.setTimeInMillis(file.lastModified());//System.currentTimeMillis()替换成long lastModified
		  return df.format(date.getTime());
	}
	/**
	 * get file's name get rid of suffix.
	 * 
	 * @param file
	 * @return last modified time
	 */
	public static String getFileName(File file) {
		String fullName=file.getName();
		String nameArr[]=fullName.split("\\.");
		String newName=new String();
		for(int index=0;index<nameArr.length-1;index++){
			newName=newName+nameArr[index];
		}
		return newName;
	}
	/**
	 * File Size Description
	 * 
	 * @param f
	 *            //file
	 * @return // File size / unit string
	 */
	public static String fileSizeMsg(File f) {
		String show = "";
		long length=0;
		if(f.isFile()){
			length = f.length();
		}else if(f.isDirectory()){
			length=getDirSize(f);
		}else{
			return 0+"B";
		}
		// file length in bits
		// CNcomment: 文件长度以比特为单位
		DecimalFormat format = new DecimalFormat("#0.0");
		if (length / 1024.0 / 1024 / 1024 >= 1) {
			show = format.format(length / 1024.0 / 1024 / 1024) + "GB";
		} else if (length / 1024.0 / 1024 >= 1) {
			show = format.format(length / 1024.0 / 1024) + "MB";
		} else if (length / 1024.0 >= 1) {
			show = format.format(length / 1024.0) + "KB";
		} else {
			show = length + "B";
		}
		return show;

	}
	/**
	 * get fildor size
	 * 
	 * @param f
	 *            // fildor object
	 * @return // fildor size
	 */
	public static long getDirSize(File f) {
		long size = 0;
		File[] files = f.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					size += getDirSize(file);
				} else {
					size += file.length();
				}
			}
		}
		return size;
	}

	/**
	 * sort file
	 * 
	 * @param fileList
	 *            // file list
	 * @param sortMethod
	 * @param MA
	 * @return // after sort lost
	 */
	public static List<File> sortFile(List<File> fileList,
			final String sortMethod, final BaseActivity MA) {

		Collections.sort(fileList, new Comparator<File>() {
			public int compare(File object1, File object2) {
				return compareFile(object1, object2, sortMethod, MA);
			}
		});

		return fileList;
	}

	/**
	 * According to the string to obtain the corresponding date
	 * 
	 * @param str
	 *            //data string
	 * @return //data
	 */
	public static long getDate(String str) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date();
		}
		return date.getTime();
	}

	/**
	 * A local file, mount the directory to sort the files to achieve CNcomment:
	 * 
	 * @param object1
	 * @param object2
	 * @param sortMethod
	 * @param MA
	 * @return
	 */
	public static int compareFile(File object1, File object2,
			String sortMethod, BaseActivity MA) {
		mx = MA;
		if (sortMethod.equals(mx.getResources().getString(R.string.file_name))) {
			return compareByName(object1, object2);
		} else if (sortMethod.equals(mx.getResources().getString(
				R.string.file_size))) {
			int len = compareBySize(object1.length(), object2.length());
			// the same size ,sort by name
			if (len == 0) {
				return compareByName(object1, object2);
			} else {
				return compareBySize(object1.length(), object2.length());
			}
		} else if (sortMethod.equals(mx.getResources().getString(
				R.string.update_time))) {
			int len = compareByDate(object1.lastModified(),
					object2.lastModified());
			// the same data ,sort by name
			if (len == 0) {
				return compareByName(object1, object2);
			} else {
				return compareByDate(object1.lastModified(),
						object2.lastModified());
			}
		}
		return 0;
	}

	/**
	 * sort according to file name
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	private static int compareByName(String object1, String object2) {
		if (object1.startsWith("d") && object2.startsWith("d"))
			return object1.split("\\|")[1].toLowerCase().compareTo(
					object2.split("\\|")[1].toLowerCase());
		if (object1.startsWith("f") && object2.startsWith("f"))
			return object1.split("\\|")[1].toLowerCase().compareTo(
					object2.split("\\|")[1].toLowerCase());
		else
			return 0;
	}

	/**
	 * sort according to file name
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	private static int compareByName(File object1, File object2) {
		String objectName1 = object1.getName().toLowerCase();
		String objectName2 = object2.getName().toLowerCase();
		int result = objectName1.compareTo(objectName2);
		if (result == 0) {
			return 0;
		} else if (result < 0) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * sort according to file size
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	private static int compareBySize(long object1, long object2) {
		long diff = object1 - object2;
		if (diff > 0)
			return 1;
		else if (diff == 0)
			return 0;
		else
			return -1;
	}

	/**
	 * file sorted by date modified
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	public static int compareByDate(long object1, long object2) {
		long diff = object1 - object2;
		if (diff > 0)
			return 1;
		else if (diff == 0)
			return 0;
		else
			return -1;
	}

	/**
	 * get need to sort file list
	 */
	private void getFiltList(String local) {
		// File[] sortFile = new File[] {};
		// file list
		if (filterCount == 0) {
			if (fileDir.size() > 0 || file.size() > 0) {
				return;
			} else {
				// sortFile = new File(currentFilePath).listFiles();
				mx.clearList(fileDir, file);
				// get local file
				if ("local".equals(local)) {

					File[] flss = new File(currentFilePath).listFiles();
					Log.i(TAG, "currentFilePath =" + currentFilePath);
					if (flss == null)
						return;
					Log.i(TAG, "SRT.LENGTH=" + flss.length);
					for (int i = 0; i < flss.length; i++) {
						// for broken into the directory contains many
						// files,click again error

						if (mx.thread.getStopRun()) {
							return;
						}
						if (flss[i].isDirectory()) {
							fileDir.add(flss[i]);
						} else {
							file.add(flss[i]);
						}
					}
				} else {
					Log.v("\33[32m UTIL", "currentFilePath" + currentFilePath
							+ "\33[0m");
					long start = System.currentTimeMillis();
					File[] files = new File[] {};
					files = new File(currentFilePath).listFiles();
					long end = System.currentTimeMillis();
					Log.v("\33[32m UTIL", "files" + (end - start) + "\33[0m");

					if (files == null)
						return;

					for (int i = 0; i < files.length; i++) {
						if (mx.thread.getStopRun()) {
							return;
						}
						File f = files[i];
						// String name = f.getName();
						if (f.isFile()) {
							file.add(f);
						} else {
							fileDir.add(f);
						}
					}
				}
			}
			/*
			 * if (file.size() > 0){ ListIterator<File> it =
			 * file.listIterator(); while (it.hasNext()){ File f = it.next();
			 * String type = FileUtil.getMIMEType(f, mx); if
			 * (type.contains("apk")){ it.remove(); } } }
			 */
		}
		// picture list
		else if (filterCount == 3) {
			photoList = new ArrayList<File>();
			getPhoto(currentFilePath, local);
		}
		// audio list
		else if (filterCount == 2) {
			musicList = new ArrayList<File>();
			getMusic(currentFilePath, local);
		}
		// video list
		else if (filterCount == 1) {
			videoList = new ArrayList<File>();
			getVideo(currentFilePath, local);
		}
	}

	/**
	 * get video collection directory
	 * 
	 * @param fileRoot
	 * @return
	 */
	private void getVideo(String fileRoot, String local) {
		// determine the search results is empty
		if (fileDir.size() > 0 || file.size() > 0) {
			return;
		} else {
			mx.clearList(fileDir, file);
			{
				File[] files = new File[] {};
				files = new File(fileRoot).listFiles();
				if (files == null)
					return;
				for (int i = 0; i < files.length; i++) {
					if (mx.thread.getStopRun()) {
						return;
					}
					File f = files[i];
					// String name = f.getName();
					if (f.isFile()) {
						videoList.add(f);
					} else {
						fileDir.add(f);
					}
				}
			}
		}
		if (videoList.size() > 0) {
			for (File f : videoList) {
				String type = FileUtil.getMIMEType(f, mx);
				if (type.contains("video")) {
					file.add(f);
				}
			}
		}

	}

	/**
	 * get audio collection directory
	 * 
	 * @param fileRoot
	 * @return
	 */
	private void getMusic(String fileRoot, String local) {

		// determine the search results is empty
		if (fileDir.size() > 0 || file.size() > 0) {
			return;
		} else {
			// files = new File(fileRoot).listFiles();
			mx.clearList(fileDir, file);
			{
				File[] files = new File[] {};
				files = new File(fileRoot).listFiles();
				if (files == null)
					return;
				for (int i = 0; i < files.length; i++) {
					if (mx.thread.getStopRun()) {
						return;
					}
					File f = files[i];
					if (f.isFile()) {
						musicList.add(f);
					} else {
						fileDir.add(f);
					}
				}
			}
		}
		if (musicList != null) {
			for (File f : musicList) {
				String type = FileUtil.getMIMEType(f, mx);
				if (type.contains("audio")) {
					file.add(f);
				}
			}
		}
	}

	/**
	 * get picturn collection directory
	 * 
	 * @param fileRoot
	 * @return
	 */
	private void getPhoto(String fileRoot, String local) {
		// determine the search results is empty
		if (fileDir.size() > 0 || file.size() > 0) {
			return;
		} else {
			mx.clearList(fileDir, file);
			{
				File[] files = new File[] {};
				files = new File(fileRoot).listFiles();
				if (files == null)
					return;
				for (int i = 0; i < files.length; i++) {
					if (mx.thread.getStopRun()) {
						return;
					}
					File f = files[i];
					if (f.isFile()) {
						photoList.add(f);
					} else {
						fileDir.add(f);
					}
				}
			}
		}
		if (photoList != null) {
			for (File f : photoList) {
				String type = FileUtil.getMIMEType(f, mx);
				if (type.contains("image")) {
					file.add(f);
				}
			}
		}
	};

	/**
	 * get file list
	 * 
	 * @param sortCount
	 * @param local
	 * @return
	 */
	public List<File> getFiles(int sortCount, String local) {
		List<File> listFile = new ArrayList<File>();
		synchronized (mx.lock) {
			getFiltList(local);
			// for broken into the directory contains many files,click again
			// error
			if (mx.thread.getStopRun()) {
				return new ArrayList<File>();
			}
			List<File> le = FileUtil.sortFile(file, mx.getResources()
					.getStringArray(R.array.sort_method)[sortCount], mx);
			if (mx.thread.getStopRun()) {
				return new ArrayList<File>();
			}
			String sortSize = mx.getResources().getString(R.string.file_size);
			String sort = mx.getResources().getStringArray(R.array.sort_method)[sortCount];
			List<File> dir = null;
			// folder compare size ,sorted by file name
			if (sort.equals(sortSize)) {
				dir = FileUtil.sortFile(fileDir,
						mx.getResources().getString(R.string.file_name), mx);
			} else {
				dir = FileUtil.sortFile(fileDir, mx.getResources()
						.getStringArray(R.array.sort_method)[sortCount], mx);
			}
			if (mx.thread.getStopRun()) {
				return new ArrayList<File>();
			}

			// to ensure that the folder is displayed in the file before
			for (int i = 0; i < dir.size(); i++) {
				listFile.add(dir.get(i));
			}

			for (int i = 0; i < le.size(); i++) {
				listFile.add(le.get(i));
			}
			Log.v("\33[32m SMB", " listFile.size(6) " + listFile.size()
					+ "\33[0m");
			mx.clearList(fileDir, file);
			if (mx.thread.getStopRun()) {
				return new ArrayList<File>();
			}
		}
		return listFile;
	}

	/**
	 * delete files
	 * 
	 * @param listFile
	 * @return
	 */
	public static boolean deleteFile(List<File> listFile) {
		for (File file : listFile) {
			if (file.isFile()) {
				if (deleteF(file)) {
					continue;
				} else {
					return false;
				}
			}
			if (file.isDirectory()) {
				if (deleteD(file)) {
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * delete file
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteF(File file) {
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * delete directory
	 * 
	 * @param dirFile
	 * @return
	 */
	public static boolean deleteD(File dirFile) {
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		Boolean flag = true;

		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {

			if (files[i].isFile()) {
				flag = FileUtil.deleteF(files[i]);
				if (!flag)
					break;
			} else {
				flag = FileUtil.deleteD(files[i]);
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;

		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Description:search files by keyword
	 * 
	 * @param keyword
	 * @return
	 */
	public static List<File> searchFile(List<File> fileList, String keyword) {
		keyword = keyword.toLowerCase();
		List<File> list = new ArrayList<File>();
		for (File f : fileList) {
			search(f, keyword,list);
		}
		return list;
	}

	public static List<File> search(File file, String keyword,List<File> list) {
		if (file.getName().toLowerCase().indexOf(keyword) >= 0) {
			list.add(file);
			return list;
		}
		if(file.isDirectory()&&!(file.getName().contains("."))){
			File childFile[]=file.listFiles();
			if(0!=childFile.length){
				for(int index=0;index<childFile.length;index++){
					search(childFile[index], keyword, list);
				}
			}
		}
		return list;
	}
}
