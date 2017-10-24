package hightecit.andalus.kuwait.user;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import hightecit.andalus.kuwait.BaseActivity;
import hightecit.andalus.kuwait.R;
import hightecit.andalus.kuwait.databinding.ActivityForgotPasswordBinding;
import hightecit.andalus.kuwait.interfaces.OnDialog;

public class ForgotPasswordActivity extends BaseActivity {

    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        init();
    }

    protected void init() {

        setFullScreen();

        binding.tvSubmit.setOnClickListener(this);

        binding.edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    isValidate();
                }
                return false;
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        baseBinding.drawerLayout.getLayoutParams().width = displayMetrics.widthPixels;
        baseBinding.drawerLayout.getLayoutParams().height = displayMetrics.heightPixels;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvSubmit:
                isValidate();
                break;
            default:
                super.onClick(v);
        }
    }

    public boolean isValidate() {
        if (binding.edtEmail.getText().toString().trim().length() == 0) {
            me.showSnackBar(me, getString(R.string.pls_entr_email));
            binding.edtEmail.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            me.showSnackBar(me, getString(R.string.pls_entr_valid_email));
            binding.edtEmail.requestFocus();
        } else {

            showSimpleDialog(getString(R.string.password_reset_link), new OnDialog() {

                @Override
                public void onOk() {

                }
            });
        }
        return true;
    }
}