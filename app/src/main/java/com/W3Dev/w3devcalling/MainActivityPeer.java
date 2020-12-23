package com.W3Dev.w3devcalling;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class MainActivityPeer extends AppCompatActivity {
    private PeerConnectionFactory factory;
    private PeerConnection peerConnection;
    private static final String TAG = "CompleteActivity";
    private static final int RC_CALL = 111;
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
    public static final int FPS = 30;
    MediaConstraints audioConstraints;
    MediaConstraints videoConstraints;
    MediaConstraints sdpConstraints;
    VideoSource videoSource;
    VideoTrack localVideoTrack;
    private EglBase rootEglBase;
    private VideoTrack videoTrackFromCamera;
    AudioSource audioSource;
    AudioTrack localAudioTrack;
    SurfaceTextureHelper surfaceTextureHelper;
    SurfaceViewRenderer surface_view, surface_view2;
    private PeerConnectionFactory peerConnectionFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_peer);

        //TODO: all muticals:::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        muticals();
/*       iceServer= PeerConnection.IceServer.builder("stun:stun.l.google.com:19302")
               .createIceServer();*/
        //TODO Buttons :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        RequestPermissions();


    }


    //TODO all functions::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private void muticals() {
        surface_view = findViewById(R.id.surface_view);
        surface_view2 = findViewById(R.id.surface_view2);
    }
    private void RequestPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                initializeSurfaceViews();
                initializePeerConnectionFactory();
                createVideoTrackFromCameraAndShowIt();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString() + "\nPlease grant all the permissions to move forward", Toast.LENGTH_SHORT).show();
                RequestPermissions();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.MODIFY_AUDIO_SETTINGS
                        , Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.BLUETOOTH)
                .check();
    }
    private void initializeSurfaceViews() {
        rootEglBase = EglBase.create();
        surface_view.init(rootEglBase.getEglBaseContext(), null);
        surface_view.setEnableHardwareScaler(true);
        surface_view.setMirror(true);

        surface_view2.init(rootEglBase.getEglBaseContext(), null);
        surface_view2.setEnableHardwareScaler(true);
        surface_view2.setMirror(true);
    }
    private void initializePeerConnectionFactory() {

        //Todo :peerconnection Initialised

        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(getApplicationContext())
                        .setEnableInternalTracer(true)
                        .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
                        .createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);

        //Create a new PeerConnectionFactory instance - using Hardware encoder and decoder.

        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        options.disableEncryption = true;
        options.disableNetworkMonitor = true;
        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(
                rootEglBase.getEglBaseContext(),  /* enableIntelVp8Encoder */true,  /* enableH264HighProfile */true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());

        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();
    }



    private void createVideoTrackFromCameraAndShowIt() {
        audioConstraints = new MediaConstraints();
        VideoCapturer videoCapturer = createandInitializeVideoCapturer();
//Create a VideoSource instance
        if (videoCapturer != null) {
            SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread"
                    , rootEglBase.getEglBaseContext());
            videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
            videoCapturer.initialize(surfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());


        }

        assert videoCapturer != null;
//        videoSource = factory.createVideoSource(false);
        //TODO error null pointer
//        videoTrackFromCamera
        videoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);


        videoTrackFromCamera = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
        videoTrackFromCamera.setEnabled(true);
        videoTrackFromCamera.addSink(surface_view);



        //startScreening

        //create an AudioSource instance
//        audioSource = factory.createAudioSource(audioConstraints);
//        localAudioTrack = factory.createAudioTrack("101", audioSource);


    }

    private VideoCapturer createandInitializeVideoCapturer() {
        VideoCapturer videoCapturer;

        if (useCamera2()) {
            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
        } else {
            videoCapturer = createCameraCapturer(new Camera1Enumerator(true));
        }

        //Create a VideoSource instance
        if (videoCapturer != null) {
            SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper
                    .create("CaptureThread"
                    , rootEglBase.getEglBaseContext());
            videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
            videoCapturer.initialize(surfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());
        }
        return videoCapturer;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(this);
    }


/*    private void buildPeerConnection(PeerConnection.Observer observer){
        factory.createPeerConnection((List<PeerConnection.IceServer>) PeerConnection.IceServer
                .builder("stun:stun.l.google.com:19302")
                .createIceServer(),observer);
    }*/
/*    private void observationCall {

        sdpConstraints=MediaConstraints.
        SdpObserver sdpObserver=new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {

            }

            @Override
            public void onSetSuccess() {

            }

            @Override
            public void onCreateFailure(String s) {

            }

            @Override
            public void onSetFailure(String s) {

            }
        }


    }*/


public void createanswer() {
}

}


























