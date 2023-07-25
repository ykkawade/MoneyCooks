package com.example.moneycooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Info extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		try {
			// Update MoneyCooks info
			TextView info = findViewById(R.id.moneycooks_info);
			info.setText(read(R.raw.info));

			TextView budgetInfo = findViewById(R.id.budgeting_info);
			budgetInfo.setText(read(R.raw.budgeting_info));

			TextView priceCompInfo = findViewById(R.id.price_comp_info);
			priceCompInfo.setText(read(R.raw.pricec_info));

			TextView scamsInfo = findViewById(R.id.scams_info);
			scamsInfo.setText(read(R.raw.scams_info));
		} catch (IOException e) { }
	}

	private String read(int id) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(id)));
		StringBuilder builder = new StringBuilder();

		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			} else {
				if (line.isEmpty()) {
					builder.append("\n\n");
				} else {
					builder.append(line);
				}
			}
		}
		return builder.toString();
	}
}