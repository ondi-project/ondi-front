package com.ondi.android_ondi.Signaling;


import com.ondi.android_ondi.Signaling.Model.Event;

public interface Signaling {

    void onSdpOffer(Event event);

    void onSdpAnswer(Event event);

    void onIceCandidate(Event event);

    void onError(Event event);

    void onException(Exception e);
}
