package com.walton.filebrowser.focus;

import android.view.View;

import androidx.annotation.NonNull;

public interface IFlying {
    void onMoveTo(@NonNull View focusView, float scaleX, float scaleY, float raduis);
}
