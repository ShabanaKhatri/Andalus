package hightecit.andalus.kuwait.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import hightecit.andalus.kuwait.BaseActivity;
import hightecit.andalus.kuwait.R;
import hightecit.andalus.kuwait.common.Utils;
import hightecit.andalus.kuwait.databinding.ActivityLoginBinding;
import hightecit.andalus.kuwait.home.HomeActivity;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        init();
    }

    protected void init() {

        setFullScreen();

        ImageView view = new ImageView(me);
        view.setBackgroundResource(R.drawable.bg_simple);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayMetrics.widthPixels, (int) (displayMetrics.heightPixels - Utils.convertDpToPixel(32, me)));
        view.setLayoutParams(params);

        binding.btnLogin.setOnClickListener(this);
        binding.tvForgot.setOnClickListener(this);

        binding.edtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    isValidate();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
                isValidate();
                break;

            case R.id.tvForgot:
                startActivity(new Intent(me, ForgotPasswordActivity.class));
                break;

            default:
                super.onClick(v);
        }
    }

    public boolean isValidate() {
        if (binding.edtUsername.getText().toString().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_entr_email));
            binding.edtUsername.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtUsername.getText().toString()).matches()) {
            me.showSnackBar(me, getString(R.string.pls_entr_valid_email));
            binding.edtUsername.requestFocus();
        } else if (binding.edtPass.getText().toString().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_entr_password));
            binding.edtPass.requestFocus();
        } else {
//            Utils.setRegistered(me, true);
            Utils.setUserId(me, "1  ");
            startActivity(new Intent(me, HomeActivity.class));
            Utils.setEmail(me, binding.edtUsername.getText().toString());
            ActivityCompat.finishAffinity(me);

            // userLogin();
        }
        return true;
    }

    private void userLogin() {

        if (Utils.checkConnectivity(me)) {
            Utils.showOnlyProgressDialog(me);
            try {

                final Uri.Builder reqParam = new Uri.Builder();
                reqParam.appendQueryParameter("device_type", "a")
                        .appendQueryParameter("device_imei", Utils.getIMEI(me))
                        .appendQueryParameter("device_token", Utils.getGCMToken(this))
                        .appendQueryParameter("password", binding.edtPass.getText().toString().trim())
                        .appendQueryParameter("email", binding.edtUsername.getText().toString().trim());

              /*  PHPService.doPost(Url.GLOBAL_URL + "login", reqParam, new PHPService.OnApiResponse<String>() {

                    @Override
                    public void onApiResponse(Exception e, String response) {
                        Utils.dismissDialog();

                        if (e != null) {
                            e.printStackTrace();

                            showSimpleDialog(getString(R.string.something_went_wrong), new onDialogClick() {
                                @Override
                                public void onOk() {
                                }
                            });
                            return;
                        }

                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            String message = object.getString("message");

                            if (status.equalsIgnoreCase("success") && message.equalsIgnoreCase("logged in successfully.")) {
                                JSONArray objDevice = new JSONArray(object.getString("data"));

                                if (objDevice.length() != 0) {
                                    Utils.setUserId(me, objDevice.getJSONObject(0).getString("user_id"));
                                    Utils.setUserType(me, objDevice.getJSONObject(0).getString("user_type"));
                                } else {
                                    Utils.setUserId(me, "");
                                }
                                Utils.setRegistered(me, true);

                                startActivity(new Intent(me, HomeActivity.class));
                                ActivityCompat.finishAffinity(me);
                            } else {
                                if (status.equalsIgnoreCase("fail") && message.equalsIgnoreCase("invalid login details.")) {
                                    showSimpleDialog(getString(R.string.invalid_login_credentials), new onDialogClick() {
                                        @Override
                                        public void onOk() {
                                            binding.edtUsername.setText("");
                                            binding.edtPass.setText("");
                                            binding.edtUsername.requestFocus();
                                        }
                                    });
                                } else {
                                    showSimpleDialog(getString(R.string.something_went_wrong), new onDialogClick() {
                                        @Override
                                        public void onOk() {
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}