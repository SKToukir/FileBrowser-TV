package com.walton.filebrowser.ui.media.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import com.walton.filebrowser.R;

public class ToastUtil {

    public static void showToast(Context ctx, int duration, String text) {
        TextView tv = new TextView(ctx);
        tv.setPadding(20, 20, 20, 20);
        tv.setTextSize(25);
        tv.setText(text);
        tv.setTextColor(ctx.getResources().getColor(R.color.white));
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.toast_bg);
        Toast toast = new Toast(ctx);
        toast.setView(tv);
        toast.setDuration(duration);
        toast.show();
    }

    public static void toast(Context context, String text) {
        TextView tv = new TextView(context);
        tv.setPadding(20, 20, 20, 20);
        tv.setTextSize(25);
        tv.setText(text);
        tv.setTextColor(context.getResources().getColor(R.color.white));
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.toast_bg);
        Toast toast = new Toast(context);
        toast.setView(tv);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
