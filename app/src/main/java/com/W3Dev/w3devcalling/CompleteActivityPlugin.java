package com.W3Dev.w3devcalling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.webrtc.SurfaceViewRenderer;

import io.W3Dev.w3devcalling.StartRendering;

public class CompleteActivityPlugin extends AppCompatActivity {
    SurfaceViewRenderer localVideoView;
    SurfaceViewRenderer remoteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_plugin);

        localVideoView = findViewById(R.id.localVideoView);
        remoteView = findViewById(R.id.remoteView);


    }


    //TODO all functions::::::::::::::::::::::::::::::::::::::::::::::::::::::::


}