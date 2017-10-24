package hightecit.andalus.kuwait.common;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import hightecit.andalus.kuwait.BaseActivity;
import hightecit.andalus.kuwait.R;


public class NoInternetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_no_internet);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}