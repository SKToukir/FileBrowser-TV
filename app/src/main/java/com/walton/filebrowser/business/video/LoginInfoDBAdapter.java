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
package com.walton.filebrowser.business.video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class LoginInfoDBAdapter {
        // LogCat
        public static final String DB_ACTION = "db_action";
        // db name
        private static final String DB_NAME = "logininfo.db";


        // db version
        private static final int DB_VERSION = 1;
        // table key ID
        // public static final String KEY_ID = "_id";


        // db table name
        private static final String DB_LOGININFO_TABLE = "logininfo";
         // table key PATH
        public static final String KEY_IP = "ip";
        // table key TIME
        public static final String KEY_USR = "usr";
        // table key SIZE
        public static final String KEY_PWD = "pwd";



        private SQLiteDatabase db;
        private Context xContext;
        private DBOpenHelper dbOpenHelper;

        public LoginInfoDBAdapter(Context context) {
               xContext = context;
        }

        /**
         * Space is not enough storage time set to read only.
         * 
         * @throws SQLiteException
         */
        public void open() throws SQLiteException {
            dbOpenHelper = new DBOpenHelper(xContext, DB_NAME, null, DB_VERSION);
            try {
                db = dbOpenHelper.getWritableDatabase();
            } catch (SQLiteException e) {
                db = dbOpenHelper.getReadableDatabase();
            }
        }

        /**
         * Call SQLiteDatabase object close () method close database.
         */
        public void close() {
            if (db != null) {
                db.close();
                db = null;
            }
        }


        public long insert_loginInfo(String ip, String usr, String pwd) {
            ContentValues newValues = new ContentValues();

            newValues.put(KEY_IP, ip);
            newValues.put(KEY_USR, usr);
            newValues.put(KEY_PWD, pwd);

            return db.insert(DB_LOGININFO_TABLE, null, newValues);
        }

        public long delete_loginInfo(String ip, String usr) {
            return db.delete(DB_LOGININFO_TABLE, KEY_IP+ "=" + "'" + ip + "'" + " and "
                            + KEY_USR+ "=" + "'" + usr + "'", null);
        }

        public boolean query_loginInfo(String ip,  String[] savedstr) {
               boolean query_ok = false;
               try {
                    Cursor cursor = db.rawQuery("select * from "+DB_LOGININFO_TABLE+" where "+KEY_IP+" ='"+ip+"';", null);
                    while (cursor != null && cursor.moveToNext()) {
                            savedstr[0] = cursor.getString(1); 
                            savedstr[1] = cursor.getString(2);
                            query_ok = true;
                    }
                    cursor.close();
              }
              catch (Exception ex) {
                   Log.i(DB_ACTION,"  Exception: "+ ex.getMessage()); 
              } 
              Log.i(DB_ACTION,"  query_ok: "+query_ok+"  out_Usr: "+savedstr[0]+"  out_pwd:"+savedstr[1]);
              return query_ok;
        }


        /**
         * Static Helper classes, used to establish, update, and open the database.
         */
        private static class DBOpenHelper extends SQLiteOpenHelper {
        /**
        * Manual create table SQL commands: CREATE TABLE BreakPointInfo ( path
        * text not null, time integer, size text not null);
        */

        private static final String DB_CREATE_LOGININFO = "CREATE TABLE " + DB_LOGININFO_TABLE
                    + " (" + KEY_IP + " text not null, " + KEY_USR + " integer,"
                    + KEY_PWD + " text not null);";

            public DBOpenHelper(Context context, String name,
                            CursorFactory factory, int version) {
                super(context, name, factory, version);
                Log.i(DB_ACTION, "DBOpenHelper");
            }

            /**
            * Function in the database set up for the first time when called, are
            * commonly used to used to create the database table, and make
            * appropriate initialization
            */
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(DB_CREATE_LOGININFO);
                Log.i(DB_ACTION, "logininfo onCreate");
            }

            /**
            * SQL commands. OnUpgrade () function in the database need to upgrade
            * was call, by calling SQLiteDatabase object execSQL () method,
            * implement the create table of commonly used to delete the old
            * database table, and the data transfer to the new version of the
            * database table
            */
            @Override
            public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {

                // For simplicity, and didn't do any data transfer, but only delete
                // the original table set up new database table
                _db.execSQL("DROP TABLE IF EXISTS " + DB_LOGININFO_TABLE);
                onCreate(_db);
                Log.i(DB_ACTION, "logininfo Upgrade");
            }

       }
}
