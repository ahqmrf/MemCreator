package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.FullImageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2/1/2017.
 */

public class PreviewPhotoListAdapter extends RecyclerView.Adapter<PreviewPhotoListAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<String> itemList;

    public PreviewPhotoListAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.preview_photo_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String photo = itemList.get(position);
        int width= context.getResources().getDisplayMetrics().widthPixels / 3 - 20;

        holder.layout.getLayoutParams().height = width;
        holder.layout.getLayoutParams().width = width;
        Picasso.with(context).load("file:" + photo).resize(width, width).into(holder.previewPhotoIV);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout layout;
        public ImageView previewPhotoIV;

        public MyViewHolder(View itemView) {
            super(itemView);

            layout = (FrameLayout) itemView.findViewById(R.id.preview_photo_layout);
            previewPhotoIV = (ImageView) itemView.findViewById(R.id.preview_photo);

            previewPhotoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("image_path", itemList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
            
        }
    }
}