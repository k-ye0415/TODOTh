package com.ioad.todoth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private int typeIndex;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private String status;

    public ListAddAdapter(Context mContext, int layout, ArrayList<Item> items, ClickCallbackListener listener) {
        this.mContext = mContext;
        this.layout = layout;
        this.items = items;
        this.callbackListener = listener;
        this.status = "insert";
    }

    public ListAddAdapter(Context mContext, int layout, ArrayList<Item> items, ClickCallbackListener listener, int typeIndex) {
        this.mContext = mContext;
        this.layout = layout;
        this.items = items;
        this.callbackListener = listener;
        this.typeIndex = typeIndex;
        this.status = "update";
        Log.e(TAG, "index " + typeIndex);
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
        holder.ivItemImage.setImageResource(items.get(position).getImage());
//        holder.tvItemText.setText(items.get(position).getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mSelectedItems.get(position, false)) {
                holder.llListGroup.setBackgroundColor(mContext.getColor(R.color.point));
            } else {
                holder.llListGroup.setBackgroundColor(mContext.getColor(R.color.base));
            }
        }
    }

    int temp;

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llListGroup;
        public ImageView ivItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (status.equals("update")) {
                updateItemSelected(typeIndex);
                temp = typeIndex;
                status = "";
            }
            llListGroup = itemView.findViewById(R.id.ll_list_group);
            ivItemImage = itemView.findViewById(R.id.iv_group_image);


            llListGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();


                    if (mSelectedItems.size() >= 1) {
                        mSelectedItems.clear();
                        toggleItemSelected(position);
                        notifyItemChanged(temp);
                        temp = position;
                    } else {
                        temp = position;
                    }

                    if (status.equals("update")) {
                        position = typeIndex;
                        typeIndex = 0;
                        toggleItemSelected(position);
                        status = "";
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
