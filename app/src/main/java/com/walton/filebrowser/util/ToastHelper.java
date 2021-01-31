package com.walton.filebrowser.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.walton.filebrowser.R;

/**
 * Created by dy_lph on 2016/6/16.
 * 自定义Toast.
 * 修改Toast布局文件
 * 控制Toast显示的生命周期
 */
public class ToastHelper {
    private static final long DELAY_TIME = 2000;
    private ToastHelper(){}
    private static ToastHelper instance;
    private Toast mToast;
    private long mTime = 0;
    private static Context context;
    public static ToastHelper newInstance(Context ctx){
        if(instance==null){
            synchronized (ToastHelper.class){
                if(instance==null){
                    instance = new ToastHelper();
                }
            }
        }
        context = ctx;
        return instance;
    }
    TextView tvDes = null;



    /**
     * show
     * @param res 字符串资源id
     */
    public void show(int res){
        if(System.currentTimeMillis() - mTime< DELAY_TIME){return;}
        if(mToast==null){
            mToast = new Toast(context);
            View toastView = View.inflate(context, R.layout.custom_toast,null);
            tvDes = (TextView) toastView.findViewById(R.id.tv_des);
            tvDes.setText(res);
            mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 80);
            mToast.setView(toastView);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        tvDes.setText(res);
        mToast.show();
        mTime = System.currentTimeMillis();
    }

    /**
     * show
     * @param str 字符串
     */
    @SuppressWarnings("unused")
    public void show(String str){
        if(System.currentTimeMillis() - mTime< DELAY_TIME){return;}
        if(mToast==null){
            mToast = new Toast(context);
            View toastView = View.inflate(context, R.layout.custom_toast,null);
            tvDes = (TextView) toastView.findViewById(R.id.tv_des);
            tvDes.setText(str);
            mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 80);
            mToast.setView(toastView);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        tvDes.setText(str);
        mToast.show();
        mTime = System.currentTimeMillis();
    }

    /**
     * 当activity 失去焦点或者是执行destroy的时候，取消toast展示
     */
    public void cancle(){
        if(mToast!=null){
            mToast.cancel();
        }
    }



}
