package com.walton.filebrowser.util;

import android.os.SystemProperties;
import android.view.KeyEvent;

import androidx.core.view.MotionEventCompat;

import com.walton.filebrowser.ui.MediaContainerApplication;

import java.util.ArrayList;
import java.util.List;

public class PackageInstallManager {
    private static PackageInstallManager manager;
    private List<Integer> input = new ArrayList();
    private List<Integer> password = new ArrayList();

    private PackageInstallManager() {
        this.password.add(82);
        this.password.add(8);
        this.password.add(9);
        this.password.add(7);
        this.password.add(9);
    }

    public static PackageInstallManager getInstance() {
        if (manager == null) {
            synchronized (PackageInstallManager.class) {
                if (manager == null) {
                    manager = new PackageInstallManager();
                }
            }
        }
        return manager;
    }

    public boolean isAllowInstallPackage() {
        return SystemProperties.getInt("persist.sys.usbinstall.enable", 1) != 0;
    }

    public boolean handleKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case MotionEventCompat.AXIS_RX:
            case MotionEventCompat.AXIS_RY:
            case MotionEventCompat.AXIS_RZ:
            case 15:
            case 16:
            case 82:
                this.input.add(Integer.valueOf(keyCode));
                if (this.input.size() > this.password.size()) {
                    this.input.remove(0);
                }
                if (this.input.size() == this.password.size() && checkPassword(this.input)) {
                    MediaContainerApplication.getInstance().setOpenInstallPackage(true);
                }
                return true;
            default:
                return false;
        }
    }

    public boolean checkPassword(List<Integer> input2) {
        if (input2 == null || this.password.size() != input2.size()) {
            return false;
        }
        int size = this.password.size();
        for (int i = 0; i < size; i++) {
            if (!this.password.get(i).equals(input2.get(i))) {
                return false;
            }
        }
        return true;
    }
}