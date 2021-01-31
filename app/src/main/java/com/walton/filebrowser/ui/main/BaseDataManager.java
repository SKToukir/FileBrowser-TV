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

import java.io.File;
import java.math.BigInteger;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.res.Resources;
import android.util.Log;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

/**
 * Data management base class. <li>The completion of the specified path data
 * scanning and classification. <li>Provide interface to return to designated
 * type of media types.
 */
public class BaseDataManager {

    // android app ResourceInstance
    private Resources resource;

    // data container
    public MediaContainerApplication mediaContainer = null;

    private Comparator<BaseData> comparator = new Comparator<BaseData>() {

        @Override
        public int compare(BaseData lData, BaseData rData) {

            String lName = lData.getName();
            String rName = rData.getName();
            if (lName != null && rName != null) {
                Collator collator = Collator.getInstance(Locale.CHINA);
                return collator.compare(lName.toLowerCase(),
                        rName.toLowerCase());

            } else {
                Log.e("comparator", "lName != null && rName != null is false");
                return 0;
            }

        }
    };

    /**
     * The base class constructor.
     *
     * @param resource
     *            {@link Resources}.
     */
    public BaseDataManager(final Resources resource) {
        this.resource = resource;
        this.mediaContainer = MediaContainerApplication.getInstance();
    }

    /**
     * For UI display need folder data and media file data.
     *
     * @param type
     *            medium type.
     * @return Returns the current directory all folders and designated type of
     *         media files.
     */
    public final synchronized List<BaseData> getUIDataList(final int type) {
        List<BaseData> local = new ArrayList<BaseData>();
        // Add all folders data
        local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_DIR));

        if (type == Constants.OPTION_STATE_ALL) {
            // Add all file data
            local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_FILE));

            // Add all designated type of media files
        } else {
            local.addAll(getMediaFileList(type));
        }

        return local;
    }

    /**
     * For specified types of media file data.
     *
     * @param type
     *            medium type.
     * @return Returns the current directory all designated type of media files.
     */
    public final synchronized ArrayList<BaseData> getMediaFileList(
            final int type) {
        ArrayList<BaseData> local = new ArrayList<BaseData>();

        switch (type) {
        case Constants.OPTION_STATE_PICTURE:
            // Add all pictures data
            local.addAll(mediaContainer
                    .getMediaData(Constants.FILE_TYPE_PICTURE));

            break;

        case Constants.OPTION_STATE_SONG:
            // Add all the music data
            local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_SONG));

            break;

        case Constants.OPTION_STATE_VIDEO:
            // Add all the video data
            local.addAll(mediaContainer.getMediaData(Constants.FILE_TYPE_VIDEO));

            break;

        default:
            break;
        }

        return local;
    }

    /**
     * Open a new thread complete file scanning and classification.
     *
     * @param path
     *            Scanning using absolute path.
     */
    public final synchronized void startScan(final String path) {
        // If the path is empty is directly to return
        if (path == null || path.length() == 0) {
            return;
        }
        // Startup thread scanning
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    // Only directory to scan
                    if (file.isDirectory()) {
                        File[] ff = file.listFiles();
                        if (file.list() != null && file.list().length > 0) {
                            // Under the current directory lists all documents
                            // (folder)
                            scan(file.listFiles());
                        } else {
                            // Clear all cache data
                            mediaContainer.clearAll();
                        }

                    }
                } catch (Exception e) {
                    Log.i("SCAN", "********scan**fail**********");
                }

                // Data scan complete
                onFinish();
            }
        }).start();

    }

    /**
     * Data scan complete.
     */
    public void onFinish() {
    }

    /*
     * Scanning the specified directory of all files or folders.
     */
    private void scan(final File[] files) {
        // all files
        List<BaseData> localFile = new ArrayList<BaseData>();
        // all folders
        List<BaseData> localFolder = new ArrayList<BaseData>();
        // all pictures
        List<BaseData> localPicture = new ArrayList<BaseData>();
        // all musics
        List<BaseData> localSong = new ArrayList<BaseData>();
        // all videos
        List<BaseData> localVideo = new ArrayList<BaseData>();

        try {
            // Under the current directory lists all documents (folder) list
            for (File f : files) {
                // Obtain filename
                String name = f.getName();
                BaseData file = new BaseData();
                // Setting absolute path
                file.setPath(f.getAbsolutePath());
                // Set parent directory path
                file.setParentPath(f.getParent());
                // Set up files (folders) name
                file.setName(name);

                // Scanning to folder
                if (f.isDirectory()) {
                    file.setType(Constants.FILE_TYPE_DIR);
                    localFolder.add(file);

                    // Scanning to file
                } else {
                    // Obtain and set the file extension
                    int pos = name.lastIndexOf(".");
                    String extension = "";
                    if (pos > 0) {
                        extension = name.toLowerCase().substring(pos + 1);
                        file.setFormat(extension);
                    }
                    String formatSize = Tools.formatSize(BigInteger.valueOf(f
                            .length()));
                    // setting file size
                    file.setSize(formatSize);
                    file.setDescription(formatSize);
                    // Set the file modification time
                    file.setModifyTime(f.lastModified());

                    // According to the file name to judgment document type, set up
                    // different types of files
                    if (check(name, resource.getStringArray(R.array.photo_filter))) {
                        file.setType(Constants.FILE_TYPE_PICTURE);

                        localPicture.add(file);

                        // music file
                    } else if (check(name,
                            resource.getStringArray(R.array.audio_filter))) {
                        file.setType(Constants.FILE_TYPE_SONG);

                        localSong.add(file);

                        // video file
                    } else if (check(name,
                            resource.getStringArray(R.array.video_filter))) {
                        file.setType(Constants.FILE_TYPE_VIDEO);

                        localVideo.add(file);

                        // playlist file
                    } else if (check(name,
                            resource.getStringArray(R.array.playlist_filter))) {
                        file.setType(Constants.FILE_TYPE_MPLAYLIST);
                        // other file
                    } else {
                        file.setType(Constants.FILE_TYPE_FILE);
                    }
                    // Save all data
                    localFile.add(file);
                }
            }
        } catch (Exception e) {

        }
        // Clear all cache data
        mediaContainer.clearAll();
        // All documents in order to list
        if (localFile.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_FILE, localFile);
        }
        // For all folders sorting
        if (localFolder.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_DIR, localFolder);
        }
        // For all image list sorting
        if (localPicture.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_PICTURE, localPicture);
        }
        // For all music list sorting
        if (localSong.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_SONG, localSong);
        }
        // For all video list sorting
        if (localVideo.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_VIDEO, localVideo);
        }

    }

    /*
     * Through the filename judgment is what types of documents..
     */
    public boolean check(final String name, final String[] extensions) {
        for (String end : extensions) {
            // Name never to null, without exception handling
            if (name.toLowerCase().endsWith(end)) {
                return true;
            }
        }

        return false;
    }

    /*
     * Cache Data.
     */
    public void putAllToCache(final int type, final List<BaseData> src) {
        // sort data
        Collections.sort(src, comparator);
        // cache to memory
        mediaContainer.putMediaData(type, src);
    }

    public void dataListEmpty(boolean isData){

    }

}
