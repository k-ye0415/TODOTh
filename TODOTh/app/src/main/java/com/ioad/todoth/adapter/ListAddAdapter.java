package com.ioad.todoth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ioad.todoth.R;
import com.ioad.todoth.bean.Item;
import com.ioad.todoth.common.ClickCallbackListener;

import java.util.ArrayList;

public class ListAddAdapter extends RecyclerView.Adapter<ListAddAdapter.ViewHolder> {

    private Context mContext;
    private int layout = 0;
    private ArrayList<Item> items;
    private LayoutInflater inflater;
    private ClickCallbackListener callbackListener;


    public ListAddAdapter(Context mContext, int layout, ArrayList<Item> items, ClickCallbackListener listener) {
        this.mContext = mContext;
        this.layout = layout;
        this.items = items;
        this.callbackListener = listener;
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
//        holder.ivItemImage.setImageDrawable(items.get(position).getImage());
        holder.tvItemText.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivItemImage;
        public TextView tvItemText;
        public CardView cvLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvLayout = itemView.findViewById(R.id.cv_layout);
            ivItemImage = itemView.findViewById(R.id.iv_list_add_item_image);
            tvItemText = itemView.findViewById(R.id.tv_list_item_text);


            cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    cvLayout.setBackgroundColor(0x0000000);
                    callbackListener.callBack(getAdapterPosition());
                }
            });

        }
    }
}
