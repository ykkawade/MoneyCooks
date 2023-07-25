package com.example.moneycooks.budget;

public class TransactionValues {
    String type;
    String name;
    float amount;
    String date;
    String mode;

    public TransactionValues() {
    }

    public TransactionValues(String type, String name, float amount, String date, String mode) {
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
