package com.ondi.android_ondi.View;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.ondi.android_ondi.OndiApplication;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.Signaling.Model.Event;
import com.ondi.android_ondi.Signaling.Model.Message;
import com.ondi.android_ondi.Signaling.SignalingListener;
import com.ondi.android_ondi.Signaling.Tyrus.SignalingServiceWebSocketClient;
import com.ondi.android_ondi.Utils.AwsV4Signer;
import com.ondi.android_ondi.Webrtc.KinesisVideoPeerConnection;
import com.ondi.android_ondi.Webrtc.KinesisVideoSdpObserver;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnection.IceServer;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RTCStats;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;

import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_CAMERA_FRONT_FACING;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_CHANNEL_ARN;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_CLIENT_ID;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_ICE_SERVER_PASSWORD;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_ICE_SERVER_TTL;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_ICE_SERVER_URI;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_ICE_SERVER_USER_NAME;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_IS_MASTER;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_REGION;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_SEND_AUDIO;
import static com.ondi.android_ondi.View.ReadyCallActivity.KEY_WSS_ENDPOINT;

public class BuyerCallActivity extends AppCompatActivity {

    private static final String TAG = "KVSWebRtcActivity";
    private static final String AudioTrackID = "KvsAudioTrack";
    private static final String VideoTrackID = "KvsVideoTrack";
    private static final String LOCAL_MEDIA_STREAM_LABEL = "KvsLocalMediaStream";
    private static final int VIDEO_SIZE_WIDTH = 400;
    private static final int VIDEO_SIZE_HEIGHT = 300;
    private static final int VIDEO_FPS = 30;
    private static final boolean ENABLE_INTEL_VP8_ENCODER = true;
    private static final boolean ENABLE_H264_HIGH_PROFILE = true;

    private static volatile SignalingServiceWebSocketClient client;
    private PeerConnectionFactory peerConnectionFactory;

    private VideoSource videoSource;
    private VideoTrack localVideoTrack;

    private AudioManager audioManager;
    private int originalAudioMode;
    private boolean originalSpeakerphoneOn;

    private AudioTrack localAudioTrack;

    private SurfaceViewRenderer remoteView;

    private PeerConnection localPeer;

    private EglBase rootEglBase = null;
    private VideoCapturer videoCapturer;

    private final List<IceServer> peerIceServers = new ArrayList<>();

    private boolean gotException = false;

    private String recipientClientId;

    private boolean master;
    private boolean isAudioSent = false;

    private String mChannelArn;
    private String mClientId;

    private String mWssEndpoint;
    private String mRegion;

    private boolean mCameraFacingFront = true;

    private AWSCredentials mCreds = null;

    private void initWsConnection() {

        final String masterEndpoint = mWssEndpoint + "?X-Amz-ChannelARN=" + mChannelArn;

        final String viewerEndpoint = mWssEndpoint + "?X-Amz-ChannelARN=" + mChannelArn + "&X-Amz-ClientId=" + mClientId;

        URI signedUri;

        runOnUiThread(() -> mCreds = OndiApplication.getCredentialsProvider().getCredentials());

        signedUri = getSignedUri(masterEndpoint, viewerEndpoint);

        final String wsHost = signedUri.toString();

        final SignalingListener signalingListener = new SignalingListener() {

            @Override
            public void onSdpOffer(final Event offerEvent) {

            }

            @Override
            public void onSdpAnswer(final Event answerEvent) {

                Log.d(TAG, "SDP answer received from signaling");

                final String sdp = Event.parseSdpEvent(answerEvent);

                final SessionDescription sdpAnswer = new SessionDescription(SessionDescription.Type.ANSWER, sdp);

                localPeer.setRemoteDescription(new KinesisVideoSdpObserver(), sdpAnswer);

            }

            @Override
            public void onIceCandidate(Event message) {

                Log.d(TAG, "Received IceCandidate from remote ");

                final IceCandidate iceCandidate = Event.parseIceCandidate(message);

                if(iceCandidate != null) {
                    // Remote sent us ICE candidates, add to local peer connection
                    final boolean addIce = localPeer.addIceCandidate(iceCandidate);

                    recipientClientId = message.getSenderClientId();

                    Log.d(TAG, "Added ice candidate " + iceCandidate + " " + (addIce ? "Successfully" : "Failed"));
                } else {
                    Log.e(TAG, "Invalid Ice candidate");
                }
            }

            @Override
            public void onError(Event errorMessage) {

                Log.e(TAG, "Received error message" + errorMessage);

            }

            @Override
            public void onException(Exception e) {
                Log.e(TAG, "Signaling client returned exception " + e.getMessage());
                gotException = true;
            }
        };


        if (wsHost != null) {
            try {
                client = new SignalingServiceWebSocketClient(wsHost, signalingListener, Executors.newFixedThreadPool(10));

                Log.d(TAG, "Client connection " + (client.isOpen() ? "Successful" : "Failed"));
            } catch (Exception e) {
                gotException = true;
            }

            if (isValidClient()) {

                Log.d(TAG, "Client connected to Signaling service " + client.isOpen());

                Log.d(TAG, "Signaling service is connected: " +
                        "Sending offer as viewer to remote peer"); // Viewer

                createSdpOffer();
            } else {
                Log.e(TAG, "Error in connecting to signaling service");
                gotException = true;
            }
        }
    }

