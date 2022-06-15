package com.ioad.todoth.adapter;

import android.content.Context;
import android.content.Intent;
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

    private Context mContext;
    private int layout = 0;
    private LayoutInflater inflater;
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
//        holder.ivListImg.setImageDrawable();
        holder.tvListTitle.setText(lists.get(position).getTitle());
//        holder.tvListCount.setText(lists.get(position).getCount());
//        holder.tvListTotal.setText(lists.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout listItem;
        public ImageView ivListImg;
        public TextView tvListTitle, tvListCount, tvListTotal;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listItem = itemView.findViewById(R.id.ll_list_item);
//            ivListImg = itemView.findViewById(R.id.iv_list_item_image);
            tvListTitle = itemView.findViewById(R.id.tv_list_title);
//            tvListCount = itemView.findViewById(R.id.tv_list_count);
//            tvListTotal = itemView.findViewById(R.id.tv_list_total);

            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, ListItemActivity.class);
                    intent.putExtra("LIST_NAME", lists.get(getAdapterPosition()).getTitle());
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
