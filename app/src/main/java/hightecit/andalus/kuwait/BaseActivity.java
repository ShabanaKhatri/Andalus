package hightecit.andalus.kuwait;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import hightecit.andalus.kuwait.common.Utils;
import hightecit.andalus.kuwait.databinding.ActivityBaseBinding;
import hightecit.andalus.kuwait.databinding.DrawerListItemBinding;
import hightecit.andalus.kuwait.databinding.HeaderSidemenuBinding;
import hightecit.andalus.kuwait.enums.Events;
import hightecit.andalus.kuwait.home.HomeActivity;
import hightecit.andalus.kuwait.interfaces.OnConfirmationDialog;
import hightecit.andalus.kuwait.interfaces.OnDialog;
import hightecit.andalus.kuwait.model.DrawerEnum;
import hightecit.andalus.kuwait.model.DrawerItem;
import hightecit.andalus.kuwait.model.Label;
import hightecit.andalus.kuwait.settings.SettingActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ANDROID-10 on 05-01-2017.
 */

public class BaseActivity extends AbstractBaseActivity implements View.OnClickListener {

    public BaseActivity me;
    public ActivityBaseBinding baseBinding;
    private List<DrawerItem> drawerItemArrayList = new ArrayList<>();
    private DrawerRecyclerAdapter adapter;
    public FragmentManager fragmentManager;
    public String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Initialize mint*/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(null/*getString(R.string.regular_font)*/)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Utils.setMyLocale(this, Utils.getLanguageDirection(this).equalsIgnoreCase("ltr") ? "en" : "ar");
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        init();
    }

    /**
     * Initialization of object members
     */
    private void init() {

        /**Initialize me object*/
        me = this;

        url = "android.resource://" + getPackageName() + "/";

        /**Setup window setting*/
        setToolBar(baseBinding.mToolbar);

        /**Setup drawer**/
        setDrawerData();

        /**Initialize of fragmentManager*/
        fragmentManager = getSupportFragmentManager();

        //baseBinding.mSearchView.setHint(Label.getLabel(getString(R.string.search)));

        if (getRedirectClass() == null)
            setRedirectClass(HomeActivity.class);

        EventBus.getDefault().register(this);
    }

    protected void setToolBar(Toolbar mToolBar) {
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getWindow().setWindowAnimations(0);
            setSupportActionBar(mToolBar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            baseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Subscribe
    public void onEvent(Events event) {
        switch (event) {
            case onRefreshSideMenu:
                setDrawerData();
                break;
        }
    }

    @Subscribe
    public void onFinish(Class<?> aClass) {
        if (aClass == this.getClass()) {
            finish();
        }
    }

    /**
     * Setup toolbar and navigation drawer
     */
    protected void setNavigationView() {
        /*setDrawerData();*/
        DrawerLayout drawer = baseBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, baseBinding.mToolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_a);
        baseBinding.drawerLayout.setScrimColor(ContextCompat.getColor(me, android.R.color.transparent));
        baseBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    protected void setDrawerData() {
        drawerItemArrayList.clear();

        DrawerItem drawerItem;

        drawerItem = new DrawerItem();
        drawerItem.setName(Label.getLabel(getString(R.string.setting)));
        drawerItem.setDrawerEnum(DrawerEnum.Settings);
        drawerItem.setImgRes(R.drawable.notification);
        drawerItemArrayList.add(drawerItem);

        if (adapter == null) {
            adapter = new DrawerRecyclerAdapter();
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            baseBinding.drawerRecycler.setLayoutManager(mLayoutManager);
            baseBinding.drawerRecycler.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public Gson getGsonInstance() {
        return new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).create();
    }

    /**
     * This method automatically load the label
     **/
    public void showErrorDialog(@StringRes int msg, @NonNull final EditText editText) {
        /*editText.clearFocus();*/
        showSimpleDialog(Label.getLabel(getString(msg)), new OnDialog() {
            @Override
            public void onOk() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                editText.setSelection(editText.length());
                editText.requestFocus();
            }
        });
    }

    /**
     * This method automatically load the label
     **/
    public void showErrorDialog(@StringRes int msg, @NonNull final View fireOnClick) {
        showSimpleDialog(Label.getLabel(getString(msg)), new OnDialog() {
            @Override
            public void onOk() {
                fireOnClick.performClick();
            }
        });
    }

    /**
     * This method automatically load the label
     **/
    public void showSimpleDialog(@StringRes int res, OnDialog... onDialogs) {
        showSimpleDialog(Label.getLabel(getString(res)), onDialogs.length > 0 ? onDialogs[0] : null);
    }

    /**
     * This method automatically load the label
     **/
    public void showConfirmationDialog(@StringRes int msg, OnConfirmationDialog... onConfirmationDialogs) {
        showConfirmationDialog(Label.getLabel(getString(msg)), onConfirmationDialogs.length > 0 ? onConfirmationDialogs[0] : null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void closeDrawer() {
        baseBinding.drawerLayout.closeDrawers();
    }

    @Override
    public void onClick(View v) {

    }

    private class DrawerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        DrawerListItemBinding itemBinding;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                itemBinding = DrawerListItemBinding.inflate(getLayoutInflater(), parent, false);
                return new ViewHolder(itemBinding);
            } else {
                HeaderSidemenuBinding headerBinding = HeaderSidemenuBinding.inflate(getLayoutInflater(), parent, false);
                return new ViewHolderHeader(headerBinding);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    break;
                case TYPE_ITEM:
                    DrawerItem obj = drawerItemArrayList.get(position - 1);
                    ViewHolder h = (ViewHolder) holder;
                    h.binding.setDraweritem(obj);
                    h.binding.executePendingBindings();

                    h.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = position - 1;
                            Intent intent;
                            switch (pos) {
                                case 0:
                                    intent = new Intent(me, SettingActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                            closeDrawer();
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return drawerItemArrayList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            DrawerListItemBinding binding;

            public ViewHolder(DrawerListItemBinding listItemBinding) {
                super(listItemBinding.getRoot());
                this.binding = listItemBinding;
            }
        }

        class ViewHolderHeader extends RecyclerView.ViewHolder {
            HeaderSidemenuBinding binding;

            public ViewHolderHeader(HeaderSidemenuBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    protected void setTitle(String title) {
        baseBinding.toolbarTitle.setText(title);
    }

    public void setFullScreen() {
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /*if (baseBinding.mSearchView.isSearchOpen()) {
            baseBinding.mSearchView.closeSearch();
            return;
        }*/
        Utils.hideSoftKeyboard(me);
        super.onBackPressed();
    }

    public void showSnackBar(Activity context, String msg) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView().findViewById(R.id.frmContainer), Label.getLabel(msg), Snackbar.LENGTH_LONG);
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.error_color));
        snackbar.show(); // Don’t forget to show!
    }
}