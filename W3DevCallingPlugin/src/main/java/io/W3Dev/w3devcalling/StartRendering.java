package io.W3Dev.w3devcalling;

import android.content.Context;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.Set;

import io.W3Dev.w3devcalling.web_rtc.AppRTCAudioManager;

public class StartRendering {

/*        MediaConstraints mediaConstraints;
    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
    public static final int FPS = 24;
        private AppRTCAudioManager audioManager;
    VideoCapturer videoCapturer;
PeerConnectionFactory factory,
    PeerConnection peerConnectionFactory
    private VideoTrack videoTrackFromCamera;
    SetupViews setupViews = new SetupViews();*/


    //TODO Alone can render camera capturrer activity

    public VideoTrack RenderForLocalViewOnly(PeerConnectionFactory factory,
                                             VideoCapturer videoCapturer,
                                             VideoTrack videoTrack,
                                             SurfaceViewRenderer surfaceViewRenderer,
                                             int VIDEO_RESOLUTION_HEIGHT,
                                             int VIDEO_RESOLUTION_WIDTH,
                                             int FPS) {
        VideoSource videoSource = factory.createVideoSource(videoCapturer);
        videoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);
        videoTrack = factory.createVideoTrack(getTrackID(), videoSource);
        videoTrack.setEnabled(true);
        videoTrack.addRenderer(new VideoRenderer(surfaceViewRenderer));
        return videoTrack;

    }

    private String getTrackID() {
        final String VIDEO_TRACK_ID = "ARDAMSv0";
        return VIDEO_TRACK_ID;
    }








/*    AudioSource audioSource;
    AudioTrack localAudioTrack;*/

/*    public void createVideoTrackFromCamandShow(MediaConstraints audioConstraints,
                                               PeerConnectionFactory factory,
                                               AppRTCAudioManager audioManager
                                              ) {
        AppRTCAudioManager audioManager;
        audioConstraints = new MediaConstraints();
        audioManager = AppRTCAudioManager.create(context);
        audioManager.start(this::onAudioManagerDevicesChanged);
        audioSource = factory.createAudioSource(audioConstraints);
        localAudioTrack = factory.createAudioTrack("101", audioSource);
    }*/


    public void startStreamingVideo(PeerConnection peerConnection,
                                    MediaStream mediaStream,
                                    AudioTrack localAudioTrack,
                                    PeerConnectionFactory factory,
                                    VideoTrack videoTrackFromCamera) {
        mediaStream = factory.createLocalMediaStream(getTrackID());
        mediaStream.addTrack(videoTrackFromCamera);
        mediaStream.addTrack(localAudioTrack);
        peerConnection.addStream(mediaStream);
    }

}
