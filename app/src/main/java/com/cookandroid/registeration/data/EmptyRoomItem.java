package com.cookandroid.registeration.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EmptyRoomItem implements Parcelable {
    Boolean isAvailable;
    String type;
    String uid;
    String building;
    int floor;
    String room_number;
    String day;
    String time;
    ArrayList<Integer> timeList;

    public EmptyRoomItem(Boolean isAvailable, String uid, String type, String building, int floor, String room_number, String day, String time) {
        this.isAvailable = isAvailable;
        this.uid = uid;
        this.type = type;
        this.building = building;
        this.floor = floor;
        this.room_number = room_number;
        this.day = day;
        this.time = time;

        String[] times = time.split(",");
        timeList = new ArrayList<>();

        for(int i = 0 ; i < times.length ; i++){
            timeList.add(Integer.parseInt(times[i]));
        }
    }

    protected EmptyRoomItem(Parcel in) {
        byte tmpIsAvailable = in.readByte();
        isAvailable = tmpIsAvailable == 0 ? null : tmpIsAvailable == 1;
        type = in.readString();
        uid = in.readString();
        building = in.readString();
        floor = in.readInt();
        room_number = in.readString();
        day = in.readString();
        time = in.readString();
    }

    public static final Creator<EmptyRoomItem> CREATOR = new Creator<EmptyRoomItem>() {
        @Override
        public EmptyRoomItem createFromParcel(Parcel in) {
            return new EmptyRoomItem(in);
        }

        @Override
        public EmptyRoomItem[] newArray(int size) {
            return new EmptyRoomItem[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean reserved) {
        isAvailable = reserved;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isAvailable == null ? 0 : isAvailable ? 1 : 2));
        dest.writeString(type);
        dest.writeString(uid);
        dest.writeString(building);
        dest.writeInt(floor);
        dest.writeString(room_number);
        dest.writeString(day);
        dest.writeString(time);
    }
}
