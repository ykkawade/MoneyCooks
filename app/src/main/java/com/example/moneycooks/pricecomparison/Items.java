package com.example.moneycooks.pricecomparison;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneycooks.R;
import com.example.moneycooks.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Items extends AppCompatActivity {
	private ListView items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_items);

		List<Item> itemAgents = new ArrayList<>();
		for (Pair<String, Integer> pair : PriceComparison.ITEMS) {
			String item = pair.getLeft();
			itemAgents.add(new Item(item));
		}
		this.items = findViewById(R.id.items_list);
		this.items.setAdapter(new ItemAdapter(this, 0, itemAgents));
	}
}