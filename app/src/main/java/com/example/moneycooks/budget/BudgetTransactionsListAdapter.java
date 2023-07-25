package com.example.moneycooks.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.moneycooks.R;

import java.util.ArrayList;

public class BudgetTransactionsListAdapter extends ArrayAdapter<TransactionValues> {

    public BudgetTransactionsListAdapter(@NonNull Context context, ArrayList<TransactionValues> transactionValuesArrayList) {
        super(context, R.layout.transaction_item, transactionValuesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TransactionValues transactionValues = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);

        TextView transactionNameTextView = convertView.findViewById(R.id.transaction_name_textview);
        TextView transactionAmountTextView = convertView.findViewById(R.id.transaction_amount);
        TextView transactionDateTextView = convertView.findViewById(R.id.transaction_date);
        TextView transactionModeTextView = convertView.findViewById(R.id.transaction_mode);

        transactionNameTextView.setText(transactionValues.name);
        transactionDateTextView.setText(transactionValues.date);
        transactionModeTextView.setText(transactionValues.mode);
        if (transactionValues.type.equalsIgnoreCase("Income")) {
            transactionAmountTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            transactionAmountTextView.setText(String.valueOf("+ $" + transactionValues.amount));
        } else {
            transactionAmountTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            transactionAmountTextView.setText(String.valueOf("- $" + transactionValues.amount));
        }
        return convertView;
    }
}