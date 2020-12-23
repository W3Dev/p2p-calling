package com.W3Dev.w3devcalling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoFileRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class MediaStreamActivity extends AppCompatActivity {

    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
    public static final int FPS = 30;
    private static final String DTLS_SRTP_KEY_AGREEMENT_CONSTRAINT = "DtlsSrtpKeyAgreement";
    private static String TAG = "SamplePeerConnectionAct";
    SurfaceViewRenderer surface_view, surface_view2;
    private EglBase rootEglBase;
    private VideoTrack videoTrackFromCamera;
    private PeerConnection localPeerConnection, remotePeerConnection;
    private PeerConnectionFactory peerConnectionFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_stream);

        muticals();

        initializeSurfaceViews();

        initializePeerConnections();

        createVideoTrackFromCameraAndShowIt();

        startStreamingVideo();


    }

    private void muticals() {
        surface_view = findViewById(R.id.surface_view);
        surface_view2 = findViewById(R.id.surface_view2);
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

    private void initializePeerConnections() {
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
        localPeerConnection = createPeerConnection(peerConnectionFactory, true);
        remotePeerConnection = createPeerConnection(peerConnectionFactory, false);

    }

    private PeerConnection createPeerConnection(PeerConnectionFactory peerConnectionFactory, boolean isLocal) {
        //TODO List of stuns
        MediaConstraints pcConstraints = new MediaConstraints();


        PeerConnection.Observer pcObserver = new PeerConnection.Observer() {

            @Override
            public void onSignalingChange(PeerConnection.SignalingState signalingState) {

            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

            }

            @Override
            public void onIceConnectionReceivingChange(boolean b) {

            }

            @Override
            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                if (isLocal) {
                    remotePeerConnection.addIceCandidate(iceCandidate);
                } else {
                    localPeerConnection.addIceCandidate(iceCandidate);
                }
            }

            @Override
            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                VideoTrack remoteVideoTrack = mediaStream.videoTracks.get(0);
                remoteVideoTrack.setEnabled(true);
                remoteVideoTrack.addSink(surface_view2);

            }

            @Override
            public void onRemoveStream(MediaStream mediaStream) {

            }

            @Override
            public void onDataChannel(DataChannel dataChannel) {

            }

            @Override
            public void onRenegotiationNeeded() {

            }

            @Override
            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

            }
        };
//        return peerConnectionFactory.createPeerConnection(pcConstraints,pcObserver);

    }

    private void createVideoTrackFromCameraAndShowIt() {
        VideoCapturer videoCapturer = createVideoCapturer();
        VideoSource videoSource = factory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);

        videoTrackFromCamera = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
        videoTrackFromCamera.setEnabled(true);
        videoTrackFromCamera.addSink(surface_view);

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

/*
    private void startStreamingVideo() {
        MediaStream mediaStream = peerConnectionFactory.createLocalMediaStream("ARDAMS");
        mediaStream.addTrack(videoTrackFromCamera);
        localPeerConnection.addStream(mediaStream);

        MediaConstraints sdpMediaConstraints = new MediaConstraints();
        localPeerConnection.createOffer(new SimpleSdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
            }
        });


    }*/


}