package com.ioad.todoth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
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
import java.util.Arrays;

public class ListAddAdapter extends RecyclerView.Adapter<ListAddAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private int layout = 0;
    private ArrayList<Item> items;
    private ClickCallbackListener callbackListener;
    private boolean isClick = true;
    private int titleIndex = 0;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private String status;

    public ListAddAdapter(Context mContext, int layout, ArrayList<Item> items, ClickCallbackListener listener) {
        this.mContext = mContext;
        this.layout = layout;
        this.items = items;
        this.callbackListener = listener;
        this.status = "insert";
    }

    public ListAddAdapter(Context mContext, int layout, ArrayList<Item> items, ClickCallbackListener listener, int index) {
        this.mContext = mContext;
        this.layout = layout;
        this.items = items;
        this.callbackListener = listener;
        this.titleIndex = index;
        this.status = "update";
        Log.e(TAG, "index " + titleIndex);
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
        Log.e(TAG, "onBindViewHolder");
        holder.ivItemImage.setImageResource(items.get(position).getImage());
        holder.tvItemText.setText(items.get(position).getName());

        if (mSelectedItems.get(position, false)) {
            holder.ivItemImage.setBackgroundColor(Color.BLUE);
        } else {
            holder.ivItemImage.setBackgroundColor(Color.WHITE);
        }
    }

    int temp;

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
            Log.e(TAG, "ViewHolder");

            if (titleIndex != 0) {
                updateItemSelected(titleIndex);
//            toggleItemSelected(position);
                temp = titleIndex;

            }
            cvLayout = itemView.findViewById(R.id.cv_layout);
            ivItemImage = itemView.findViewById(R.id.iv_list_add_item_image);
            tvItemText = itemView.findViewById(R.id.tv_list_item_text);


            ivItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();


                    if (mSelectedItems.size() >= 1) {
//                        mSelectedItems.delete(temp);
                        mSelectedItems.clear();
//                        mSelectedItems.put(position, false);
                        toggleItemSelected(position);
                        notifyItemChanged(temp);
                        temp = position;
                    } else {
                        temp = position;
                    }

                    if (titleIndex != 0) {
                        position = titleIndex;
                        titleIndex = 0;
                        toggleItemSelected(position);
                    } else {
                        toggleItemSelected(position);
                    }

                    callbackListener.callBack(position, status);
                }
            });

        }
    }


    private void toggleItemSelected(int position) {
        if (mSelectedItems.get(position, false) == true) {
            mSelectedItems.delete(position);
            notifyItemChanged(position);
        } else {
            mSelectedItems.put(position, true);
            notifyItemChanged(position);
        }
    }

    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }

    private void updateItemSelected(int position) {
        mSelectedItems.put(position, true);
    }

}
