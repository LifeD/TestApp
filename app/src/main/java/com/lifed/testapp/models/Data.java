package com.lifed.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Data implements Parcelable {

    @SerializedName("name")
    private String city;
    @SerializedName("ts")
    private long timestamp;
    @SerializedName("t")
    private int temperature;

    private DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

    /**
     * This is used by GsonConverterFactory
     */
    public Data() {
    }

    private Data(Parcel in) {
        city = in.readString();
        timestamp = in.readLong();
        temperature = in.readInt();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getCity() {
        return city;
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return dateFormat.format(calendar.getTime());
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeLong(timestamp);
        dest.writeInt(temperature);
    }
}
