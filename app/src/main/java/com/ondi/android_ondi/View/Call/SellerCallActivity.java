package com.ondi.android_ondi.View.Call;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.ondi.android_ondi.View.Payment.PaymentActivity;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;

import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_CAMERA_FRONT_FACING;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_CHANNEL_ARN;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_CLIENT_ID;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_ICE_SERVER_PASSWORD;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_ICE_SERVER_TTL;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_ICE_SERVER_URI;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_ICE_SERVER_USER_NAME;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_IS_MASTER;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_REGION;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_SEND_AUDIO;
import static com.ondi.android_ondi.View.Chat.ChatActivity.KEY_WSS_ENDPOINT;

public class SellerCallActivity extends AppCompatActivity {
    private static final String TAG = "zzanzu";

    private String mChannelArn;
    private String mClientId;

    private String mWssEndpoint;
    private String mRegion;

    private boolean master = true;
    private boolean isAudioSent = true;

    private boolean mCameraFacingFront = true;

    private EglBase rootEglBase = null; // EglBase : webrtc 라이브러리

    private final List<PeerConnection.IceServer> peerIceServers = new ArrayList<>();

    ArrayList<String> mUserNames;
    ArrayList<String> mPasswords;
    ArrayList<Integer> mTTLs;
    ArrayList<List<String>> mUrisList;

    private PeerConnectionFactory peerConnectionFactory;

    private static final boolean ENABLE_INTEL_VP8_ENCODER = true;
    private static final boolean ENABLE_H264_HIGH_PROFILE = true;

    private VideoCapturer videoCapturer;

    private SurfaceViewRenderer localView;

    private VideoSource videoSource;
    private VideoTrack localVideoTrack;

    private static final String VideoTrackID = "KvsVideoTrack";
    private static final String AudioTrackID = "KvsAudioTrack";
    private static final String LOCAL_MEDIA_STREAM_LABEL = "KvsLocalMediaStream";

    private AudioTrack localAudioTrack;

    private AudioManager audioManager;
    private int originalAudioMode;
    private boolean originalSpeakerphoneOn;

    private static final int VIDEO_SIZE_WIDTH = 400;
    private static final int VIDEO_SIZE_HEIGHT = 300;
    private static final int VIDEO_FPS = 30;

    private boolean gotException = false;
    private static volatile SignalingServiceWebSocketClient client;

    private AWSCredentials mCreds = null;

