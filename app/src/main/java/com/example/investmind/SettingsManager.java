package com.example.investmind;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class SettingsManager {
    private static final String PREF_NAME = "investmind_settings";
    private static final String KEY_CURRENCY = "currency_code";
    private static final String KEY_DECIMAL_SEP = "decimal_separator";
    private static final String KEY_THOUSANDS_SEP = "thousands_separator";
    private static final String KEY_TEXT_INPUT_ENABLED = "text_input_enabled";
    
    // Default values
    private static final String DEFAULT_CURRENCY = "EUR";
    private static final String DEFAULT_DECIMAL_SEP = ",";
    private static final String DEFAULT_THOUSANDS_SEP = ".";
    private static final boolean DEFAULT_TEXT_INPUT_ENABLED = false;
    
    private final SharedPreferences prefs;
    
    public SettingsManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    // Currency settings
    public String getCurrencyCode() {
        return prefs.getString(KEY_CURRENCY, DEFAULT_CURRENCY);
    }
    
    public void setCurrencyCode(String currencyCode) {
        prefs.edit().putString(KEY_CURRENCY, currencyCode).apply();
    }
    
    public Currency getCurrency() {
        try {
            return Currency.getInstance(getCurrencyCode());
        } catch (IllegalArgumentException e) {
            return Currency.getInstance(DEFAULT_CURRENCY);
        }
    }
    
    public String getCurrencySymbol() {
        return getCurrency().getSymbol();
    }
    
    // Number format settings
    public String getDecimalSeparator() {
        return prefs.getString(KEY_DECIMAL_SEP, DEFAULT_DECIMAL_SEP);
    }
    
    public void setDecimalSeparator(String separator) {
        prefs.edit().putString(KEY_DECIMAL_SEP, separator).apply();
    }
    
    public String getThousandsSeparator() {
        return prefs.getString(KEY_THOUSANDS_SEP, DEFAULT_THOUSANDS_SEP);
    }
    
    public void setThousandsSeparator(String separator) {
        prefs.edit().putString(KEY_THOUSANDS_SEP, separator).apply();
    }
    
    // Text input settings
    public boolean isTextInputEnabled() {
        return prefs.getBoolean(KEY_TEXT_INPUT_ENABLED, DEFAULT_TEXT_INPUT_ENABLED);
    }
    
    public void setTextInputEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_TEXT_INPUT_ENABLED, enabled).apply();
    }
    
    // Formatting helpers
    public NumberFormat getCurrencyFormat() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol(getCurrencySymbol());
        symbols.setDecimalSeparator(getDecimalSeparator().charAt(0));
        symbols.setGroupingSeparator(getThousandsSeparator().charAt(0));
        
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        }
        
        return format;
    }
    
    public NumberFormat getCurrencyFormatNoDecimals() {
        NumberFormat format = getCurrencyFormat();
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);
        return format;
    }
    
    public String formatCurrency(double amount) {
        return getCurrencyFormat().format(amount);
    }
    
    public String formatCurrencyNoDecimals(double amount) {
        return getCurrencyFormatNoDecimals().format(amount);
    }
    
    public DecimalFormat getNumberFormat(int maxDecimals) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(getDecimalSeparator().charAt(0));
        symbols.setGroupingSeparator(getThousandsSeparator().charAt(0));
        
        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(symbols);
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(maxDecimals);
        format.setMinimumFractionDigits(0);
        
        return format;
    }
    
    // Parse number from text considering user's decimal separator
    public double parseNumber(String text) throws NumberFormatException {
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Empty input");
        }
        
        // Remove thousands separators
        String cleaned = text.replace(getThousandsSeparator(), "");
        
        // Replace decimal separator with standard dot
        cleaned = cleaned.replace(getDecimalSeparator(), ".");
        
        // Remove any currency symbols or spaces
        cleaned = cleaned.replaceAll("[^0-9.-]", "");
        
        return Double.parseDouble(cleaned);
    }
}
