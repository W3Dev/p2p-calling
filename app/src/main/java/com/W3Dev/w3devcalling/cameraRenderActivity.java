package com.W3Dev.w3devcalling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;

import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
/*import org.webrtc.CapturerObserver;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;*/
import org.webrtc.EglBase;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class cameraRenderActivity extends AppCompatActivity {
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
    public static final int FPS = 30;
    EglBase rootEglBase;
    SurfaceViewRenderer surface_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_render);
        initialixeSurfaceView();
//        initializePeerConnectionFactory();
    }

    private void initialixeSurfaceView() {
        rootEglBase = EglBase.create();
        surface_view = findViewById(R.id.surface_view);
        surface_view.init(rootEglBase.getEglBaseContext(), null);
        surface_view.setEnableHardwareScaler(true);
        surface_view.setMirror(true);

        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true);
        PeerConnectionFactory peerConnectionFactory = new PeerConnectionFactory(null);
        peerConnectionFactory.setVideoHwAccelerationOptions(rootEglBase.getEglBaseContext(), rootEglBase.getEglBaseContext());
        ;

/*     {   PeerConnectionFactory.InitializationOptions initializationOptions =
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
                rootEglBase.getEglBaseContext(),  *//* enableIntelVp8Encoder *//*true,  *//* enableH264HighProfile *//*true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());

        PeerConnectionFactory peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();}*/

        createVideoTrackAndShowIt(peerConnectionFactory);
    }

    private void createVideoTrackAndShowIt(PeerConnectionFactory peerConnectionFactory) {
        VideoCapturer videoCapturer = createVideoCapturer();
        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer);
        videoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);

        VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
        localVideoTrack.setEnabled(true);
//        localVideoTrack.addSink(surface_view);
        localVideoTrack.addRenderer(new VideoRenderer(surface_view));
    }


    private VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer;
        if (useCamera2()) {
            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
        } else {
            videoCapturer = createCameraCapturer(new Camera1Enumerator(true));
        }
        return videoCapturer;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
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


}