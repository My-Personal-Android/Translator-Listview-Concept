package com.example.ghousiacomputers.translator;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =
            onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaplayer();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mediaPlayer.start();
                    }
                }
            };
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaplayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_words);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> arr = new ArrayList<Word>();
        arr.add(new Word("black", "noir", R.drawable.color_black, R.raw.color_black));
        arr.add(new Word("red", "rouge", R.drawable.color_red, R.raw.color_red));
        arr.add(new Word("Dusty yellow", "jaune poussiéreux", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        arr.add(new Word("green", "vert", R.drawable.color_green, R.raw.color_green));
        arr.add(new Word("Gray", "gris", R.drawable.color_gray, R.raw.color_gray));
        arr.add(new Word("yellow", "jaune", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        arr.add(new Word("white", "blanc", R.drawable.color_white, R.raw.color_white));
        arr.add(new Word("brown", "marron", R.drawable.color_brown, R.raw.color_brown));

        WordAdapter arrayAdapter = new WordAdapter(this, arr, R.color.category_colors);
        ListView listView = (ListView) findViewById(R.id.ListView_Words);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word w = arr.get(position);
                releaseMediaplayer();

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), w.getAudioResource());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }


            }
        });


    }

    protected void onStop() {
        super.onStop();
        releaseMediaplayer();
    }

    protected void onPause() {
        super.onPause();
        releaseMediaplayer();
    }

    private void releaseMediaplayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);

        }
    }
}
