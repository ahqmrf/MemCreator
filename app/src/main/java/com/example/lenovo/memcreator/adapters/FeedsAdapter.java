package com.example.lenovo.memcreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.activities.ViewMemory;
import com.example.lenovo.memcreator.objects.Memory;
import com.squareup.picasso.Picasso;

import java.io.File;
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
            if(memory.getPics() != null) {
                holder.memoryIcon.setImageBitmap(BitmapFactory.decodeFile(memory.getPics()));
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


            holder.memoryDate.setText(builder.toString());
            holder.memoryTime.setText(memory.getTime());
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
        public TextView memoryDate;
        public TextView memoryTime;
        public LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            memoryIcon = (ImageView) itemView.findViewById(R.id.memory_icon);
            memoryTitle = (TextView) itemView.findViewById(R.id.memory_title);
            memoryDescription = (TextView) itemView.findViewById(R.id.memory_description);
            memoryDate = (TextView) itemView.findViewById(R.id.memory_date);
            memoryTime = (TextView) itemView.findViewById(R.id.memory_time);
            layout = (LinearLayout) itemView.findViewById(R.id.item);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item :
                    viewMemory(itemList.get(getAdapterPosition()));
                    break;
            }
        }

        private void viewMemory(Memory memory) {
            Intent intent = new Intent(context, ViewMemory.class);
            intent.putExtra("memory_object", memory);
            context.startActivity(intent);
        }
    }
}
