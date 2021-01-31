package com.walton.filebrowser.FileFilter;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by penghuilou on 2016/3/7.
 */
public class MyFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        if(!pathname.getName().startsWith(".")){
            return  true;
        }else{
            return false;
        }
    }
}
