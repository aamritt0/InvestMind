# Documentation: History Feature Implementation

## Overview
This document outlines the implementation of the "Storico" (History) feature in the InvestMind application. The feature allows users to view a persistent list of their past calculations, filter them by search, and navigate seamlessly between the Home and History screens.

## 1. File Changes & Additions

### New Files
*   **`com.example.investmind.HistoryItem.java`**: A data model class representing a single calculation record. It holds fields for:
    *   `type`: The type of calculation (e.g., "Interesse Semplice", "Interesse Composto").
    *   `title`: Display title.
    *   `timestamp`: Time of calculation (saved as milliseconds).
    *   `amount`: The formatted result string (e.g., "€12.450,00").
    *   `details`: A summary of inputs (e.g., "€10.000 + 5.0% per 10 anni").

*   **`com.example.investmind.HistoryManager.java`**: A utility class handling persistent storage.
    *   **Mechanism**: Uses Android's `SharedPreferences` to store data.
    *   **Serialization**: Converts the list of `HistoryItem` objects into a JSON string using `org.json.JSONArray` and `JSONObject` for storage, and parses it back when retrieving.
    *   **Key Methods**: 
        *   `saveHistoryItem(Context, HistoryItem)`: Adds a new item to the top of the list and saves.
        *   `getHistoryItems(Context)`: Retrieves and parses the full list of items.

*   **`com.example.investmind.HistoryActivity.java`**: The activity responsible for the History screen.
    *   Initializes the `RecyclerView`.
    *   Sets up the `BottomNavigationView`.
    *   Implements the search logic to filter the adapter.

*   **`com.example.investmind.HistoryAdapter.java`**: The `RecyclerView.Adapter` that binds `HistoryItem` data to the UI views.
    *   Includes a `filter(String query)` method to support real-time searching by title or details.

*   **Resources (Layouts & Drawables)**:
    *   `res/layout/activity_history.xml`: The main layout matching the Tailwind mockup (Title, Rounded Search Bar, RecyclerView).
    *   `res/layout/item_history.xml`: The layout for individual history rows, using `MaterialCardView` with rounded corners.
    *   `res/drawable/ic_search.xml`: Vector icon for the search bar.
    *   `res/drawable/ic_percent.xml`: Vector icon for Simple Interest items.
    *   `res/drawable/bg_search_rounded.xml`: Background drawable for the search container.
    *   `res/drawable/bg_circle.xml`: Background for the icon container within history items.

### Modified Files
*   **`AndroidManifest.xml`**: Registered `HistoryActivity` so the app can navigate to it.
*   **`MainActivity.java`**: Added `BottomNavigationView` logic to handle navigation to the `HistoryActivity`.
*   **`SimpleCalcActivity.java` & `CompoundCalcActivity.java`**:
    *   Updated the `Intent` creating `ResultActivity` to include a new extra: `"CALC_TYPE"`. This tells the result screen which type of calculation was just performed.
*   **`ResultActivity.java`**:
    *   Extracted the input data (Principal, Rate, Years) and the result.
    *   Constructed a formatted `details` string.
    *   Automatically calls `HistoryManager.saveHistoryItem(...)` whenever the result screen is loaded.

## 2. How it Works

### The Storage Mechanism
We avoided complex databases (like Room or SQLite) for this scope, opting for a lightweight and efficient solution using **SharedPreferences**.

1.  **Saving**:
    *   When `ResultActivity` starts, it creates a `HistoryItem` object.
    *   `HistoryManager` reads the existing JSON string from SharedPreferences.
    *   It parses this JSON into a List, adds the new item to index 0 (top of the list), and converts the List back to a JSON string.
    *   This string is written back to SharedPreferences.

2.  **Loading**:
    *   `HistoryActivity` asks `HistoryManager` for the items.
    *   The manager reads the JSON string, parses it into `HistoryItem` objects, and returns the List.
    *   The `HistoryAdapter` displays these items.

### Navigation Logic
*   The `BottomNavigationView` is present in both `MainActivity` and `HistoryActivity`.
*   To prevent a "stack" of activities from building up (pressing back 10 times to exit), we use `Intent` flags or simple logic:
    *   From Home to History: Starts `HistoryActivity`.
    *   From History to Home: Starts `MainActivity` with `FLAG_ACTIVITY_REORDER_TO_FRONT` (or similar logic) to return to the existing home instance.
*   `overridePendingTransition(0, 0)` is used to disable the default slide animation, making the tab switch feel instant and native.

### Search Logic
*   The search bar in `HistoryActivity` has a `TextWatcher`.
*   As you type, `onTextChanged` calls `adapter.filter(text)`.
*   The adapter maintains two lists: `items` (all data) and `filteredItems` (what is currently shown).
*   It loops through `items` and checks if the Title or Details contain the search text, updating the `RecyclerView` instantly.
