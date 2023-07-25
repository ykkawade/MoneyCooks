package com.example.moneycooks.budget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneycooks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TooManyListenersException;

public class BudgetTransactions extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseReference db;
    private BudgetValues budgetValues;
    private ListView transactionListView;
    private Button addTransaction;
    private AlertDialog dialog;
    private String TAG = "BUDGET_ACTIVITY";
    private int numberOfTransactions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_transactions);

        transactionListView = findViewById(R.id.list_view_transactions);
        addTransaction = findViewById(R.id.add_transaction);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        db = database.getReference().child("budgetvalues");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    budgetValues = ds.getValue(BudgetValues.class);
                }
                if (budgetValues != null) {
                    budgetValues.getTransactionValues().remove("null");
                    ArrayList<TransactionValues> transactionValuesArrayList = new ArrayList<>(budgetValues.getTransactionValues().values());

                    ArrayList<TransactionValues> revArrayList = new ArrayList<>();
                    for (int i = transactionValuesArrayList.size() - 1; i >= 0; i--) {

                        // Append the elements in reverse order
                        revArrayList.add(transactionValuesArrayList.get(i));
                    }

                    if (transactionValuesArrayList.size() < 1) {
                        //Log.i(TAG, " In if Bracket " + transactionValuesArrayList.get(0).name);
                        transactionListView.setVisibility(View.GONE);
                    } else {
                        transactionListView.setVisibility(View.VISIBLE);
                        BudgetTransactionsListAdapter budgetTransactionsListAdapter = new BudgetTransactionsListAdapter(
                                BudgetTransactions.this, transactionValuesArrayList);
                        transactionListView.setAdapter(budgetTransactionsListAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BudgetTransactions.this);
                builder.setTitle("Enter Transaction Details");

                View addTransactionView = getLayoutInflater().inflate(R.layout.budget_add_transaction, null);
                Spinner addTransactionType = addTransactionView.findViewById(R.id.add_transaction_type);
                ArrayAdapter adapter = ArrayAdapter.createFromResource(BudgetTransactions.this, R.array.transaction_types,
                        android.R.layout.simple_dropdown_item_1line);
                addTransactionType.setAdapter(adapter);
                addTransactionType.setOnItemSelectedListener(BudgetTransactions.this);

                EditText addTransactionName = addTransactionView.findViewById(R.id.add_transaction_name);
                EditText addTransactionAmount = addTransactionView.findViewById(R.id.add_transaction_amount);
                EditText addTransactionDate = addTransactionView.findViewById(R.id.add_transaction_date);
                Spinner addTransactionMode = addTransactionView.findViewById(R.id.add_transaction_mode);

                ArrayAdapter adapterMode = ArrayAdapter.createFromResource(BudgetTransactions.this, R.array.transaction_mode,
                        android.R.layout.simple_dropdown_item_1line);
                addTransactionMode.setAdapter(adapterMode);
                addTransactionMode.setOnItemSelectedListener(BudgetTransactions.this);
                addTransactionDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // on below line we are getting
                        // the instance of our calendar.
                        final Calendar c = Calendar.getInstance();

                        // on below line we are getting
                        // our day, month and year.
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        // on below line we are creating a variable for date picker dialog.
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                // on below line we are passing context.
                                BudgetTransactions.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // on below line we are setting date to our edit text.
                                        addTransactionDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                },
                                // on below line we are passing year,
                                // month and day for selected date in our date picker.
                                year, month, day);
                        // at last we are calling show to
                        // display our date picker dialog.
                        datePickerDialog.show();
                    }
                });

                Button submitButton = addTransactionView.findViewById(R.id.add_transaction_submit);

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db = database.getReference().child("budgetvalues");

                        ArrayList<TransactionValues> transactionValuesArrayList = new ArrayList<>(budgetValues.getTransactionValues().values());
                        numberOfTransactions = transactionValuesArrayList.size();
                        TransactionValues transactionValues = new TransactionValues(
                                addTransactionType.getSelectedItem().toString(),
                                addTransactionName.getText().toString(),
                                Float.parseFloat(addTransactionAmount.getText().toString()),
                                addTransactionDate.getText().toString(),
                                addTransactionMode.getSelectedItem().toString()
                        );
                        db.child("User_1")
                                .child("transactionValues")
                                .child("T_" + numberOfTransactions)
                                .setValue(transactionValues);
                        float income = budgetValues.getIncome();
                        float expense = budgetValues.getExpense();
                        float availableBalance = budgetValues.getAvailableBalance();
                        float monthlyBudget = budgetValues.getMonthlyBudget();
                        if (addTransactionType.getSelectedItem().toString().equalsIgnoreCase("Income")) {
                            db.child("User_1")
                                    .child("income")
                                    .setValue(income
                                            + Float.parseFloat(addTransactionAmount
                                            .getText().toString()));

                            db.child("User_1")
                                    .child("availableBalance")
                                    .setValue(availableBalance
                                            + Float.parseFloat(addTransactionAmount
                                            .getText().toString()));

                        } else {
                            db.child("User_1")
                                    .child("expense")
                                    .setValue(
                                            expense + Float.parseFloat(addTransactionAmount
                                                    .getText().toString()));

                            db.child("User_1")
                                    .child("monthlyBudget")
                                    .setValue(monthlyBudget
                                            - Float.parseFloat(addTransactionAmount
                                            .getText().toString()));

                            db.child("User_1")
                                    .child("availableBalance")
                                    .setValue(availableBalance
                                            - Float.parseFloat(addTransactionAmount
                                            .getText().toString()));
                        }

                        dialog.dismiss();
                    }
                });
                builder.setView(addTransactionView);
                dialog = builder.create();
                dialog.show();
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView = (TextView) view;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
