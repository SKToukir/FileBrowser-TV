//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>
package com.walton.filebrowser.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.walton.filebrowser.business.video.LoginInfoDBAdapter;
import com.mstar.android.samba.SmbDevice;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.walton.filebrowser.R;

public class LoginSambaDialog extends Dialog {
    private static final String TAG = "LoginSambaDialog";
    private EditText user = null;
    private EditText password = null;
    private Button confirm = null;
    private Button cancel = null;
    private Handler handler = null;
    private Context mContext = null;
    private LoginInfoDBAdapter dbAdapter = null;
    private SmbDevice mSmbDevice = null;

    private Window mWindow = null;
    private Handler mHandler = new Handler();
    private WindowManager mWindowManager = null;

    public LoginSambaDialog(Context context, SmbDevice smbDevice) {
        super(context);
        mContext = context;
        mSmbDevice = smbDevice;
    }

    public LoginSambaDialog(Context context, Handler handler, SmbDevice smbDevice) {
        super(context);
        this.handler = handler;
        mContext = context;
        mSmbDevice = smbDevice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Instantiation new window
        mWindow = getWindow();
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.samba_input);

        mWindowManager = mWindow.getWindowManager();

        setCancelable(false);
        findViews();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        mHandler.post(new Runnable() {
            public void run() {

                Display display = mWindowManager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);

                // Definition of window width and height
                int width = (int) (point.x * 0.25);
                int height = (int) (point.y * 0.35);

                WindowManager.LayoutParams prams = mWindow.getAttributes();
                prams.width = width;
                prams.height = height;
                mWindowManager.updateViewLayout(mWindow.getDecorView(), prams);
            }
        });
        super.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (user.hasFocus()) {
                password.requestFocus();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    private void findViews() {
        user = (EditText) findViewById(R.id.samba_user_name);
        password = (EditText) findViewById(R.id.samba_user_pass);
        confirm = (Button) findViewById(R.id.samba_login);
        cancel = (Button) findViewById(R.id.samba_cancel);
        user.requestFocus();
        confirm.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);

        if(mSmbDevice != null){
            if(dbAdapter == null){
                 dbAdapter = new LoginInfoDBAdapter(mContext);
            }
            dbAdapter.open();
            String savedstr[] = new String[2];
            dbAdapter.query_loginInfo(mSmbDevice.getAddress(), savedstr);
            Log.d(TAG, "  setText   usr:"+savedstr[0]+"   passwd:"+savedstr[1]);
            user.setText(savedstr[0]);
            password.setText(savedstr[1]);
            dbAdapter.close();
        }
        setOnEditActionListener();
    }

    private void setOnEditActionListener() {
        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i(TAG, "onEditorAction ----------- actionId:" + actionId);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }

        });
    }

    private Button.OnClickListener onClickListener = new Button.OnClickListener(){
    @Override
        public void onClick(View arg0){
                responseButton(arg0.getId());
                cancel();
        }
    };

    private void responseButton(int id){
        Message msg = new Message();
        switch (id) {
        case R.id.samba_login:
            // Retrieve the user input user name
            String usr = user.getText().toString();
            usr.replace("\\", "\\\\");
            // Get the password of user input
            String pwd = password.getText().toString();
            Log.i(TAG, "user: " + usr + " pass: " + pwd);
            Bundle b = new Bundle();
            b.putString("USERNAME",usr);
            b.putString("PASSWORD", pwd);
            msg.setData(b);
            msg.what = 1;
            handler.sendMessage(msg);
            break;
        case R.id.samba_cancel:
            msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            break;
        }
    }

    private static String stringFilter(String  str) throws PatternSyntaxException{
        String regEx="[^a-zA-Z0-9_@.]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
