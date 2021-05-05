package io.W3Dev.w3devcalling;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class InitializeRtcEngine {

    private static final int RC_CALL = 111;
    public Context context;

    @AfterPermissionGranted(RC_CALL)
    public void getRealtimePermits(String perms) {
        if (EasyPermissions.hasPermissions(context, perms)) {

            Toast.makeText(context, "Got Perms", Toast.LENGTH_SHORT).show();

/*            connectToSignallingServer();

            initializeSurfaceViews();

            initializePeerConnectionFactory();

            createVideoTrackFromCameraAndShowIt();

            initializePeerConnections();

            startStreamingVideo();*/

        } else {
            EasyPermissions.requestPermissions((Activity) context, "Need some permissions", RC_CALL, perms);
        }
    }

    //TODO SdpObserver class
    private void doAnswer(PeerConnection peerConnection, SdpObserver SimpleSdpObserver) {
        peerConnection.createAnswer(new SimpleSdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                //      peerConnection.setLocalDescription(new SimpleSdpObserver(), sessionDescription);
                JSONObject message = new JSONObject();
                try {
                    message.put("type", "answer");
                    message.put("sdp", sessionDescription.description);
                    //  sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new MediaConstraints());
    }


}
