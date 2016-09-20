package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderPaymentTran implements Parcelable {

    long OrderPaymentTranId;
    long linktoOrderMasterId;
    short linktoPaymentTypeMasterId;
    int linktoCustomerMasterId;
    String PaymentDateTime;
    double AmountPaid;
    String Remark;
    boolean IsDeleted;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;

    //Extras
    String PaymentType;
    double totalAmount;

    public static final Creator<OrderPaymentTran> CREATOR = new Creator<OrderPaymentTran>() {
        @Override
        public OrderPaymentTran createFromParcel(Parcel in) {
            OrderPaymentTran objOrderPaymentTran = new OrderPaymentTran();
            objOrderPaymentTran.OrderPaymentTranId = in.readLong();
            objOrderPaymentTran.linktoOrderMasterId = in.readLong();
            objOrderPaymentTran.linktoPaymentTypeMasterId =(short) in.readInt();
            objOrderPaymentTran.linktoCustomerMasterId = in.readInt();
            objOrderPaymentTran.PaymentDateTime = in.readString();
            objOrderPaymentTran.AmountPaid = in.readDouble();
            objOrderPaymentTran.Remark = in.readString();
            objOrderPaymentTran.IsDeleted = in.readByte() != 0;
            objOrderPaymentTran.CreateDateTime = in.readString();
            objOrderPaymentTran.linktoUserMasterIdCreatedBy =(short) in.readInt();
            objOrderPaymentTran.UpdateDateTime = in.readString();
            objOrderPaymentTran.linktoUserMasterIdUpdatedBy =(short) in.readInt();
            objOrderPaymentTran.PaymentType = in.readString();
            objOrderPaymentTran.totalAmount = in.readDouble();
            return objOrderPaymentTran;
        }

        @Override
        public OrderPaymentTran[] newArray(int size) {
            return new OrderPaymentTran[size];
        }
    };

    public long getOrderPaymentTranId() {
        return OrderPaymentTranId;
    }

    public void setOrderPaymentTranId(long orderPaymentTranId) {
        OrderPaymentTranId = orderPaymentTranId;
    }

    public long getLinktoOrderMasterId() {
        return linktoOrderMasterId;
    }

    public void setLinktoOrderMasterId(long linktoOrderMasterId) {
        this.linktoOrderMasterId = linktoOrderMasterId;
    }

    public short getLinktoPaymentTypeMasterId() {
        return linktoPaymentTypeMasterId;
    }

    public void setLinktoPaymentTypeMasterId(short linktoPaymentTypeMasterId) {
        this.linktoPaymentTypeMasterId = linktoPaymentTypeMasterId;
    }

    public int getLinktoCustomerMasterId() {
        return linktoCustomerMasterId;
    }

    public void setLinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public String getPaymentDateTime() {
        return PaymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        PaymentDateTime = paymentDateTime;
    }

    public double getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        AmountPaid = amountPaid;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        IsDeleted = isDeleted;
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

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static Creator<OrderPaymentTran> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(OrderPaymentTranId);
        dest.writeLong(linktoOrderMasterId);
        dest.writeInt(linktoPaymentTypeMasterId);
        dest.writeInt(linktoCustomerMasterId);
        dest.writeString(PaymentDateTime);
        dest.writeDouble(AmountPaid);
        dest.writeString(Remark);
        dest.writeByte((byte) (IsDeleted ? 1 : 0));
        dest.writeString(CreateDateTime);
        dest.writeInt(linktoUserMasterIdCreatedBy);
        dest.writeString(UpdateDateTime);
        dest.writeInt(linktoUserMasterIdUpdatedBy);
        dest.writeString(PaymentType);
        dest.writeDouble(totalAmount);
    }
}
