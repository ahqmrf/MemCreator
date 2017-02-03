package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.FullImageActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Lenovo on 1/26/2017.
 */

public class CandidatePhotoListAdapter extends RecyclerView.Adapter<CandidatePhotoListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> itemList;
    private Set<Integer> selectedIndices = new TreeSet<>();
    public ImageLoader imageLoader = ImageLoader.getInstance();

    public CandidatePhotoListAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .writeDebugLogs() // Remove for release app
                .build();
        imageLoader.init(config);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    public TreeSet<Integer> getSelectedIndices() {
        return (TreeSet<Integer>) selectedIndices;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_photo_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String photo = itemList.get(position);
        int width= context.getResources().getDisplayMetrics().widthPixels / 3 - 20;

        holder.layout.getLayoutParams().height = width;
        holder.layout.getLayoutParams().width = width;
        String uri = Uri.fromFile(new File(photo)).toString();
        String decoded = Uri.decode(uri);
        imageLoader.displayImage(decoded, holder.candidatePhotoIV);
        //Picasso.with(context).load("file:" + photo).placeholder(R.drawable.loading).resize(width, width).into(holder.candidatePhotoIV);
        //holder.candidatePhotoIV.setImageBitmap(Jury.getSuitableBitmap(photo, 150, 150));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout layout;
        public ImageView candidatePhotoIV;
        public CheckBox box;

        public MyViewHolder(View itemView) {
            super(itemView);

            layout = (FrameLayout) itemView.findViewById(R.id.candidate_photo_layout);
            candidatePhotoIV = (ImageView) itemView.findViewById(R.id.candidate_photo);

            candidatePhotoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("image_path", itemList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            box = (CheckBox) itemView.findViewById(R.id.btn_select);

            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox)v;
                    if(checkBox.isChecked()) {
                        selectedIndices.add(getAdapterPosition());
                    }
                    else {
                        if(selectedIndices.contains(getAdapterPosition())) {
                            selectedIndices.remove(getAdapterPosition());
                        }
                    }
                }
            });
        }
    }
}
