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
package com.walton.filebrowser.ui.video;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.walton.filebrowser.R;

public class RoundViewDialogHolder {
    // private RoundViewDialog roundViewDialog;
    private RelativeLayout menuBgRelativeLayout;
    private ImageView menuFoucsImage;
    private ImageView upImage;
    private ImageView rightImage;
    private ImageView downImage;
    private ImageView leftImage;
    private TextView upText;
    private TextView rightText;
    private TextView downText;
    private TextView leftText;
    private boolean turnUp = true; //
    private boolean turnRight = true; //
    private boolean turnDown = true; //
    private boolean turnLeft = true; //
    //
    protected Animation bigToSmall = null;
    protected Animation fadeIn = null;
    protected Animation fadeOut = null;
    protected Animation currentToUp = null;
    protected Animation currentToRight = null;
    protected Animation currentToDown = null;
    protected Animation currentToLeft = null;
    protected Animation rotateFadeIn = null;
    protected LayoutAnimationController layoutFadeIn = null;
    protected LayoutAnimationController layoutFadeOut = null;
    private int moveUpDistance = 0; //
    private int moveRightDistance = 0; //
    private int moveDownDistance = 0; //
    private int moveLeftDistance = 0; //

    public RoundViewDialogHolder(RoundViewDialog roundViewDialog) {
        // this.roundViewDialog = roundViewDialog;
        Context context = roundViewDialog.getContext();
        menuBgRelativeLayout = (RelativeLayout) roundViewDialog
                .findViewById(R.id.menuBgRelativeLayout);
        menuFoucsImage = (ImageView) roundViewDialog
                .findViewById(R.id.menuFocusImage);
        upImage = (ImageView) roundViewDialog.findViewById(R.id.upImage);
        rightImage = (ImageView) roundViewDialog.findViewById(R.id.rightImage);
        downImage = (ImageView) roundViewDialog.findViewById(R.id.downImage);
        leftImage = (ImageView) roundViewDialog.findViewById(R.id.leftImage);
        upText = (TextView) roundViewDialog.findViewById(R.id.upText);
        rightText = (TextView) roundViewDialog.findViewById(R.id.rightText);
        downText = (TextView) roundViewDialog.findViewById(R.id.downText);
        leftText = (TextView) roundViewDialog.findViewById(R.id.leftText);
        bigToSmall = AnimationUtils.loadAnimation(context, R.anim.big_to_small);
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        currentToUp = AnimationUtils.loadAnimation(context,
                R.anim.current_to_up);
        rotateFadeIn = AnimationUtils.loadAnimation(context,
                R.anim.since_rotate_and_fade_in);
        layoutFadeIn = AnimationUtils.loadLayoutAnimation(context,
                R.anim.layout_fade_in);
        layoutFadeOut = AnimationUtils.loadLayoutAnimation(context,
                R.anim.layout_fade_out);
    }

    /**
     *
     */
    protected void initAnimation() {
        RoundViewDialog.keyResponse = false;
        menuBgRelativeLayout.startAnimation(rotateFadeIn);
        rotateFadeIn.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAllInvisible();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAllVisible();
                menuFoucsImageFlickerHandler.post(flickerThread);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    Handler menuFoucsImageFlickerHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            menuFoucsImage.startAnimation(fadeIn);
            fadeIn.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (msg.arg1 == 2) {
                        RoundViewDialog.keyResponse = true;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            // System.out.println(msg.arg1);
            if (msg.arg1 == 2) {
                menuFoucsImageFlickerHandler.removeCallbacks(flickerThread);
            } else {
                menuFoucsImageFlickerHandler.postDelayed(flickerThread, 1000);
            }
        }
    };
    Runnable flickerThread = new Runnable() {
        int i = 0;

        @Override
        public void run() {
            Message msg = menuFoucsImageFlickerHandler.obtainMessage();
            msg.arg1 = i;
            menuFoucsImageFlickerHandler.handleMessage(msg);
            i++;
        }
    };

    protected void currentToUpAnimation() {
        if (moveUpDistance == 0) {
            moveUpDistance = (upImage.getTop() - downImage.getBottom()) / 2
                    - (upText.getBottom() - menuFoucsImage.getTop()) / 2;
        }
        TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f,
                moveUpDistance);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.setFillBefore(true);
        // anim.setFillAfter(true);
        RoundViewDialog.keyResponse = false;
        menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RoundViewDialog.keyResponse = true;
                upText.startAnimation(bigToSmall);
                bigToSmall.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
            }
        });
    }

    protected void currentToRightAnimation() {
        if (moveRightDistance == 0) {
            moveRightDistance = (rightImage.getRight() - leftImage.getLeft())
                    / 2 - (rightText.getRight() - menuFoucsImage.getRight())
                    / 2;
        }
        TranslateAnimation anim = new TranslateAnimation(0.0f,
                moveRightDistance, 0.0f, 0.0f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.setFillBefore(true);
        // anim.setFillAfter(true);
        RoundViewDialog.keyResponse = false;
        menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RoundViewDialog.keyResponse = true;
                rightText.startAnimation(bigToSmall);
                bigToSmall.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
            }
        });
    }

    protected void currentToDownAnimation() {
        if (moveDownDistance == 0) {
            moveDownDistance = (downImage.getBottom() - upImage.getTop()) / 2
                    - (downText.getTop() - menuFoucsImage.getBottom()) / 2;
        }
        TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f,
                moveDownDistance);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.setFillBefore(true);
        // anim.setFillAfter(true);
        RoundViewDialog.keyResponse = false;
        menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RoundViewDialog.keyResponse = true;
                downText.startAnimation(bigToSmall);
                bigToSmall.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
            }
        });
    }

    protected void currentToLeftAnimation() {
        if (moveLeftDistance == 0) {
            moveLeftDistance = (leftImage.getLeft() - rightImage.getRight())
                    / 2 - (leftText.getLeft() - menuFoucsImage.getLeft()) / 2;
        }
        TranslateAnimation anim = new TranslateAnimation(0.0f,
                moveLeftDistance, 0.0f, 0.0f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.setFillBefore(true);
        // anim.setFillAfter(true);
        RoundViewDialog.keyResponse = false;
        menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RoundViewDialog.keyResponse = true;
                leftText.startAnimation(bigToSmall);
                bigToSmall.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
            }
        });
    }

    protected void setAllVisible() {
        menuFoucsImage.setVisibility(View.VISIBLE);
        upText.setVisibility(View.VISIBLE);
        rightText.setVisibility(View.VISIBLE);
        downText.setVisibility(View.VISIBLE);
        leftText.setVisibility(View.VISIBLE);
    }

    protected void setAllInvisible() {
        menuFoucsImage.setVisibility(View.INVISIBLE);
        upText.setVisibility(View.INVISIBLE);
        rightText.setVisibility(View.INVISIBLE);
        downText.setVisibility(View.INVISIBLE);
        leftText.setVisibility(View.INVISIBLE);
    }

    public boolean processUpDownKey(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (turnUp) {
                currentToUpAnimation();
            }
        } else {
            if (turnDown) {
                currentToDownAnimation();
            }
        }
        return true;
    }

    public boolean processLeftRightKey(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (turnLeft) {
                currentToLeftAnimation();
            }
        } else {
            if (turnRight) {
                currentToRightAnimation();
            }
        }
        return true;
    }
}
