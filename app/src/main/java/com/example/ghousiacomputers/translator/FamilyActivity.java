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

public class FamilyActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =

           new AudioManager.OnAudioFocusChangeListener() {
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
        arr.add(new Word("Father", "pere", R.drawable.family_father, R.raw.family_father));
        arr.add(new Word("Mother", "mere", R.drawable.family_mother, R.raw.family_mother));
        arr.add(new Word("Older Sister", "grande  soeur", R.drawable.family_older_sister, R.raw.family_older_sister));
        arr.add(new Word("Older Brother", "grande frere", R.drawable.family_older_brother, R.raw.family_older_brother));
        arr.add(new Word("Younger Brother", "plus jeune frere", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        arr.add(new Word("Younger Sister", "plus jeune garcon", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        arr.add(new Word("Son", "fils", R.drawable.family_son, R.raw.family_son));
        arr.add(new Word("Daughter", "fille", R.drawable.family_daughter, R.raw.family_daughter));
        arr.add(new Word("Grand Mother", "grand-mère", R.drawable.family_grandmother, R.raw.family_grandmother));
        arr.add(new Word("CouGrand Father", "grand-pere", R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter arrayAdapter = new WordAdapter(this, arr, R.color.category_family);
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
    }

    private void releaseMediaplayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);

        }
    }
}
