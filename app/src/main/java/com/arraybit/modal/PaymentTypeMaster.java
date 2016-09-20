package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentTypeMaster  implements Parcelable{

    short PaymentTypeMasterId;
    String ShortName;
    String PaymentType;
    String Description;
    short linktoPaymentTypeCategoryMasterId;
    boolean IsEnabled;
    short linktoBusinessMasterId;
    short linktoUserMasterIdCreatedBy;
    String CreateDateTime;
    short linktoUserMasterIdUpdatedBy;
    String UpdateDateTime;

    /// Extra
    String PaymentTypeCategory;
    String Business;
    String UserCreatedBy;
    String UserUpdatedBy;

    public static final Creator<PaymentTypeMaster> CREATOR = new Creator<PaymentTypeMaster>() {
        @Override
        public PaymentTypeMaster createFromParcel(Parcel in) {
            PaymentTypeMaster objPaymentTypeMaster = new PaymentTypeMaster();
            objPaymentTypeMaster.PaymentTypeMasterId =(short) in.readInt();
            objPaymentTypeMaster.ShortName = in.readString();
            objPaymentTypeMaster.PaymentType = in.readString();
            objPaymentTypeMaster.Description = in.readString();
            objPaymentTypeMaster.linktoPaymentTypeCategoryMasterId =(short) in.readInt();
            objPaymentTypeMaster.IsEnabled = in.readByte() != 0;
            objPaymentTypeMaster.linktoBusinessMasterId =(short) in.readInt();
            objPaymentTypeMaster.linktoUserMasterIdCreatedBy =(short) in.readInt();
            objPaymentTypeMaster.CreateDateTime = in.readString();
            objPaymentTypeMaster.linktoUserMasterIdUpdatedBy =(short) in.readInt();
            objPaymentTypeMaster.UpdateDateTime = in.readString();
            objPaymentTypeMaster.PaymentTypeCategory = in.readString();
            objPaymentTypeMaster.Business = in.readString();
            objPaymentTypeMaster.UserCreatedBy = in.readString();
            objPaymentTypeMaster.UserUpdatedBy = in.readString();
            return objPaymentTypeMaster;
        }

        @Override
        public PaymentTypeMaster[] newArray(int size) {
            return new PaymentTypeMaster[size];
        }
    };

    public short getPaymentTypeMasterId() {
        return PaymentTypeMasterId;
    }

    public void setPaymentTypeMasterId(short paymentTypeMasterId) {
        PaymentTypeMasterId = paymentTypeMasterId;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public short getLinktoPaymentTypeCategoryMasterId() {
        return linktoPaymentTypeCategoryMasterId;
    }

    public void setLinktoPaymentTypeCategoryMasterId(short linktoPaymentTypeCategoryMasterId) {
        this.linktoPaymentTypeCategoryMasterId = linktoPaymentTypeCategoryMasterId;
    }

    public boolean isEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        IsEnabled = isEnabled;
    }

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public short getLinktoUserMasterIdCreatedBy() {
        return linktoUserMasterIdCreatedBy;
    }

    public void setLinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) {
        this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public short getLinktoUserMasterIdUpdatedBy() {
        return linktoUserMasterIdUpdatedBy;
    }

    public void setLinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) {
        this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy;
    }

    public String getUpdateDateTime() {
        return UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        UpdateDateTime = updateDateTime;
    }

    public String getPaymentTypeCategory() {
        return PaymentTypeCategory;
    }

    public void setPaymentTypeCategory(String paymentTypeCategory) {
        PaymentTypeCategory = paymentTypeCategory;
    }

    public String getBusiness() {
        return Business;
    }

    public void setBusiness(String business) {
        Business = business;
    }

    public String getUserCreatedBy() {
        return UserCreatedBy;
    }

    public void setUserCreatedBy(String userCreatedBy) {
        UserCreatedBy = userCreatedBy;
    }

    public String getUserUpdatedBy() {
        return UserUpdatedBy;
    }

    public void setUserUpdatedBy(String userUpdatedBy) {
        UserUpdatedBy = userUpdatedBy;
    }

    public static Creator<PaymentTypeMaster> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PaymentTypeMasterId);
        dest.writeString(ShortName);
        dest.writeString(PaymentType);
        dest.writeString(Description);
        dest.writeInt(linktoPaymentTypeCategoryMasterId);
        dest.writeByte((byte) (IsEnabled ? 1 : 0));
        dest.writeInt(linktoBusinessMasterId);
        dest.writeInt(linktoUserMasterIdCreatedBy);
        dest.writeString(CreateDateTime);
        dest.writeInt(linktoUserMasterIdUpdatedBy);
        dest.writeString(UpdateDateTime);
        dest.writeString(PaymentTypeCategory);
        dest.writeString(Business);
        dest.writeString(UserCreatedBy);
        dest.writeString(UserUpdatedBy);
    }
}
