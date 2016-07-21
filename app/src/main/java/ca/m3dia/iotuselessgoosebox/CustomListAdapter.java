package ca.m3dia.iotuselessgoosebox;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Datatellit1 on 7/21/2016.
 */
public class CustomListAdapter extends RecyclerView.Adapter {
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
        return Common.presetTitles.length;
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
        }

        public void bindView(int pos) {
            int displayNum = pos + 1;
            customIndex.setText(displayNum + "");
            customTitle.setText(Common.presetTitles[pos]);
            customDescription.setText(Common.presetDescriptions[pos]);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
