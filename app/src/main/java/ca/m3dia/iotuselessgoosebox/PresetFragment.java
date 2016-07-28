package ca.m3dia.iotuselessgoosebox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleDevice;

/**
 * Created by Umar Bhutta.
 */
public class PresetFragment extends Fragment {
    private TextView togglePresetTextView;
    private String json = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preset, container, false);
        setupList(view);

        togglePresetTextView = (TextView) view.findViewById(R.id.togglePresetTextView);

        togglePresetTextView.setText(Html.fromHtml("<u>Tap here</u> to set the box to Preset Mode. Toggling the switch will then randomly execute one of the preset actions. <br /><br />Alternatively, you can tap a preset action manually to execute it immediately."));

        togglePresetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create json from letters ArrayList
                json = "{\"type\":0, \"data\":[";

                int i = 0;
                for(String member : Common.presetTitles) {
                    json += "\"" + i + "\",";
                    i++;
                }

                json = json.substring(0, json.length() - 1);
                json += "]}";

                new Thread() {
                    @Override
                    public void run() {
                        // Make the Particle call here

                        ArrayList<String> jsonList = new ArrayList<>();
                        ArrayList<String> toggleType = new ArrayList<>();
                        jsonList.add(json);
                        toggleType.add("PRESET");

                        try {
                            int resultCode = Common.currDevice.callFunction("toggleType", toggleType);
                            Common.currDevice.callFunction("jsonParser", jsonList);

                            //capture resultCode from particle function to toast to user
                            if (resultCode == 0) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "Preset Mode now enabled.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "Failed to enable Preset Mode.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (ParticleCloudException | ParticleDevice.FunctionDoesNotExistException | IOException e) {
                            e.printStackTrace();
                        }
                        jsonList.clear();
                        toggleType.clear();
                    }
                }.start();
            }
        });



        return view;
    }

    private void setupList(View view) {
        //setup recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //setup adapter
        PresetListAdapter presetListAdapter = new PresetListAdapter(getActivity());
        //attach adapter to recycler view
        recyclerView.setAdapter(presetListAdapter);

        //set LayoutManager for recyclerView. Use vertical list
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //attach layout manager to recyclerView
        recyclerView.setLayoutManager(layoutManager);

        //divider lines
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
    }
}