    private boolean isValidClient() {
        return client != null && client.isOpen();
    }

    @Override
    protected void onDestroy() {
        Thread.setDefaultUncaughtExceptionHandler(null);

        audioManager.setMode(originalAudioMode);
        audioManager.setSpeakerphoneOn(originalSpeakerphoneOn);

        if (rootEglBase != null) {
            rootEglBase.release();
            rootEglBase = null;
        }

        if (remoteView != null) {
            remoteView.release();
            remoteView = null;
        }

        if (localPeer != null) {
            localPeer.dispose();
            localPeer = null;
        }

        if (videoSource != null) {
            videoSource.dispose();
            videoSource = null;
        }

        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
            } catch (InterruptedException e) {
                Log.e(TAG, "Failed to stop webrtc video capture. ", e);
            }
            videoCapturer = null;
        }

        if (client != null) {
            client.disconnect();
            client = null;
        }

        finish();

        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Start websocket after adding local audio/video tracks
        initWsConnection();

        if (!gotException && isValidClient()) {
            Toast.makeText(this, "Signaling Connected", Toast.LENGTH_LONG).show();
        } else {
            notifySignalingConnectionFailed();
        }
    }

    private void notifySignalingConnectionFailed() {
        finish();
        Toast.makeText(this, "Connection error to signaling", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        mChannelArn = intent.getStringExtra(KEY_CHANNEL_ARN);
        mWssEndpoint = intent.getStringExtra(KEY_WSS_ENDPOINT);

        mClientId = intent.getStringExtra(KEY_CLIENT_ID);
        if (mClientId == null || mClientId.isEmpty()) {
            mClientId = UUID.randomUUID().toString();
        }
        master = intent.getBooleanExtra(KEY_IS_MASTER, false);
        ArrayList<String> mUserNames = intent.getStringArrayListExtra(KEY_ICE_SERVER_USER_NAME);
        ArrayList<String> mPasswords = intent.getStringArrayListExtra(KEY_ICE_SERVER_PASSWORD);
        ArrayList<Integer> mTTLs = intent.getIntegerArrayListExtra(KEY_ICE_SERVER_TTL);
        ArrayList<List<String>> mUrisList = (ArrayList<List<String>>) intent.getSerializableExtra(KEY_ICE_SERVER_URI);
        mRegion = intent.getStringExtra(KEY_REGION);
        mCameraFacingFront = intent.getBooleanExtra(KEY_CAMERA_FRONT_FACING, true);

        rootEglBase = EglBase.create();

        PeerConnection.IceServer stun = PeerConnection
                .IceServer
                .builder(String.format("stun:stun.kinesisvideo.%s.amazonaws.com:443", mRegion))
                .createIceServer();

        peerIceServers.add(stun);

        if (mUrisList != null) {
            for (int i = 0; i < mUrisList.size(); i++) {
                String turnServer = mUrisList.get(i).toString();
                if( turnServer != null) {
                    IceServer iceServer = IceServer.builder(turnServer.replace("[", "").replace("]", ""))
                            .setUsername(mUserNames.get(i))
                            .setPassword(mPasswords.get(i))
                            .createIceServer();
                    Log.d(TAG, "IceServer details (TURN) = " + iceServer.toString());
                    peerIceServers.add(iceServer);
                }
            }
        }

        setContentView(R.layout.activity_buyer_call);

        PeerConnectionFactory.initialize(PeerConnectionFactory
                .InitializationOptions
                .builder(this)
                .createInitializationOptions());

        peerConnectionFactory =
                PeerConnectionFactory.builder()
                        .setVideoDecoderFactory(new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext()))
                        .setVideoEncoderFactory(new DefaultVideoEncoderFactory(rootEglBase.getEglBaseContext(), ENABLE_INTEL_VP8_ENCODER, ENABLE_H264_HIGH_PROFILE))
                        .createPeerConnectionFactory();

        videoCapturer = createVideoCapturer();

        videoSource = peerConnectionFactory.createVideoSource(false);
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create(Thread.currentThread().getName(), rootEglBase.getEglBaseContext());
        videoCapturer.initialize(surfaceTextureHelper, this.getApplicationContext(), videoSource.getCapturerObserver());

        localVideoTrack = peerConnectionFactory.createVideoTrack(VideoTrackID, videoSource);

        if(isAudioSent) {

            AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
            localAudioTrack = peerConnectionFactory.createAudioTrack(AudioTrackID, audioSource);
            localAudioTrack.setEnabled(true);

        }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        originalAudioMode = audioManager.getMode();
        originalSpeakerphoneOn = audioManager.isSpeakerphoneOn();

        // Start capturing video
        videoCapturer.startCapture(VIDEO_SIZE_WIDTH, VIDEO_SIZE_HEIGHT, VIDEO_FPS);
        localVideoTrack.setEnabled(true);

        remoteView = findViewById(R.id.remote_view);
        remoteView.init(rootEglBase.getEglBaseContext(), null);
    }

    private VideoCapturer createVideoCapturer() {

        VideoCapturer videoCapturer;

        Logging.d(TAG, "Create camera");
        videoCapturer = createCameraCapturer(new Camera1Enumerator(false));

        return videoCapturer;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {

        final String[] deviceNames = enumerator.getDeviceNames();

        Logging.d(TAG, "Enumerating cameras");

        for (String deviceName : deviceNames) {

            if (mCameraFacingFront ? enumerator.isFrontFacing(deviceName) : enumerator.isBackFacing(deviceName)) {

                Logging.d(TAG, "Camera created");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private void createLocalPeerConnection() {

        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(peerIceServers);

        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.ENABLED;

        localPeer = peerConnectionFactory.createPeerConnection(rtcConfig, new KinesisVideoPeerConnection() {

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {

                super.onIceCandidate(iceCandidate);

                Message message = createIceCandidateMessage(iceCandidate);
                Log.d(TAG, "Sending IceCandidate to remote peer " + iceCandidate.toString());
                client.sendIceCandidate(message);  /* Send to Peer */

            }

            @Override
            public void onAddStream(MediaStream mediaStream) {

                super.onAddStream(mediaStream);

                Log.d(TAG, "Adding remote video stream (and audio) to the view");

                addRemoteStreamToVideoView(mediaStream);
            }
        });

        if (localPeer != null) {

            localPeer.getStats(rtcStatsReport -> {

                Map<String, RTCStats> statsMap = rtcStatsReport.getStatsMap();

                Set<Map.Entry<String, RTCStats>> entries = statsMap.entrySet();

                for (Map.Entry<String, RTCStats> entry : entries) {

                    Log.d(TAG, "Stats: " + entry.getKey() + " ," + entry.getValue());

                }
            });
        }

        addStreamToLocalPeer();
    }

    private Message createIceCandidateMessage(IceCandidate iceCandidate) {
        String sdpMid = iceCandidate.sdpMid;
        int sdpMLineIndex = iceCandidate.sdpMLineIndex;
        String sdp = iceCandidate.sdp;

        String messagePayload =
                "{\"candidate\":\""
                        + sdp
                        + "\",\"sdpMid\":\""
                        + sdpMid
                        + "\",\"sdpMLineIndex\":"
                        + sdpMLineIndex
                        + "}";

        String senderClientId = mClientId;

        return new Message("ICE_CANDIDATE", "", senderClientId,
                new String(Base64.encode(messagePayload.getBytes(),
                        Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP)));
    }

    private void addStreamToLocalPeer() {

        MediaStream stream = peerConnectionFactory.createLocalMediaStream(LOCAL_MEDIA_STREAM_LABEL);

        if (!stream.addTrack(localVideoTrack)) {

            Log.e(TAG, "Add video track failed");
        }

        localPeer.addTrack(stream.videoTracks.get(0), Collections.singletonList(stream.getId()));

        if(isAudioSent) {
            if (!stream.addTrack(localAudioTrack)) {

                Log.e(TAG, "Add audio track failed");
            }

            if (stream.audioTracks.size() > 0) {
                localPeer.addTrack(stream.audioTracks.get(0), Collections.singletonList(stream.getId()));
                Log.d(TAG, "Sending audio track ");
            }
        }

    }

    // when mobile sdk is viewer
    private void createSdpOffer() {

        MediaConstraints sdpMediaConstraints = new MediaConstraints();

        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));

        if (localPeer == null) {

            createLocalPeerConnection();
        }

        localPeer.createOffer(new KinesisVideoSdpObserver() {

            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {

                super.onCreateSuccess(sessionDescription);

                localPeer.setLocalDescription(new KinesisVideoSdpObserver(), sessionDescription);

                Message sdpOfferMessage = Message.createOfferMessage(sessionDescription, mClientId);

                if (isValidClient()) {
                    client.sendSdpOffer(sdpOfferMessage);
                } else {
                    notifySignalingConnectionFailed();
                }
            }
        }, sdpMediaConstraints);
    }

    private void addRemoteStreamToVideoView(MediaStream stream) {

        final VideoTrack remoteVideoTrack = stream.videoTracks != null && stream.videoTracks.size() > 0? stream.videoTracks.get(0) : null;

        AudioTrack remoteAudioTrack  = stream.audioTracks != null && stream.audioTracks.size() > 0 ? stream.audioTracks.get(0) : null;

        if(remoteAudioTrack != null ) {
            remoteAudioTrack.setEnabled(true);
            Log.d(TAG, "remoteAudioTrack received: State=" + remoteAudioTrack.state().name());
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.setSpeakerphoneOn(true);
        }

        if(remoteVideoTrack != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "remoteVideoTrackId=" + remoteVideoTrack.id() + " videoTrackState=" + remoteVideoTrack.state());
                        remoteVideoTrack.addSink(remoteView);
                    } catch (Exception e) {
                        Log.e(TAG, "Error in setting remote video view" + e);
                    }
                }
            });
        } else {
            Log.e(TAG, "Error in setting remote track");
        }

    }


    private URI getSignedUri(String masterEndpoint, String viewerEndpoint) {
        URI signedUri;

        if (master) {
            signedUri = AwsV4Signer.sign(URI.create(masterEndpoint), mCreds.getAWSAccessKeyId(),
                    mCreds.getAWSSecretKey(), mCreds instanceof AWSSessionCredentials ? ((AWSSessionCredentials) mCreds).getSessionToken() : "", URI.create(mWssEndpoint), mRegion);
        } else {
            signedUri = AwsV4Signer.sign(URI.create(viewerEndpoint), mCreds.getAWSAccessKeyId(),
                    mCreds.getAWSSecretKey(), mCreds instanceof AWSSessionCredentials ? ((AWSSessionCredentials)mCreds).getSessionToken() : "", URI.create(mWssEndpoint), mRegion);
        }
        return signedUri;
    }
}