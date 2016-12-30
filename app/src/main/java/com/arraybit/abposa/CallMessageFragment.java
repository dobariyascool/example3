package com.arraybit.abposa;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.modal.Events;
import com.rey.material.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallMessageFragment extends DialogFragment implements View.OnClickListener {

    Button btnCall, btnMessage;
    Events objEvents = null;
    String businessName = null;
    String businessEmail = null;

    public CallMessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_message, container, false);
        try {
            Bundle bundle = getArguments();
            objEvents = bundle.getParcelable("Event");
            businessName = bundle.getString("BusinessName", null);
            businessEmail = bundle.getString("BusinessEmail", null);

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            btnCall = (Button) view.findViewById(R.id.btnCall);
            btnMessage = (Button) view.findViewById(R.id.btnMessage);

            Drawable btnCallIcon, btnmessageIcon;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                btnCallIcon = VectorDrawableCompat.create(getResources(), R.drawable.call, getContext().getTheme());
                btnmessageIcon = VectorDrawableCompat.create(getResources(), R.drawable.message, getContext().getTheme());
            } else {
                btnCallIcon = getResources().getDrawable(R.drawable.call, getContext().getTheme());
                btnmessageIcon = getResources().getDrawable(R.drawable.message, getContext().getTheme());
            }

            btnCall.setCompoundDrawablesRelativeWithIntrinsicBounds(null, btnCallIcon, null, null);
            btnMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(null, btnmessageIcon, null, null);

            btnCall.setOnClickListener(this);
            btnMessage.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCall) {

            //call the calling interface
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0" + objEvents.getPersonMobile()));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            dismiss();
        } else if (v.getId() == R.id.btnMessage) {
            String sms = null;

            if (objEvents.getType() == 0) {
                sms = "Hello " + objEvents.getUserName() + ",\n" +
                        "\n" +
                        "Many many happy returns of the day...\n" +
                        "Wishing you a very HAPPY BIRTHDAY...!\n";
                if (businessName != null && !businessName.equals("")) {
                    sms = sms + "\n" + "From: " + businessName;
                }
                if (businessEmail != null && !businessEmail.equals("")) {
                    sms = sms + "\n" + "Web: " + businessEmail;
                }
            } else if (objEvents.getType() == 1) {
                sms = "Hello " + objEvents.getUserName() + ",\n" +
                        "\n" +
                        "Wish you a very HAPPY WEDDING ANNIVERSARY.\n" +
                        "May your marriage be blessed with Love, Joy & Happiness.\n" +
                        "Best wishes to you both.\n";
                if (businessName != null && !businessName.equals("")) {
                    sms = sms + "\n" + "From: " + businessName;
                }
                if (businessEmail != null && !businessEmail.equals("")) {
                    sms = sms + "\n" + "Web: " + businessEmail;
                }
            }
            Uri uri = Uri.parse("smsto:0" + objEvents.getPersonMobile());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", sms);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            dismiss();
        }
    }

}
