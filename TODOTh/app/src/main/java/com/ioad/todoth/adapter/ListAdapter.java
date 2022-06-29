package com.ioad.todoth.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ioad.todoth.R;
import com.ioad.todoth.activity.ListAddActivity;
import com.ioad.todoth.activity.ListItemActivity;
import com.ioad.todoth.activity.MainActivity;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.DBHelper;
import com.ioad.todoth.common.Util;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private int layout = 0;
    private ArrayList<List> lists;
    private int[] colors;
    int listSeq;
    Dialog dialog;
    WindowManager.LayoutParams layoutParams;
    Window window;
    // delete dialog
    Button btnDeleteCancel, btnDelete;
    DBHelper helper;

    public ListAdapter(Context mContext, int layout, ArrayList<List> lists, int[] colors) {
        this.mContext = mContext;
        this.layout = layout;
        this.lists = lists;
        this.colors = colors;
        this.helper = new DBHelper(mContext);
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
//        holder.listItem.setBackgroundColor(colors[lists.get(position).getTypeIndex()]);
        holder.ivListImg.setBackgroundColor(colors[lists.get(position).getTypeIndex()]);
//        holder.ivListImg.setImageResource(Util.listImage[lists.get(position).getTypeIndex()]);
        holder.tvListTitle.setText(lists.get(position).getTitleName());
        holder.tv_sub_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.tv_sub_menu);
                popupMenu.inflate(R.menu.group_list_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.group_modify:
                                intent = new Intent(mContext, ListAddActivity.class);
                                listSeq = Integer.parseInt(lists.get(position).getSeq());
                                intent.putExtra("LIST_SEQ", listSeq);
                                intent.putExtra("STATUS", "update");
                                mContext.startActivity(intent);
                                break;
                            case R.id.group_delete:
                                listSeq = Integer.parseInt(lists.get(position).getSeq());
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
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    View.OnClickListener dialogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = ((Activity) mContext).getIntent();
            switch (view.getId()) {
                case R.id.btn_group_delete_cancel:
                    dialog.dismiss();
                    break;
                case R.id.btn_group_delete:
                    helper.deleteListGroupData(listSeq);
                    dialog.dismiss();
                    ((Activity) mContext).finish(); //현재 액티비티 종료 실시
                    ((Activity) mContext).overridePendingTransition(0, 0); //효과 없애기
                    ((Activity) mContext).startActivity(intent); //현재 액티비티 재실행 실시
                    ((Activity) mContext).overridePendingTransition(0, 0); //효과 없애기
                    break;
            }
        }
    };


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout listItem;
        public View ivListImg;
        public TextView tvListTitle, tv_sub_menu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listItem = itemView.findViewById(R.id.ll_list_group_item);
            ivListImg = itemView.findViewById(R.id.iv_list_group_image);
            tvListTitle = itemView.findViewById(R.id.tv_list_group_title);

            tv_sub_menu = itemView.findViewById(R.id.tv_sub_menu);

            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, ListItemActivity.class);
                    intent.putExtra("LIST_SEQ", Integer.parseInt(lists.get(getAdapterPosition()).getSeq()));
                    intent.putExtra("LIST_TYPE", lists.get(getAdapterPosition()).getType());
                    intent.putExtra("TYPE_INDEX", lists.get(getAdapterPosition()).getTypeIndex());
                    intent.putExtra("TITLE_NAME", lists.get(getAdapterPosition()).getTitleName());
                    Log.e(TAG, "LIST_SEQ " + lists.get(getAdapterPosition()).getSeq());
                    Log.e(TAG, "LIST_TYPE " + lists.get(getAdapterPosition()).getType());
                    Log.e(TAG, "TYPE_INDEX " + lists.get(getAdapterPosition()).getTypeIndex());
                    Log.e(TAG, "TITLE_NAME " + lists.get(getAdapterPosition()).getTitleName());
                    mContext.startActivity(intent);
                }
            });



        }
    }



}
