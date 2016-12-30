package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposa.R;
import com.arraybit.modal.Events;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventsViewHolder> {
    public boolean isItemAnimate;
    ArrayList<Events> alEvents;
    View view;
    Context context;
    String eventType;
    EventClickListener objEventClickListener;

    public EventAdapter(Context context, EventClickListener objEventClickListener, ArrayList<Events> alEvents, String eventType) {
        this.context = context;
        this.alEvents = alEvents;
        this.eventType = eventType;
        this.objEventClickListener= objEventClickListener;
    }

    public void EventsDataChanged(ArrayList<Events> result) {
        alEvents.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_event, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        Events objEvents = alEvents.get(position);
        holder.txtUserName.setText(objEvents.getUserName());

        if (eventType.equals(context.getResources().getString(R.string.todays_event))) {
            if (objEvents.getType() == 0) {
//                holder.txtEventName.setText(context.getResources().getString(R.string.birthday));
                holder.txtEventName.setVisibility(View.GONE);
            } else if (objEvents.getType() == 1) {
                holder.txtEventName.setText(context.getResources().getString(R.string.anniversary));
            }
        } else if (eventType.equals(context.getResources().getString(R.string.upcoming_event))) {
            holder.txtEventName.setText(objEvents.getEventDate());
        }
        if (objEvents.getType() == 0) {
            Picasso.with(holder.ivEvent.getContext()).load(R.drawable.birthday_event).into(holder.ivEvent);
        } else if (objEvents.getType() == 1) {
            Picasso.with(holder.ivEvent.getContext()).load(R.drawable.anniversary_event).into(holder.ivEvent);
        }
    }

    @Override
    public int getItemCount() {
        return alEvents.size();
    }

    class EventsViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserName, txtEventName;
        ImageView ivEvent;
        LinearLayout cvEvents;

        public EventsViewHolder(View itemView) {
            super(itemView);

            cvEvents = (LinearLayout) itemView.findViewById(R.id.cvEvents);
            ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);

            cvEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objEventClickListener.EventOnClick(getAdapterPosition(),alEvents.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface EventClickListener
    {
        void EventOnClick(int position, Events events);
    }
}
