package com.cookandroid.registeration.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ClassroomItem implements Parcelable {
    int cid;
    String type;
    String day;
    String time;
    String building;
    String room_number;
    ArrayList<Integer> timeList;

    public ClassroomItem(int cid, String type, String building, String room_number, String day, String time) {
        this.cid = cid;
        this.type = type;
        this.building = building;
        this.room_number = room_number;
        this.day = day;
        this.time = time;

        String[] times = time.split(",");
        timeList = new ArrayList<>();

        for(int i = 0 ; i < times.length ; i++){
            timeList.add(Integer.parseInt(times[i]));
        }
    }

    protected ClassroomItem(Parcel in) {
        building = in.readString();
        room_number = in.readString();
        day = in.readString();
        time = in.readString();
    }

    public static final Creator<ClassroomItem> CREATOR = new Creator<ClassroomItem>() {
        @Override
        public ClassroomItem createFromParcel(Parcel in) {
            return new ClassroomItem(in);
        }

        @Override
        public ClassroomItem[] newArray(int size) {
            return new ClassroomItem[size];
        }
    };

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<Integer> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<Integer> timeList) {
        this.timeList = timeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cid);
        dest.writeString(type);
        dest.writeString(day);
        dest.writeString(time);
        dest.writeString(building);
        dest.writeString(room_number);
    }
}
