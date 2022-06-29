package com.ioad.todoth.common;

import android.content.Context;
import android.widget.Toast;

import com.ioad.todoth.R;

public class Util {

    public static String[] listType = {
            "Daily",
            "Work",
            "Trip",
            "Exercise",
            "Study",
            "etc."
    };
    public static int[] listImage = {
            R.drawable.daily_icon,
            R.drawable.work_icon,
            R.drawable.trip_icon,
            R.drawable.exercise_icon,
            R.drawable.study_icon,
            R.drawable.etc_icon
    };

    public static int[] groupColor = {
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6,
            R.color.color7
    };


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