    private Map<String, PeerConnection> peerList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_call);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getStreamingDataFromIntent();

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
                    PeerConnection.IceServer iceServer = PeerConnection.IceServer.builder(turnServer.replace("[", "").replace("]", ""))
                            .setUsername(mUserNames.get(i))
                            .setPassword(mPasswords.get(i))
                            .createIceServer();
                    Log.d(TAG, "IceServer details (TURN) = " + iceServer.toString());
                    peerIceServers.add(iceServer);
                }
            }
        }

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

        /**
         * Local video view
         */
        localView = findViewById(R.id.local_view);
        localView.init(rootEglBase.getEglBaseContext(), null);
        localView.setEnableHardwareScaler(true);

        videoSource = peerConnectionFactory.createVideoSource(false);
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create(Thread.currentThread().getName(), rootEglBase.getEglBaseContext());
        videoCapturer.initialize(surfaceTextureHelper, this.getApplicationContext(), videoSource.getCapturerObserver());

        localVideoTrack = peerConnectionFactory.createVideoTrack(VideoTrackID, videoSource);
        localVideoTrack.addSink(localView);

        /**
         * Audio
         */
        if(isAudioSent) {
            AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
            localAudioTrack = peerConnectionFactory.createAudioTrack(AudioTrackID, audioSource);
            localAudioTrack.setEnabled(true);
        }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        originalAudioMode = audioManager.getMode();
        originalSpeakerphoneOn = audioManager.isSpeakerphoneOn();

        /**
         * Start capturing video
         */
        videoCapturer.startCapture(VIDEO_SIZE_WIDTH, VIDEO_SIZE_HEIGHT, VIDEO_FPS);
        localVideoTrack.setEnabled(true);

        ImageView endIv = findViewById(R.id.btn_seller_call_end);

        endIv.setOnClickListener(v -> finish());
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

    @Override
    protected void onDestroy() {
        Thread.setDefaultUncaughtExceptionHandler(null);

        audioManager.setMode(originalAudioMode);
        audioManager.setSpeakerphoneOn(originalSpeakerphoneOn);

        if (rootEglBase != null) {
            rootEglBase.release();
            rootEglBase = null;
        }

        Iterator<String> iter = peerList.keySet().iterator();
        while (iter.hasNext()) {
            peerList.get(iter.next()).dispose();
            iter.remove();
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

        if (localView != null) {
            localView.release();
            localView = null;
        }

        if (client != null) {
            client.disconnect();
            client = null;
        }

        super.onDestroy();
    }

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
                Log.d(TAG, "Received SDP Offer: Setting Remote Description ");

                final String sdp = Event.parseOfferEvent(offerEvent);

                String recipientClientId = offerEvent.getSenderClientId();
                PeerConnection localPeer = createLocalPeerConnection(recipientClientId);

                peerList.put(recipientClientId, localPeer);

                addStreamToLocalPeer(recipientClientId);
                peerList.get(recipientClientId).setRemoteDescription(new KinesisVideoSdpObserver(),
                        new SessionDescription(SessionDescription.Type.OFFER, sdp));

                Log.d(TAG, "Received SDP offer: Creating answer");

                createSdpAnswer(recipientClientId);
            }

            @Override
            public void onSdpAnswer(final Event answerEvent) {

            }

            @Override
            public void onIceCandidate(Event message) {

                Log.d(TAG, "Received IceCandidate from remote ");

                final IceCandidate iceCandidate = Event.parseIceCandidate(message);

                if(iceCandidate != null) {
                    // Remote sent us ICE candidates, add to local peer connection
                    final boolean addIce = peerList.get(message.getSenderClientId()).addIceCandidate(iceCandidate);

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
            } else {
                Log.e(TAG, "Error in connecting to signaling service");
                gotException = true;
            }
        }
    }

    private void createSdpAnswer(String clientId) {
        peerList.get(clientId).createAnswer(new KinesisVideoSdpObserver() {

            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                Log.d(TAG, "Creating answer : success");
                super.onCreateSuccess(sessionDescription);
                peerList.get(clientId).setLocalDescription(new KinesisVideoSdpObserver(), sessionDescription);
                Message answer = Message.createAnswerMessage(sessionDescription, master, clientId);
                client.sendSdpAnswer(answer);
            }

        }, new MediaConstraints());
    }

    private PeerConnection createLocalPeerConnection(String clientId) {

        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(peerIceServers);

        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.ENABLED;

        PeerConnection localPeer = peerConnectionFactory.createPeerConnection(rtcConfig, new KinesisVideoPeerConnection() {

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {

                super.onIceCandidate(iceCandidate);

                Message message = createIceCandidateMessage(iceCandidate, clientId);
                Log.d(TAG, "Sending IceCandidate to remote peer " + iceCandidate.toString());
                client.sendIceCandidate(message);  /* Send to Peer */
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {

                super.onAddStream(mediaStream);

                Log.d(TAG, "Adding remote video stream (and audio) to the view");
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

            localPeer.getSenders();
        }

        return localPeer;
    }

    private void addStreamToLocalPeer(String clientId) {

        MediaStream stream = peerConnectionFactory.createLocalMediaStream(LOCAL_MEDIA_STREAM_LABEL);

        if (!stream.addTrack(localVideoTrack)) {

            Log.e(TAG, "Add video track failed");
        }

        peerList.get(clientId).addTrack(stream.videoTracks.get(0), Collections.singletonList(stream.getId()));

        if(isAudioSent) {
            if (!stream.addTrack(localAudioTrack)) {

                Log.e(TAG, "Add audio track failed");
            }

            if (stream.audioTracks.size() > 0) {
                peerList.get(clientId).addTrack(stream.audioTracks.get(0), Collections.singletonList(stream.getId()));
                Log.d(TAG, "Sending audio track ");
            }
        }

    }

    private Message createIceCandidateMessage(IceCandidate iceCandidate, String clientId) {
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

        String senderClientId = "";

        return new Message("ICE_CANDIDATE", clientId, senderClientId,
                new String(Base64.encode(messagePayload.getBytes(),
                        Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP)));
    }

    private URI getSignedUri(String masterEndpoint, String viewerEndpoint) {
        URI signedUri;

        signedUri = AwsV4Signer.sign(URI.create(masterEndpoint), mCreds.getAWSAccessKeyId(),
                mCreds.getAWSSecretKey(), mCreds instanceof AWSSessionCredentials ? ((AWSSessionCredentials) mCreds).getSessionToken() : "", URI.create(mWssEndpoint), mRegion);

        return signedUri;
    }

    private boolean isValidClient() {
        return client != null && client.isOpen();
    }

    private void notifySignalingConnectionFailed() {
        finish();
        Toast.makeText(this, "Connection error to signaling", Toast.LENGTH_LONG).show();
    }

    // MainActivity에서 스트리밍 관련 데이터 받아옴
    private void getStreamingDataFromIntent() {
        Intent intent = getIntent();

        mChannelArn = intent.getStringExtra(KEY_CHANNEL_ARN);
        mWssEndpoint = intent.getStringExtra(KEY_WSS_ENDPOINT);

        mClientId = intent.getStringExtra(KEY_CLIENT_ID);
        if (mClientId == null || mClientId.isEmpty()) {
            mClientId = UUID.randomUUID().toString();
        }
        master = intent.getBooleanExtra(KEY_IS_MASTER, true);
        isAudioSent = intent.getBooleanExtra(KEY_SEND_AUDIO, true);
        mUserNames = intent.getStringArrayListExtra(KEY_ICE_SERVER_USER_NAME);
        mPasswords = intent.getStringArrayListExtra(KEY_ICE_SERVER_PASSWORD);
        mTTLs = intent.getIntegerArrayListExtra(KEY_ICE_SERVER_TTL);
        mUrisList = (ArrayList<List<String>>) intent.getSerializableExtra(KEY_ICE_SERVER_URI);
        mRegion = intent.getStringExtra(KEY_REGION);
        mCameraFacingFront = intent.getBooleanExtra(KEY_CAMERA_FRONT_FACING, true);
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
}
