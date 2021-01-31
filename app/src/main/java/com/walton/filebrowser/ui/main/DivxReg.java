package com.walton.filebrowser.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.MenuItem;
import android.os.Bundle;

import android.widget.PopupMenu;
import android.media.MediaMetadataRetriever;
import java.util.Map;
import java.util.HashMap;

import com.walton.filebrowser.R;

import android.content.Context;
import android.content.DialogInterface;

public class DivxReg extends Activity
{
    private static final int  METADATA_KEY_DIVX_DRM_IS_DEVICE_FIRSTTIME_REG = 51;
    private Context mContext;
    private View mView;

    private static String registrationCode = "";
    private static String deregistrationCode = "";
    public DivxReg(Context context,View view){
        mContext = context;
        mView = view;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /*protected String getDrmGetInfo(String[] sRegs){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        for(int i=0;i<4;i++){
            sRegs[i] = retriever.extractMetadata(METADATA_KEY_DIVX_DRM_IS_DEVICE_FIRSTTIME_REG + i);
        }
        retriever.release();
    }*/
    private String getDrmRegInfo(int ID) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
         //retriever.setDataSource("");
        Map<String, String> header =new HashMap<String, String>();
        header.put("key","DIVX_DRM")  ;
        header.put("value","DIVX_DRM")  ;
        String sValue = null;
        try {
            retriever.setDataSource("DIVX_DRM",header);
            sValue = retriever.extractMetadata(METADATA_KEY_DIVX_DRM_IS_DEVICE_FIRSTTIME_REG +ID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return sValue;
    }
    private void showDrmUI(final int id) {
        CharSequence msg = getCharSeqFromId(id);
        AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
        dlg.setMessage(msg);
        dlg.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do nothing
            }
        });
        dlg.show();
    }
    private CharSequence getCharSeqFromId(int id) {
        CharSequence text = "";

        switch(id){
            case R.string.divx_vod_drm_register:
                text = mContext.getText(R.string.divx_vod_drm_register) + "\n"
                        + "\n"
                        + mContext.getText(R.string.divx_vod_drm_registration_code)
                        + registrationCode
                        + "\n" + "\n"
                        + mContext.getText(R.string.divx_vod_drm_register_at)
                        + "\n" + mContext.getText(R.string.video_divx_vod_url)
                        + "\n";
                break;
            case R.string.divx_vod_drm_deregistration_confirm:
                text = mContext.getText(R.string.divx_vod_drm_deregistration_confirm)
                        + "\n";
                break;
            case R.string.divx_vod_drm_deregister:
                text = mContext.getText(R.string.divx_vod_drm_deregistration_code)
                        + deregistrationCode
                        + "\n"
                        + "\n"
                        + mContext.getText(R.string.divx_vod_drm_deregister_at)
                        + "\n"
                        + mContext.getText(R.string.video_divx_vod_url)
                        + "\n"
                        + "\n"
                        + mContext.getText(R.string.divx_vod_drm_deregister) + "\n";
                break;
            case R.string.divx_not_support:
                text = mContext.getText(R.string.divx_not_support);
            default:
                break;
        }
        return text;
    }
    public void showPopupMenu() {
        final PopupMenu popup = new PopupMenu(mContext, mView);
        popup.getMenuInflater().inflate(R.menu.regmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int itemid = item.getItemId();
                if(itemid == R.id.vodreg) {
                    String sFirstReg = getDrmRegInfo(0);
                    // if is not support DivX,show not support dialog
                    if (sFirstReg == null) {
                        showDrmUI(R.string.divx_not_support);
                    } else if(sFirstReg.equals("1")) {
                        registrationCode = getDrmRegInfo(2);
                        showDrmUI(R.string.divx_vod_drm_register);
                    } else {
                        String sDeviceReg = getDrmRegInfo(1);
                        if(sDeviceReg != null && sDeviceReg.equals("1")) {
                            new AlertDialog.Builder(mContext)
                            .setMessage(getCharSeqFromId(R.string.divx_vod_drm_deregistration_confirm))
                            .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deregistrationCode = getDrmRegInfo(3);
                                    new AlertDialog.Builder(mContext)
                                    .setMessage(getCharSeqFromId(R.string.divx_vod_drm_deregister))
                                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            registrationCode = getDrmRegInfo(2);
                                            showDrmUI(R.string.divx_vod_drm_register);
                                        }
                                    }).setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int whichButton) {
                                            return ;
                                        }
                                    }).show();
                                }
                            }).setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,int whichButton) {
                                    return ;
                                }
                            }).show();
                        }else {
                            deregistrationCode = getDrmRegInfo(3);
                            new AlertDialog.Builder(mContext)
                            .setMessage(getCharSeqFromId(R.string.divx_vod_drm_deregister))
                            .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    registrationCode = getDrmRegInfo(2);
                                    showDrmUI(R.string.divx_vod_drm_register);
                                }
                            }).setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,int whichButton) {
                                    return ;
                                }
                            }).show();
                        }
                    }
                }
                return true;
            }
        });
        popup.show();
    }
}

