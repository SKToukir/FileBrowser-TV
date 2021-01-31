package com.walton.filebrowser;

interface IMusicStatusListener {
    void musicPlayException(String errMessage);
    boolean musicPlayErrorWithMsg(String errMessage);
    void handleSongSpectrum();
    void handleMessageInfo(String strMessage);
    void musicPrepared();
    void musicCompleted();
    void musicSeekCompleted();
}