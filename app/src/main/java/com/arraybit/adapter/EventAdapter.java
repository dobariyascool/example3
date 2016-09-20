package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
    private LayoutInflater inflater;

    public EventAdapter(Context context, ArrayList<Events> alEvents, String eventType) {
        this.context = context;
        this.alEvents = alEvents;
        inflater = LayoutInflater.from(context);
        this.eventType = eventType;
    }

    public void EventsDataChanged(ArrayList<Events> result) {
        alEvents.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (eventType.equals(context.getResources().getString(R.string.todays_event))) {
            view = LayoutInflater.from(context).inflate(R.layout.row_todays_event, parent, false);
//        } else if (eventType.equals(context.getResources().getString(R.string.upcoming_event))) {
//            view = LayoutInflater.from(context).inflate(R.layout.row_upcoming_event, parent, false);
//        }
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        Events objEvents = alEvents.get(position);
        holder.txtUserName.setText(objEvents.getUserName());

        if (eventType.equals(context.getResources().getString(R.string.todays_event))) {
            if (objEvents.getType() == 0) {
                holder.txtEventName.setText(context.getResources().getString(R.string.birthday));
            } else if (objEvents.getType() == 1) {
                holder.txtEventName.setText(context.getResources().getString(R.string.anniversary));
            }
        } else if (eventType.equals(context.getResources().getString(R.string.upcoming_event))) {
            holder.txtEventName.setText(objEvents.getEventDate());
        }
        if (objEvents.getType() == 0) {
            Picasso.with(holder.ivEvent.getContext()).load(R.drawable.birthday_event).into(holder.ivEvent);
//            holder.ivEvent.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.birthday_event));
        } else if (objEvents.getType() == 1) {
            Picasso.with(holder.ivEvent.getContext()).load(R.drawable.anniversary_event).into(holder.ivEvent);
//            holder.ivEvent.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.anniversary_event));
        }
    }

    @Override
    public int getItemCount() {
        return alEvents.size();
    }

    class EventsViewHolder extends RecyclerView.ViewHolder {

        TextView txtEventDate, txtUserName, txtEventName;
        ImageView ivEvent;
        LinearLayout cvEvents;

        public EventsViewHolder(View itemView) {
            super(itemView);

            cvEvents = (LinearLayout) itemView.findViewById(R.id.cvEvents);
            ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
//            if (eventType.equals(context.getResources().getString(R.string.todays_event))) {
                txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);
//            } else if (eventType.equals(context.getResources().getString(R.string.upcoming_event))) {
//                txtEventDate = (TextView) itemView.findViewById(R.id.txtEventDate);
//            }
        }
    }
}
