package com.walton.filebrowser.ui.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.mstar.android.MDisplay;
import com.walton.filebrowser.R;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;

public class SurfaceView4K2K extends SurfaceView implements Callback
{
    private static final String TAG = "SurfaceView4K2K";

    private Bitmap bitmap = null;

    private FileInputStream fileInputStream = null;

    private Context context;

    public SurfaceView4K2K(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        getContext();
    }

    public SurfaceView4K2K(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        getHolder().addCallback(this);
    }

    public SurfaceView4K2K(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        getHolder().addCallback(this);
    }
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0){
        // TODO Auto-generated method stub
        Log.i(TAG,"========surfaceCreated==========");
        Log.i(TAG,"=====SurfaceView.width: "+getWidth()+"======SurfaceView.height: "+getHeight());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0){
        // TODO Auto-generated method stub
        Log.d(TAG, "********surfaceDestroyed******");
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    // 图片读取和加载start.
    protected void drawImage() {
        Log.i(TAG,"============drawImage===XUNING============");
        if (PhotoPlayerActivity.is4K2KMode){
            Surface surface = getHolder().getSurface();
            //getIdentity
            int firstpos = surface.toString().indexOf("identity=") + 9;
            int lastpos = surface.toString().lastIndexOf(")");
            String identitystring = surface.toString().substring(firstpos, lastpos);
            int identityint = Integer.parseInt(identitystring);
            Log.d(TAG, "apk xx.toString()=" + surface.toString() + ";firstpos=" + firstpos + ";lastpos="
                    + lastpos + ";identitystring=" + identitystring + ";identityint=" + identityint);

            MDisplay.setBypassTransformMode(identityint, 1);
        }

        Canvas canvas = getHolder().lockCanvas(null);
        Log.i(TAG,"#####======canvas is :"+canvas+"======");
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
        }
        if (bitmap != null && canvas != null) {
            Paint paint = new Paint();
            int bitmapHeight = bitmap.getHeight();
            int bitmapWidth = bitmap.getWidth();
            int surfaceHeight = this.getHeight();
            int surfaceWidth = this.getWidth();
            android.graphics.Rect src = new android.graphics.Rect();
            android.graphics.Rect dst = new android.graphics.Rect();
            src.left = 0;
            src.top = 0;
            src.bottom = bitmapHeight;
            src.right = bitmapWidth;
            Log.i(TAG,"$$$$____xuning______surfaceHeight  is :"+surfaceHeight);
            Log.i(TAG,"$$$$____xuning______surfaceWidth  is :"+surfaceWidth);
            Log.i(TAG,"$$$$____xuning______src.bottom  is :"+src.bottom);
            Log.i(TAG,"$$$$____xuning______src.right  is :"+src.right);

            dst.left = (surfaceWidth-bitmapWidth)/2;
            dst.top = (surfaceHeight-bitmapHeight)/2;
            dst.bottom = (surfaceHeight+bitmapHeight)/2;
            dst.right = (surfaceWidth+bitmapWidth)/2;
            Log.i(TAG,"$$$$____xuninglong___ss___dst.left  is :"+dst.left);
            Log.i(TAG,"$$$$____xuninglong__ss____dst.top  is :"+dst.top);
            Log.i(TAG,"$$$$____xuninglong__ss____dst.bottom  is :"+dst.bottom);
            Log.i(TAG,"$$$$____xuninglong__ss____dst.right  is :"+dst.right);
            Log.i(TAG,"============Stephen bbbb================");
            canvas.drawBitmap(bitmap, src, dst, paint);
            Log.i(TAG,"============Stephen xxzzzooo================");
        }

        if (canvas != null) {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    protected boolean setImagePath(String imagePath) {
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }
        bitmap = decodeBitmap(imagePath);

        if (bitmap != null) {
            postInvalidate();
            return true;
        } else {
            return false;
        }
    }

    protected Bitmap decodeBitmap(String imagePath) {
        File file = new File(imagePath);
        Log.d(TAG, "size of photo : " + file.length());
        if (!file.exists()) {
            return null;
        }

        // 4k2k mode 时，大于100M的图片强制不让其解析
        if(file.length() / 1024 / 1024 > 100) {
              return setDefaultPhoto();
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            closeSilently(fileInputStream);
            fileInputStream = new FileInputStream(imagePath);
            FileDescriptor fd = fileInputStream.getFD();
            if (fd == null) {
                closeSilently(fileInputStream);
                return null;
            }
            // 插拔磁盘，下面这个必须设置为false.
            options.inPurgeable = false;
            options.inInputShareable = true;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            Log.i("*************", "options " + options.outHeight + " " + options.outWidth);
            if (!checkImageSize(options)) {
                closeSilently(fileInputStream);

                return setDefaultPhoto();
            }

            options.inPreferredConfig = Config.ARGB_8888;

            options.inSampleSize = 1;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);

            bitmap = resizeDownIfTooBig(bitmap, 3840, true);

        } catch (Exception e) {
            try {
                closeSilently(fileInputStream);
                fileInputStream = new FileInputStream(imagePath);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            } catch (Exception error) {
                error.printStackTrace();

                return null;
            } finally {
                closeSilently(fileInputStream);
            }
        } finally {
            closeSilently(fileInputStream);
        }

        return ensureGLCompatibleBitmap(bitmap);
    }

    private void closeSilently(Closeable c) {
        if (c == null) {
            return;
        }

        try {
            c.close();
        } catch (Throwable t) {
        }
    }

    // This function should not be called directly from
    // DecodeUtils.requestDecode(...), since we don't have the knowledge
    // if the bitmap will be uploaded to GL.
    private static Bitmap ensureGLCompatibleBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.getConfig() != null)
            return bitmap;

        Bitmap newBitmap = bitmap.copy(Config.ARGB_8888, false);
        bitmap.recycle();
        System.gc();

        return newBitmap;
    }

    // Resize the bitmap if each side is >= targetSize * 2
    private Bitmap resizeDownIfTooBig(Bitmap bitmap, int targetSize, boolean recycle) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        float scale = Math.max((float)targetSize / srcWidth, (float)targetSize / srcHeight);
        if (scale > 0.5f) {
            return bitmap;
        }

        return resizeBitmapByScale(bitmap, scale, recycle);
    }

    private Bitmap resizeBitmapByScale(Bitmap bitmap, float scale, boolean recycle) {
        int width = Math.round(bitmap.getWidth() * scale);
        int height = Math.round(bitmap.getHeight() * scale);
        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        }

        Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
        Canvas canvas = new Canvas(target);
        canvas.scale(scale, scale);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (recycle) {
            bitmap.recycle();
        }

        return target;
    }

    private static Config getConfig(Bitmap bitmap) {
        Config config = bitmap.getConfig();
        if (config == null) {
            config = Config.ARGB_8888;
        }

        return config;
    }

    public boolean checkImageSize(BitmapFactory.Options options) {

        long limit = 100000000;// 最大消100MB内存

        long pixSize = options.outWidth * options.outHeight * 4;
        if (pixSize <= limit) {
            return true;
        }

        return false;
    }

    private Bitmap setDefaultPhoto() {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.ARGB_8888;

        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        @SuppressLint("ResourceType") InputStream is = this.getResources().openRawResource(R.drawable.default_sound_back);
        if (is == null) {
            return null;
        }

        return ensureGLCompatibleBitmap(BitmapFactory.decodeStream(is, null, opt));
    }

}
