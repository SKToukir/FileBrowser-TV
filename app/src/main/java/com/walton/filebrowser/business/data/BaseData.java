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

package com.walton.filebrowser.business.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.main.MediaThumbnail;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;


/**
 * Data provide foundation class（photo Music Video Other search）.
 */
public class BaseData implements Parcelable {
    private MediaThumbnail mediaThumbnail;
    //video's thumbnail(may be used to other,e.q music ,photo)
    private Drawable thumbnail;
    // file name
    private String name;

    // file AP(absolute path)
    private String path;

    // parent AP(absolute path)
    private String parentPath;

    // file size
    private String size;

    // file format
    private String format;

    // dile description
    private String description;

    // artist
    private String artist;

    // title
    private String title;

    // modifyTime
    private long modifyTime;

    // Music files in the database of the key word
    private long id = 0;

    // Album art id
    private long album;

    // file type
    private int type;

    // Image resources id
    private int icon;

    private int _duration;

    // Duration, in seconds for the unit
    private String duration;

    private String key;

    private String usbName;

    private String storageTotal;

    private String storageFree;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsbName() {
        return usbName;
    }

    public void setUsbName(String usbName) {
        this.usbName = usbName;
    }

    public String getStorageTotal() {
        return storageTotal;
    }

    public void setStorageTotal(String storageTotal) {
        this.storageTotal = storageTotal;
    }

    public String getStorageFree() {
        return storageFree;
    }

    public void setStorageFree(String storageFree) {
        this.storageFree = storageFree;
    }

    public String getStorageUses() {
        return storageUses;
    }

    public void setStorageUses(String storageUses) {
        this.storageUses = storageUses;
    }

    private String storageUses;

    public BaseData() {
    }

    public BaseData(int type) {
        this.type = type;
        // According to the types of sure icon
        if (Constants.FILE_TYPE_PICTURE == type) {
            this.icon = R.drawable.icon_file_pic;
        } else if (Constants.FILE_TYPE_SONG == type) {
            this.icon = R.drawable.icon_file_song;
        } else if (Constants.FILE_TYPE_VIDEO == type) {
            this.icon = R.drawable.icon_file_video;
        } else if (Constants.FILE_TYPE_FILE == type) {
            this.icon = R.drawable.icon_file_file;
        } else if (Constants.FILE_TYPE_MPLAYLIST == type) {
            this.icon = R.drawable.icon_file_file;
        } else if (Constants.FILE_TYPE_DIR == type) {
            this.icon = R.drawable.icon_file_folder;
        } else if (Constants.FILE_TYPE_RETURN == type) {
            this.icon = R.drawable.icon_file_return;
        }

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the parentPath
     */
    public String getParentPath() {
        return parentPath;
    }

    /**
     * @param parentPath the parentPath to set
     */
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    public void setDuration(int duration) {
        this._duration = duration;
        this.duration = Tools.formatDuration(duration);
    }

    public int getDuration() {
        return _duration;
    }

    public String getDuration2() {
        return duration;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;

        if (Constants.FILE_TYPE_DIR == type) {
            this.icon = R.drawable.icon_file_folder;
        } else if (Constants.FILE_TYPE_RETURN == type) {
            this.icon = R.drawable.icon_file_return;
        } else if (Constants.FILE_TYPE_PICTURE == type) {
            this.icon = R.drawable.icon_file_pic;
        } else if (Constants.FILE_TYPE_SONG == type) {
            this.icon = R.drawable.icon_file_song;
        } else if (Constants.FILE_TYPE_VIDEO == type) {
            this.icon = R.drawable.icon_file_video;
        } else if (Constants.FILE_TYPE_MPLAYLIST == type) {
            this.icon = R.drawable.icon_file_file;
        } else if (Constants.FILE_TYPE_FILE == type) {
            this.icon = R.drawable.icon_file_file;
        }
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the modifyTime
     */
    public long getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime the modifyTime to set
     */
    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return the icon
     */
    public int getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public Drawable getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(Drawable thumbnail) {
        this.thumbnail= thumbnail;
    }
    /**
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the album
     */
    public long getAlbum() {
        return album;
    }

    /**
     * @param album the album to set
     */
    public void setAlbum(long album) {
        this.album = album;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(size);
        dest.writeString(format);
        dest.writeString(artist);
        dest.writeLong(modifyTime);
        dest.writeLong(id);
        dest.writeLong(album);
        dest.writeInt(_duration);

    }

    public static final Creator<BaseData> CREATOR = new Creator<BaseData>() {
        @Override
        public BaseData createFromParcel(Parcel source) {
            BaseData file = new BaseData(Constants.FILE_TYPE_PICTURE);
            file.name = source.readString();
            file.path = source.readString();
            file.size = source.readString();
            file.format = source.readString();
            file.artist = source.readString();
            file.modifyTime = source.readLong();
            file.id = source.readLong();
            file.album = source.readLong();
            file._duration = source.readInt();

            return file;
        }

        @Override
        public BaseData[] newArray(int size) {
            return new BaseData[size];
        }
    };

}
