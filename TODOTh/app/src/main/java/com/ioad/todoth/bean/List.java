package com.ioad.todoth.bean;

import java.util.Map;

public class List {
    String seq;
    String type;
    String titleName;
    String content;
    String date;
    String finish;
    String count;
    String total;
    Map<String, Boolean> finishMap;
    boolean isChecked;
    int typeIndex;
    int groupSeq;
    String selectDate;

    public List(String seq, String type, int typeIndex, String titleName) {
        this.seq = seq;
        this.type = type;
        this.typeIndex = typeIndex;
        this.titleName = titleName;
    }

    public List(String seq, String content, String finish, boolean isChecked, String selectDate) {
        this.seq = seq;
        this.content = content;
        this.finish = finish;
        this.isChecked = isChecked;
        this.selectDate = selectDate;
    }

    public List(String seq, String type, String titleName, String content, int groupSeq, int typeIndex) {
        this.seq = seq;
        this.type = type;
        this.content = content;
        this.titleName = titleName;
        this.groupSeq = groupSeq;
        this.typeIndex = typeIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getGroupSeq() {
        return groupSeq;
    }

    public void setGroupSeq(int groupSeq) {
        this.groupSeq = groupSeq;
    }

    public String getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }
}
