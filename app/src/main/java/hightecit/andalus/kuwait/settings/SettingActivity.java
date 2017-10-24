package hightecit.andalus.kuwait.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import hightecit.andalus.kuwait.BaseActivity;
import hightecit.andalus.kuwait.R;
import hightecit.andalus.kuwait.common.Utils;
import hightecit.andalus.kuwait.databinding.ActivitySettingBinding;
import hightecit.andalus.kuwait.databinding.RowSettingListItemsBinding;
import hightecit.andalus.kuwait.user.ChangePasswordActivity;
import hightecit.andalus.kuwait.user.MyProfileActivity;

public class SettingActivity extends BaseActivity {

    ArrayList<String> settingList;
    private ActivitySettingBinding binding;
    private MySettingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        init();
    }

    private void init() {

        setTitle(getString(R.string.setting));

        settingList = new ArrayList<>();
        settingList.add(getString(R.string.my_profile));
        settingList.add(getString(R.string.change_password));
        settingList.add(getString(R.string.push_notification));

        binding.rvSetting.setHasFixedSize(true);
        binding.rvSetting.setLayoutManager(new LinearLayoutManager(me));

        mAdapter = new MySettingAdapter();
        binding.rvSetting.setAdapter(mAdapter);


        // getPushStatus();

    }

    public class MySettingAdapter extends RecyclerView.Adapter<MySettingAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RowSettingListItemsBinding itemBinding = DataBindingUtil.inflate(me.getLayoutInflater(), R.layout.row_setting_list_items, parent, false);
            return new ViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.itemBinding.executePendingBindings();

            if (position == 2) {
                holder.itemBinding.tglBtnNotification.setVisibility(View.VISIBLE);
                holder.itemBinding.imgSettingNext.setVisibility(View.GONE);
            }

            holder.itemBinding.tglBtnNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Utils.setPushStatus(me, b);
                }
            });

            if (Utils.getPushStatus(me)) {
                holder.itemBinding.tglBtnNotification.setChecked(Utils.getPushStatus(me));
            } else {
                holder.itemBinding.tglBtnNotification.setChecked(false);
            }

            holder.itemBinding.tglBtnNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        // setPushStatus("1");
                    } else {
                        // setPushStatus("0");
                    }

                }
            });

            holder.itemBinding.tvSettingItem.setText(settingList.get(position));

            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Utils.getUserId(me).isEmpty()) {
                        switch (position) {
                            case 0://my_profile
                                 startActivity(new Intent(SettingActivity.this, MyProfileActivity.class));
                                break;
                            case 1://Change Password
                                 startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class));
                                break;
                            case 2://saved address
                                break;

                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return settingList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private RowSettingListItemsBinding itemBinding;
            private View row;

            ViewHolder(RowSettingListItemsBinding itemBinding) {
                super(itemBinding.getRoot());
                row = itemBinding.getRoot();
                this.itemBinding = itemBinding;
            }
        }
    }

   /* private void getPushStatus() {

        if (Utils.checkConnectivity(me)) {
            Utils.showOnlyProgressDialog(me);
            try {

                final Uri.Builder reqParam = new Uri.Builder();
                reqParam.appendQueryParameter("device_type", "a")
                        .appendQueryParameter("device_imei", Utils.getIMEI(me))
                        .appendQueryParameter("device_token", Utils.getGCMToken(this))
                        .appendQueryParameter("user_id", Utils.getUserId(me));

                PHPService.doPost(Utils.GLOBAL_URL + "getpushstatus", reqParam, new PHPService.OnApiResponse<String>() {

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
                                JSONObject objDevice = new JSONObject(object.getString("data"));

                                String push_status = objDevice.optString("push_status");

                                if (push_status.equalsIgnoreCase("1")) {
                                    Utils.setPushStatus(me, true);
                                } else {
                                    Utils.setPushStatus(me, false);
                                }

                                mAdapter = new MySettingAdapter();
                                binding.rvSetting.setAdapter(mAdapter);
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
    }

    private void setPushStatus(final String pushStatus) {

        if (Utils.checkConnectivity(me)) {
            Utils.showOnlyProgressDialog(me);
            try {

                final Uri.Builder reqParam = new Uri.Builder();
                reqParam.appendQueryParameter("device_type", "a")
                        .appendQueryParameter("device_imei", Utils.getIMEI(me))
                        .appendQueryParameter("device_token", Utils.getGCMToken(this))
                        .appendQueryParameter("user_id", Utils.getUserId(me))
                        .appendQueryParameter("push_status", pushStatus);

                PHPService.doPost(Utils.GLOBAL_URL + "updatepushstatus", reqParam, new PHPService.OnApiResponse<String>() {

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
                            String status1 = object.getString("status");
                            String message = object.getString("message");

                            if (status1.equalsIgnoreCase("success")) {
                                JSONObject objDevice = new JSONObject(object.getString("data"));

                                String push_status = objDevice.optString("push_status");

                                if (pushStatus.equalsIgnoreCase("1")) {
                                    Utils.setPushStatus(me, true);
                                } else {
                                    Utils.setPushStatus(me, false);
                                }

                                mAdapter.notifyDataSetChanged();

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
}
