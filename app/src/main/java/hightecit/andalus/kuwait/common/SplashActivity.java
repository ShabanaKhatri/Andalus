package hightecit.andalus.kuwait.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import hightecit.andalus.kuwait.BaseActivity;
import hightecit.andalus.kuwait.databinding.ActivitySplashBinding;
import hightecit.andalus.kuwait.home.HomeActivity;
import hightecit.andalus.kuwait.user.LoginActivity;

import static hightecit.andalus.kuwait.fcm.MyFirebaseInstanceIDService.ACTION_FCM_TOKEN_GENERATED;

//Server key give to php team
// AAAAPSxhZt8:APA91bH6ztEndxcIz_8ORdmvGxN_8eSJg_qMt7IU9mOgn7svk3yxJMO0_1MoSjLHZZjGKeICoL_GDqym5mnes8hh2gMPJjhegZ47fWkWtA0RXCjk-FsCNIxcnYEoaqD_NsuRvTGrf8K-

//legacy server key
//        AIzaSyD9YPVT4MGa6_g7I0dSUpB4Gbmx2cAr2o8


public class SplashActivity extends BaseActivity {

    public static final byte READ_PHONE_STATE = 101;
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        init();
    }

    private void init() {
        setFullScreen();

        if (!Utils.getIsFirstTime(me)) {
            if (Utils.checkConnectivity(me)) {
                if (Utils.getGCMToken(me).isEmpty()) {
                    LocalBroadcastManager.getInstance(me).registerReceiver(FCMTokenGenerated, new IntentFilter(ACTION_FCM_TOKEN_GENERATED));
                } else {
                    // deviceRegistration();
                }
            }
        } else {
            Handler handler = new Handler();
            handler.postDelayed(r, 3000);
        }
    }

    final Runnable r = new Runnable() {
        public void run() {
            if (Utils.getUserId(me).isEmpty()) {
                startActivity(new Intent(me, LoginActivity.class));
                finish();
            } else {
                startActivity(new Intent(me, HomeActivity.class));
                finish();
            }
        }
    };

    private BroadcastReceiver FCMTokenGenerated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //deviceRegistration();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //deviceRegistration();
                } else {
                    ActivityCompat.requestPermissions(me, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                }
                break;
        }
    }

  /*  private void deviceRegistration() {

        if (Utils.checkPermission(me, android.Manifest.permission.READ_PHONE_STATE)) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.customProgress.setVisibility(View.VISIBLE);
                }
            });

            if (Utils.checkConnectivity(me)) {
                try {

                    String imei = Utils.generateIMEI(me);
                    Utils.setIMEI(me, imei);

                    final Uri.Builder reqParam = new Uri.Builder();
                    reqParam.appendQueryParameter("device_type", "a")
                            .appendQueryParameter("device_imei", Utils.getIMEI(me))
                            .appendQueryParameter("device_token", Utils.getGCMToken(this));

                    PHPService.doPost(""+ "registerdevice", reqParam, new PHPService.OnApiResponse<String>() {

                        @Override
                        public void onApiResponse(Exception e, String response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.customProgress.setVisibility(View.GONE);
                                }
                            });

                            if (e != null) {
                                e.printStackTrace();

                                showSimpleDialog(getString(R.string.something_went_wrong), new onDialogClick() {
                                    @Override
                                    public void onOk() {
                                        ActivityCompat.finishAffinity(me);
                                    }
                                });
                                return;
                            }

                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if (status.equalsIgnoreCase("success") && message.equalsIgnoreCase("device has been registered successfully.")) {
                                    JSONObject objDevice = new JSONObject(object.getString("data"));

                                    if (objDevice.length() != 0) {
                                        Utils.setDeviceId(me, objDevice.getString("device_id"));
                                    } else {
                                        Utils.setDeviceId(me, "");
                                    }

                                    Utils.setIsFirstTime(me, true);

                                    startActivity(new Intent(me, LoginActivity.class));
                                    finish();

                                } else {
                                    showSimpleDialog(getString(R.string.something_went_wrong), new onDialogClick() {
                                        @Override
                                        public void onOk() {
                                            ActivityCompat.finishAffinity(me);
                                        }
                                    });
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            ActivityCompat.requestPermissions(me, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }
    }*/
}