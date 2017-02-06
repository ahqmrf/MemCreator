package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.SelectPhotosActivity;
import com.example.lenovo.memcreator.models.Folder;
import com.example.lenovo.memcreator.widgets.SquareImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lenovo on 1/31/2017.
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.FolderViewHolder> {

    private Context context;
    private ArrayList<Folder> folderList;
    public ImageLoader imageLoader = ImageLoader.getInstance();

    public interface Callback {
        void onFolderClick(String path);
    }

    public FolderListAdapter(Context context, ArrayList<Folder> folderList, Callback callback) {
        this.context = context;
        this.folderList = folderList;
        mCallback = callback;
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

    private Callback mCallback;

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.folderName.setText(folder.getFolderName());
        File file = new File(folder.getIconPath());
        if(file.exists()) {
            String uri = Uri.fromFile(file).toString();
            String decoded = Uri.decode(uri);
            imageLoader.displayImage(decoded, holder.folderIcon);
        }
        else {
            holder.folderIcon.setImageResource(R.drawable.loading);
        }
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public FrameLayout folderLayout;
        public TextView folderName;
        public SquareImageView folderIcon;

        public FolderViewHolder(View itemView) {
            super(itemView);

            folderLayout = (FrameLayout) itemView.findViewById(R.id.folder_layout);
            folderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            folderIcon = (SquareImageView) itemView.findViewById(R.id.iv_folder_icon);
            folderLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.folder_layout :
                    mCallback.onFolderClick(folderList.get(getAdapterPosition()).getFolderPath());
                    break;
            }
        }
    }
}
