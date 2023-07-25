package com.example.moneycooks.budget;

import java.util.HashMap;

public class BudgetValues {
    float availableBalance;
    float monthlyBudget;
    float monthlyEndLimit;
    float income;
    float expense;

    HashMap<String, TransactionValues> transactionValues;
    public BudgetValues() {
    }

    public BudgetValues(float availableBalance, float monthlyBudget, float monthlyEndLimit, float income,
                        float expense, HashMap<String, TransactionValues> transactionValues) {
        this.availableBalance = availableBalance;
        this.monthlyBudget = monthlyBudget;
        this.monthlyEndLimit = monthlyEndLimit;
        this.income = income;
        this.expense = expense;
        this.transactionValues = transactionValues;
    }

    public float getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(float availableBalance) {
        this.availableBalance = availableBalance;
    }

    public float getMonthlyBudget() {
        return monthlyBudget;
    }

    public HashMap<String, TransactionValues> getTransactionValues() {
        return transactionValues;
    }

    public void setTransactionValues(HashMap<String, TransactionValues> transactionValues) {
        this.transactionValues = transactionValues;
    }

    public void setMonthlyBudget(float monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public float getMonthlyEndLimit() {
        return monthlyEndLimit;
    }

    public void setMonthlyEndLimit(float monthlyEndLimit) {
        this.monthlyEndLimit = monthlyEndLimit;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getExpense() {
        return expense;
    }

    public void setExpense(float expense) {
        this.expense = expense;
    }
}
