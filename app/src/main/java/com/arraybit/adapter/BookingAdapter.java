package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.abposa.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.BookingMaster;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingMasterViewHolder> {
    public boolean isItemAnimate = false;
    Context context;
    LayoutInflater inflater;
    View ConvertView;
    int previousPosition;
    ArrayList<BookingMaster> alBookingMaster;
    BookingOnClickListener objBookingOnClickListener;
    int position;
    Date toDate, toTime, currentTime, currentDate;
    String today, strCurrentTime;
    SimpleDateFormat sdfDate = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfTime = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US);

    public BookingAdapter(Context context, ArrayList<BookingMaster> result) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.alBookingMaster = result;
        this.objBookingOnClickListener = (BookingOnClickListener) context;
    }

    @Override
    public BookingMasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConvertView = inflater.inflate(R.layout.row_booking, parent, false);
        return new BookingMasterViewHolder(ConvertView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BookingMasterViewHolder holder, int position) {
        BookingMaster objBookingMaster = alBookingMaster.get(position);

        holder.txtDate.setText(String.valueOf(objBookingMaster.getToDate()));
        holder.txtTime.setText(String.valueOf(objBookingMaster.getFromTime()) + " To " + String.valueOf(objBookingMaster.getToTime()));
        holder.txtPersonName.setText(objBookingMaster.getBookingPersonName());

        if (objBookingMaster.getEmail() != null && !objBookingMaster.getEmail().equals("")) {
            holder.txtEmail.setVisibility(View.VISIBLE);
            holder.txtEmail.setText(objBookingMaster.getEmail());
        } else if (objBookingMaster.getPhone() != null && !objBookingMaster.getPhone().equals("")) {
            holder.txtEmail.setVisibility(View.VISIBLE);
            holder.txtEmail.setText(objBookingMaster.getPhone());
        } else {
            holder.txtEmail.setVisibility(View.GONE);
        }

        if (objBookingMaster.getToDate() != null) {
            Calendar calendar = Calendar.getInstance();
            try {
                toDate = sdfDate.parse(objBookingMaster.getToDate());
                toTime = sdfTime.parse(objBookingMaster.getToTime());
                today = sdfDate.format(new Date());
                currentDate = sdfDate.parse(today);

                if (objBookingMaster.getBookingStatus() != Globals.BookingStatus.Cancelled.getValue()) {
                    if (toDate.compareTo(currentDate) < 0) {
                        holder.ibCancelBooking.setVisibility(View.GONE);
                    } else {
                        strCurrentTime = sdfTime.format(calendar.getTime());
                        currentTime = sdfTime.parse(strCurrentTime);
                        if (toDate.compareTo(currentDate) > 0) {
                            holder.ibCancelBooking.setVisibility(View.VISIBLE);
                        } else {
                            if (toTime.getTime() > currentTime.getTime() && !objBookingMaster.getIsPreOrder()) {
                                holder.ibCancelBooking.setVisibility(View.VISIBLE);
                            } else {
                                holder.ibCancelBooking.setVisibility(View.GONE);
                            }
                        }
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.New.getValue()) {
            holder.txtBookingStatus.setText("Pending");
        } else if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.Modified.getValue()) {
            holder.txtBookingStatus.setText(Globals.BookingStatus.Modified.toString());
        } else if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.Confirmed.getValue()) {
            holder.txtBookingStatus.setText(Globals.BookingStatus.Confirmed.toString());
        } else if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.Cancelled.getValue()) {
            holder.txtBookingStatus.setText(Globals.BookingStatus.Cancelled.toString());
            holder.ibCancelBooking.setVisibility(View.GONE);
        }

        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return this.alBookingMaster.size();
    }

    public void BookingDataChanged(ArrayList<BookingMaster> result) {
        alBookingMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public void BookingDataChanged(BookingMaster objBookingMaster) {
        alBookingMaster.add(0, objBookingMaster);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public void UpdateBookingStatus(int position) {
        alBookingMaster.get(position).setBookingStatus((short) Globals.BookingStatus.Cancelled.getValue());
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public interface BookingOnClickListener {
        void CancelClickListener(BookingMaster objBookingMaster, int position);
    }

    class BookingMasterViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtTime;
        TextView txtPersonName;
        TextView txtBookingStatus, txtEmail;
        ImageButton ibCancelBooking;

        public BookingMasterViewHolder(View view) {
            super(view);

            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtPersonName = (TextView) view.findViewById(R.id.txtPersonName);
            txtEmail = (TextView) view.findViewById(R.id.txtEmail);
            txtBookingStatus = (TextView) view.findViewById(R.id.txtStatus);
            ibCancelBooking = (ImageButton) view.findViewById(R.id.ibCancelBooking);

            Drawable btnCancleIcon;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                btnCancleIcon = VectorDrawableCompat.create(context.getResources(), R.drawable.cancel, context.getTheme());
            } else {
                btnCancleIcon = context.getResources().getDrawable(R.drawable.cancel, context.getTheme());
            }
            ibCancelBooking.setImageDrawable(btnCancleIcon);

            ibCancelBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        objBookingOnClickListener.CancelClickListener(alBookingMaster.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}

