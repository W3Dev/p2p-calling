package com.W3Dev.w3devcalling.background;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataChecker extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkNetworkCondition();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    private void checkNetworkCondition() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        String downSpeed = String.valueOf(nc.getLinkDownstreamBandwidthKbps());
        String upSpeed = String.valueOf(nc.getLinkUpstreamBandwidthKbps());
        Toast.makeText(getApplicationContext(), "The Downlink is :" + downSpeed, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "The Uplink is :" + upSpeed, Toast.LENGTH_SHORT).show();

    }

}
