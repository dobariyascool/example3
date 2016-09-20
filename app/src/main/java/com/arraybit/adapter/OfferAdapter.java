package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposa.OfferDetailActivity;
import com.arraybit.abposa.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.OfferMaster;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    public boolean isItemAnimate = false;
    View view;
    Context context;
    ArrayList<OfferMaster> alOfferMaster;
    int previousPosition;
    SimpleDateFormat sdfDate = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("d MMM", Locale.US);
    int i = 0;

    public OfferAdapter(Context context, ArrayList<OfferMaster> result) {
        this.context = context;
        alOfferMaster = result;
    }


    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final OfferViewHolder holder, int position) {
        OfferMaster objOfferMaster = alOfferMaster.get(position);
        holder.txtOfferTitle.setText(objOfferMaster.getOfferTitle());
        if (objOfferMaster.getOfferContent().equals("null") || objOfferMaster.getOfferContent().equals("")) {
            holder.txtOfferContent.setVisibility(View.GONE);
        } else {
            holder.txtOfferContent.setVisibility(View.VISIBLE);
            holder.txtOfferContent.setText(objOfferMaster.getOfferContent());
        }
        if (objOfferMaster.getToDate() == null || objOfferMaster.getToDate().equals("")) {
            holder.dateLayout.setVisibility(View.GONE);
        } else {
            holder.dateLayout.setVisibility(View.VISIBLE);
            try {
                Date date = sdfDate.parse(objOfferMaster.getToDate());
                String str = sdfDateFormat.format(date);
                holder.txtOfferExpiredDate.setText(str);
            } catch (ParseException e) {
                holder.dateLayout.setVisibility(View.GONE);
            }
        }
        if (!objOfferMaster.getMd_ImagePhysicalName().equals("")) {
            Picasso.with(holder.ivOffer.getContext()).load(objOfferMaster.getMd_ImagePhysicalName()).into(holder.ivOffer);
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
        return alOfferMaster.size();
    }

    public void OffersDataChanged(ArrayList<OfferMaster> result) {
        alOfferMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {

        TextView txtOfferTitle, txtOfferContent, txtOfferExpiredDate;
        ImageView ivOffer, ivOfferDetail;
        CardView cvOffer;
        LinearLayout titleLayout, dateLayout;

        public OfferViewHolder(View itemView) {
            super(itemView);

            titleLayout = (LinearLayout) itemView.findViewById(R.id.titleLayout);
            dateLayout = (LinearLayout) itemView.findViewById(R.id.dateLayout);

            cvOffer = (CardView) itemView.findViewById(R.id.cvOffer);

            ivOffer = (ImageView) itemView.findViewById(R.id.ivOffer);

            txtOfferTitle = (TextView) itemView.findViewById(R.id.txtOfferTitle);
            txtOfferContent = (TextView) itemView.findViewById(R.id.txtOfferContent);
            txtOfferExpiredDate = (TextView) itemView.findViewById(R.id.txtOfferExpiredDate);

            cvOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OfferDetailActivity.class);
                    intent.putExtra("isDirect",false);
                    intent.putExtra("OfferMasterId",alOfferMaster.get(getAdapterPosition()).getOfferMasterId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
