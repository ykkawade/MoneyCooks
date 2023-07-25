package com.example.moneycooks.pricecomparison;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moneycooks.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
	private Context context;
	private List<Item> items;

	public ItemAdapter(@NonNull Context context, int resource, List<Item> items) {
		super(context, resource, items);

		this.context = context;
		this.items = new ArrayList<>(items);
	}

	@Override
	public View getView(int i, View view, ViewGroup group) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.items_list, null);
		}
		Item item = this.items.get(i);

		ImageView image = view.findViewById(R.id.item_image);
		TextView text = view.findViewById(R.id.item_text);

		image.setImageResource(PriceComparison.ITEMS.get(i).getRight());
		text.setText(item.item());

		return view;
	}
}

class Item {
	private String item;

	public Item(String item) {
		this.item = item;
	}

	public String item() {
		return this.item;
	}
}