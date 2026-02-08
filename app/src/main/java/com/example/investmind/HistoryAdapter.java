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
            String lowerQuery = query.toLowerCase();
            
            // Strip common currency symbols for numeric search
            String numericQuery = query.replaceAll("[€$£¥₹\\s,.]+", "");
            
            for (HistoryItem item : items) {
                boolean matches = false;
                
                // Search by title
                if (item.getTitle().toLowerCase().contains(lowerQuery)) {
                    matches = true;
                }
                
                // Search by details
                if (item.getDetails().toLowerCase().contains(lowerQuery)) {
                    matches = true;
                }
                
                // Search by calculation name
                if (item.getCalculationName() != null && 
                    item.getCalculationName().toLowerCase().contains(lowerQuery)) {
                    matches = true;
                }
                
                // Search by principal amount (numeric) - strip currency symbols
                if (item.getPrincipalAmount() > 0 && !numericQuery.isEmpty()) {
                    String principalStr = String.valueOf((int)item.getPrincipalAmount());
                    if (principalStr.contains(numericQuery)) {
                        matches = true;
                    }
                }
                
                // Also search in the formatted amount string (with currency)
                if (item.getAmount() != null) {
                    String amountStripped = item.getAmount().replaceAll("[€$£¥₹\\s,.]+", "");
                    if (!numericQuery.isEmpty() && amountStripped.contains(numericQuery)) {
                        matches = true;
                    }
                    if (item.getAmount().toLowerCase().contains(lowerQuery)) {
                        matches = true;
                    }
                }
                
                if (matches) {
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
        
        // Show calculation name if available, otherwise show title
        if (item.getCalculationName() != null && !item.getCalculationName().isEmpty()) {
            holder.tvCalculationName.setText(item.getCalculationName());
            holder.tvCalculationName.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(item.getTitle());
        } else {
            holder.tvCalculationName.setVisibility(View.GONE);
            holder.tvTitle.setText(item.getTitle());
        }
        
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
        TextView tvTitle, tvDate, tvAmount, tvCalculationName;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCalculationName = itemView.findViewById(R.id.tvCalculationName);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
