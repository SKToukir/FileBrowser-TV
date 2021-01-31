//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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
package com.walton.filebrowser.business.video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.util.Constants;

import com.walton.filebrowser.util.Tools;

/**
 *
 * @author (andrew.gao)
 *
 * @since 1.0
 *
 * @date 2017-7-16
 */
public class SubtitleTool {
    private final static String TAG = "SubtitleTool";
    public static final int SUBTITLE_FORMATE_NULL = 0;
    // Text Subtitles category
    public static final int SUBTITLE_FORMATE_SRT = 1;
    public static final int SUBTITLE_FORMATE_SSA = 2;
    public static final int SUBTITLE_FORMATE_ASS = 3;
    public static final int SUBTITLE_FORMATE_SMI = 4;
    public static final int SUBTITLE_FORMATE_TXT = 5;
    // image Subtitles category
    public static final int SUBTITLE_FORMATE_IDX = 6;
    public static final int SUBTITLE_FORMATE_SUP = 7;
    public static final int SUBTITLE_FORMATE_SUB = 8;
    private int cuurentSubtitleType = SUBTITLE_FORMATE_NULL;
    private String VideoPath = "";
    private static String[] SUBTITLE_FORMATE_ARRAY = new String[9];
    public static final float PIP_SUBTITLE_SIZE_MAJOR = 15.0f;
    public static final float PIP_SUBTITLE_SIZE_MEDIUM = 10.0f;
    public static final float PIP_SUBTITLE_SIZE_SMALL = 7.0f;
    public static final float SUBTITLE_SIZE_MAJOR = 45.0f;
    public static final float SUBTITLE_SIZE_MEDIUM = 35.0f;
    public static final float SUBTITLE_SIZE_SMALL = 25.0f;
    public static float currentPipSubSize = PIP_SUBTITLE_SIZE_MEDIUM;
    public static float currentSubSize = SUBTITLE_SIZE_MEDIUM;
    private List<BaseData> mSubtitleList = new ArrayList<BaseData>();
    /**
     *
     * Subtitle auxiliary class constructor.
     *
     * @param path
     *            Play video path.
     *
     * @return
     */
    public SubtitleTool(String path) {
        VideoPath = path;
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_NULL] = "";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_SRT] = ".srt";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_SSA] = ".ssa";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_ASS] = ".ass";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_SMI] = ".smi";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_TXT] = ".txt";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_IDX] = ".idx";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_SUP] = ".sup";
        SUBTITLE_FORMATE_ARRAY[SUBTITLE_FORMATE_SUB] = ".sub";
    }

    /**
     *
     * According to the broadcast video path for the path all supported formats
     * subtitles path string.
     *
     * @param format
     *            Value is SUBTITLE FORMATE NULL acquired when all supported
     *            formats subtitles. Can also be used to retrieve a specified
     *            format subtitles.
     *
     * @return ArrayList<String> Under the specified path all subtitles.
     */
    public ArrayList<String> getSubtitlePathList(final int format) {
        ArrayList<String> list = new ArrayList<String>();
        // Obtain all support format subtitles
        int index = VideoPath.lastIndexOf(File.separator) + 1;
        String path = VideoPath.substring(0, index);
        int length = SUBTITLE_FORMATE_ARRAY.length;
        Log.i(TAG,"path:"+path);
        if (Tools.isSambaPlaybackUrl(path)) {
            mSubtitleList = MediaContainerApplication.getInstance().getMediaData(Constants.FILE_TYPE_FILE);
            for (BaseData tmpBaseData : mSubtitleList) {
                String tmpSubtitlePath = tmpBaseData.getPath();
                Log.i(TAG,"tmpSubtitlePath:"+tmpSubtitlePath);
                for (int j = SUBTITLE_FORMATE_SRT; j < length; j++) {
                    if (tmpSubtitlePath.toLowerCase().contains(SUBTITLE_FORMATE_ARRAY[j])) {
                        list.add(tmpSubtitlePath);
                    }
                }
            }
        } else {
            File file = new File(path);
            scan(file.listFiles(), list);
        }
        if (format == SUBTITLE_FORMATE_NULL)
            return list;
        else
            return FilterFormat(list, format);
    }

    /**
     *
     * According to the broadcast video acquisition and video name exactly the
     * same title full path.
     *
     * @param
     *
     * @return String And video name exactly the same title full path.
     */
    public String getSubtitlePath() {
        cuurentSubtitleType = SUBTITLE_FORMATE_NULL;
        String strSubtitlePath = "";
        String titlePath = VideoPath.substring(0, VideoPath.lastIndexOf("."));
        // text srt format
        strSubtitlePath = titlePath + ".srt";
        if (Tools.isFileExist(strSubtitlePath)) {
            cuurentSubtitleType = SUBTITLE_FORMATE_SRT;
            return strSubtitlePath;
        } else
            strSubtitlePath = "";
        // text ssa format
        strSubtitlePath = titlePath + ".ssa";
        if (Tools.isFileExist(strSubtitlePath)) {
            cuurentSubtitleType = SUBTITLE_FORMATE_SSA;
            return strSubtitlePath;
        } else
            strSubtitlePath = "";
        // text ass format
        strSubtitlePath = titlePath + ".ass";
        if (Tools.isFileExist(strSubtitlePath)) {
            cuurentSubtitleType = SUBTITLE_FORMATE_ASS;
            return strSubtitlePath;
        } else
            strSubtitlePath = "";
        // text smi format
        strSubtitlePath = titlePath + ".smi";
        if (Tools.isFileExist(strSubtitlePath)) {
            cuurentSubtitleType = SUBTITLE_FORMATE_SMI;
            return strSubtitlePath;
        } else
            strSubtitlePath = "";
        // text txt format
        strSubtitlePath = titlePath + ".txt";
        if (Tools.isFileExist(strSubtitlePath)) {
            cuurentSubtitleType = SUBTITLE_FORMATE_TXT;
            return strSubtitlePath;
        } else
            strSubtitlePath = "";
        // image format
        strSubtitlePath = titlePath + ".idx";
        String tempImagePath = titlePath + ".sub";
        if (Tools.isFileExist(strSubtitlePath)
                && Tools.isFileExist(tempImagePath)) {
            cuurentSubtitleType = SUBTITLE_FORMATE_IDX;
            return strSubtitlePath;
        } else
            strSubtitlePath = "";
        return strSubtitlePath;
    }

    public int getCuurentSubtitleType() {
        return cuurentSubtitleType;
    }

    public boolean isTextSubtitleType() {
        if (cuurentSubtitleType > 0
                && cuurentSubtitleType < SUBTITLE_FORMATE_IDX)
            return true;
        else
            return false;
    }

    public boolean isImageSubtitleType() {
        if (cuurentSubtitleType >= SUBTITLE_FORMATE_IDX)
            return true;
        else
            return false;
    }

    /**
     *
     * scan file
     */
    private void scan(File[] files, ArrayList<String> fileList) {
        String FullFileName;
        int j;
        int length = SUBTITLE_FORMATE_ARRAY.length;
        if (files == null) {
            return;
        }
        for (File currentFile : files) {
            // get file name
            FullFileName = currentFile.getAbsolutePath();
            // Judgment is a folder or a file
            if (!currentFile.isDirectory()) {
                // Filter files in the file can identify the subtitles format
                for (j = SUBTITLE_FORMATE_SRT; j < length; j++) {
                    int index = FullFileName.lastIndexOf(".");
                    if (index >= 0 && index < FullFileName.length()) {
                        String fileType = FullFileName.substring(index);
                        Log.i(TAG, "scan: fileType: " + fileType);
                        if (fileType.toLowerCase().equals(SUBTITLE_FORMATE_ARRAY[j])) {
                            fileList.add(FullFileName);
                        }
                    }
                }
            }
        }
    }

    /*
     *
     * Filter appointed format
     */
    private ArrayList<String> FilterFormat(ArrayList<String> fileList,
            int format) {
        ArrayList<String> retList = new ArrayList<String>();
        int size = fileList.size();
        String strName;
        for (int index = 0; index < size; index++) {
            strName = fileList.get(index);
            if (strName.toLowerCase().contains(SUBTITLE_FORMATE_ARRAY[format])) {
                retList.add(strName);
            }
        }
        return retList;
    }

    public static float getCurrentPIPSubSize(){
        return currentPipSubSize;
    }

    public static float getCurrentSubSize(){
        return currentSubSize;
    }

    public static void setCurrentPIPSubSize(float size){
        currentPipSubSize = size;
        if(size == PIP_SUBTITLE_SIZE_MAJOR){
            currentSubSize = SUBTITLE_SIZE_MAJOR;
        }else if(size == PIP_SUBTITLE_SIZE_MEDIUM){
            currentSubSize = SUBTITLE_SIZE_MEDIUM;
        }else{
            currentSubSize = SUBTITLE_SIZE_SMALL;
        }
    }
}
