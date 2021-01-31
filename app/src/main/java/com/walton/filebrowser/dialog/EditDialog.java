package com.walton.filebrowser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.util.ToastHelper;
import com.walton.filebrowser.util.UiUtils;

/**
 * Created by lph on 2016/7/29.
 * 带输入框的对话框
 */
public class EditDialog extends Dialog implements View.OnClickListener {

    private final String mTitle;
    private final String mContent;
    public TextView mTvTitle;
    private EditText mEtName;
    private Button mBtnOk;
    private Button mBtnCancle;
    private Context mContext;

    private OnEditDialogListener mOnEditDialogListener;

    public void setmOnEditDialogListener(OnEditDialogListener mOnEditDialogListener) {
        this.mOnEditDialogListener = mOnEditDialogListener;
    }

    public EditDialog(Context context, String title, String content) {
        super(context, R.style.Dialog);
        mTitle = title;
        mContent = content;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dialog);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mEtName = (EditText) findViewById(R.id.et_name);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnCancle = (Button) findViewById(R.id.btn_cancle);
        init();
    }

    private void init() {
        if(mContent!=null){
            mEtName.setText(mContent);
            mEtName.selectAll();
        }
        mTvTitle.setText(mTitle);

        mBtnOk.setOnClickListener(this);
        mBtnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                if (mEtName.getText().toString().isEmpty()){
                    ToastHelper.newInstance(UiUtils.getContext()).show("Field is empty!");
                    return;
                }
                mOnEditDialogListener.onOkButtonClick(mEtName.getText().toString());
                break;
            case R.id.btn_cancle:
                cancel();
                break;
            case R.id.et_name:
                break;
        }
//        cancel();
    }
}
