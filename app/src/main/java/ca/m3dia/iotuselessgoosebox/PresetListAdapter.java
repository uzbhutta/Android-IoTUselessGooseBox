package ca.m3dia.iotuselessgoosebox;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Datatellit1 on 7/21/2016.
 */
public class PresetListAdapter extends RecyclerView.Adapter {
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
            presetTitle = (TextView) itemView.findViewById(R.id.preserTitle);
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

        }
    }
}
