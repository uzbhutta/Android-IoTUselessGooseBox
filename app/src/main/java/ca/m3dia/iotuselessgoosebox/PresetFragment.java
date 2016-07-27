package ca.m3dia.iotuselessgoosebox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;

/**
 * Created by umarb_000 on 2016-07-20.
 */
public class PresetFragment extends Fragment {
    private TextView togglePresetTextView;
    private String json = "";
    ParticleDevice currDevice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preset, container, false);
        setupList(view);

        togglePresetTextView = (TextView) view.findViewById(R.id.togglePresetTextView);
        togglePresetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create json from letters ArrayList
                json = "{\"type\":0, \"data\":[";

                for(String member : Common.presetTitles) {
                    json += "\"" + member + "\",";
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
                            ParticleCloudSDK.getCloud().logIn("umar.bhutta@hotmail.com", "560588123rocks");
                            currDevice = ParticleCloudSDK.getCloud().getDevice("31001c000e47343432313031");

                            int resultCode = currDevice.callFunction("toggleType", toggleType);
                            currDevice.callFunction("jsonParser", jsonList);

                            //capture resultCode from particle function to toast to user
                            if (resultCode == 1) {
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
    }
}
