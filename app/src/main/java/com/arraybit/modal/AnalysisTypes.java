package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class AnalysisTypes implements Parcelable {

    int id;
    String desc;
    boolean isGraph;

    public static final Creator<AnalysisTypes> CREATOR = new Creator<AnalysisTypes>() {
        @Override
        public AnalysisTypes createFromParcel(Parcel in) {
            AnalysisTypes analysisTypes = new AnalysisTypes();

            analysisTypes.id = in.readInt();
            analysisTypes.desc = in.readString();
            analysisTypes.isGraph = in.readByte() != 0;
            return analysisTypes;
        }

        @Override
        public AnalysisTypes[] newArray(int size) {
            return new AnalysisTypes[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isGraph() {
        return isGraph;
    }

    public void setGraph(boolean graph) {
        isGraph = graph;
    }

    public static Creator<AnalysisTypes> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(desc);
        dest.writeByte((byte) (isGraph ? 1 : 0));
    }
}
