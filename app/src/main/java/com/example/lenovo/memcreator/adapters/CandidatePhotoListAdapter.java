package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.FullImageActivity;
import com.example.lenovo.memcreator.models.PhotoItem;
import com.example.lenovo.memcreator.widgets.SquareImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Lenovo on 1/26/2017.
 */

public class CandidatePhotoListAdapter extends RecyclerView.Adapter<CandidatePhotoListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PhotoItem> itemList;
    private Set<Integer> selectedPositions;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImageOptions;

    public CandidatePhotoListAdapter(Context context, ArrayList<PhotoItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.loading)
                .showImageOnFail(R.drawable.loading)
                .build();
        selectedPositions = new TreeSet<>();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Set<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_photo_item, parent, false);
        return new MyViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PhotoItem photo = itemList.get(position);
        imageLoader.displayImage(photo.getPathUri(), holder.candidatePhotoIV, displayImageOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
            }
        });

        holder.box.setChecked(photo.isSelected());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout layout;
        public SquareImageView candidatePhotoIV;
        public CheckBox box;
        public int size;

        public MyViewHolder(final View itemView, final ViewGroup photoList) {
            super(itemView);

            layout = (FrameLayout) itemView.findViewById(R.id.candidate_photo_layout);
            candidatePhotoIV = (SquareImageView) itemView.findViewById(R.id.candidate_photo);

            candidatePhotoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("image_path", itemList.get(getAdapterPosition()).getPath());
                    context.startActivity(intent);
                }
            });


            box = (CheckBox) itemView.findViewById(R.id.btn_select);

            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemList.get(getAdapterPosition()).setSelected(isChecked);
                    if (isChecked) {
                        selectedPositions.add(getAdapterPosition());
                        box.setBackgroundResource(R.drawable.ic_check_box_black_24dp);
                    } else {
                        selectedPositions.remove(getAdapterPosition());
                        box.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                    }
                }
            });

            ViewTreeObserver vto = photoList.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    photoList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    size = photoList.getMeasuredWidth() / 2;
                    candidatePhotoIV.getLayoutParams().height = candidatePhotoIV.getLayoutParams().width = size;
                }
            });
        }
    }
}
