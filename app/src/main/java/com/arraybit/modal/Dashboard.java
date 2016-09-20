package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class Dashboard implements Parcelable{

    int linktoBusinessMasterId;
    int RegisteredUserBooking;
    int CanceledBooking;
    int CanceledOrder;
    int PreOrder;

    public static final Creator<Dashboard> CREATOR = new Creator<Dashboard>() {
        @Override
        public Dashboard createFromParcel(Parcel in) {
            Dashboard objDashboard= new Dashboard();
            objDashboard.linktoBusinessMasterId = in.readInt();
            objDashboard.RegisteredUserBooking = in.readInt();
            objDashboard.CanceledBooking = in.readInt();
            objDashboard.CanceledOrder = in.readInt();
            objDashboard.PreOrder = in.readInt();
            return objDashboard;
        }

        @Override
        public Dashboard[] newArray(int size) {
            return new Dashboard[size];
        }
    };

    public int getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(int linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public int getRegisteredUserBooking() {
        return RegisteredUserBooking;
    }

    public void setRegisteredUserBooking(int registeredUserBooking) {
        RegisteredUserBooking = registeredUserBooking;
    }

    public int getCanceledBooking() {
        return CanceledBooking;
    }

    public void setCanceledBooking(int canceledBooking) {
        CanceledBooking = canceledBooking;
    }

    public int getCanceledOrder() {
        return CanceledOrder;
    }

    public void setCanceledOrder(int canceledOrder) {
        CanceledOrder = canceledOrder;
    }

    public int getPreOrder() {
        return PreOrder;
    }

    public void setPreOrder(int preOrder) {
        PreOrder = preOrder;
    }

    public static Creator<Dashboard> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(linktoBusinessMasterId);
        dest.writeInt(RegisteredUserBooking);
        dest.writeInt(CanceledBooking);
        dest.writeInt(CanceledOrder);
        dest.writeInt(PreOrder);
    }
}
