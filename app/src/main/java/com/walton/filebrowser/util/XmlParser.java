//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
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
package com.walton.filebrowser.util;

import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    private static final String TAG = "XmlParser";

    private static XmlParser mInstance = null;

    private List<Pair<String, Integer>> mList = null;

    public XmlParser() {
        mList = new ArrayList<Pair<String, Integer>>();
    }

    public static XmlParser getInstance() {
        if (mInstance == null) {
            synchronized (XmlParser.class) {
                if (mInstance == null) {
                    mInstance = new XmlParser();
                }
            }
        }
        return mInstance;
    }

    /**
     * Parse xml and store name and value as list
     *
     * @param xmlPath String
     * @return boolean, true: success, false: failure
     */
    public boolean parseXml(String xmlPath) {
        mList.clear();
        try {
            FileInputStream stream = getStreamFromXmlFile(xmlPath);
            if (stream != null) {
                XmlPullParser myParser = Xml.newPullParser();
                myParser.setInput(stream, null);
                int event = myParser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if ("CompilerFlag".equals(myParser.getName())
                                    || "Capability".equals(myParser.getName())) {
                                int compilerFlagCount = myParser.getAttributeCount();
                                String config = null;
                                for (int i = 0; i < compilerFlagCount; i++) {
                                    String name = myParser.getAttributeName(i);
                                    if ("name".equals(name)) {
                                        config = myParser.getAttributeValue(i);
                                    } else if ("value".equals(name)) {
                                        Log.d(TAG,
                                                "add Pair <"
                                                        + config
                                                        + ", "
                                                        + Integer.parseInt(myParser
                                                                .getAttributeValue(i)) + ">");
                                        mList.add(new Pair<String, Integer>(config, Integer
                                                .parseInt(myParser.getAttributeValue(i))));
                                    }
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                    }
                    event = myParser.next();
                }
                return true;
            } else {
                return false;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get list of xml parsing result
     *
     * @return List<Pair<String, Integer>>
     */
    public List<Pair<String, Integer>> getList() {
        return new ArrayList<Pair<String, Integer>>(mList);
    }

    private FileInputStream getStreamFromXmlFile(String xmlPath) {
        Log.d(TAG, "getStreamFromXmlFile: xmlPath = " + xmlPath);
        File file = new File(xmlPath);
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
