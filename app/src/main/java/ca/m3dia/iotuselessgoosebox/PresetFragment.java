package ca.m3dia.iotuselessgoosebox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by umarb_000 on 2016-07-20.
 */
public class PresetFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preset, container, false);

        //setup recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //setup adapter
        PresetListAdapter presetListAdapter = new PresetListAdapter();
        //attach adapter to recycler view
        recyclerView.setAdapter(presetListAdapter);

        //set LayoutManager for recyclerView. Use vertical list
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //attach layout manager to recyclerView
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}
