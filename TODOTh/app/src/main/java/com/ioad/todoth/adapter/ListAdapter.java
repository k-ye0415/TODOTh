package com.ioad.todoth.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ioad.todoth.R;
import com.ioad.todoth.activity.ListItemActivity;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.Util;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private int layout = 0;
    private ArrayList<List> lists;

    public ListAdapter(Context mContext, int layout, ArrayList<List> lists) {
        this.mContext = mContext;
        this.layout = layout;
        this.lists = lists;
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
        holder.ivListImg.setImageResource(Util.listImage[lists.get(position).getTypeIndex()]);
        holder.tvListTitle.setText(lists.get(position).getTitleName());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout listItem;
        public ImageView ivListImg;
        public TextView tvListTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listItem = itemView.findViewById(R.id.ll_list_item);
            ivListImg = itemView.findViewById(R.id.iv_list_item_image);
            tvListTitle = itemView.findViewById(R.id.tv_list_title);

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
