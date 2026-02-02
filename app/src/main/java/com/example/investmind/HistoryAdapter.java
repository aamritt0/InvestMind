package com.example.investmind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryItem> items = new ArrayList<>();
    private List<HistoryItem> filteredItems = new ArrayList<>();

    public void setItems(List<HistoryItem> items) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }
    
    public void filter(String query) {
        if (query.isEmpty()) {
            filteredItems = new ArrayList<>(items);
        } else {
            filteredItems.clear();
            for (HistoryItem item : items) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                    item.getDetails().toLowerCase().contains(query.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = filteredItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAmount.setText(item.getAmount());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(item.getTimestamp())));

        if (item.getType() != null && item.getType().contains("Simple")) {
            holder.ivIcon.setImageResource(R.drawable.ic_percent);
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_trending);
        }
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
