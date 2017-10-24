package hightecit.andalus.kuwait;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by ANDROID-10 on 04-06-2016.
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

    public BaseActivity me;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = (BaseActivity) getActivity();
    }

    @CallSuper
    protected void init(View rootView) {

    }

    @Override
    public void onClick(View v) {
    }
}