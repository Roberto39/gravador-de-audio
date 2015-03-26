package br.edu.puccampinas.gravadoraudio;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;


public class Gravador extends Activity {

    class RecordButton extends Button {

        Boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {

                onRecord(mStartRecording);
                if(mStartRecording){
                    setText("Stop Recording");
                }else{
                    setText("Start Recording");
                }
                mStartRecording = !mStartRecording;

            }
        };

        public RecordButton(Context context) {
            super(context);
            setText("Start Recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button{

        Boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if(mStartPlaying){
                    setText("Stop Playing");
                }else{
                    setText("Start Playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context context) {
            super(context);
            setText("Start Playing");
            setOnClickListener(clicker);
        }
    }

    private static final String LOG_TAG = "AudioRecorder";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton mPlayButton = null;
    private MediaPlayer mPlayer = null;

    public Gravador(){
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio_gravado.3gp";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG,"Entrou no metodo onCreate()");


        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);

        //adicionando o botao na activity, manualmente, pelo layout linear, criado acima.
        ll.addView(mRecordButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,0
        ));

        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,0
        ));

        setContentView(ll);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"Entrou no onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"Entrou no onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mRecorder != null){
            mRecorder.release();
            mRecorder = null;
        }

        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void onRecord(Boolean start){
        if(start){
            startRecording();
        }else{
            stopRecording();
        }
    }

    private void onPlay(Boolean start){
        if(start){
            startPlaying();
        }else{
            stopPlaying();
        }
    }

    private void startPlaying(){
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() media player failed");
        }
    }

    private void stopPlaying(){
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() Media Recorder failed");
        }

        mRecorder.start();
    }

    private void stopRecording(){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

}