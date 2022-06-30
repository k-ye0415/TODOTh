package com.ioad.todoth.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ioad.todoth.R;
import com.ioad.todoth.activity.ListAddActivity;
import com.ioad.todoth.activity.ListItemActivity;
import com.ioad.todoth.activity.MainActivity;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.ClickCallbackListener;
import com.ioad.todoth.common.DBHelper;
import com.ioad.todoth.common.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private int layout = 0;
    private ArrayList<List> lists;

    private DBHelper helper;

    private ClickCallbackListener callbackListener;
    private ArrayList<String> numbers;
    Dialog dialog;
    WindowManager.LayoutParams layoutParams;
    Window window;
    // write dialog
    EditText etItem;
    Button btnAddCancel, btnAdd, btnKeepAdd;
    TextView btnSetDate, tvItemDate, tvItemTime;
    LinearLayout llItemDate;

    // delete dialog
    Button btnDeleteCancel, btnDelete;
    int selectIndex;

    // date write dialog
    CalendarView cvDate;
    Switch btnTimeSwitch;
    LinearLayout llDateTime;
    TimePicker tpDateTime;
    Button btnDateCancel, btnDateAdd;

    boolean isSwitched = false;
    String selectDate;
    String selectTime;
    boolean isSelectedDate = false;
    boolean isSelectedTime = false;

    private long now = 0;
    private Date date = null;
    private SimpleDateFormat dateFormat = null;


    public ListItemAdapter(Context mContext, int layout, ArrayList<List> lists, ClickCallbackListener listener) {
        this.mContext = mContext;
        this.layout = layout;
        this.lists = lists;
        this.callbackListener = listener;
        this.numbers = new ArrayList<>();
        this.helper = new DBHelper(this.mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final int index = position;

        holder.cbCheck.setChecked(lists.get(position).isChecked());

        if (lists.get(position).isChecked()) {
            holder.tvContent.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvContent.setPaintFlags(0);
        }

        holder.tvContent.setText(lists.get(position).getContent());
        if (lists.get(position).getSelectDate().equals("null")) {
            holder.tvListDate.setText("");
        } else {
            holder.tvListDate.setText(lists.get(position).getSelectDate());
        }
        holder.cbCheck.setTag(lists.get(position));

//        Log.e(TAG, "체크박스 선택 상황 : " + lists.get(position).getSeq() + lists.get(position).isChecked());

        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                List list = (List) compoundButton.getTag();
                list.setChecked(compoundButton.isChecked());
                lists.get(index).setChecked(compoundButton.isChecked());

                if (isCheck) {
                    if (lists.get(index).isChecked()) {
                        Log.e(TAG, lists.get(index).isChecked() + "");
                        Log.e(TAG, lists.get(index).getContent());

                        holder.tvContent.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                        numbers.add(lists.get(index).getSeq());
                    }
                } else {
                    if (!lists.get(index).isChecked()) {
                        Log.e(TAG, lists.get(index).isChecked() + "");
                        Log.e(TAG, lists.get(index).getContent());

                        holder.tvContent.setPaintFlags(0);

                        numbers.remove(lists.get(index).getSeq());
                    }
                }
                Log.e(TAG, numbers.toString());
                callbackListener.callBackList(numbers);

            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llList;
        public CheckBox cbCheck;
        public TextView tvContent, tvListDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llList = itemView.findViewById(R.id.ll_list_item);
            cbCheck = itemView.findViewById(R.id.cb_check);
            tvContent = itemView.findViewById(R.id.tv_list_content);
            tvListDate = itemView.findViewById(R.id.tv_list_date);

            llList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e(TAG, "This is Long Click");
                    selectIndex = Integer.parseInt(lists.get(getAdapterPosition()).getSeq());
                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.delete_dialog_layout);

                    layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    window = dialog.getWindow();
                    window.setAttributes(layoutParams);
                    dialog.show();
                    dialog.setCancelable(false);

                    btnDeleteCancel = dialog.findViewById(R.id.btn_group_delete_cancel);
                    btnDelete = dialog.findViewById(R.id.btn_group_delete);

                    btnDeleteCancel.setOnClickListener(dialogBtnOnClickListener);
                    btnDelete.setOnClickListener(dialogBtnOnClickListener);
                    return true;
                }
            });

            llList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "This is Click");
                    selectIndex = Integer.parseInt(lists.get(getAdapterPosition()).getSeq());
                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.write_dialog_layout);

                    layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    window = dialog.getWindow();
                    window.setAttributes(layoutParams);
                    dialog.show();
                    dialog.setCancelable(false);

                    etItem = dialog.findViewById(R.id.et_content);
                    btnAddCancel = dialog.findViewById(R.id.btn_item_add_cancel);
                    btnAdd = dialog.findViewById(R.id.btn_item_add);
                    btnKeepAdd = dialog.findViewById(R.id.btn_item_keep_adding);
                    tvItemDate = dialog.findViewById(R.id.tv_item_date);
                    tvItemTime = dialog.findViewById(R.id.tv_item_time);
                    btnSetDate = dialog.findViewById(R.id.btn_set_date);
                    llItemDate = dialog.findViewById(R.id.ll_item_date);

                    btnAdd.setText("수정");
                    tvItemDate.setText(lists.get(getAdapterPosition()).getSelectDate());
                    tvItemTime.setText(lists.get(getAdapterPosition()).getSelectTime());
                    btnKeepAdd.setVisibility(View.GONE);

                    etItem.setText(lists.get(getAdapterPosition()).getContent());
                    btnAddCancel.setOnClickListener(dialogBtnOnClickListener);
                    btnAdd.setOnClickListener(dialogBtnOnClickListener);
                    btnSetDate.setOnClickListener(dialogBtnOnClickListener);
                }
            });

        }
    }

    View.OnClickListener dialogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = ((Activity) mContext).getIntent();
            switch (view.getId()) {
                case R.id.btn_date_cancel:
                case R.id.btn_item_add_cancel:
                case R.id.btn_group_delete_cancel:
                    dialog.dismiss();
                    break;
                case R.id.btn_item_add:
                    String content = etItem.getText().toString();
                    if (content.length() != 0) {
                        helper.updateListItemData(selectIndex, content, selectDate, selectTime);
                        dialog.dismiss();
                        ((Activity) mContext).finish(); //현재 액티비티 종료 실시
                        ((Activity) mContext).overridePendingTransition(0, 0); //효과 없애기
                        ((Activity) mContext).startActivity(intent); //현재 액티비티 재실행 실시
                        ((Activity) mContext).overridePendingTransition(0, 0); //효과 없애기
                    } else {
                        Util.showToast(mContext, "리스트 이름을 작성해주세요!");
                    }
                    break;
                case R.id.btn_group_delete:
                    helper.deleteListData(selectIndex);
                    dialog.dismiss();
                    ((Activity) mContext).finish(); //현재 액티비티 종료 실시
                    ((Activity) mContext).overridePendingTransition(0, 0); //효과 없애기
                    ((Activity) mContext).startActivity(intent); //현재 액티비티 재실행 실시
                    ((Activity) mContext).overridePendingTransition(0, 0); //효과 없애기
                    break;
                case R.id.btn_set_date:
                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.write_date_dialog_layout);

                    layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    window = dialog.getWindow();
                    window.setAttributes(layoutParams);
                    dialog.show();

                    cvDate = dialog.findViewById(R.id.cv_date);
                    btnTimeSwitch = dialog.findViewById(R.id.btn_time_switch);
                    llDateTime = dialog.findViewById(R.id.ll_date_time);
                    tpDateTime = dialog.findViewById(R.id.tp_date_time);
                    btnDateCancel = dialog.findViewById(R.id.btn_date_cancel);
                    btnDateAdd = dialog.findViewById(R.id.btn_date_add);

                    llDateTime.setVisibility(View.GONE);

                    btnTimeSwitch.setOnCheckedChangeListener(switchOnChangeListener);
                    cvDate.setOnDateChangeListener(dateChangeListener);
                    tpDateTime.setOnTimeChangedListener(timeChangedListener);
                    btnDateCancel.setOnClickListener(dialogBtnOnClickListener);
                    btnDateAdd.setOnClickListener(dialogBtnOnClickListener);
                    break;
                case R.id.btn_date_add:
                    if (!isSelectedDate) {
                        selectDate = getCurrentDate();
                    }
                    if (isSwitched) {
                        if (!isSelectedTime) {
                            selectTime = getCurrentTime();
                        }
                    }
                    dialog.dismiss();
                    llItemDate.setVisibility(View.VISIBLE);
                    tvItemDate.setText(selectDate);
                    tvItemTime.setText(selectTime);
                    break;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener switchOnChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
            if (isCheck) {
                llDateTime.setVisibility(View.VISIBLE);
                isSwitched = true;
            } else {
                llDateTime.setVisibility(View.GONE);
                isSwitched = false;
            }
        }
    };

    CalendarView.OnDateChangeListener dateChangeListener = new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
            String yearStr = String.valueOf(year);
            String monthStr = month < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
            String dayStr = day < 10 ? "0" + day : String.valueOf(day);
            selectDate = yearStr + "-" + monthStr + "-" + dayStr;
            Util.showToast(mContext, selectDate);
            isSelectedDate = true;
        }
    };

    TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
            String hourStr = "";
            if (hour < 10) {
                hourStr = "0" + hour;
            } else {
                if (hour > 12) {
                    hourStr = String.valueOf(hour -= 12);
                } else {
                    hourStr = String.valueOf(hour);
                }
            }
            String minuteStr = minute < 10 ? "0" + minute : String.valueOf(minute);
            selectTime = "오후 " + hourStr + ":" + minuteStr;
            isSelectedTime = true;
        }
    };

    private String getCurrentDate() {
        String result;
        now = System.currentTimeMillis();
        date = new Date(now);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result = dateFormat.format(date);
        return result;
    }

    private String getCurrentTime() {
        String result;
        now = System.currentTimeMillis();
        date = new Date(now);
        dateFormat = new SimpleDateFormat("a h:mm");
        result = dateFormat.format(date);
        return result;
    }

}
