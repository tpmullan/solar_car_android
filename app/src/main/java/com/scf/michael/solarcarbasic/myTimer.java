package com.scf.michael.solarcarbasic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import static android.app.Service.START_STICKY;

/**
 * Created by Michael on 7/17/2017.
 */

public class myTimer extends MainActivity {

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    //@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //super.onStartCommand(intent, flags, startId);
        Toast.makeText(getApplicationContext(), "start command", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // your code here
        Toast.makeText(getApplicationContext(), "wakka wakka", Toast.LENGTH_SHORT).show();

        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                Toast.makeText(getApplicationContext(), "woah!", Toast.LENGTH_SHORT).show();
                //updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        Toast.makeText(getApplicationContext(), "woah!", Toast.LENGTH_SHORT).show();
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
