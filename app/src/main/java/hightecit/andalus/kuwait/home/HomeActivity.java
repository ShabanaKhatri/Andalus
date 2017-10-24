package hightecit.andalus.kuwait.home;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import hightecit.andalus.kuwait.BaseActivity;
import hightecit.andalus.kuwait.R;
import hightecit.andalus.kuwait.databinding.ActivityHomeBinding;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        init();
    }

    private void init() {
        setTitle(getString(R.string.home));
        setNavigationView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem search_menu = menu.findItem(R.id.action_logout);
        Drawable drawable_logout = (Drawable) search_menu.getIcon();
        drawable_logout.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        search_menu.setIcon(drawable_logout);
        return super.onCreateOptionsMenu(menu);
    }
}
