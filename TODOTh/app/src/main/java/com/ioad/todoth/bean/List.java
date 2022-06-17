package com.ioad.todoth.bean;

import java.util.Map;

public class List {
    String seq;
    String title;
    String content;
    String date;
    String finish;
    String count;
    String total;
    Map<String, Boolean> finishMap;
    boolean isChecked;
    int index;
    String titleName;

    public List(String seq, String content, Map<String, Boolean> finishMap) {
        this.seq = seq;
        this.content = content;
        this.finishMap = finishMap;
    }


    public List(String seq, String title, int index, String titleName) {
        this.seq = seq;
        this.title = title;
        this.index = index;
        this.titleName = titleName;
    }

    public List(String seq, String content, String finish, boolean isChecked) {
        this.seq = seq;
        this.content = content;
        this.finish = finish;
        this.isChecked = isChecked;
    }

    public List(String seq, String title, String content) {
        this.seq = seq;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public Map<String, Boolean> getFinishMap() {
        return finishMap;
    }

    public void setFinishMap(Map<String, Boolean> finishMap) {
        this.finishMap = finishMap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
