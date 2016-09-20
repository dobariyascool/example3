package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRightsTran implements Parcelable{

    int UserRightsTranId;
    short linktoUserRightsMasterId;
    short linktoRoleMasterId;

    public static final Creator<UserRightsTran> CREATOR = new Creator<UserRightsTran>() {
        @Override
        public UserRightsTran createFromParcel(Parcel in) {
            UserRightsTran objUserRightsTran = new UserRightsTran();
            objUserRightsTran.UserRightsTranId = in.readInt();
            objUserRightsTran.linktoUserRightsMasterId = (short) in.readInt();
            objUserRightsTran.linktoRoleMasterId = (short) in.readInt();
            return objUserRightsTran;
        }

        @Override
        public UserRightsTran[] newArray(int size) {
            return new UserRightsTran[size];
        }
    };

    public int getUserRightsTranId() {
        return UserRightsTranId;
    }

    public void setUserRightsTranId(int userRightsTranId) {
        UserRightsTranId = userRightsTranId;
    }

    public short getLinktoUserRightsMasterId() {
        return linktoUserRightsMasterId;
    }

    public void setLinktoUserRightsMasterId(short linktoUserRightsMasterId) {
        this.linktoUserRightsMasterId = linktoUserRightsMasterId;
    }

    public short getLinktoRoleMasterId() {
        return linktoRoleMasterId;
    }

    public void setLinktoRoleMasterId(short linktoRoleMasterId) {
        this.linktoRoleMasterId = linktoRoleMasterId;
    }

    public static Creator<UserRightsTran> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(UserRightsTranId);
        dest.writeInt(linktoUserRightsMasterId);
        dest.writeInt(linktoRoleMasterId);
    }
}
