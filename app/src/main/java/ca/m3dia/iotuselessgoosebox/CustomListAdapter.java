package ca.m3dia.iotuselessgoosebox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;

/**
 * Created by Umar Bhutta.
 */
public class CustomListAdapter extends RecyclerView.Adapter {

    Context mContext;

    public CustomListAdapter(Context context) {
        mContext = context;
    }

    public void delete(int pos) {
        CustomFragment.name.remove(pos);
        CustomFragment.info.remove(pos);
        CustomFragment.letters.remove(pos);

        notifyItemRemoved(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, parent, false);
        return new CustomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CustomListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return CustomFragment.name.size();
    }

    private class CustomListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView customIndex;
        private TextView customTitle;
        private TextView customDescription;
        private ImageView customDelete;

        public CustomListViewHolder(View itemView) {
            super(itemView);
            customIndex = (TextView) itemView.findViewById(R.id.customIndex);
            customTitle = (TextView) itemView.findViewById(R.id.customTitle);
            customDescription = (TextView) itemView.findViewById(R.id.customDescription);
            customDelete = (ImageView) itemView.findViewById(R.id.customDelete);

            itemView.setOnClickListener(this);
            customDelete.setOnClickListener(this);
        }

        public void bindView(int pos) {
            int displayNum = pos + 1;
            customIndex.setText(displayNum + "");
            customTitle.setText(CustomFragment.name.get(pos));
            customDescription.setText(CustomFragment.info.get(pos));
        }

        @Override
        public void onClick(View v) {
            if (v == customDelete) {
                delete(getLayoutPosition());

                Toast.makeText(mContext, "Custom item " + (getLayoutPosition() + 1) +  " has been deleted.", Toast.LENGTH_SHORT).show();
            } else {
                final String sequence = CustomFragment.letters.get(getLayoutPosition());
                Toast.makeText(mContext, sequence + " triggered.", Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        // Make the Particle call here
                        ArrayList<String> immediateAction = new ArrayList<>();
                        immediateAction.add(sequence);

                        try {
                            ParticleCloudSDK.getCloud().logIn("umar.bhutta@hotmail.com", "560588123rocks");
                            ParticleDevice currDevice = ParticleCloudSDK.getCloud().getDevice("1e003d001747343337363432");

                            currDevice.callFunction("cus-test", immediateAction);

                        } catch (ParticleCloudException | ParticleDevice.FunctionDoesNotExistException | IOException e) {
                            e.printStackTrace();
                        }
                        immediateAction.clear();
                    }
                }.start();
            }
        }
    }
}
