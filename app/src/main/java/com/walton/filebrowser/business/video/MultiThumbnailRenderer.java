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

/**
 * Created by nate.luo on 14-10-28.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A renderer to read each video frame from a media player,
 * draw it over a surface texture.
 */
public class MultiThumbnailRenderer implements MyGLSurfaceView.Renderer {
    private static final String TAG = "MultiThumbnailRenderer";
    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mEditor = null;
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    private static final int BORDER_WIDTH = 2;
    private final float[] mTriangleVerticesData = {
            // X, Y, Z, U, V
            -1.0f, -1.0f, 0, 0.f, 0.f,
            1.0f, -1.0f, 0, 1.f, 0.f,
            -1.0f,  1.0f, 0, 0.f, 1.f,
            1.0f,  1.0f, 0, 1.f, 1.f,
    };

    private FloatBuffer mTriangleVertices;

    private final String mVertexShader =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec2 aTextureCoord;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    // the matrix must be included as a modifier of gl_Position
                    "  gl_Position = uMVPMatrix * aPosition;\n" +
                    "  vTextureCoord = aTextureCoord;\n" +
                    "}\n";

    private final String mFragmentShader =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "  gl_FragColor.a = 1.0;\n" +
                    "}\n";

    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];

    private int mProgram;
    private int mTextureID = 1;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int maPositionHandle;
    private int maTextureHandle;
    // RTT(Render to Texture)
    private final String mVertexShaderRTT =
            "uniform mat4 uSTMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec4 aTextureCoord;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = aPosition;\n" +
                    "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n" +
                    "}\n";

    private final String mFragmentShaderRTT =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";

    private int mProgramRTT;
    private int maPositionHandleRTT;
    private int maTextureHandleRTT;

    private Context mContext;
    private Handler mHandler;
    private SurfaceTexture mSurfaceTexture;
    private int mFrameAvailableNumber = 0;
    private int mFrameDrawedNumber = 0;
    private boolean isFrameClearInit = false;

    // RTT(Render to Texture) variables
    private static final int NUM_THUMBNAILS = 6;
    private int[] mTextures = new int[NUM_THUMBNAILS];
    private int[] mFramebuffers = new int[NUM_THUMBNAILS];
    private boolean isFrambufferInit = false;
    private boolean hasRenderToTexture = false;
    private boolean mNeedBreak = false;

    public void setupFramebuffer() {
        if (isFrambufferInit)
            return;

        int thumbnailWidth = mSharedPreferences.getInt("thumbnailWidth", 273);  // on FullHD Panel thumbnailWidth default is 273
        int thumbnailHeight = mSharedPreferences.getInt("thumbnailHeight", 175); // on FullHD Panel thumbnailHeight default is 175

        // Generate Texture ID
        GLES20.glGenTextures(NUM_THUMBNAILS, mTextures, 0);
        checkGlError("glGenTextures");
        // Generate Texture Buffer
        GLES20.glGenFramebuffers(NUM_THUMBNAILS, mFramebuffers, 0);
        checkGlError("glGenFramebuffers");

        for (int i = 0; i < NUM_THUMBNAILS; ++i) {
            // Bind Texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i]);
            checkGlError("glBindTexture");
            //  void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels)
            // level & border default  = 0
            // or use function void texImage2D (int target, int level, Bitmap bitmap, int border) instead.
            // GLUtils.texImage2D (GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, thumbnailWidth, thumbnailHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            checkGlError("glTexImage2D");
            // set filter
            // void glTexParameterx (int target, int pname, int param)
            // pname set GL_TEXTURE_MAG_FILTER & GL_TEXTURE_MIN_FILTER
            // param set GL_LINEAR(need CPU & GPU do more operation) or GL_NEAREST
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            checkGlError("glTexParameteri GL_TEXTURE_MIN_FILTER");
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            checkGlError("glTexParameteri GL_TEXTURE_MAG_FILTER");
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            checkGlError("glBindTexture");

            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffers[i]);
            checkGlError("glBindFramebuffer");
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextures[i], 0);
            checkGlError("glFramebufferTexture2D");
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            checkGlError("glBindFramebuffer");
        }

        isFrambufferInit = true;
    }

    private void renderToTexture(int index) {
        int thumbnailWidth = mSharedPreferences.getInt("thumbnailWidth", 273);
        int thumbnailHeight = mSharedPreferences.getInt("thumbnailHeight", 175);

        if (index < 0) index = 0;
        else if (index >= NUM_THUMBNAILS) index = NUM_THUMBNAILS - 1;

        // Load the program, which is the basics rules to draw the vertexes and textures.
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgramRTT);
        checkGlError("glUseProgram");

        GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);
        checkGlError("glUniformMatrix4fv mSTMatrix");

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffers[index]);
        checkGlError("glBindFramebuffer");
        GLES20.glViewport(0, 0, thumbnailWidth, thumbnailHeight);
        checkGlError("glViewport");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        checkGlError("glClear");
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        checkGlError("glDrawArrays");
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        checkGlError("glBindFramebuffer");
        GLES20.glFinish();
        checkGlError("glFinish");
        GLES20.glUseProgram(mProgram);
        checkGlError("mProgram");
    }

    public MultiThumbnailRenderer(Context context) {
        Log.i(TAG, "VideoDumpRenderer ---------------------- begin");
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("VideoGLSurfaceView", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mTriangleVertices = ByteBuffer.allocateDirect(
                mTriangleVerticesData.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangleVertices.put(mTriangleVerticesData).position(0);

        Matrix.setIdentityM(mSTMatrix, 0);
        mSTMatrix[5] = -1.0f;
        mSTMatrix[13] = 1.0f;
    }

    public void setContext(Context context) {
        Log.i(TAG, "setContext ------------------- begin context:" + context);
        mContext = context;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "setSurfaceTexture ------------------- begin surfaceTexture:" + surfaceTexture);
        mSurfaceTexture = surfaceTexture;
        mFrameAvailableNumber = 0;
        mSurfaceTexture.setOnFrameAvailableListener(mFrameAvaliableListener);
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void doClear() {
        Log.i(TAG, "========== GLSurfaceView.Renderer doClear ===========");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //transluent
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void doBreak() {
        mNeedBreak = true;
    }

    private void initialize() {
        Log.i(TAG, "[MM APK] [multi-thumb]  initialize --------------");
        // Load the program, which is the basics rules to draw the vertexes and textures.
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgramRTT);
        checkGlError("glUseProgram");

        // Load the vertexes coordinates. Simple here since it only draw a rectangle
        // that fits the whole screen.
        mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(maPositionHandleRTT, 3, GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
        checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(maPositionHandleRTT);
        checkGlError("glEnableVertexAttribArray maPositionHandleRTT");

        // Load the texture coordinates, which is essentially a rectangle that fits
        // the whole video frame.
        mTriangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(maTextureHandleRTT, 2, GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
        checkGlError("glVertexAttribPointer maTextureHandleRTT");

        GLES20.glEnableVertexAttribArray(maTextureHandleRTT);
        checkGlError("glEnableVertexAttribArray maTextureHandleRTT");

        // Load the program, which is the basics rules to draw the vertexes and textures.
        GLES20.glUseProgram(mProgram);
        checkGlError("glUseProgram");

        // Load the vertexes coordinates. Simple here since it only draw a rectangle
        // that fits the whole screen.
        mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
        checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        checkGlError("glEnableVertexAttribArray maPositionHandle");

        // Load the texture coordinates, which is essentially a rectangle that fits
        // the whole video frame.
        mTriangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
        checkGlError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(maTextureHandle);
        checkGlError("glEnableVertexAttribArray maTextureHandle");

        // Set up the GL matrices.
        Matrix.setIdentityM(mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        checkGlError("glUniformMatrix4fv mMVPMatrix");

        // Initial clear.
        //  GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //transluent
        // Draw background color
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Called to draw the current frame.
     * This method is responsible for drawing the current frame.
     */
    public void onDrawFrame(GL10 glUnused) {
       // Log.i(TAG, "------------onDrawFrame ----------");
        if ("monaco".equals(Tools.getHardwareName())) {
            if (mSurfaceTexture == null) {
                // Fix flash when first onDrawFrame.
                if (!isFrameClearInit) {
                    doClear();
                    isFrameClearInit = true;
                }
            }
            do {
                if (VideoGLSurfaceView.bNeedClear) {
                    doClear();
                    VideoGLSurfaceView.bNeedClear = false;
                    return;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (mFrameAvailableNumber <= 0 || mSurfaceTexture == null);
        } else {
            if (mFrameAvailableNumber <= 0 || mSurfaceTexture == null) {
                // Log.i(TAG, "mFrameAvailableNumber:" + mFrameAvailableNumber + " mSurfaceTexture:" + mSurfaceTexture);
//                boolean thumbnailOnHover = sharedPreferences.getBoolean("ThumbnailOnHover", false);
//                if (thumbnailOnHover) {
//                    int index = sharedPreferences.getInt("Index", -1);
//                    // setThumbnailOnFocus(index);
//                    return;
//                }
                return;
            }
        }

        initialize();
        int i = 0;
        long mLastTimeMillis = System.currentTimeMillis();
        while (true) {
            // Log.i(TAG, "mFrameAvailableNumber:" + mFrameAvailableNumber + " i:" + i);
            if (!Tools.isThumbnailModeOn() || mNeedBreak) {
                mNeedBreak = false;
                break;
            }

            if (mFrameDrawedNumber >= mFrameAvailableNumber) {
//                if ("monaco".equals(Tools.getHardwareName())) {
//                    long currentTimeMillis = System.currentTimeMillis();
//                    if (currentTimeMillis-mLastTimeMillis >= 2000) {
//                        Log.i(TAG, "currentTimeMillis:" + currentTimeMillis + " mLastTimeMillis:" + mLastTimeMillis);
//                        mEditor.putBoolean("DrawFrameFinished", true);
//                        mEditor.commit();
//
//                        synchronized(this) {
//                            mFrameAvailableNumber -= mFrameDrawedNumber;
//                        }
//                        mFrameDrawedNumber = 0;
//                        showSameSurfaceTexture(i);
//                        for (int start = i; start < 5; start++) {
//                            mEditor.putInt("TextureTimeStamp" + start, (int)(mSurfaceTexture.getTimestamp() / 1000000));
//                            mEditor.commit();
//                        }
//                        showThumbnailBorderView();
//                        return;
//                    }
//                }
                continue;
            }
            mFrameDrawedNumber++;
            Log.i(TAG, "[MM APK] [multi-thumb] updateTexImage : " + i + "/" + (NUM_THUMBNAILS-1));
            mSurfaceTexture.updateTexImage();
            checkGlError("updateTexImage");

            long getTimestamp = mSurfaceTexture.getTimestamp();
            Log.i(TAG, "[MM APK] [multi-thumb] getTimestamp:" + getTimestamp + " : " + i + "/" + (NUM_THUMBNAILS-1));
            if (i >= NUM_THUMBNAILS) {
                Log.i(TAG, "i >= NUM_THUMBNAILS    !!!!!");
                i = NUM_THUMBNAILS - 1;
                // return;
            }
            if (getTimestamp != -1) {
                if (i == 0) {
                    mEditor.putBoolean("DrawFrameFinished", false);
                    mEditor.commit();
                }
                mEditor.putInt("TextureTimeStamp" + i, (int)(getTimestamp / 1000000));
                mEditor.commit();
            } else {
                if ("monaco".equals(Tools.getHardwareName())) {
                    // if show the same texture, thumbnail would show mosaic
                    if (i <= 0) {
                        setThumbnailOnFocus(-1);
                    } else if (i < 5) {
                        showSameSurfaceTexture(i);
                        for (int start = i; start < 5; start++) {
                            mEditor.putInt("TextureTimeStamp" + start, mSharedPreferences.getInt("TextureTimeStamp" + (i-1), 0));
                            mEditor.commit();
                        }
                    }
                }

                mEditor.putBoolean("SeekBarOnHover", false);
                mEditor.putBoolean("DrawFrameFinished", true);
                mEditor.commit();
//                synchronized(this) {
                    mFrameAvailableNumber -= mFrameDrawedNumber;
//                }
                mFrameDrawedNumber = 0;
//                    Log.i(TAG, "ready to return mFrameAvailableNumber:" + mFrameAvailableNumber + " i:" + i);
                // showThumbnailBorderView();
                mEditor.putBoolean("ThumbnailBorderViewFocus", true);
                return;
            }

            mSurfaceTexture.getTransformMatrix(mSTMatrix);
            renderToTexture(i); // if not calling this function, will show black.
            drawFrameBuffer(i);
            i++;
        }
    }

    private void drawFrameBuffer(int i) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i]);

        int thumbnailWidth = mSharedPreferences.getInt("thumbnailWidth", 273);
        int thumbnailHeight = mSharedPreferences.getInt("thumbnailHeight", 175);
        Log.i(TAG, "thumbnailWidth:" + thumbnailWidth + " thumbnailHeight:" + thumbnailHeight);
        GLES20.glViewport((thumbnailWidth + 15)*i + BORDER_WIDTH, BORDER_WIDTH, thumbnailWidth - BORDER_WIDTH, thumbnailHeight - BORDER_WIDTH);

        Log.i(TAG, "[MM APK] [multi-thumb] GLES20.glDrawArrays  : " + i + "/" + (NUM_THUMBNAILS-1));
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        checkGlError("glDrawArrays");
        GLES20.glFinish();
    }

    private void showSameSurfaceTexture(int start) {
        Log.i(TAG, "showSameSurfaceTexture start:" + start);
        int thumbnailWidth = mSharedPreferences.getInt("thumbnailWidth", 273);
        int thumbnailHeight = mSharedPreferences.getInt("thumbnailHeight", 175);
        int num = Tools.getThumbnailNumber();
        for (int i = start; i < num; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[start-1]);
            GLES20.glViewport((thumbnailWidth + 15)*i + BORDER_WIDTH, BORDER_WIDTH, thumbnailWidth - BORDER_WIDTH, thumbnailHeight - BORDER_WIDTH);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            checkGlError("glDrawArrays");
        }
    }

    public void setThumbnailOnFocus(int index) {
        Log.i(TAG, "setThumbnailOnFocus index:" + index);
        boolean thumbnailBorderViewFocusFlag = mSharedPreferences.getBoolean("ThumbnailBorderViewFocus", false);
        if (!thumbnailBorderViewFocusFlag) {
            return;
        }
        initialize();
        int thumbnailWidth = mSharedPreferences.getInt("thumbnailWidth", 273);
        int thumbnailHeight = mSharedPreferences.getInt("thumbnailHeight", 175);

        int getThumbnailNumber = Tools.getThumbnailNumber();
        for (int i = 0; i < getThumbnailNumber; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i]);
            Log.i(TAG, "index:" + index + " i:" + i);
            if (index==i) {
                GLES20.glViewport((thumbnailWidth + 15)*i + BORDER_WIDTH, BORDER_WIDTH, thumbnailWidth - BORDER_WIDTH, thumbnailHeight + 100 - BORDER_WIDTH);
            } else {
                GLES20.glViewport((thumbnailWidth + 15)*i + BORDER_WIDTH, BORDER_WIDTH, thumbnailWidth - BORDER_WIDTH, thumbnailHeight - BORDER_WIDTH);
            }
            Log.i(TAG, "[MM APK] [multi-thumb] GLES20.glDrawArrays  i:" + i);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            checkGlError("glDrawArrays");
            if (mHandler != null) {
                Message msg = mHandler.obtainMessage(Constants.SetThumbnailBorderViewOnFocus);
                msg.arg1 = index;
                mHandler.sendMessage(msg);
            }
        }
    }

    private void showThumbnailBorderView() {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage(Constants.ShowThumbnailBorderView);
            mHandler.sendMessage(msg);
        }
    }


    /* EX.The TimeStamp of Multi-thumb output in MMP
    10:00:38.281  [MMP] [multi-thumb] target_pts: 26514, flip_pts: 27727, fliped:1/5
    10:00:38.573  [MMP] [multi-thumb] target_pts: 28514, flip_pts: 29929, fliped:2/5
    10:00:38.905  [MMP] [multi-thumb] target_pts: 30514, flip_pts: 31998, fliped:3/5
    10:00:39.333  [MMP] [multi-thumb] target_pts: 32514, flip_pts: 34200, fliped:4/5
    10:00:39.717  [MMP] [multi-thumb] target_pts: 34514, flip_pts: 36336, fliped:5/5
    10:00:39.785  [MMP] [multi-thumb] target_pts: -1, flip end frame
     */
    SurfaceTexture.OnFrameAvailableListener mFrameAvaliableListener = new SurfaceTexture.OnFrameAvailableListener() {
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            /* For simplicity, SurfaceTexture calls here when it has new
             * data available.  Call may come in from some random thread,
             * so let's be safe and use synchronize. No OpenGL calls can be done here.
             */
            Log.i(TAG, "[MM APK] [multi-thumb] onFrameAvailable");
            // synchronized sometimes block, so comment out it.
//            synchronized (this) {
                mFrameAvailableNumber++;
//            }
            Log.i(TAG, "[MM APK] [multi-thumb] mFrameAvailableNumber : " + mFrameAvailableNumber + "/" + (NUM_THUMBNAILS-1));
        }
    };

    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        Log.i(TAG, "onSurfaceChanged -------------------- begin" + "Surface size: width:" + width + " height:" + height);
        GLES20.glViewport(0, 0, 225, 150);
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated");

            /* Set up shaders and handles to their variables */
        mProgram = createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) {
            return;
        }
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }

        mProgramRTT = createProgram(mVertexShaderRTT, mFragmentShaderRTT);
        if (mProgramRTT == 0) {
            return;
        }
        maPositionHandleRTT = GLES20.glGetAttribLocation(mProgramRTT, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandleRTT == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandleRTT = GLES20.glGetAttribLocation(mProgramRTT, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandleRTT == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        muSTMatrixHandle = GLES20.glGetUniformLocation(mProgramRTT, "uSTMatrix");
        checkGlError("glGetUniformLocation uSTMatrix");
        if (muSTMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uSTMatrix");
        }
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private void deleteProgram() {
        GLES20.glDeleteProgram(mProgram);
        GLES20.glDeleteProgram(mProgramRTT);
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

}
