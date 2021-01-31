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
package com.walton.filebrowser.ui.music;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.walton.filebrowser.util.Tools;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Log;


/**
 *
 * Read lyrics content
 *
 *
 *
 * @author xuc
 */
public class LrcRead {
    private final static String TAG = "LrcRead";
    private List<LyricContent> LyricList;
    private LyricContent mLyricContent;
    private BufferedReader mBufferedReader;
    private InputStreamReader mInputStreamReader;
    private boolean isClose = true;
    private BufferedInputStream bis;
    private InputStream is;
    private InputStream isForGetCharset;
    public LrcRead() {
        mLyricContent = new LyricContent();
        LyricList = new ArrayList<LyricContent>();
    }

    public String GetCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            if (bis != null) {
                bis.close();
            }
            bis = new BufferedInputStream(new FileInputStream(file));
            // bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "unicode";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0x5B
                    && first3Bytes[1] == (byte) 0x30) {
                charset = "ISO8859-1";
                checked = true;
            } else {
                charset = "GBK";
                checked = true;
            }
            // bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                bis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return charset;
    }

    public String GetCharset(final String filePath) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            if (bis != null) {
                bis.close();
            }
            if (Tools.isNetPlayback(filePath)) {
                try {
                    isForGetCharset = new URL(filePath).openStream();
                } catch (MalformedURLException e) {
                     e.printStackTrace();
                } catch (IOException e) {
                     e.printStackTrace();
                }
                if (isForGetCharset == null) {
                    Log.i(TAG,"isForGetCharset is null");
                    return null;
                }
                bis = new BufferedInputStream(isForGetCharset);
            } else {
                File file = new File(filePath);
                bis = new BufferedInputStream(new FileInputStream(file));
            }
            // bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "unicode";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0x5B
                    && first3Bytes[1] == (byte) 0x30) {
                charset = "ISO8859-1";
                checked = true;
            } else {
                charset = "GBK";
                checked = true;
            }
            // bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                bis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return charset;
    }

    public void Read(String url) throws FileNotFoundException, IOException {
        String Lrc_data = "";
        String tmpPath = null;
        if (Tools.isSambaPlaybackUrl(url)) {
            tmpPath = Tools.convertToHttpUrl(url);
        } else {
            tmpPath = url;
        }
        final String realPath  = tmpPath;
        if (Tools.isNetPlayback(realPath)) {
            try {
                is = new URL(realPath).openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File mFile = new File(realPath);
            is = new FileInputStream(mFile);

        }
        if (is == null) {
            Log.i(TAG,"is is null");
            return;
        }
        String charset = Tools.getFileEncoding(realPath);
        Log.i(TAG,"charset:"+charset);

        if(charset == null){
            return;
        }
        if (!charset.equalsIgnoreCase("big5") && !charset.equalsIgnoreCase("UTF-8")) {
            charset = GetCharset(realPath);
        }
        mInputStreamReader = new InputStreamReader(is,charset);
        // One line read
        mBufferedReader = new BufferedReader(mInputStreamReader);
        while ((Lrc_data = mBufferedReader.readLine()) != null) {
            isClose = false;
            AnalyzeLRC(Lrc_data);
            /*
             *
             * Lrc_data=Lrc_data.replace("[", "");
             *
             * Lrc_data=Lrc_data.replace("]", "@"); String
             *
             * splitLrc_data[]=Lrc_data.split("@"); if(splitLrc_data.length>1){
             *
             * mLyricContent.setLyric(splitLrc_data[1]); int LyricTime=
             *
             * TimeStr(splitLrc_data[0]); mLyricContent.setLyricTime(LyricTime);
             *
             * LyricList.add(mLyricContent); mLyricContent=new LyricContent(); }
             */
        }
        // The list of words according to the time axis sort
        Collections.sort(LyricList, new Sort());
        mBufferedReader.close();
        mInputStreamReader.close();
        if (is!=null) {
            is.close();
        }
        if (isForGetCharset!=null) {
            isForGetCharset.close();
        }
        isClose = true;
    }

    /**
     *
     * The list of words according to the time axis sort
     *
     *
     *
     * @author neddy
     */
    private class Sort implements Comparator<LyricContent> {
        public Sort() {
        }

        public int compare(LyricContent tl1, LyricContent tl2) {
            return sortUp(tl1, tl2);
        }

        private int sortUp(LyricContent tl1, LyricContent tl2) {
            if (tl1.getLyricTime() < tl2.getLyricTime())
                return -1;
            else if (tl1.getLyricTime() > tl2.getLyricTime())
                return 1;
            else
                return 0;
        }
    }

    /**
     *
     * Will a line of lyrics stripping, time axis and lyrics separation (note:
     * [03:16. 52] [02:12. 92] [00:50. 74] you love me a little you tried again
     * this form of lyrics)
     *
     * @param LRCText
     *
     * @return
     */
    private String AnalyzeLRC(String LRCText) {
        try {
            int pos1 = LRCText.indexOf("[");
            int pos2 = LRCText.indexOf("]");
            if (pos1 == 0 && pos2 != -1) {
                Long time[] = new Long[GetPossiblyTagCount(LRCText)];
                time[0] = TimeToLong(LRCText.substring(pos1 + 1, pos2));
                if (time[0] == -1)
                    return ""; // LRCText
                String strLineRemaining = LRCText;
                int i = 1;
                while (pos1 == 0 && pos2 != -1) {
                    strLineRemaining = strLineRemaining.substring(pos2 + 1);
                    pos1 = strLineRemaining.indexOf("[");
                    pos2 = strLineRemaining.indexOf("]");
                    if (pos2 != -1) {
                        time[i] = TimeToLong(strLineRemaining.substring(
                                pos1 + 1, pos2));
                        if (time[i] == -1)
                            return ""; // LRCText
                        i++;
                    }
                }
                int length = time.length;
                for (int j = 0; j < length; j++) {
                    if (time[j] != null) {
                        mLyricContent.setLyric(strLineRemaining);
                        mLyricContent.setLyricTime(time[j].intValue());
                        LyricList.add(mLyricContent);
                        mLyricContent = new LyricContent();
                    }
                }
                return strLineRemaining;
            } else
                return "";
        } catch (Exception e) {
            return "";
        }
    }

    private int GetPossiblyTagCount(String Line) {
        String strCount1[] = Line.split("\\[");
        String strCount2[] = Line.split("\\]");
        if (strCount1.length == 0 && strCount2.length == 0)
            return 1;
        else if (strCount1.length > strCount2.length)
            return strCount1.length;
        else
            return strCount2.length;
    }

    public void close() {
        if (mBufferedReader != null && isClose == false) {
            try {
                mBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mInputStreamReader != null && isClose == false) {
            try {
                mInputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long TimeToLong(String Time) {
        try {
            String[] s1 = Time.split(":");
            int min = Integer.parseInt(s1[0]);
            String[] s2 = s1[1].split("\\.");
            int sec = Integer.parseInt(s2[0]);
            int mill = 0;
            if (s2.length > 1)
                mill = Integer.parseInt(s2[1]);
            return min * 60 * 1000 + sec * 1000 + mill * 10;
        } catch (Exception e) {
            return -1;
        }
    }

    public int TimeStr(String timeStr) {
        System.out.println(" in TimeStr time Str = " + timeStr);
        timeStr = timeStr.replace(":", ".");
        timeStr = timeStr.replace(".", "@");
        String timeData[] = timeStr.split("@");
        int minute = 0;
        int second = 0;
        int currentTime = 0;
        int millisecond = 0;
        try {
            minute = Integer.parseInt(timeData[0]);
            second = Integer.parseInt(timeData[1]);
            millisecond = Integer.parseInt(timeData[2]);
        } catch (Exception e) {
            minute = 0;
            second = 0;
            millisecond = 0;
        }
        currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        return currentTime;
    }

    public List<LyricContent> GetLyricContent() {
        return LyricList;
    }
}
