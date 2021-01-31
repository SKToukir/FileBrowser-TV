package com.walton.filebrowser.ui.media.util;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

import com.walton.filebrowser.ui.media.activity.BaseActivity;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SocketClient {

	// server name
	private static final String SOCKET_NAME = "configserver";

	// Whether the command to the end of the logo
	// CNcomment:命令是否执行结束标识
	private boolean running = false;

	// Socket
	private LocalSocket s = null;

	// Socket address
	private LocalSocketAddress l;

	// input stream
	private InputStream is;

	// output stream
	private OutputStream os;

	// data out/in stream
	private DataOutputStream dos;

	// To receive command execution return value
	private String rec_data = null;

	// Page associated with
	private BaseActivity comActivity = null;

	/**/
	// Constructor
	// @param comActivity Page associated with

	public SocketClient(BaseActivity comActivity) {
		running = true;
		connect();
		new Thread(local_receive).start();
		this.comActivity = comActivity;

	}

	public SocketClient(BaseActivity comActivity, boolean isStartThread) {
		running = true;
		connect();
		if (isStartThread) {
			new Thread(local_receive).start();
		}
		this.comActivity = comActivity;
	}

	public SocketClient() {

		running = true;
		connect();
	}

	public void connect() {
		try {
			s = new LocalSocket();
			l = new LocalSocketAddress(SOCKET_NAME,
					LocalSocketAddress.Namespace.RESERVED);
			s.connect(l);
			is = s.getInputStream();
			os = s.getOutputStream();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// send ISO mount command
	// @param s //ISO mount command

	public void writeMess(String s) {
		try {
			Log.e("SocketClient", s);
			dos = new DataOutputStream(os);
			int strLen = s.getBytes().length;
			byte[] sendLen = intToBytes2(strLen);
			byte[] allLen = new byte[s.getBytes().length + 4];

			byte[] srcLen = s.getBytes();

			for (int i = 0; i < (s.getBytes().length + 4); i++) {
				if (i < 4) {
					allLen[i] = sendLen[i];
				} else {
					allLen[i] = srcLen[i - 4];
				}
			}
			dos.write(allLen);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * int转为字节
	 * @param n
	 * @return
	 */
	private byte[] intToBytes2(int n) {
		byte[] b = new byte[4];
		for (int i = 3; i >= 0; i--) {
			b[i] = (byte) (n >> (i * 8));
		}
		return b;
	}

	/**
	 * 异步获得命令执行结果 
	 */
	public void readNetResponseAsyn() {
		try {
			InputStream m_Rece = s.getInputStream();
			byte[] data;
			int receiveLen = 0;
			while (running) {
				receiveLen = m_Rece.available();
				data = new byte[receiveLen];
				if (receiveLen != 0) {
					m_Rece.read(data);
					rec_data = new String(data);
					Log.w("TAG", rec_data);
					// successful
					if (rec_data.contains("execute ok")) {
						comActivity.getHandler().sendEmptyMessage(10);
						running = false;
					}
					// fail
					else if (rec_data.contains("failed execute")) {
						comActivity.getHandler().sendEmptyMessage(10);
						running = false;
					}
				}
				// Infinite loop, to prevent excessive use of resources
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			m_Rece.close();
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同步获得命令执行结果
	 */
	public void readNetResponseSync() {
		try {
			InputStream m_Rece = s.getInputStream();
			byte[] data;
			int receiveLen = 0;
			while (running) {
				receiveLen = m_Rece.available();
				data = new byte[receiveLen];
				if (receiveLen != 0) {
					m_Rece.read(data);
					rec_data = new String(data);
					Log.w("TAG", rec_data);
					// successful
					if (rec_data.contains("execute ok")) {
						running = false;
					}
					// fail
					else if (rec_data.contains("failed execute")) {
						running = false;
					}
				}
				// Infinite loop, to prevent excessive use of resources
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			m_Rece.close();
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Thread local_receive = new Thread() {
		public void run() {
			readNetResponseAsyn();
		}
	};

	/**
	 * disconnect Socket
	 */
	public void close() {
		try {
			dos.close();
			is.close();
			os.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
