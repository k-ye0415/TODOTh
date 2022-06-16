package com.ioad.todoth.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ioad.todoth.R;
import com.ioad.todoth.activity.ListItemActivity;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.ClickCallbackListener;
import com.ioad.todoth.common.DBHelper;
import com.ioad.todoth.common.ItemTouchHelperListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> implements ItemTouchHelperListener {

    private Context mContext;
    private int layout = 0;
    private LayoutInflater inflater;
    private ArrayList<List> lists;

    private DBHelper helper;

    private ClickCallbackListener callbackListener;
    private ArrayList<String> numbers;
    private Dialog dialog;
    EditText etItem;
    Button btnAddCancel, btnAdd;
    int index;


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

        holder.cbCheck.setTag(lists.get(position));

        Log.e("TAG", lists.get(position).getSeq() + lists.get(position).isChecked());

        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                List list = (List) compoundButton.getTag();
                list.setChecked(compoundButton.isChecked());
                lists.get(index).setChecked(compoundButton.isChecked());

                if (isCheck) {
                    if (lists.get(index).isChecked()) {
                        Log.e("TAG", lists.get(index).isChecked() + "");
                        Log.e("TAG", lists.get(index).getContent());

                        holder.tvContent.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                        numbers.add(lists.get(index).getSeq());
                    }
                } else {
                    if (!lists.get(index).isChecked()) {
                        Log.e("TAG", lists.get(index).isChecked() + "");
                        Log.e("TAG", lists.get(index).getContent());

                        holder.tvContent.setPaintFlags(0);

                        numbers.remove(lists.get(index).getSeq());
                    }
                }
                Log.e("TAG", numbers.toString());
                callbackListener.callBackList(numbers);

            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public boolean onItemMove(int startPosition, int endPosition) {
        String number = lists.get(startPosition).getSeq();
        lists.remove(endPosition);
//        lists.add(startPosition , number);

        notifyItemMoved(startPosition,endPosition);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        String number = lists.get(position).getSeq();

        lists.remove(position);

        notifyItemRemoved(position);
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Window window = dialog.getWindow();
        window.setAttributes(layoutParams);
        dialog.show();

        etItem = dialog.findViewById(R.id.et_item);
        btnAddCancel = dialog.findViewById(R.id.btn_item_add_cancel);
        btnAdd = dialog.findViewById(R.id.btn_item_add);

        etItem.setText(lists.get(position).getContent());

        index = Integer.parseInt(lists.get(position).getSeq());
        btnAddCancel.setOnClickListener(dialogBtnOnClickListener);
        btnAdd.setOnClickListener(dialogBtnOnClickListener);
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        lists.remove(position);
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cbCheck;
        public TextView tvContent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cbCheck = itemView.findViewById(R.id.cb_check);
            tvContent = itemView.findViewById(R.id.tv_list_content);
        }
    }

    View.OnClickListener dialogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_item_add_cancel:
                    dialog.dismiss();
                    break;
                case R.id.btn_item_add:
                    String content = etItem.getText().toString();
                    if (content.length() != 0) {
                        helper.updateListItemData("TODO_LIST", index, content);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(mContext, "Writ TODO", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
