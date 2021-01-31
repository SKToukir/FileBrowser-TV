package com.walton.filebrowser.ui.media.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import com.walton.filebrowser.R;

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context, R.style.progress_dialog);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress);

    }

    public static CustomProgressDialog show(Context context) {
        CustomProgressDialog dialog = new CustomProgressDialog(context, R.style.dialog);
        dialog.show();
        return dialog;
    }
}
