package com.msomu.handysms.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msomu.handysms.R;
import com.msomu.handysms.models.SmsDataClass;

import java.util.List;

/**
 * Created by msomu on 13/03/16.
 */
public class SmsViewAdapter extends RecyclerView.Adapter<SmsViewAdapter.ViewHolder> implements View.OnClickListener {

    private List<SmsDataClass> items;
    private OnItemClickListener onItemClickListener;

    public SmsViewAdapter(List<SmsDataClass> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_item_recyler, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SmsDataClass item = items.get(position);
        holder.text.setText(item.getBody());
        holder.date.setText("" + item.getDate());
        holder.sender.setText(item.getAddress());
        holder.typeOfSMS.setText(item.getTypeOfCateogry());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickListener.onItemClick(v, (SmsDataClass) v.getTag());
                }
            }, 200);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, SmsDataClass viewModel);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView date;
        public TextView sender;
        public TextView typeOfSMS;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_body);
            date = (TextView) itemView.findViewById(R.id.date);
            sender = (TextView) itemView.findViewById(R.id.sender);
            typeOfSMS = (TextView) itemView.findViewById(R.id.typeOfSMS);
        }
    }
}
