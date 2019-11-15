//package com.oshop.hm.services;
//
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
//import com.oshop.hm.constant.ConstantValues;
//import com.oshop.hm.network.StartAppRequests;
//
//import static com.facebook.FacebookSdk.getApplicationContext;
//
//
///**
// * FirebaseInstanceIdService Gets FCM instance ID token from Firebase Cloud Messaging Server
// */
//
//public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//
//
//    //*********** Called whenever the Token is Generated or Refreshed ********//
//
//    @Override
//    public void onTokenRefresh() {
//        super.onTokenRefresh();
//
//        if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("fcm")) {
//
//            StartAppRequests.RegisterDeviceForFCM(getApplicationContext());
//
//        }
//
//    }
//
//}
