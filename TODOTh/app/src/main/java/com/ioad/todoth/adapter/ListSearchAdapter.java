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

import java.util.ArrayList;

public class ListSearchAdapter extends RecyclerView.Adapter<ListSearchAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private int layout = 0;
    private ArrayList<List> lists;

    public ListSearchAdapter(Context mContext, int layout, ArrayList<List> lists) {
        this.mContext = mContext;
        this.layout = layout;
        this.lists = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvContent.setText(lists.get(position).getContent());
        holder.tvTitle.setText(lists.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llSearch;
        public ImageView ivImage;
        public TextView tvContent, tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llSearch = itemView.findViewById(R.id.ll_search_item);
            ivImage = itemView.findViewById(R.id.iv_search_image);
            tvContent = itemView.findViewById(R.id.tv_search_content);
            tvTitle = itemView.findViewById(R.id.tv_search_title);

            llSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(mContext, ListItemActivity.class);
                    intent.putExtra("LIST_TYPE",lists.get(position).getType());
                    mContext.startActivity(intent);
                }
            });


        }
    }
}
