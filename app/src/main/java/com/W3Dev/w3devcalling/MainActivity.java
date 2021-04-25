package com.W3Dev.w3devcalling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DataChannel;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "CompleteActivity";
//    private static final int RC_CALL = 111;
//    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
//    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
//    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
//    public static final int FPS = 30;
//
//    private Socket socket;
//    private boolean isInitiator;
//    private boolean isChannelReady;
//    private boolean isStarted;
//
//
//    MediaConstraints audioConstraints;
//    MediaConstraints videoConstraints;
//    MediaConstraints sdpConstraints;
//    VideoSource videoSource;
//    VideoTrack localVideoTrack;
//    AudioSource audioSource;
//    AudioTrack localAudioTrack;
//    SurfaceTextureHelper surfaceTextureHelper;
//
//    private PeerConnection peerConnection;
//    private EglBase rootEglBase;
//    private PeerConnectionFactory factory;
//    private VideoTrack videoTrackFromCamera;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @AfterPermissionGranted(RC_CALL)
//    private void start() {
//        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//
//
//            createVideoTrackFromCameraAndShowIt();
//
///*
//            initializePeerConnections();
//*/
//
//            startStreamingVideo();
//        } else {
//            EasyPermissions.requestPermissions(this, "Need some permissions", RC_CALL, perms);
//        }
//    }
//
//
////MirtDPM4
//
///*    private void doAnswer() {
//        peerConnection.createAnswer(new SimpleSdpObserver() {
//            @Override
//            public void onCreateSuccess(SessionDescription sessionDescription) {
//                peerConnection.setLocalDescription(new SimpleSdpObserver(), sessionDescription);
//                JSONObject message = new JSONObject();
//                try {
//                    message.put("type", "answer");
//                    message.put("sdp", sessionDescription.description);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new MediaConstraints());
//    }*/
//
//    private void maybeStart() {
//        Log.d(TAG, "maybeStart: " + isStarted + " " + isChannelReady);
//        if (!isStarted && isChannelReady) {
//            isStarted = true;
//            if (isInitiator) {
//                doCall();
//            }
//        }
//    }
//    private void doCall() {
//        MediaConstraints sdpMediaConstraints = new MediaConstraints();
//
//        sdpMediaConstraints.mandatory.add(
//                new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
//        sdpMediaConstraints.mandatory.add(
//                new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
//        peerConnection.createOffer(new SimpleSdpObserver() {
//            @Override
//            public void onCreateSuccess(SessionDescription sessionDescription) {
//                Log.d(TAG, "onCreateSuccess: ");
//                peerConnection.setLocalDescription(new SimpleSdpObserver(), sessionDescription);
//                JSONObject message = new JSONObject();
//                try {
//                    message.put("type", "offer");
//                    message.put("sdp", sessionDescription.description);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, sdpMediaConstraints);
//    }
//
//
//
//    private void createVideoTrackFromCameraAndShowIt() {
//        audioConstraints = new MediaConstraints();
//        VideoCapturer videoCapturer = createVideoCapturer();
//        videoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);
//
//        videoTrackFromCamera = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
//        videoTrackFromCamera.setEnabled(true);
//        //create an AudioSource instance
//        audioSource = factory.createAudioSource(audioConstraints);
//        localAudioTrack = factory.createAudioTrack("101", audioSource);
//
//    }
//
///*
//    private void initializePeerConnections() {
//        peerConnection = createPeerConnection(factory);
//    }
//*/
//
//    private void startStreamingVideo() {
//        MediaStream mediaStream = factory.createLocalMediaStream("ARDAMS");
//        mediaStream.addTrack(videoTrackFromCamera);
//        mediaStream.addTrack(localAudioTrack);
//        peerConnection.addStream(mediaStream);
//
//    }
//
///*
//    private PeerConnection createPeerConnection(PeerConnectionFactory factory) {
//        ArrayList<PeerConnection.IceServer> iceServers = new ArrayList<>();
//        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
//
//        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
//        MediaConstraints pcConstraints = new MediaConstraints();
//
//        PeerConnection.Observer pcObserver = new PeerConnection.Observer() {
//            @Override
//            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
//                Log.d(TAG, "onSignalingChange: ");
//            }
//
//            @Override
//            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//                Log.d(TAG, "onIceConnectionChange: ");
//            }
//
//            @Override
//            public void onIceConnectionReceivingChange(boolean b) {
//                Log.d(TAG, "onIceConnectionReceivingChange: ");
//            }
//
//            @Override
//            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
//                Log.d(TAG, "onIceGatheringChange: ");
//            }
//
//            @Override
//            public void onIceCandidate(IceCandidate iceCandidate) {
//                Log.d(TAG, "onIceCandidate: ");
//                JSONObject message = new JSONObject();
//
//                try {
//                    message.put("type", "candidate");
//                    message.put("label", iceCandidate.sdpMLineIndex);
//                    message.put("id", iceCandidate.sdpMid);
//                    message.put("candidate", iceCandidate.sdp);
//
//                    Log.d(TAG, "onIceCandidate: sending candidate " + message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
//                Log.d(TAG, "onIceCandidatesRemoved: ");
//            }
//
//            @Override
//            public void onAddStream(MediaStream mediaStream) {
//
//            }
//
//
//            @Override
//            public void onRemoveStream(MediaStream mediaStream) {
//                Log.d(TAG, "onRemoveStream: ");
//            }
//
//            @Override
//            public void onDataChannel(DataChannel dataChannel) {
//                Log.d(TAG, "onDataChannel: ");
//            }
//
//            @Override
//            public void onRenegotiationNeeded() {
//                Log.d(TAG, "onRenegotiationNeeded: ");
//            }
//
//            @Override
//            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
//
//            }
//        };
//
//        return factory.createPeerConnection(rtcConfig, pcConstraints, pcObserver);
//    }
//*/
//
//    private VideoCapturer createVideoCapturer() {
//        VideoCapturer videoCapturer;
//        if (useCamera2()) {
//            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
//        } else {
//            videoCapturer = createCameraCapturer(new Camera1Enumerator(true));
//        }
//        return videoCapturer;
//    }
//
//    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
//        final String[] deviceNames = enumerator.getDeviceNames();
//
//        for (String deviceName : deviceNames) {
//            if (enumerator.isFrontFacing(deviceName)) {
//                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
//
//                if (videoCapturer != null) {
//                    return videoCapturer;
//                }
//            }
//        }
//
//        for (String deviceName : deviceNames) {
//            if (!enumerator.isFrontFacing(deviceName)) {
//                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
//
//                if (videoCapturer != null) {
//                    return videoCapturer;
//                }
//            }
//        }
//
//        return null;
//    }
//
//    private boolean useCamera2() {
//        return Camera2Enumerator.isSupported(this);
//    }

    Button button2, button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        RequestPermissions();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), cameraRenderActivity.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CompleteFunctionalityActivity.class));
            }
        });
    }

    private void checkNetworkCondition() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        int upSpeed = nc.getLinkUpstreamBandwidthKbps();
        Toast.makeText(this, downSpeed, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, upSpeed, Toast.LENGTH_SHORT).show();
    }

    private void RequestPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString() + "\nPlease grant all the permissions to move forward", Toast.LENGTH_SHORT).show();
                RequestPermissions();
                checkNetworkCondition();
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
}


