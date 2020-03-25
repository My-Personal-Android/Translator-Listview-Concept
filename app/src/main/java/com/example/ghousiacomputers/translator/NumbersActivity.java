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

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer mediaplayer;
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =
            onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mediaplayer.pause();
                        mediaplayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaplayer();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mediaplayer.start();
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
        arr.add(new Word("One", "Un", R.drawable.number_one, R.raw.number_one));
        arr.add(new Word("Two", "Deux", R.drawable.number_two, R.raw.number_two));
        arr.add(new Word("Three", "Trois", R.drawable.number_three, R.raw.number_three));
        arr.add(new Word("Four", "Quatre", R.drawable.number_four, R.raw.number_four));
        arr.add(new Word("Five", "Cinq", R.drawable.number_five, R.raw.number_five));
        arr.add(new Word("Six", "Six", R.drawable.number_six, R.raw.number_six));
        arr.add(new Word("Seven", "Sept", R.drawable.number_seven, R.raw.number_seven));
        arr.add(new Word("Eight", "Huit", R.drawable.number_eight, R.raw.number_eight));
        arr.add(new Word("Nine", "Neuf", R.drawable.number_nine, R.raw.number_nine));
        arr.add(new Word("Ten", "Dix", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter arrayAdapter = new WordAdapter(this, arr, R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.ListView_Words);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word w = arr.get(position);

                releaseMediaplayer();

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaplayer = MediaPlayer.create(getApplicationContext(), w.getAudioResource());
                    mediaplayer.start();
                    mediaplayer.setOnCompletionListener(onCompletionListener);
                }


            }
        });


    }

    protected void onPause() {
        super.onPause();
        releaseMediaplayer();
    }

    protected void onStop() {
        super.onStop();
        releaseMediaplayer();
    }

    private void releaseMediaplayer() {
        if (mediaplayer != null) {
            mediaplayer.release();
            mediaplayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

}
