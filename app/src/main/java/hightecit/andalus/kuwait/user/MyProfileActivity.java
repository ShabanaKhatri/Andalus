package hightecit.andalus.kuwait.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import hightecit.andalus.kuwait.databinding.ActivityMyProfileBinding;
import hightecit.andalus.kuwait.interfaces.OnConfirmationDialog;
import hightecit.andalus.kuwait.interfaces.OnDialog;

public class MyProfileActivity extends BaseActivity {

    ActivityMyProfileBinding binding;
    /*Remove this static values while adding webservice*/
    String name = "Test", phone = "8866558866";
    TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        init();
    }

    protected void init() {
        setTitle(getString(R.string.my_profile));
        binding.tvEmail.setText(Utils.getEmail(me));
        binding.edtFullName.setText(name);
        binding.edtContactNumber.setText(phone);
        ImageView view = new ImageView(me);
        view.setBackgroundResource(R.drawable.bg_simple);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayMetrics.widthPixels, (int) (displayMetrics.heightPixels - Utils.convertDpToPixel(100, me)));
        view.setLayoutParams(params);
        binding.frmMain.addView(view, 0);

        binding.tvSaveChanges.setOnClickListener(this);

        binding.edtContactNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    isValidate();
                }
                return false;
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeDisability();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        binding.edtFullName.addTextChangedListener(textWatcher);
        binding.edtContactNumber.addTextChangedListener(textWatcher);

        //getProfileData();
    }

   /* private void getProfileData() {
        if (Utils.checkConnectivity(me)) {
            try {
                Utils.showOnlyProgressDialog(me);

                final Uri.Builder reqParam = new Uri.Builder();
                reqParam.appendQueryParameter("user_id", Utils.getUserId(me));

                PHPService.doGet(Utils.GLOBAL_URL + "profiledata", reqParam, new PHPService.OnApiResponse<String>() {

                    @Override
                    public void onApiResponse(Exception e, String response) {
                        Utils.dismissDialog();

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

                            if (status.equalsIgnoreCase("success") && message.equalsIgnoreCase("Record found.")) {
                                JSONObject objDevice = new JSONObject(object.getString("data"));

                                if (objDevice.length() != 0) {

                                    binding.tvEmail.setText(objDevice.getString("email"));
                                    binding.edtFullName.setText(objDevice.getString("fullname"));
                                    binding.edtContactNumber.setText(objDevice.getString("contact_number"));

                                    Utils.setCountryCode(me, objDevice.getString("country_code"));

                                    binding.edtFullName.setSelection(binding.edtFullName.getText().length());
                                    binding.edtContactNumber.setSelection(binding.edtContactNumber.getText().length());

                                    name = objDevice.getString("fullname");
                                    phone = objDevice.getString("contact_number");

                                    binding.edtFullName.addTextChangedListener(textWatcher);
                                    binding.edtContactNumber.addTextChangedListener(textWatcher);
                                }
                            } else {
                                showSimpleDialog(getString(R.string.something_went_wrong), new onDialogClick() {
                                    @Override
                                    public void onOk() {
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
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSaveChanges:
                isValidate();
                break;

            case R.id.ivBack:
                checkDataBlank();
                break;

            default:
                super.onClick(v);
        }
    }

    private void changeDisability() {
        if (name.equals(binding.edtFullName.getText().toString()) && phone.equals(binding.edtContactNumber.getText().toString())) {
            binding.tvSaveChanges.setEnabled(false);
        } else {
            binding.tvSaveChanges.setEnabled(true);
        }
    }

    private void checkDataBlank() {
        if (!name.equals(binding.edtFullName.getText().toString()) && phone.equals(binding.edtContactNumber.getText().toString())) {
            showConfirmationDialog(getString(R.string.are_you_sure_want_back), new OnConfirmationDialog() {
                @Override
                public void onYes() {
                    finish();
                }

                @Override
                public void onNo() {

                }
            });
        } else {
            finish();
        }

    }

    public boolean isValidate() {

        if (binding.edtFullName.getText().toString().trim().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_entr_full_name));
            binding.edtFullName.requestFocus();
            return false;
        } else if (binding.edtContactNumber.getText().toString().trim().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_entr_contact_number));
            binding.edtContactNumber.requestFocus();
            return false;
        } else if (binding.edtContactNumber.getText().toString().trim().length() < 7 || binding.edtContactNumber.getText().toString().trim().length() > 15) {
            me.showSnackBar(me, getString(R.string.contact_number_must_min_7_max_15));
            binding.edtContactNumber.requestFocus();
            return false;
        } else {
            showSimpleDialog(getString(R.string.profile_updated_successfully), new OnDialog() {
                @Override
                public void onOk() {
                    finish();
                }
            });
            // updateProfileData();
        }
        return true;
    }

   /* private void updateProfileData() {
        if (Utils.checkConnectivity(me)) {
            try {
                Utils.showOnlyProgressDialog(me);

                final Uri.Builder reqParam = new Uri.Builder();
                reqParam.appendQueryParameter("user_id", Utils.getUserId(me))
                        .appendQueryParameter("fullname", binding.edtFullName.getText().toString())
                        .appendQueryParameter("country_code", Utils.getCountryCode(me))
                        .appendQueryParameter("contact_number", binding.edtContactNumber.getText().toString());


                PHPService.doPost(Utils.GLOBAL_URL + "updateprofile", reqParam, new PHPService.OnApiResponse<String>() {

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

                            if (status.equalsIgnoreCase("success") && message.equalsIgnoreCase("profile has been updated successfully.")) {
                                JSONObject objDevice = new JSONObject(object.getString("data"));

                                if (objDevice.length() != 0) {

                                    if (objDevice.length() != 0) {
                                        Utils.setUserId(me, objDevice.optString("user_id"));
                                        Utils.setFullName(me, objDevice.optString("fullname"));
                                        Utils.setCountryCode(me, objDevice.optString("country_code"));
                                        Utils.setContactNumber(me, objDevice.optString("contact_number"));

                                        showSimpleDialog(getString(R.string.profile_updated_successfully), new onDialogClick() {
                                            @Override
                                            public void onOk() {
                                                finish();
                                            }
                                        });
                                    }
                                }
                            } else {
                                showSimpleDialog(getString(R.string.something_went_wrong), new onDialogClick() {
                                    @Override
                                    public void onOk() {
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
    }*/

    @Override
    public void onBackPressed() {
        checkDataBlank();
    }
}