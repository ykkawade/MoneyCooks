package com.example.moneycooks.budget;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Budget extends AppCompatActivity {
    private static final String TAG = "BUDGET ACTIVITY";
    private ProgressBar progressBar;
    private TextView availableBalanceTextView,
            monthlyBudgetTextView,
            monthlyBudgetLabel,
            progressBarEndValueTextView,
            incomeTextView,
            expenseTextView;
    private Button viewTransactionsButton;
    private DatabaseReference userDataBase;
    private DatabaseReference transactionDataBase;
    private BudgetValues budgetValues;
    private HashMap<String, TransactionValues> transactions = new HashMap<>();
    private TransactionValues transactionValues;
    private int numberOfTransactions = 1;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        availableBalanceTextView = findViewById(R.id.available_balance_text_view);
        monthlyBudgetLabel = findViewById(R.id.monthly_budget_label);
        monthlyBudgetTextView = findViewById(R.id.monthly_budget_text_view);
        progressBar = findViewById(R.id.progressBar);
        progressBarEndValueTextView = findViewById(R.id.progress_bar_end_value);
        incomeTextView = findViewById(R.id.income_text_view);
        expenseTextView = findViewById(R.id.expense_text_view);
        viewTransactionsButton = findViewById(R.id.view_transaction_button);

        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date date = new Date();
        Log.d("Month", dateFormat.format(date));
        String monthName = "Balance for " + dateFormat.format(date);
        monthlyBudgetLabel.setText(monthName);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        userDataBase = database.getReference().child("budgetvalues");
        userDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    budgetValues = ds.getValue(BudgetValues.class);
                }
                if (budgetValues == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Budget.this);
                    builder.setTitle("Enter Details");

                    View view = getLayoutInflater().inflate(R.layout.budget_first_time_user, null);

                    EditText totalBalance, monthlyBalance;
                    Button submitButton;

                    totalBalance = view.findViewById(R.id.total_balance);
                    monthlyBalance = view.findViewById(R.id.monthly_budget);
                    submitButton = view.findViewById(R.id.submit);

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String tempTotalBalance = totalBalance.getText().toString();
                            String tempMonthlyBalance = monthlyBalance.getText().toString();

                            transactionValues = new TransactionValues("null", "null", 0, date.toString(), "null");
                            transactions.put("null", transactionValues);
                            transactionDataBase = userDataBase.child("User_1");
                            budgetValues = new BudgetValues(Float.parseFloat(tempTotalBalance),
                                    Float.parseFloat(tempMonthlyBalance), Float.parseFloat(tempMonthlyBalance), 0, 0, transactions);
                            transactionDataBase.setValue(budgetValues);

                            dialog.dismiss();
                        }
                    });

                    builder.setView(view);
                    dialog = builder.create();
                    dialog.show();

                } else {
                    String totalBalanceString, totalMonthlyBudgetString, monthlyEndLimitString,
                            incomeString, expenseString;
                    totalBalanceString = String.format("%.2f", Math.abs(budgetValues.getAvailableBalance()));
                    totalMonthlyBudgetString = String.format("%.2f", Math.abs(budgetValues.getMonthlyBudget()));
                    incomeString = String.format("%.2f", Math.abs(budgetValues.getIncome()));
                    expenseString = String.format("%.2f", Math.abs(budgetValues.getExpense()));

                    if (budgetValues.getAvailableBalance() >= 0)
                        availableBalanceTextView.setText("$" + totalBalanceString);
                    else
                        availableBalanceTextView.setText("-$" + totalBalanceString);


                    if (budgetValues.getMonthlyBudget() >= 0)
                        monthlyBudgetTextView.setText("$" + totalMonthlyBudgetString);
                    else
                        monthlyBudgetTextView.setText("-$" + totalMonthlyBudgetString);

                    if (budgetValues.getIncome() >= 0)
                        incomeTextView.setText("$" + incomeString);
                    else
                        incomeTextView.setText("$0");


                    if (budgetValues.getExpense() >= 0)
                        expenseTextView.setText("$" + expenseString);
                    else
                        expenseTextView.setText("$0");


                    monthlyEndLimitString = String.format("%.2f", budgetValues.getMonthlyEndLimit());


                    progressBar.setMax((int) budgetValues.getMonthlyEndLimit());
                    progressBar.setProgress((int) (budgetValues.getMonthlyEndLimit() - budgetValues.getMonthlyBudget()));
                    progressBarEndValueTextView.setText("$" + monthlyEndLimitString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });

        viewTransactionsButton.setOnClickListener(view -> startActivity(new Intent(Budget.this, BudgetTransactions.class)));
    }
}
