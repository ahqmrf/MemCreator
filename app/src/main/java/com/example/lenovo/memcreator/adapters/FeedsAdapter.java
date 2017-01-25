package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.EditingMemoryActivity;
import com.example.lenovo.memcreator.activities.PromptDeleteConfirmationActivity;
import com.example.lenovo.memcreator.activities.ViewMemory;
import com.example.lenovo.memcreator.objects.Memory;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/20/2017.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Memory> itemList;

    public FeedsAdapter(Context context, ArrayList<Memory> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    public void updateList(ArrayList<Memory> newItemList) {
        itemList.clear();
        itemList = newItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.memory_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(itemList.size() > 0) {
            Memory memory = itemList.get(position);
            if(memory.getIcon() != null) {
                holder.memoryIcon.setImageBitmap(BitmapFactory.decodeFile(memory.getIcon()));
            } else {
                holder.memoryIcon.setImageResource(R.drawable.moments);
            }
            holder.memoryTitle.setText(memory.getName());
            holder.memoryDescription.setText(memory.getText());


            StringBuilder builder = new StringBuilder();
            String tokens[] = memory.getDate().split("-");
            builder.append(tokens[2] + "/");
            builder.append(tokens[1] + "/");
            builder.append(tokens[0].charAt(2) + "" + tokens[0].charAt(3));

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView memoryIcon;
        public TextView memoryTitle;
        public TextView memoryDescription;
        public Button moreBtn;
        public LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            memoryIcon = (ImageView) itemView.findViewById(R.id.memory_icon);
            memoryTitle = (TextView) itemView.findViewById(R.id.memory_title);
            memoryDescription = (TextView) itemView.findViewById(R.id.memory_description);
            moreBtn = (Button) itemView.findViewById(R.id.btn_more);
            layout = (LinearLayout) itemView.findViewById(R.id.item);
            layout.setOnClickListener(this);
            moreBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item :
                    viewMemory(itemList.get(getAdapterPosition()));
                    break;
                case R.id.btn_more:
                    showPopUp();
                    break;
            }
        }

        private void showPopUp() {
            PopupMenu popupMenu = new PopupMenu(context, layout);
            popupMenu.inflate(R.menu.item_context_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.mi_view:
                            viewMemory(itemList.get(getAdapterPosition()));
                            break;
                        case R.id.mi_edit:
                            editMemory(itemList.get(getAdapterPosition()));
                            break;
                        case R.id.mi_delete:
                            deleteMemory(itemList.get(getAdapterPosition()));
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

        private void viewMemory(Memory memory) {
            Intent intent = new Intent(context, ViewMemory.class);
            intent.putExtra("memory_object", memory);
            context.startActivity(intent);
        }

        private void editMemory(Memory memory) {
            Intent intent = new Intent(context, EditingMemoryActivity.class);
            intent.putExtra("memory", memory);
            context.startActivity(intent);
        }

        private void deleteMemory(Memory memory) {
            Intent intent = new Intent(context, PromptDeleteConfirmationActivity.class);
            intent.putExtra("memory", memory);
            context.startActivity(intent);
        }
    }
}
