package com.ondi.android_ondi.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.regions.Region;
import com.amazonaws.services.kinesisvideo.AWSKinesisVideoClient;
import com.amazonaws.services.kinesisvideo.model.ChannelRole;
import com.amazonaws.services.kinesisvideo.model.CreateSignalingChannelRequest;
import com.amazonaws.services.kinesisvideo.model.CreateSignalingChannelResult;
import com.amazonaws.services.kinesisvideo.model.DescribeSignalingChannelRequest;
import com.amazonaws.services.kinesisvideo.model.DescribeSignalingChannelResult;
import com.amazonaws.services.kinesisvideo.model.GetSignalingChannelEndpointRequest;
import com.amazonaws.services.kinesisvideo.model.GetSignalingChannelEndpointResult;
import com.amazonaws.services.kinesisvideo.model.ResourceEndpointListItem;
import com.amazonaws.services.kinesisvideo.model.ResourceNotFoundException;
import com.amazonaws.services.kinesisvideo.model.SingleMasterChannelEndpointConfiguration;
import com.amazonaws.services.kinesisvideosignaling.AWSKinesisVideoSignalingClient;
import com.amazonaws.services.kinesisvideosignaling.model.GetIceServerConfigRequest;
import com.amazonaws.services.kinesisvideosignaling.model.GetIceServerConfigResult;
import com.amazonaws.services.kinesisvideosignaling.model.IceServer;
import com.ondi.android_ondi.OndiApplication;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class ReadyCallActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String KEY_CHANNEL_NAME = "channelName";
    public static final String KEY_CLIENT_ID = "clientId";
    public static final String KEY_REGION = "region";
    public static final String KEY_CHANNEL_ARN = "channelArn";
    public static final String KEY_WSS_ENDPOINT = "wssEndpoint";
    public static final String KEY_IS_MASTER = "isMaster";
    public static final String KEY_ICE_SERVER_USER_NAME = "iceServerUserName";
    public static final String KEY_ICE_SERVER_PASSWORD = "iceServerPassword";
    public static final String KEY_ICE_SERVER_TTL = "iceServerTTL";
    public static final String KEY_ICE_SERVER_URI = "iceServerUri";
    public static final String KEY_CAMERA_FRONT_FACING = "cameraFrontFacing";
    private static final String KEY_SEND_VIDEO = "sendVideo";
    public static final String KEY_SEND_AUDIO = "sendAudio";
    private static final String[] KEY_OF_OPTIONS = {
            KEY_SEND_VIDEO,
            KEY_SEND_AUDIO,
    };

    public final String MY_CLIENT_ID = "client-id-0602";

    public static List<ResourceEndpointListItem> mEndpointList = new ArrayList<>();
    public static List<IceServer> mIceServerList = new ArrayList<>();
    public static String mChannelArn = null;

    private boolean isMaster = true;

    private EditText channelNameEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 9393);
        }

        setContentView(R.layout.activity_ready_call);

        channelNameEdt = findViewById(R.id.edt_channel_name);
        Button logout_btn = findViewById(R.id.btn_logout);
        logout_btn.setOnClickListener(view -> {
            AWSMobileClient.getInstance().signOut();
            startActivity(new Intent(ReadyCallActivity.this, LoginActivity.class));
            finish();
        });
        Button viewerBtn = findViewById(R.id.btn_viewer);
        viewerBtn.setOnClickListener(view -> {
            isMaster = false;
        });
    }

    public void startButtonClick(View view) {
        String region = OndiApplication.getRegion();
        String channelName = channelNameEdt.getText().toString();

        if (isMaster) {
            startStreaming(region, channelName, ChannelRole.MASTER, SellerCallActivity.class);
        } else {
            startStreaming(region, channelName, ChannelRole.VIEWER, BuyerCallActivity.class);
        }
    }

    private void startStreaming(String region, String channelName, ChannelRole role, Class<?> destination) {
        updateSignalingChannelInfo(region, channelName, role);

        if (mChannelArn != null) {
            Bundle extras = setExtras();
            Intent intent = new Intent(this, destination);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    private void updateSignalingChannelInfo(String region, String channelName, ChannelRole role) {
        mEndpointList.clear();
        mIceServerList.clear();
        mChannelArn = null;
        UpdateSignalingChannelInfoTask task = new UpdateSignalingChannelInfoTask();
        try {
            task.execute(region, channelName, role).get();
        } catch (Exception e) {
            Log.e(TAG, "Failed to wait for response of UpdateSignalingChannelInfoTask", e);
        }
    }

    private Bundle setExtras() {
        final Bundle extras = new Bundle();
        final String channelName = channelNameEdt.getText().toString();
        final String clientId = MY_CLIENT_ID;
        final String region = OndiApplication.getRegion();

        extras.putString(KEY_CHANNEL_NAME, channelName);
        extras.putString(KEY_CLIENT_ID, clientId);
        extras.putString(KEY_REGION, region);
        extras.putString(KEY_CHANNEL_ARN, mChannelArn);
        extras.putBoolean(KEY_IS_MASTER, isMaster);

        if (mIceServerList.size() > 0) {
            ArrayList<String> userNames = new ArrayList<>(mIceServerList.size());
            ArrayList<String> passwords = new ArrayList<>(mIceServerList.size());
            ArrayList<Integer> ttls = new ArrayList<>(mIceServerList.size());
            ArrayList<List<String>> urisList = new ArrayList<>();
            for (IceServer iceServer : mIceServerList) {
                userNames.add(iceServer.getUsername());
                passwords.add(iceServer.getPassword());
                ttls.add(iceServer.getTtl());
                urisList.add(iceServer.getUris());
            }
            extras.putStringArrayList(KEY_ICE_SERVER_USER_NAME, userNames);
            extras.putStringArrayList(KEY_ICE_SERVER_PASSWORD, passwords);
            extras.putIntegerArrayList(KEY_ICE_SERVER_TTL, ttls);
            extras.putSerializable(KEY_ICE_SERVER_URI, urisList);
        } else {
            extras.putStringArrayList(KEY_ICE_SERVER_USER_NAME, null);
            extras.putStringArrayList(KEY_ICE_SERVER_PASSWORD, null);
            extras.putIntegerArrayList(KEY_ICE_SERVER_TTL, null);
            extras.putSerializable(KEY_ICE_SERVER_URI, null);
        }

        for (ResourceEndpointListItem endpoint : mEndpointList) {
            if (endpoint.getProtocol().equals("WSS")) {
                extras.putString(KEY_WSS_ENDPOINT, endpoint.getResourceEndpoint());
            }
        }

        extras.putBoolean(KEY_OF_OPTIONS[0], true); // Send Video 옵션 선택
        extras.putBoolean(KEY_OF_OPTIONS[1], true); // Send Audio 옵션 선택

        extras.putBoolean(KEY_CAMERA_FRONT_FACING, true); // 전면 카메라 설정(후면은 false)

        return extras;
    }




    static class UpdateSignalingChannelInfoTask extends AsyncTask<Object, String, String> {
        UpdateSignalingChannelInfoTask() { }

        private AWSKinesisVideoClient getAwsKinesisVideoClient(final String region) {
            final AWSKinesisVideoClient awsKinesisVideoClient = new AWSKinesisVideoClient(
                    AWSMobileClient.getInstance().getCredentials());
            awsKinesisVideoClient.setRegion(Region.getRegion(region));
            awsKinesisVideoClient.setSignerRegionOverride(region);
            awsKinesisVideoClient.setServiceNameIntern("kinesisvideo");
            return awsKinesisVideoClient;
        }

        private AWSKinesisVideoSignalingClient getAwsKinesisVideoSignalingClient(final String region, final String endpoint) {
            final AWSKinesisVideoSignalingClient client = new AWSKinesisVideoSignalingClient(
                    OndiApplication.getCredentialsProvider().getCredentials());
            client.setRegion(Region.getRegion(region));
            client.setSignerRegionOverride(region);
            client.setServiceNameIntern("kinesisvideo");
            client.setEndpoint(endpoint);
            return client;
        }

        @Override
        protected String doInBackground(Object... objects) {
            final String region = (String) objects[0];
            final String channelName = (String) objects[1];
            final ChannelRole role = (ChannelRole) objects[2];
            AWSKinesisVideoClient awsKinesisVideoClient = null;
            try {
                awsKinesisVideoClient = getAwsKinesisVideoClient(region);
            } catch (Exception e) {
                return "Create client failed with " + e.getLocalizedMessage();
            }

            try {
                DescribeSignalingChannelResult describeSignalingChannelResult = awsKinesisVideoClient.describeSignalingChannel(
                        new DescribeSignalingChannelRequest()
                                .withChannelName(channelName));

                Log.i(TAG, "Channel ARN is " + describeSignalingChannelResult.getChannelInfo().getChannelARN());
                mChannelArn = describeSignalingChannelResult.getChannelInfo().getChannelARN();
            } catch (final ResourceNotFoundException e) {
                if (role.equals(ChannelRole.MASTER)) {
                    try {
                        CreateSignalingChannelResult createSignalingChannelResult = awsKinesisVideoClient.createSignalingChannel(
                                new CreateSignalingChannelRequest()
                                        .withChannelName(channelName));

                        mChannelArn = createSignalingChannelResult.getChannelARN();
                    } catch (Exception ex) {
                        return "Create Signaling Channel failed with Exception " + ex.getLocalizedMessage();
                    }
                } else {
                    return "Signaling Channel " + channelName +" doesn't exist!";
                }
            } catch (Exception ex) {
                return "Describe Signaling Channel failed with Exception " + ex.getLocalizedMessage();
            }

            try {
                GetSignalingChannelEndpointResult getSignalingChannelEndpointResult = awsKinesisVideoClient.getSignalingChannelEndpoint(
                        new GetSignalingChannelEndpointRequest()
                                .withChannelARN(mChannelArn)
                                .withSingleMasterChannelEndpointConfiguration(
                                        new SingleMasterChannelEndpointConfiguration()
                                                .withProtocols("WSS", "HTTPS")
                                                .withRole(role)));

                Log.i(TAG, "Endpoints " + getSignalingChannelEndpointResult.toString());
                mEndpointList.addAll(getSignalingChannelEndpointResult.getResourceEndpointList());
            } catch (Exception e) {
                return "Get Signaling Endpoint failed with Exception " + e.getLocalizedMessage();
            }

            String dataEndpoint = null;
            for (ResourceEndpointListItem endpoint : mEndpointList) {
                if (endpoint.getProtocol().equals("HTTPS")) {
                    dataEndpoint = endpoint.getResourceEndpoint();
                }
            }

            try {
                final AWSKinesisVideoSignalingClient awsKinesisVideoSignalingClient = getAwsKinesisVideoSignalingClient(region, dataEndpoint);
                GetIceServerConfigResult getIceServerConfigResult = awsKinesisVideoSignalingClient.getIceServerConfig(
                        new GetIceServerConfigRequest().withChannelARN(mChannelArn).withClientId(role.name()));
                mIceServerList.addAll(getIceServerConfigResult.getIceServerList());
            } catch (Exception e) {
                return "Get Ice Server Config failed with Exception " + e.getLocalizedMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
//                AlertDialog.Builder diag = new AlertDialog.Builder(mFragment.get().getContext());
//                diag.setPositiveButton("OK", null).setMessage(result).create().show();
                Log.d(TAG, "onPostExecute: OK!");
            }
        }
    }
}
