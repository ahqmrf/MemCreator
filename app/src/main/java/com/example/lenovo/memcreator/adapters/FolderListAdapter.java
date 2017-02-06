package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.models.Folder;
import com.example.lenovo.memcreator.widgets.SquareImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/31/2017.
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.FolderViewHolder> {

    private DisplayImageOptions displayImageOptions;
    private Context context;
    private ArrayList<Folder> folderList;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public interface Callback {
        void onFolderClick(String path);
    }

    public FolderListAdapter(Context context, ArrayList<Folder> folderList, Callback callback) {
        this.context = context;
        this.folderList = folderList;
        mCallback = callback;
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.loading)
                .showImageOnFail(R.drawable.loading)
                .build();
    }

    private Callback mCallback;

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.folderName.setText(folder.getFolderName());
        imageLoader.displayImage(folder.getIconPathUri(), holder.folderIcon, displayImageOptions);

    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public FrameLayout folderLayout;
        public TextView folderName;
        public SquareImageView folderIcon;
        public int size;

        public FolderViewHolder(View itemView, final ViewGroup folderListView) {
            super(itemView);

            folderLayout = (FrameLayout) itemView.findViewById(R.id.folder_layout);
            folderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            folderIcon = (SquareImageView) itemView.findViewById(R.id.iv_folder_icon);
            folderLayout.setOnClickListener(this);
            ViewTreeObserver vto = folderListView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    folderListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    size = folderListView.getMeasuredWidth() / 3;
                    folderIcon.getLayoutParams().height = folderIcon.getLayoutParams().width = size;
                }
            });

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
