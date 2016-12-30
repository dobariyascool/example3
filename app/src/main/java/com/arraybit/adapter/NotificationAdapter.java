package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.abposa.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.NotificationMaster;
import com.bumptech.glide.Glide;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    public boolean isItemAnimate;
    Context context;
    ArrayList<NotificationMaster> alNotificationMaster;
    LayoutInflater layoutInflater;
    View view;
    int previousPosition;
    OnClickListener objOnClickListener;

    // Constructor
    public NotificationAdapter(Context context, ArrayList<NotificationMaster> result, OnClickListener objOnClickListener) {
        this.context = context;
        this.alNotificationMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.objOnClickListener = objOnClickListener;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationMaster objNotificationMaster = alNotificationMaster.get(position);
        holder.txtNotificationTitle.setText(objNotificationMaster.getNotificationTitle());
        holder.txtNotificationTime.setText(objNotificationMaster.getNotificationDateTime());
        if (objNotificationMaster.getNotificationText() == null || objNotificationMaster.getNotificationText().equals("")) {
            holder.txtNotificationText.setVisibility(View.GONE);
        } else {
            holder.txtNotificationText.setText(objNotificationMaster.getNotificationText());
        }

        if (objNotificationMaster.getNotificationImageName() != null && !objNotificationMaster.getNotificationImageName().equals("")) {
            holder.ivNotificationImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(objNotificationMaster.getNotificationImageName()).asBitmap().into(holder.ivNotificationImage);
        } else {
            holder.ivNotificationImage.setVisibility(View.GONE);
        }

        //holder animation
        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alNotificationMaster.size();
    }

    public void NotificationDataRemove(int position) {
        isItemAnimate = true;
        alNotificationMaster.remove(position);
        notifyItemRemoved(position);
    }

    public void NotificationDataChanged(ArrayList<NotificationMaster> result) {
        alNotificationMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void OnRemoveClick(NotificationMaster objNotificationMaster, int position);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView txtNotificationTitle, txtNotificationTime, txtNotificationText;
        CardView cvItem;
        ImageView ivClear, ivNotificationImage;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            txtNotificationTitle = (TextView) itemView.findViewById(R.id.txtNotificationTitle);
            txtNotificationTime = (TextView) itemView.findViewById(R.id.txtNotificationTime);
            txtNotificationText = (TextView) itemView.findViewById(R.id.txtNotificationText);
            ivClear = (ImageView) itemView.findViewById(R.id.ivClear);
            ivNotificationImage = (ImageView) itemView.findViewById(R.id.ivNotificationImage);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            ivClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    objOnClickListener.OnRemoveClick(alNotificationMaster.get(getAdapterPosition()), getAdapterPosition());
//                    alNotificationMaster.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }
}
