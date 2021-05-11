package io.W3Dev.w3devcalling;

import android.content.Context;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.Set;

import io.W3Dev.w3devcalling.web_rtc.AppRTCAudioManager;

public class StartRendering {


    // MediaConstraints audioConstraints;
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
    public static final int FPS = 24;
    AudioSource audioSource;
    AudioTrack localAudioTrack;
    Context context;
    SetupViews setupViews = new SetupViews();
    private AppRTCAudioManager audioManager;
    private VideoTrack videoTrackFromCamera;
    private PeerConnectionFactory factory;

    public void createVideoTrackFromCamandShow(MediaConstraints audioConstraints) {
        audioConstraints = new MediaConstraints();
        VideoCapturer videoCapturer = setupViews.createVideoCapturer();
        VideoSource videoSource = factory.createVideoSource(videoCapturer);
        videoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);

        videoTrackFromCamera = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
        videoTrackFromCamera.setEnabled(true);
        //videoTrackFromCamera.addRenderer(new VideoRenderer(surface_view1));

        audioManager = AppRTCAudioManager.create(context);
        audioManager.start(this::onAudioManagerDevicesChanged);


        audioSource = factory.createAudioSource(audioConstraints);
        localAudioTrack = factory.createAudioTrack("101", audioSource);
    }


    public void onAudioManagerDevicesChanged(
            final AppRTCAudioManager.AudioDevice device,
            final Set<AppRTCAudioManager.AudioDevice> availableDevices) {
        // TODO(henrika): add callback handler.
    }


    public void startStreamingVideo(PeerConnection peerConnection) {
        MediaStream mediaStream = factory.createLocalMediaStream("ARDAMS");
        mediaStream.addTrack(videoTrackFromCamera);
        mediaStream.addTrack(localAudioTrack);
        peerConnection.addStream(mediaStream);
        //   sendMessage("got user media");
    }


}
