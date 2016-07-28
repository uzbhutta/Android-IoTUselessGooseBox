package ca.m3dia.iotuselessgoosebox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class PresetListAdapter extends RecyclerView.Adapter {
    private Context mContext;

    public PresetListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_list_item, parent, false);
        return new PresetListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PresetListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return Common.presetTitles.length;
    }

    private class PresetListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView presetIndex;
        private TextView presetTitle;
        private TextView presetDescription;

        public PresetListViewHolder(View itemView) {
            super(itemView);
            presetIndex = (TextView) itemView.findViewById(R.id.presetIndex);
            presetTitle = (TextView) itemView.findViewById(R.id.presetTitle);
            presetDescription = (TextView) itemView.findViewById(R.id.presetDescription);

            itemView.setOnClickListener(this);
        }

        public void bindView(int pos) {
            int displayNum = pos + 1;
            presetIndex.setText(displayNum + "");
            presetTitle.setText(Common.presetTitles[pos]);
            presetDescription.setText(Common.presetDescriptions[pos]);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition() + 1;
            final String presetTrigger = pos + "";
            Toast.makeText(mContext, Common.presetTitles[getLayoutPosition()] + " triggered.", Toast.LENGTH_SHORT).show();

            new Thread() {
                @Override
                public void run() {
                    // Make the Particle call here
                    ArrayList<String> immediateAction = new ArrayList<>();
                    immediateAction.add(presetTrigger);

                    try {
                        Common.currDevice.callFunction("pre-test", immediateAction);

                    } catch (ParticleCloudException | ParticleDevice.FunctionDoesNotExistException | IOException e) {
                        e.printStackTrace();
                    }
                    immediateAction.clear();
                }
            }.start();

        }
    }
}
