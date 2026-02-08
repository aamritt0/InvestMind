package com.example.investmind;

public class HistoryItem {
    private String type;
    private String title; // "Interesse Composto" etc.
    private long timestamp;
    private String amount; // Formatted currency string
    private String details; // Short description/subtitle
    private String calculationName; // Optional user-provided name
    private double principalAmount; // Numeric principal value for searching

    public HistoryItem(String type, String title, long timestamp, String amount, String details) {
        this(type, title, timestamp, amount, details, null, 0.0);
    }
    
    public HistoryItem(String type, String title, long timestamp, String amount, String details, String calculationName, double principalAmount) {
        this.type = type;
        this.title = title;
        this.timestamp = timestamp;
        this.amount = amount;
        this.details = details;
        this.calculationName = calculationName;
        this.principalAmount = principalAmount;
    }
    
    public String getType() { return type; }
    public String getTitle() { return title; }
    public long getTimestamp() { return timestamp; }
    public String getAmount() { return amount; }
    public String getDetails() { return details; }
    public String getCalculationName() { return calculationName; }
    public double getPrincipalAmount() { return principalAmount; }
}
