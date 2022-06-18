package com.ioad.todoth.common;

import java.util.ArrayList;

public interface ClickCallbackListener {
    public void callBack(int position, String status);
    public void callBackList(ArrayList<String> numbers);
}
