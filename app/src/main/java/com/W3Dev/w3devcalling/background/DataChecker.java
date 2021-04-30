package com.W3Dev.w3devcalling.background;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.W3Dev.w3devcalling.R;

import java.util.Timer;
import java.util.TimerTask;

public class DataChecker extends Service {
    int counter;
    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void addDatatoTextView(int upspeed, int downspeed, View view) {
        TextView up = view.findViewById(upspeed);
        TextView down = view.findViewById(downspeed);
        up.setText(upspeed);
        down.setText(downspeed);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
    }

    public void checkNetworkCondition() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        String downSpeed = String.valueOf(nc.getLinkDownstreamBandwidthKbps());
        String upSpeed = String.valueOf(nc.getLinkUpstreamBandwidthKbps());
        Log.i("pkkkk", "" + upSpeed + downSpeed);
        Intent intent = new Intent();
        intent.setAction("yuy");
        intent.putExtra("upspeed", upSpeed);
        intent.putExtra("downspeed", downSpeed);
        sendBroadcast(intent);
    }

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                //  Log.i("Count", "=========  " + (counter++));
                checkNetworkCondition();

            }
        };
        timer.schedule(timerTask, 1000, 5000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
