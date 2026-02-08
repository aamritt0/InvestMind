package com.example.investmind;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final String PREF_NAME = "investmind_history";
    private static final String KEY_HISTORY = "history_items";

    public static void saveHistoryItem(Context context, HistoryItem item) {
        List<HistoryItem> items = getHistoryItems(context);
        items.add(0, item); // Add to top
        saveItems(context, items);
    }

    public static List<HistoryItem> getHistoryItems(Context context) {
        List<HistoryItem> items = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_HISTORY, "[]");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                
                // Support backward compatibility - old items won't have these fields
                String calculationName = obj.optString("calculationName", null);
                double principalAmount = obj.optDouble("principalAmount", 0.0);
                
                items.add(new HistoryItem(
                    obj.getString("type"),
                    obj.getString("title"),
                    obj.getLong("timestamp"),
                    obj.getString("amount"),
                    obj.getString("details"),
                    calculationName,
                    principalAmount
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    private static void saveItems(Context context, List<HistoryItem> items) {
        JSONArray array = new JSONArray();
        for (HistoryItem item : items) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("type", item.getType());
                obj.put("title", item.getTitle());
                obj.put("timestamp", item.getTimestamp());
                obj.put("amount", item.getAmount());
                obj.put("details", item.getDetails());
                
                // Add new fields
                if (item.getCalculationName() != null && !item.getCalculationName().isEmpty()) {
                    obj.put("calculationName", item.getCalculationName());
                }
                if (item.getPrincipalAmount() > 0) {
                    obj.put("principalAmount", item.getPrincipalAmount());
                }
                
                array.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_HISTORY, array.toString()).apply();
    }
}
