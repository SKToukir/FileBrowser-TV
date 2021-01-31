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


package com.walton.filebrowser.ui.video;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import android.R.color;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.walton.filebrowser.business.adapter.ListDataListAdapter;
import com.walton.filebrowser.util.Constants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DivxInfoDialog extends Dialog {
    private static final int  KEY_PARAMETER_GET_TITLE_COUNT = 2025;
    private static final int  KEY_PARAMETER_GET_ALL_TITLE_NAME=2026;
    private static final int  KEY_PARAMETER_GET_EDITION_COUNT=2027;
    private static final int  KEY_PARAMETER_GET_ALL_EDITION_NAME=2028;
    private static final int  KEY_PARAMETER_GET_AUTHOR_CHAPTER_COUNT=2029;
    private static final int  KEY_PARAMETER_GET_ALL_CHAPTER_NAME=2030;
    private static final int  KEY_PARAMETER_GET_LAW_RATING=2031;
    private static final int  KEY_PARAMETER_GET_ALL_AUDIO_TRACK_INFO=2032;
    private static final int  KEY_PARAMETER_GET_TITLE_EDITION=2033;
    private static final int  KEY_PARAMETER_GET_CHAPTER=2034;
    private static final int  KEY_PARAMETER_SET_TITLE_EDITION=2035;
    private static final int  KEY_PARAMETER_SET_CHAPTER=2036;
    private static final int KEY_PARAMETER_GET_ACTIVE_AUDIO_TRACK_INFO = 2037;
    private static final int KEY_PARAMETER_GET_ACTIVE_AUDIO_TRACK_NAME = 2038;
    private static final int KEY_PARAMETER_GET_ACTIVE_SUBTITLE_TRACK_NAME = 2039;
    private static final String TAG = "DivXInfoDialog";
    private static String mAllLang[]=new String[]{"de","en","es","el","fr","hr","it","nl",
          "pl","pt","ru","ro","sv","ar","zh","ja","ko","nb"};
    private static String mNameType[]=new String[]{"title","edition","chapter"};
    private static String mAllLangs[]=new String[]{"ger","eng","spa","gre","fre","hrv","ita","nld","pol","por","rus","rou","swe","ara","chi","jpn","kor","nor"};
    private ListView divxInfoList;
    private TextView tv;
    private int  mCurDlgID;
    private String mLangID;
    private int nChatperCnt;
    private int nCurChapter;
    private int nfitChapter;
    private boolean isThresold;
    private int nTitles;
    private int []nEditions;

    private VideoPlayerActivity mVideoPlayActivity;
    ListDataListAdapter simpleAdapter;
    public final int TITLE = 0;
    public final int EDITION = 1;
    public final int TITLE_EDITION = 2;

    public DivxInfoDialog(VideoPlayerActivity activity,int DlgId) {
            super(activity);
            mVideoPlayActivity = activity;
            mCurDlgID = DlgId;
            mLangID=getSystemLangID();
        }

    public DivxInfoDialog(VideoPlayerActivity activity) {
            super(activity);
            mVideoPlayActivity = activity;
            mLangID=getSystemLangID();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.divx_info_list);
        Window w = getWindow();
        w.setBackgroundDrawableResource(color.transparent);
        //InitListViewContent();
        //addEventListener();
    }

    private static String getSystemLangID(){
        String sLang = Locale.getDefault().getLanguage();
        Log.i(TAG,"the default language is:"+sLang);
        for (int i=0;i<18;i++) {
             if (sLang.equals(mAllLang[i])) {
                 return mAllLangs[i];
             }
        }
        return "und";
    }

    private int getTiltleCount(){
        Parcel p=mVideoPlayActivity.getDivxPlusInfo(KEY_PARAMETER_GET_TITLE_COUNT);
        p.setDataPosition(0);
        int nTitle = p.readInt();
        p.recycle();
        Log.i(TAG,"the title cnt is:"+nTitle);
        return nTitle;
    }

    /***
        * yannis,you must follow the order:int string string
        ***/
    private void getCurLangName(int nNum,int nType,String[] sNam){
        Log.i(TAG,"getCurLangName nNum = "+nNum);
        Parcel p = mVideoPlayActivity.getDivxPlusInfo(nType);
        p.setDataPosition(0);
        for (int i=0;i < nNum;i++) {
             int nLang = p.readInt(); // read lang nums
             boolean bDefault = true;
             if (nLang > 0) {
                 for (int j=0;j < nLang;j++) {
                      String sTemp = p.readString(); // read current content name
                      Log.i(TAG,nType+"-"+i+" name:"+sTemp);
                      String slang = p.readString(); //read currnet content language
                      Log.i(TAG,nType+"-"+i+" Lang:"+slang+"the sys lang:"+mLangID);
                      if ((slang.substring(0,3)).equals(mLangID)) {
                          bDefault = false;
                          sNam[i] = sTemp;
                      }
                 }
             }
             if (!bDefault) continue;
             int index = (nType-2024)/2 - 1;
             sNam[i] = mNameType[index]+" "+(i+1)+" ";
        }
        p.recycle();
    }

    private void getTitleName(int num,String[] sTitle){
        getCurLangName(num, KEY_PARAMETER_GET_ALL_TITLE_NAME, sTitle);
    }

    private void getEditionCount(int[] nEdition){
        Parcel p=mVideoPlayActivity.getDivxPlusInfo(KEY_PARAMETER_GET_EDITION_COUNT);
        p.setDataPosition(0);
        p.readIntArray(nEdition);
        Log.i(TAG,"the Edition cnt is:"+Arrays.toString(nEdition));
        p.recycle();
    }

    private void getEditionName(int num,String[] sEdition){
        getCurLangName(num, KEY_PARAMETER_GET_ALL_EDITION_NAME, sEdition);
    }

    public int getAuthorChapterCount(){
        Parcel p=mVideoPlayActivity.getDivxPlusInfo(KEY_PARAMETER_GET_AUTHOR_CHAPTER_COUNT);
        p.setDataPosition(0);
        int nChapter= p.readInt();
        p.recycle();
        return nChapter;
    }

    private void getChapterName(int num,String[] sChapter){
        getCurLangName(num, KEY_PARAMETER_GET_ALL_CHAPTER_NAME, sChapter);
    }

    public String getCurDivxInfo(int nType){
        String sRet = "";
        switch (nType) {
            case 0 : //title/edition
                 sRet = getCurTitleEdition(TITLE_EDITION);
                 break;
            case 1 :  //chapter
                 sRet = getCurChapter();
                 break;
            case 2 : //lawRating
                 sRet = getLawRating();
                 break;
            case 3 :
                sRet = getDivxAudioInfo();
                 break;
            case 4 :
                sRet = getDivxSubtitleName();
                 break;
            default :
                 break;
        }
        return sRet;
    }

    private void InitListViewContent(){
        //tv =(TextView)findViewById(R.id.divx_info);
        String[] listData;
        Log.i(TAG,"InitListView--Contentcurrent Dlg ID is:"+mCurDlgID);
        if (mCurDlgID>0){
            int nChapters=getAuthorChapterCount();
            Log.i(TAG,"the author chapter's num is:"+nChapters);
            if (nChapters>0) {
                String[] sChapt =new String[nChapters];
                getChapterName(nChapters,sChapt);
                listData = new String[nChapters];
                for (int i=0;i < nChapters;i++) {
                     listData[i]=sChapt[i];
                }
                tv.setText("Author Chapters");
            } else {
                listData=new String[10];
                for (int i=0;i<10;i++){
                     listData[i]="Chapter"+" #"+(i+1);
                }
                tv.setText("Auto Chapters");
            }
        } else {
            nTitles= getTiltleCount();
            nEditions=new int[nTitles];
            getEditionCount(nEditions);
            int nTotal = 0;
            String[] sTitles=new String[nTitles];
            getTitleName(nTitles,sTitles);
            for (int i=0;i<nTitles;i++) {
                 nTotal += nEditions[i];
            }
            String[] sEditions= new String[nTotal];
            getEditionName(nTotal,sEditions);
            String stext;
            List<String> mList=new ArrayList<String>();
            nTotal=0;
            for (int i=0;i<nTitles;i++) {
                 if (nEditions[i]>0) {
                     for (int j=0;j<nEditions[i];j++) {
                         stext = "Title "+(mList.size()+1)+"-"+sEditions[nTotal++]+" of " +sTitles[i];
                         mList.add(stext);
                     }
                 } else {
                     stext = "Title "+(mList.size()+1)+"-"+sTitles[i];
                     mList.add(stext);
                 }
            }
            int size = mList.size();
            listData = (String[])mList.toArray(new String[size]);
            tv.setText("Title/Edition");
        }
        simpleAdapter = new ListDataListAdapter(mVideoPlayActivity, listData);
        //divxInfoList  = (ListView) findViewById(R.id.divx_chapter);
        divxInfoList.setAdapter(simpleAdapter);
        divxInfoList.setDividerHeight(0);
        divxInfoList.requestFocus();
        divxInfoList.setEnabled(true);
        divxInfoList.setFocusable(true);
        divxInfoList.setFocusableInTouchMode(true);
        divxInfoList.setSelection(0);
        simpleAdapter.notifyDataSetChanged();
    }

    private String getLawRating() {
        Parcel p=mVideoPlayActivity.getDivxPlusInfo(KEY_PARAMETER_GET_LAW_RATING);
        p.setDataPosition(0);
        String sLR = p.readString();
        Log.i(TAG,"getLawRating():"+sLR);
        p.recycle();
        return sLR;
    }

    // choose: 0,1,2 means; only title,only edition, both them.
    private String getCurTitleEdition(int choose) {
        int[] nTE = new int[2];
        Parcel p=mVideoPlayActivity.getDivxPlusInfo(KEY_PARAMETER_GET_TITLE_EDITION);
        p.setDataPosition(0);
        p.readIntArray(nTE);
        p.recycle();
        nTitles= getTiltleCount();
        nEditions=new int[nTitles];
        getEditionCount(nEditions);
        int nCurTitle = nTE[0];
        String[] sTitles=new String[nTitles];
        getTitleName(nTitles,sTitles);
        String sTE = sTitles[nCurTitle];
        if (nEditions[nCurTitle]>0) {
            int nTotal = 0;
            int curEdt = 0;
            for (int i=0;i<nTitles;i++) {
                 if (i == nCurTitle) curEdt = nTotal;
                 nTotal += nEditions[i];
            }
            String[] sEditions= new String[nTotal];
            getEditionName(nTotal,sEditions);
            int nCurEdition = nTE[1];
            Log.i(TAG,"sEditions[nCurEdition+curEdt]:"+sEditions[nCurEdition+curEdt]);
            Log.i(TAG,"sEditions[nCurEdition+curEdt].substring(0,7):"+sEditions[nCurEdition+curEdt].substring(0,6));
            Log.i(TAG,"sTitles[nCurTitle]:"+sTitles[nCurTitle]);
            Log.i(TAG,"sTitles[nCurTitle].substring(0,5):"+sTitles[nCurTitle].substring(0,4));
            if ("edition".equals(sEditions[nCurEdition+curEdt].substring(0,7)) || choose == TITLE) {
                sTE = sTitles[nCurTitle];
            } else if ("title".equals(sTitles[nCurTitle].substring(0,5))|| choose == EDITION) {
                sTE = sEditions[nCurEdition+curEdt];
            } else {
                sTE = sEditions[nCurEdition+curEdt] + " of " + sTitles[nCurTitle];
            }
        }
        Log.i(TAG,"getCurTitleEdition:"+sTE);
        return sTE;
    }

    public String getCurTitleEditionInfo(int choose) {
        String sTE= getCurTitleEdition(choose);
        String sRet = " "+ getLawRating();
        sRet = sTE + "\n" + sRet;
        return sRet;
    }

    public boolean startPlayNextTitle(int direct) {
        Log.i(TAG,"startPlayNextTitle:"+direct);
        int[] nCurTE = new int[2];
        Parcel p=mVideoPlayActivity.getDivxPlusInfo(KEY_PARAMETER_GET_TITLE_EDITION);
        p.setDataPosition(0);
        p.readIntArray(nCurTE);
        p.recycle();
        int nextTitle = nCurTE[0]+nCurTE[1]+direct;
        if (nextTitle<0) return false;
        nTitles= getTiltleCount();
        nEditions=new int[nTitles];
        getEditionCount(nEditions);

        int nTotal =0 ;
        for (int i=0;i<nTitles;i++) {
             if (nEditions[i]>0){
                 for (int j=0;j<nEditions[i];j++) {
                      if (nextTitle == nTotal++){
                          int[] nTE =new int[]{i,j};
                          setCurTitleEdition(nTE);
                          mVideoPlayActivity.SaveTitleByFileName(i);
                          mVideoPlayActivity.SaveEditionByFileName(j);
                          return true;
                      }
                 }
             } else {
                 if(nextTitle == nTotal++){
                    int[] nTE =new int[]{i,0};
                    setCurTitleEdition(nTE);
                    mVideoPlayActivity.SaveTitleByFileName(i);
                    mVideoPlayActivity.SaveEditionByFileName(0);
                    return true;
                 }
            }
        }
        return false;
    }

    private String getCurChapter() {
        int nChapters=getAuthorChapterCount();
        nChatperCnt = nChapters;
        String sChapter="";
        Log.i(TAG,"getCurChapter nChapters:"+nChapters);
        if (nChapters>0) {
            String[] sChapt =new String[nChapters];
            getChapterName(nChapters,sChapt);
            Parcel p=mVideoPlayActivity.getDivxPlusInfo(KEY_PARAMETER_GET_CHAPTER);
            p.setDataPosition(0);
            nCurChapter = p.readInt();
            p.recycle();
            sChapter = sChapt[nCurChapter];
            Log.i(TAG,"getCurChapter nCurChapter:"+nCurChapter);
            Log.i(TAG,"getCurChapter sChapter:"+sChapter);
        } else {
            nCurChapter = mVideoPlayActivity.getCurChapter();
            sChapter ="Auto Chapter #" + nCurChapter;
            nCurChapter = nCurChapter - 1;
        }
        return sChapter;
    }

    public boolean  setChapterByNum(int num) {
        int nChapters=getAuthorChapterCount();
        num = num-1;
        if (nChapters>0 ) {
            num = (num+nChapters)%nChapters;
            Log.i(TAG,"setChapterByNum:"+num);
            setCurChapter(num);
        } else {
            num = (num+10)%10;
            Log.i(TAG,"auto chapter setChapterByNum:"+num);
            mVideoPlayActivity.seekToPosition(num);
        }
        // at here, num+1 won't be bigger than chapter count.videoHandler.sendEmptyMessageDelayed(Constants.DIVX_NEXT_ACTION, 0);
        mVideoPlayActivity.videoHandler.sendEmptyMessageDelayed(Constants.DIVX_SHOW_CHAPTER_ACTION,500);
        return true;
    }

    public String setChapterByStep(int direction,int lChapt) {
       String sChapter = "Chapter ";
       Log.i(TAG,"the direction is:"+direction);
       if ( direction == 0 && !isThresold) nfitChapter += 1;
       Log.i(TAG,"modify pos is:"+nfitChapter);
       if (nChatperCnt>0) {
           setCurChapter(nfitChapter);
       } else {
           if (direction<0) {
               Log.i(TAG,"the direction is less than 0!");
           } else if(direction>0) {
               if (nfitChapter == lChapt) {
                   nfitChapter += 1;
               }
               if (nfitChapter > 9) {
                   nfitChapter = 9;
               }
           }
           Log.i(TAG,"seek to pos is:"+nfitChapter);
           //mVideoPlayActivity.seekToPosition(nfitChapter);
       }
       sChapter += nfitChapter+1;
       return sChapter;
    }

    public boolean isAutoChapter() {
        return (nChatperCnt>0) ? false:true;
    }

    public boolean IsKeyChapter(int direct,int lChapt) {
        String sChapter = getCurChapter();
        Log.i(TAG,"the current chapter is:"+nCurChapter);
        nfitChapter= nCurChapter;
        nfitChapter = nfitChapter + direct ;
        boolean bKeyChap = false;
        if (nChatperCnt>0) {
            if (nfitChapter<0) {
                nfitChapter = 0;
                bKeyChap = true;
            } else if (nfitChapter>=nChatperCnt) {
                nfitChapter = nChatperCnt -1;
                bKeyChap = true;
            }
        } else {
           Log.i(TAG,"the fit chapter is:"+nfitChapter);
           if (nfitChapter<0) {
               nfitChapter = 0;
               bKeyChap = true;
           } else if (nfitChapter>=10 || lChapt>=10) {
               nfitChapter = 9;
               bKeyChap = true;
           }
        }
        isThresold = bKeyChap;
        return bKeyChap;
    }

    public int getLastChapter() {
        return nfitChapter;
    }

    public void setCurTitleEdition(int[] nTE) {
        Parcel p = Parcel.obtain();
        Log.i(TAG,"set CurTitleEdition nTE[0]:"+nTE[0]);
        Log.i(TAG,"set CurTitleEdition nTE[1]:"+nTE[1]);
        p.writeInt(nTE[0]);
        p.writeInt(nTE[1]);
        //mVideoPlayActivity.clearSubtitleText();
        mVideoPlayActivity.setDivxPlusInfo(KEY_PARAMETER_SET_TITLE_EDITION,p);
        //mVideoPlayActivity.initProgressBar();
        p.recycle();
    }

    private void setCurChapter(int nc) {
        Parcel p = Parcel.obtain();
        p.writeInt(nc);
        mVideoPlayActivity.setDivxPlusInfo(KEY_PARAMETER_SET_CHAPTER,p);
        p.recycle();
     }

    private String getCurLangName(int key) {
        return getCurLangName(key,mVideoPlayActivity);
    }

    public static String getCurLangName(int key, VideoPlayerActivity activity) {
        String sRet = "undefined";
        Parcel p = activity.getDivxPlusInfo(key);
        p.setDataPosition(0);
        int nLang = p.readInt(); // read lang nums
        if (nLang > 0) {
            String curName = "";
            String slang = "";
            for (int j=0;j < nLang;j++) {
                 curName = p.readString(); // read current content name
                 Log.i(TAG,"the curent Name is:"+curName);
                 slang = p.readString(); //read currnet content language
                 slang = slang.substring(0,3);
                 String langID = getSystemLangID();
                 if (slang.equals(langID)) {
                     Log.i(TAG,"the fixed name is:"+curName);
                     sRet = curName;
                     break;
                 }
             }
        }
        return sRet;
    }

    private String getDivxAudioInfo(){
        return getDivxAudioInfo(mVideoPlayActivity);
    }

    public static String getDivxAudioInfo(VideoPlayerActivity activity) {
        Parcel p = activity.getDivxPlusInfo(KEY_PARAMETER_GET_ACTIVE_AUDIO_TRACK_INFO);
        p.setDataPosition(0);
        String sCodec = p.readString();//codec;
        Log.i(TAG,"audio codec is:"+sCodec);
        String sLang = p.readString();//Lang
        Log.i(TAG,"audio lang is:"+sLang);//language ,not used.
        String sChannel = p.readInt() + "";//channel
        Log.i(TAG,"audio channel is:"+sChannel);
        String sName=getCurLangName(KEY_PARAMETER_GET_ACTIVE_AUDIO_TRACK_NAME,activity);
        Log.i(TAG,"audio name is:"+sName);
        String sRet = sLang + ", " + sName + ", " + sCodec + ", " + sChannel;
        return sRet;
     }

    private void getDivxAudioInfo(String[] sAudio) {
        getDivxAudioInfo(sAudio,mVideoPlayActivity);
    }
    public static void getDivxAudioInfo(String[] sAudio,VideoPlayerActivity activity) {
        Parcel p = activity.getDivxPlusInfo(KEY_PARAMETER_GET_ACTIVE_AUDIO_TRACK_INFO);
        String sName = "undefined";
        if (p != null) {
            p.setDataPosition(0);
            String sCodec = p.readString();//codec;
            sAudio[0]= sCodec;
            Log.i(TAG,"the codec is:"+sCodec);
            String slan = p.readString();//language
            Log.i(TAG,"the Language is:"+slan);
            String sChannel = p.readInt() + " Channels";//channel
            Log.i(TAG,"the Channel is:"+sChannel);
            sAudio[1] = sChannel;
            sName=getCurLangName(KEY_PARAMETER_GET_ACTIVE_AUDIO_TRACK_NAME,activity);
            sAudio[2] = sName;
        }
        if (sName.equals("undefined")) {
            int vid = activity.getVideoPlayHolder().getViewId();
            String audio = AudioTrackManager.getAudioTackSettingOpt(0, vid);
            int numberAudioTrack = Integer.parseInt(audio.substring(
                    audio.length() - 1, audio.length())) ;
            sAudio[2] = "Audio "+numberAudioTrack;
        }
    }

    public String getDivxSubtitleName() {
        String sName = getCurLangName(KEY_PARAMETER_GET_ACTIVE_SUBTITLE_TRACK_NAME);
        Log.i(TAG,"KEY_PARAMETER_GET_ACTIVE_SUBTITLE_TRACK_NAME is:"+sName);
        if(sName.equals("undefined")){
           int vid = mVideoPlayActivity.getVideoPlayHolder().getViewId();
           String subtitle = SubtitleManager.getSubtitleSettingOptValue(2, vid);
           Log.i(TAG,"subtitle is:"+subtitle);
           if (subtitle == null) {
               return null;
           }
           Matcher matcher = Pattern.compile("[^0-9]").matcher(subtitle);
           int numberSubtitleTrack = 0;
           try {
               numberSubtitleTrack = Integer.parseInt(matcher.replaceAll("").trim());
               Log.i(TAG,"numberSubtitleTrack:"+numberSubtitleTrack);
               numberSubtitleTrack++;
               sName = "Subtitle "+numberSubtitleTrack;
           } catch (Exception e) {
               e.printStackTrace();
           }

        }
        Log.i(TAG,"Subtitle name is:"+sName);
        return sName;
     }

    private void addEventListener() {
        divxInfoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position, long id) {
               Log.i(TAG,"position is:"+position);
               if (mCurDlgID>0) {
                   int nChapters=getAuthorChapterCount();
                   if (nChapters>0) {
                       setCurChapter(position);
                   }else{
                       //mVideoPlayActivity.seekToPosition(position);
                   }
               } else {
                   int nTotal =0 ;
                   //mVideoPlayActivity.clearSubtitleText();
                   for (int i=0;i<nTitles;i++) {
                        if (nEditions[i]>0) {
                            for (int j=0;j<nEditions[i];j++) {
                                 if (position == nTotal++) {
                                     int[] nTE =new int[]{i,j};
                                     Log.i(TAG,"set current title :"+i+";curent edition:"+j);
                                     setCurTitleEdition(nTE);
                                     //mVideoPlayActivity.showTEInfo();
                                     return;
                                }
                            }
                        } else {
                            if (position == nTotal++) {
                                int[] nTE =new int[]{i,0};
                                Log.i(TAG,"set current title :"+i+";curent edition:0");
                                setCurTitleEdition(nTE);
                                //mVideoPlayActivity.showTEInfo();
                                return;
                            }
                        }
                   }
               }
               dismiss();
            }
        });
        divxInfoList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                 Log.i(TAG,"Item selected position is:"+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
       });
    }
}
