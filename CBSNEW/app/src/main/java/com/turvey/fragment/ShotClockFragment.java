package com.turvey.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;
import com.turvey.cbs.R;

import java.util.Random;

public class ShotClockFragment extends Fragment {
    RippleBackground rippleBackground;
    public static final int RECORD_AUDIO = 0;
    MediaPlayer mp = null;
    FloatingActionButton fabstart, fabcancel;
    TextView shotseconds, dbview;
    final int min = 2;
    final int max = 5;
    Handler clockHandler = new Handler();
    final Handler dbMeter = new Handler();
    String randomString;
    int random, secs, mins, milliseconds;
    Long startTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updateTime = 0L;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    final Runnable updater = new Runnable() {

        public void run() {
            updateTv();
        }

        ;
    };

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updateTime / 1000);
            mins = secs / 60;
            secs %= 60;
            milliseconds = (int) (updateTime % 1000);

            shotseconds.setText(mins + ":" + String.format("%1d", secs) + ":" + String.format("%3d", milliseconds));
            clockHandler.postDelayed(this, 0);
        }
    };

    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shot_clock_ripple, container, false);
        initButtons(view);
        return view;
    }

    public void initButtons(View view) {
        shotseconds = (TextView) view.findViewById(R.id.shotseconds);
        dbview = (TextView) view.findViewById(R.id.dbview);
        mp = MediaPlayer.create(getContext(), R.raw.beepone);
        fabstart = (FloatingActionButton) view.findViewById(R.id.FABstart);
        fabcancel = (FloatingActionButton) view.findViewById(R.id.FABcancel);
        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        fabstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                random = new Random().nextInt((max - min) + 1) + min;
                randomString = Integer.toString(random);
                Toast.makeText(getContext(), "Clock will start in " + randomString + " seconds.", Toast.LENGTH_LONG).show();
                init();
            }
        });

        fabcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTime();
            }
        });
    }

    private void init() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rippleBackground.startRippleAnimation();
                mp.start();
                startTime();
            }
        }, random * 1000);
    }

    private void stopTime() {
        rippleBackground.stopRippleAnimation();
        clockHandler.removeCallbacks(updateTimerThread);
        Toast.makeText(getContext(), "Stopped at " + mins + ":" + String.format("%1d", secs) + ":" + String.format("%3d", milliseconds), Toast.LENGTH_LONG).show();
        shotseconds.setText("0:0:000");
        stopRecorder();
    }

    private void startTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doSound();
                startTime = SystemClock.uptimeMillis();
                clockHandler.postDelayed(updateTimerThread, 0);
            }
        }, mp.getDuration());
    }

    //------------------------------SOUND RECORDER AND DB METER------------------------------------
    private void doSound() {
        if (runner == null) {
            runner = new Thread() {
                public void run() {
                    while (runner != null) {
                        try {
                            Thread.sleep(1000);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) {
                        }
                        ;
                        dbMeter.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
    }

    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);

        } else {
            startRecorder();
        }

    }

    public void onPause() {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder() {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try {
                mRecorder.start();
            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            //mEMA = 0.0;

    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTv() {
        Double d = getAmplitudeEMA() /10;
        Integer i = d.intValue();
       // dbview.setText(i + " dB");
        if (i > 1000) {
            stopTime();
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;

    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
    //------------------------------SOUND RECORDER AND DB METER------------------------------------
}

