package com.arraybit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.abposa.R;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Switch;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyAccountViewHolder> {

    Context context;
    ArrayList<String> alString;
    ArrayList<Boolean> isSwitch;
    LayoutInflater layoutInflater;
    View view;
    OptionClickListener objOptionClickListener;
    SharePreferenceManage objSharePreferenceManage;
    String strOnOff;
    Activity activity;

    public MyAccountAdapter(ArrayList<String> result, ArrayList<Boolean> isSwitch, Context context, Activity activity, OptionClickListener objOptionClickListener) {
        this.alString = result;
        this.isSwitch = isSwitch;
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context);
        this.objOptionClickListener = objOptionClickListener;
    }

    @Override
    public MyAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_myaccount, parent, false);
        return new MyAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAccountViewHolder holder, int position) {
        holder.txtTitle.setText(alString.get(position));
        if (isSwitch.get(position)) {
            holder.sPushNotificationOnOff.setVisibility(View.VISIBLE);
            objSharePreferenceManage = new SharePreferenceManage();

            if (objSharePreferenceManage.GetPreference("NotificationSettingPreference", "Push", context) == null) {
                objSharePreferenceManage.CreatePreference("NotificationSettingPreference", "Push", "true", context);
                holder.sPushNotificationOnOff.setChecked(true);
                Globals.EnableBroadCastReceiver(activity);
            } else {
                strOnOff = objSharePreferenceManage.GetPreference("NotificationSettingPreference", "Push", context);
                if (strOnOff.equals("false")) {
                    holder.sPushNotificationOnOff.setChecked(false);
                    Globals.DisableBroadCastReceiver(activity);
                } else {
                    holder.sPushNotificationOnOff.setChecked(true);
                    Globals.EnableBroadCastReceiver(activity);
                }
            }
        } else {
            holder.sPushNotificationOnOff.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return alString.size();
    }

//    public void SetSwitch(int position)
//    {
//        sPushNotificationOnOff.setChecked(true);
//    }

    public interface OptionClickListener {
        void OptionClick(int id);

        void NotificationOnOff(int position, Switch aSwitch);
    }

    public class MyAccountViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        CardView cvOptions;
        LinearLayout titleLayout;
        Switch sPushNotificationOnOff;

        public MyAccountViewHolder(View itemView) {
            super(itemView);

            titleLayout = (LinearLayout) itemView.findViewById(R.id.titleLayout);
            cvOptions = (CardView) itemView.findViewById(R.id.cvOptions);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            sPushNotificationOnOff = (Switch) itemView.findViewById(R.id.sPushNotificationOnOff);

//            if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 19) {
//                titleLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.card_view_with_border));
//            }
            cvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objOptionClickListener.OptionClick(getAdapterPosition());

                }
            });

            sPushNotificationOnOff.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch aSwitch, boolean b) {
                    objOptionClickListener.NotificationOnOff(getAdapterPosition(), aSwitch);
                }
            });

        }
    }
}
