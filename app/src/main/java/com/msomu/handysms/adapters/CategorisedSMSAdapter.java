package com.msomu.handysms.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msomu.handysms.R;
import com.msomu.handysms.models.ProviderModel;

import java.util.List;

/**
 * Created by msomu on 13/03/16.
 */
public class CategorisedSMSAdapter extends RecyclerView.Adapter<CategorisedSMSAdapter.ViewHolder> implements View.OnClickListener {

    private List<ProviderModel> items;
    private OnItemClickListener onItemClickListener;

    public CategorisedSMSAdapter(List<ProviderModel> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyler, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProviderModel item = items.get(position);
        holder.text.setText(item.getProvider());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClick(v, (ProviderModel) v.getTag());
    }

    public interface OnItemClickListener {

        void onItemClick(View view, ProviderModel providerModel);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_provider);
        }
    }
}
