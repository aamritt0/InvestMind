package com.example.investmind;

public class HistoryItem {
    private String type;
    private String title; // "Interesse Composto" etc.
    private long timestamp;
    private String amount; // Formatted currency string
    private String details; // Short description/subtitle

    public HistoryItem(String type, String title, long timestamp, String amount, String details) {
        this.type = type;
        this.title = title;
        this.timestamp = timestamp;
        this.amount = amount;
        this.details = details;
    }
    
    public String getType() { return type; }
    public String getTitle() { return title; }
    public long getTimestamp() { return timestamp; }
    public String getAmount() { return amount; }
    public String getDetails() { return details; }
}
