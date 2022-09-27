package com.example.playaudio;

import android.net.Uri;

public class ModelAudio {
    String audiotitle;
    String audioartist;
    String audioalbum;
    Uri audiouri;



    public void setAudioartist(String audioartist) {
        this.audioartist = audioartist;
    }

    public String getAudioartist() {
        return audioartist;
    }

    public void setAudioalbum(String audioalbum) {
        this.audioalbum = audioalbum;
    }

    public String getAudioalbum() {
        return audioalbum;
    }


    public void setAudiotitle(String audiotitle) {
        this.audiotitle = audiotitle;
    }

    public String getAudiotitle() {
        return audiotitle;
    }

    public void setAudiouri(Uri audiouri) {
        this.audiouri = audiouri;
    }

    public Uri getAudiouri() {
        return audiouri;
    }
}
