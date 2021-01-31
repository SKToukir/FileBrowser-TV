package com.walton.filebrowser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.walton.filebrowser.R;

/**
 * Created by lph on 2016/7/28.
 */
public class NormalDialog extends Dialog implements View.OnClickListener {
    private Button mBtnOk;
    private Button mBtnCancle;
    private TextView mTvTitle;
    private String mTitle;

    public void setmListener(OnNormalDialogButtonClickListener mListener) {
        this.mListener = mListener;
    }

    private OnNormalDialogButtonClickListener mListener;

    public NormalDialog(Context context, String title) {
        super(context, R.style.Dialog);
        mTitle = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_dialog);
        init();
    }

    private void init() {
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnCancle = (Button) findViewById(R.id.btn_cancle);
        mTvTitle = (TextView) findViewById(R.id.tv_ask);
        mBtnOk.setOnClickListener(this);
        mBtnCancle.setOnClickListener(this);
//        mTvTitle.setText(mTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                mListener.onOkButtonClick();
                break;
            case R.id.btn_cancle:
                mListener.onCancleButtonClick();
                break;
        }
        cancel();
    }


}
