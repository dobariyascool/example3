package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Events implements Parcelable
{
    String UserName ;
    String EventName ;
    String EventDate;
    short type ;
    String EventType;
    String PersonMobile;
    short linktoBusinessMasterId ;

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            Events objEvents = new Events();
            objEvents.UserName = in.readString();
            objEvents.EventName = in.readString();
            objEvents.EventDate = in.readString();
            objEvents.type= (short) in.readInt();
            objEvents.EventType = in.readString();
            objEvents.PersonMobile = in.readString();
            objEvents.linktoBusinessMasterId= (short) in.readInt();
            return objEvents;
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getPersonMobile() {
        return PersonMobile;
    }

    public void setPersonMobile(String personMobile) {
        PersonMobile = personMobile;
    }

    public static Creator<Events> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserName);
        dest.writeString(EventName);
        dest.writeString(EventDate);
        dest.writeInt(type);
        dest.writeString(EventType);
        dest.writeString(PersonMobile);
        dest.writeInt(linktoBusinessMasterId);
    }
}
