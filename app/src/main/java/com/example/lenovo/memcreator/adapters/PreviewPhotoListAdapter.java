package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.FullImageActivity;
import com.example.lenovo.memcreator.widgets.SquareImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lenovo on 2/1/2017.
 */

public class PreviewPhotoListAdapter extends RecyclerView.Adapter<PreviewPhotoListAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<String> itemList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImageOptions;

    public PreviewPhotoListAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.loading)
                .showImageOnFail(R.drawable.loading)
                .build();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.preview_photo_item, parent, false);
        return new MyViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String photo = itemList.get(position);
        imageLoader.displayImage(Uri.fromFile(new File(photo)).toString(), holder.previewPhotoIV, displayImageOptions);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout layout;
        public SquareImageView previewPhotoIV;
        public int size;

        public MyViewHolder(View itemView, final ViewGroup listView) {
            super(itemView);

            layout = (FrameLayout) itemView.findViewById(R.id.preview_photo_layout);
            previewPhotoIV = (SquareImageView) itemView.findViewById(R.id.preview_photo);

            previewPhotoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("image_path", itemList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            ViewTreeObserver vto = listView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    size = listView.getMeasuredWidth() / 3;
                    previewPhotoIV.getLayoutParams().height = previewPhotoIV.getLayoutParams().width = size;
                }
            });

        }
    }
}
