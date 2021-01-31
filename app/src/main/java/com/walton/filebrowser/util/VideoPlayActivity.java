package com.walton.filebrowser.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.walton.filebrowser.R;

public class VideoPlayActivity extends Activity {

    public static final String VIDEO_PATH = "video_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_paly_video);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String path = intent.getStringExtra(VIDEO_PATH);
        playVideo(path);
    }

    private void playVideo(String path) {
        Uri uri = Uri.parse(path);
        VideoView videoView = (VideoView)this.findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
    }
}
