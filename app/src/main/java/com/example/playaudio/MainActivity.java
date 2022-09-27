package com.example.playaudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static androidx.core.net.MailTo.parse;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ModelAudio> audioArrayList;
    Button play,previous,next;
    MediaPlayer mediaPlayer;
    TextView presenttime;
    TextView totaltime;
    TextView currentsong;
    SeekBar seekBar;
    double curpos;
    int audioindex=0;
    int length = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenttime = (TextView)findViewById(R.id.prestime);
        play = (Button)findViewById(R.id.play);
        previous = (Button)findViewById(R.id.prev);
        next = (Button)findViewById(R.id.next);
        totaltime = (TextView)findViewById(R.id.totaltime);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        currentsong = (TextView)findViewById(R.id.currsong);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        AudioAdapter adapter = new AudioAdapter(this, tempAudioList);
//        ListView listView = findViewById(R.id.list);
//        listView.setAdapter(adapter);
        audioArrayList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        getAudioFiles();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekedpos = seekBar.getProgress();
                mediaPlayer.seekTo(seekedpos);

            }
        });
    }


        public void getAudioFiles() {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

//            Cursor cursor = MainActivity.this.getContentResolver().query(uri,
//                    null,
//                    MediaStore.Audio.Media.DATA + " like ? ",
//                    new String[]{"/Internal storage/Download"}, // Put your device folder / file location here.
//                    null);
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            Log.d("Checkkkk", uri.getPath());

            //looping through all rows and adding to list
            if (cursor != null && cursor.moveToFirst()) {

                do {
                    String fullname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    Log.d("cursor",fullname);
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                    String time=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//                    Log.d("Time",time);
//                    if(fullname.endsWith(".mp3")&&(!album.contains("WhatsApp Audio")))
//                        if(fullname.endsWith(".ogg"))
                        {ModelAudio modelAudio = new ModelAudio();
                        modelAudio.setAudiotitle(title);
                        modelAudio.setAudioartist(artist);
                        modelAudio.setAudiouri(Uri.parse(url));
                        modelAudio.setAudioalbum(album);
                        audioArrayList.add(modelAudio);}
                } while (cursor.moveToNext());
            }

            AudioAdapter adapter = new AudioAdapter(this, audioArrayList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos, View v) {
                    playAudio(pos);
                }
            });

        }
            public void playAudio(int pos) {
                try  {
                    audioindex = pos;
                    mediaPlayer.reset();
                    //set file path
                    mediaPlayer.setDataSource(this, audioArrayList.get(pos).getAudiouri());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    currentsong.setText(audioArrayList.get(pos).getAudiotitle());
                    play.setText("PAUSE");
                    Log.d("work","working");
            //            pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("error",e.toString());
                }
                setAudioProgress();
            }
            public void setAudioProgress()
            {
                curpos = mediaPlayer.getCurrentPosition();
                double totalpos = mediaPlayer.getDuration();
                presenttime.setText(timeconversion((long)curpos));
                totaltime.setText(timeconversion((long)totalpos));
                seekBar.setMax((int)mediaPlayer.getDuration());
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            curpos = mediaPlayer.getCurrentPosition();
                            presenttime.setText(timeconversion((long) curpos));
                            seekBar.setProgress((int) curpos);
                            handler.postDelayed(this, 1000);
                        } catch (IllegalStateException ed){
                            ed.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(runnable, 1000);
            }


            public String timeconversion(long time)
            {
                String audioTime;
                int dur = (int) time;
                int hrs = (dur / 3600000);
                int mns = (dur / 60000) % 60000;
                int scs = dur % 60000 / 1000;

                if (hrs > 0) {
                    audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
                } else {
                    audioTime = String.format("%02d:%02d", mns, scs);
                }
                return audioTime;
            }
            public void play(View view)
                {
                        if (mediaPlayer.isPlaying())
                        {
                            play.setText("PLAY");
                            mediaPlayer.pause();
                        }
                        else
                        {
                            play.setText("PAUSE");
                            mediaPlayer.start();
                        }
                }

    //             public void play(View view)
//             {
//                 if(!mediaPlayer.isPlaying())
//                 {
//                     mediaPlayer.start();
//                     mediaPlayer.seekTo(length);
//                     play.setText("PAUSE");
//                 }
//                 if(mediaPlayer.isPlaying())
//                 {
//                     length = mediaPlayer.getCurrentPosition();
//                     mediaPlayer.pause();
//                     play.setText("PLAY");
//                 }
//             }
             public void previous(View view)
             {
                 mediaPlayer.stop();
                 if(!(audioindex==0))
                     playAudio(audioindex-1);
                 else
                 {
                     playAudio(audioArrayList.size()-1);
                 }
             }
             public void next(View view)
             {
                 mediaPlayer.stop();
                 if(!(audioindex==audioArrayList.size()-1))
                     playAudio(audioindex+1);
                 else
                 {
                     playAudio(0);
                 }
             }
//    public void playaudio(int position)
//    {
//        Uri uri = Uri.parse(audioArrayList.get(position).getAudiouri().toString());
//        mediaPlayer = MediaPlayer.create(MainActivity.this,uri);
//        mediaPlayer.start();
//    }
}


