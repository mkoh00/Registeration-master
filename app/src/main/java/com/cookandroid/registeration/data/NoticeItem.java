package com.cookandroid.registeration.data;

public class NoticeItem {
    String notice;
    String name;
    String date;

    public NoticeItem(String notice, String name, String date) {
        this.notice = notice;
        this.name = name;
        this.date = date;
    }

    public String getNotice() {
        return notice;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
