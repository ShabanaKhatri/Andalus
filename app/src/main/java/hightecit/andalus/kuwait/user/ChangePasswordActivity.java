package hightecit.andalus.kuwait.user;

import android.os.Bundle;
import android.view.View;

import hightecit.andalus.kuwait.BaseActivity;
import hightecit.andalus.kuwait.R;
import hightecit.andalus.kuwait.databinding.ActivityChangePasswordBinding;
import hightecit.andalus.kuwait.interfaces.OnDialog;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        init();
    }

    private void init() {

        setTitle(getString(R.string.change_password));

        binding.tvSaveChanges.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSaveChanges:
                isValidate();
                break;
            default:
                super.onClick(v);
        }
    }

    public boolean isValidate() {

        binding.edtOldPassword.setText(binding.edtOldPassword.getText().toString().trim());
        binding.edtNewPassword.setText(binding.edtNewPassword.getText().toString().trim());
        binding.edtConfirmPassword.setText(binding.edtConfirmPassword.getText().toString().trim());

        if (binding.edtOldPassword.getText().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_entr_pw));
            binding.edtOldPassword.setSelection(binding.edtOldPassword.getText().length());
            binding.edtOldPassword.requestFocus();
            return false;
        } else if (binding.edtNewPassword.getText().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_enter_new_password));
            binding.edtNewPassword.setSelection(binding.edtNewPassword.getText().length());
            binding.edtNewPassword.requestFocus();
            return false;
        } else if (binding.edtNewPassword.getText().length() < 6 || binding.edtNewPassword.getText().length() > 16) {
            me.showSnackBar(me, getString(R.string.password_length));
            binding.edtNewPassword.setSelection(binding.edtNewPassword.getText().length());
            binding.edtNewPassword.requestFocus();
            return false;
        } else if (binding.edtConfirmPassword.getText().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_enter_confirm_password));
            binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.getText().length());
            binding.edtConfirmPassword.requestFocus();
            return false;
        } else if (!binding.edtConfirmPassword.getText().toString().equals(binding.edtNewPassword.getText().toString())) {
            me.showSnackBar(me, getString(R.string.password_mismatch));
            binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.getText().length());
            binding.edtConfirmPassword.requestFocus();
            return false;
        } else {
            showSimpleDialog(getString(R.string.your_password_changed_success), new OnDialog() {
                @Override
                public void onOk() {
                    finish();
                }
            });
            /*if (Utils.checkConnectivity(me)) {
                Utils.showOnlyProgressDialog(me);
                try {

                    final Uri.Builder reqParam = new Uri.Builder();
                    reqParam.appendQueryParameter("user_id", Utils.getUserId(me))
                            .appendQueryParameter("old_password", binding.edtOldPassword.getText().toString())
                            .appendQueryParameter("new_password", binding.edtNewPassword.getText().toString());

                    PHPService.doPost(Utils.GLOBAL_URL + "changepassword", reqParam, new PHPService.OnApiResponse<String>() {

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

                                if (status.equalsIgnoreCase("success")) {
                                    showSimpleDialog(getString(R.string.your_password_changed_success), new onDialogClick() {
                                        @Override
                                        public void onOk() {
                                            finish();
                                        }
                                    });
                                } else if (status.equalsIgnoreCase("fail") && message.equalsIgnoreCase("current password does not match.")) {
                                    showSimpleDialog(getString(R.string.current_pw_not_match), null);
                                } else {
                                    showSimpleDialog(getString(R.string.something_went_wrong), null);
                                }
                            } catch (
                                    JSONException e1)

                            {
                                e1.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }
        return true;
    }
}
