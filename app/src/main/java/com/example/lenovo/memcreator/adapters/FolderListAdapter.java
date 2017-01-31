package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.SelectPhotosActivity;
import com.example.lenovo.memcreator.models.Folder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/31/2017.
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.FolderViewHolder> {

    private Context context;
    private ArrayList<Folder> folderList;

    public interface Callback {
        public void onFolderClick(String path);
    }

    public FolderListAdapter(Context context, ArrayList<Folder> folderList, Callback callback) {
        this.context = context;
        this.folderList = folderList;
        mCallback = callback;
    }

    private Callback mCallback;

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int size = width / 3 - 15;
        holder.folderLayout.getLayoutParams().width = size;
        holder.folderLayout.getLayoutParams().height = size;
        Folder folder = folderList.get(position);
        holder.folderName.setText(folder.getFolderName());

        Picasso.with(context).load("file:" + folder.getIconPath()).resize(size, size).into(holder.folderIcon);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public FrameLayout folderLayout;
        public TextView folderName;
        public ImageView folderIcon;

        public FolderViewHolder(View itemView) {
            super(itemView);

            folderLayout = (FrameLayout) itemView.findViewById(R.id.folder_layout);
            folderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            folderIcon = (ImageView) itemView.findViewById(R.id.iv_folder_icon);
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

        private void showFolderImages(String folderDirectory) {
            Intent intent = new Intent(context, SelectPhotosActivity.class);
            intent.putExtra("folder", folderDirectory);
            context.startActivity(intent);
        }
    }
}
