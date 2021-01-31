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
package com.walton.filebrowser.dlna.server;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Dlna Class Constants.
 */
public class DLNAConstants {
	public static final String PATH = "/data/data/com.jrm.filefly/";
	public static final int MEDIA_TYPE_IMAGE = 0x001;
	public static final int MEDIA_TYPE_AUDIO = 0x002;
	public static final int MEDIA_TYPE_VIDEO = 0x003;
	public static final int SOURCE_FROM_DLNA = 0x010;// source from dlna.
	public static Map<String, Integer> MEDIATYPE = new HashMap<String, Integer>();
	static {
		// Picture suffix lowercase.
		MEDIATYPE.put("jpg", MEDIA_TYPE_IMAGE);
		MEDIATYPE.put("png", MEDIA_TYPE_IMAGE);
		MEDIATYPE.put("bmp", MEDIA_TYPE_IMAGE);
		MEDIATYPE.put("gif", MEDIA_TYPE_IMAGE);
		MEDIATYPE.put("jpeg", MEDIA_TYPE_IMAGE);
		MEDIATYPE.put("tif", MEDIA_TYPE_IMAGE);
		// Audio suffix lowercase.
		MEDIATYPE.put("mp3", MEDIA_TYPE_AUDIO);
		MEDIATYPE.put("wma", MEDIA_TYPE_AUDIO);
		MEDIATYPE.put("wav", MEDIA_TYPE_AUDIO);
		// Video suffix lowercase.
		MEDIATYPE.put("wmv", MEDIA_TYPE_VIDEO);
		MEDIATYPE.put("mp4", MEDIA_TYPE_VIDEO);
		MEDIATYPE.put("rmvb", MEDIA_TYPE_VIDEO);
		MEDIATYPE.put("rm", MEDIA_TYPE_VIDEO);
		MEDIATYPE.put("avi", MEDIA_TYPE_VIDEO);
		MEDIATYPE.put("flv", MEDIA_TYPE_VIDEO);
		MEDIATYPE.put("3gp", MEDIA_TYPE_VIDEO);
	}

	public static void printI(Class<?> cls, String msg) {
		Log.i(cls.getName(), msg);
	}

	public static void printE(Class<?> cls, String msg) {
		Log.i(cls.getName(), msg);
	}

	public static int mediaTypeTemp(String profix) {
		String temp = profix.toLowerCase();
		if (MEDIATYPE.get(temp) == null) {
			return 0;
		}
		return MEDIATYPE.get(temp) > 0 ? MEDIATYPE.get(temp) : 0;
	}

	/**
	 * 
	 * Through the MIME type judgment is what type of file
	 */
	public static int checkMimeType(String checked) {
		if (checked == null) {
			return -1;
		}
		if (checked.indexOf("image") != -1) {
			return DLNAConstants.MEDIA_TYPE_IMAGE;
		}
		if (checked.indexOf("audio") != -1) {
			return DLNAConstants.MEDIA_TYPE_AUDIO;
		}
		if (checked.indexOf("video") != -1) {
			return DLNAConstants.MEDIA_TYPE_VIDEO;
		}
		return -1;
	}
}
