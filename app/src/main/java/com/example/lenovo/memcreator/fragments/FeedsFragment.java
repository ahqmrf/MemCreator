package com.example.lenovo.memcreator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.adapters.FeedsAdapter;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Memory;

import java.util.ArrayList;

public class FeedsFragment extends Fragment {

    private MyDatabaseManager manager;
    private RecyclerView feeds;
    private Button refresh;
    private FeedsAdapter adapter;
    private ArrayList<Memory> itemList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        // TO DO

        manager = new MyDatabaseManager(getContext(), null, null, 1);
        feeds = (RecyclerView) view.findViewById(R.id.rv_feeds);

        itemList = manager.getMemory();

        for (int i = 0; i < itemList.size(); i++) {
            System.out.println(itemList.get(i));
        }

        feeds.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FeedsAdapter(getContext(), itemList);
        feeds.setAdapter(adapter);

        refresh = (Button) view.findViewById(R.id.btn_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        return view;
    }

    private void update() {
        itemList = manager.getMemory();
        adapter.updateList(itemList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }
}
