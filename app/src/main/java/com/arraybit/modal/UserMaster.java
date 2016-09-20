package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class UserMaster implements Parcelable {

    int UserMasterId;
    String Username;
    String Password;
    short linktoRoleMasterId;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    String LastLoginDateTime;
    short LoginFailCount;
    String LastLockoutDateTime;
    String LastPasswordChangedDateTime;
    String Comment;
    short linktoBusinessMasterId;
    boolean IsEnabled;
    boolean IsDeleted;
    short linktoBusinessTypeMasterId;
    short linktoBusinessGroupMasterId;

    //Extras
    String Role;
    public static final Creator<UserMaster> CREATOR = new Creator<UserMaster>() {
        @Override
        public UserMaster createFromParcel(Parcel in) {
            UserMaster objUserMaster = new UserMaster();
            objUserMaster.UserMasterId = in.readInt();
            objUserMaster.Username = in.readString();
            objUserMaster.Password = in.readString();
            objUserMaster.linktoRoleMasterId = (short) in.readInt();
            objUserMaster.CreateDateTime = in.readString();
            objUserMaster.linktoUserMasterIdCreatedBy = (short) in.readInt();
            objUserMaster.UpdateDateTime = in.readString();
            objUserMaster.linktoUserMasterIdUpdatedBy = (short) in.readInt();
            objUserMaster.LastLoginDateTime = in.readString();
            objUserMaster.LoginFailCount = (short) in.readInt();
            objUserMaster.LastLockoutDateTime = in.readString();
            objUserMaster.LastPasswordChangedDateTime = in.readString();
            objUserMaster.Comment = in.readString();
            objUserMaster.linktoBusinessMasterId = (short) in.readInt();
            objUserMaster.IsEnabled = in.readByte() != 0;
            objUserMaster.IsDeleted = in.readByte() != 0;
            objUserMaster.linktoBusinessTypeMasterId = (short) in.readInt();
            objUserMaster.linktoBusinessGroupMasterId = (short) in.readInt();
            objUserMaster.Role = in.readString();
            return objUserMaster;
        }

        @Override
        public UserMaster[] newArray(int size) {
            return new UserMaster[size];
        }
    };

    public static Creator<UserMaster> getCREATOR() {
        return CREATOR;
    }

    public int getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(int userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public short getLinktoRoleMasterId() {
        return linktoRoleMasterId;
    }

    public void setLinktoRoleMasterId(short linktoRoleMasterId) {
        this.linktoRoleMasterId = linktoRoleMasterId;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public short getLinktoUserMasterIdCreatedBy() {
        return linktoUserMasterIdCreatedBy;
    }

    public void setLinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) {
        this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy;
    }

    public String getUpdateDateTime() {
        return UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        UpdateDateTime = updateDateTime;
    }

    public short getLinktoUserMasterIdUpdatedBy() {
        return linktoUserMasterIdUpdatedBy;
    }

    public void setLinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) {
        this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy;
    }

    public String getLastLoginDateTime() {
        return LastLoginDateTime;
    }

    public void setLastLoginDateTime(String lastLoginDateTime) {
        LastLoginDateTime = lastLoginDateTime;
    }

    public short getLoginFailCount() {
        return LoginFailCount;
    }

    public void setLoginFailCount(short loginFailCount) {
        LoginFailCount = loginFailCount;
    }

    public String getLastLockoutDateTime() {
        return LastLockoutDateTime;
    }

    public void setLastLockoutDateTime(String lastLockoutDateTime) {
        LastLockoutDateTime = lastLockoutDateTime;
    }

    public String getLastPasswordChangedDateTime() {
        return LastPasswordChangedDateTime;
    }

    public void setLastPasswordChangedDateTime(String lastPasswordChangedDateTime) {
        LastPasswordChangedDateTime = lastPasswordChangedDateTime;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public boolean isEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        IsEnabled = isEnabled;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        IsDeleted = isDeleted;
    }

    public short getLinktoBusinessTypeMasterId() {
        return linktoBusinessTypeMasterId;
    }

    public void setLinktoBusinessTypeMasterId(short linktoBusinessTypeMasterId) {
        this.linktoBusinessTypeMasterId = linktoBusinessTypeMasterId;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public void setEnabled(boolean enabled) {
        IsEnabled = enabled;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public short getLinktoBusinessGroupMasterId() {
        return linktoBusinessGroupMasterId;
    }

    public void setLinktoBusinessGroupMasterId(short linktoBusinessGroupMasterId) {
        this.linktoBusinessGroupMasterId = linktoBusinessGroupMasterId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(UserMasterId);
        parcel.writeString(Username);
        parcel.writeString(Password);
        parcel.writeInt(linktoRoleMasterId);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeString(LastLoginDateTime);
        parcel.writeInt(LoginFailCount);
        parcel.writeString(LastLockoutDateTime);
        parcel.writeString(LastPasswordChangedDateTime);
        parcel.writeString(Comment);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeInt(linktoBusinessTypeMasterId);
        parcel.writeInt(linktoBusinessGroupMasterId);
        parcel.writeString(Role);
    }


}
