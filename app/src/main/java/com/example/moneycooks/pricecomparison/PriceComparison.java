package com.example.moneycooks.pricecomparison;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneycooks.R;
import com.example.moneycooks.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceComparison extends AppCompatActivity {
	public static final List<Pair<String, Integer>> ITEMS = new ArrayList<>();

	private static final List<Integer> IMAGES = Arrays.asList(
			R.drawable.smartwater_1l, R.drawable.smartwater_1_5l, R.drawable.smartwater_750ml, R.drawable.smartwater_20oz, R.drawable.smartwater_23_7,
			R.drawable.dasani_20oz, R.drawable.dasani_1l, R.drawable.aha_12oz, R.drawable.aha_16oz,
			R.drawable.soda_20oz, R.drawable.sprite_20oz, R.drawable.coca_cola_20oz, R.drawable.fanta_20oz, R.drawable.dr_pepper_2l, R.drawable.dr_pepper_20oz,
			R.drawable.monster_energy, R.drawable.reign_energy, R.drawable.croissant, R.drawable.chocolate_croissant, R.drawable.cheese_danish, R.drawable.muffin,
			R.drawable.doritos_nc_3_12oz, R.drawable.doritos_nc_2_75oz, R.drawable.doritos_nc_9_25oz, R.drawable.doritos_cr_9_25oz, R.drawable.cheetos_2_75oz, R.drawable.cheetos_8_5oz,
			R.drawable.pizza_pie, R.drawable.waffles, R.drawable.gold_peak_tea, R.drawable.honest_tea, R.drawable.peace_tea, R.drawable.fuze_tea,
			R.drawable.fairlife_yup, R.drawable.fairlife_core_power, R.drawable.vitaminwater, R.drawable.minute_maid, R.drawable.simply_juice, R.drawable.powerade,
			R.drawable.trolli, R.drawable.chex_mix_8_75oz, R.drawable.chex_mix_3_75oz, R.drawable.wrap, R.drawable.salad, R.drawable.sandwich, R.drawable.side_salad,
			R.drawable.pbnj, R.drawable.cheese_and_fruit, R.drawable.cruidite, R.drawable.hummus_and_pita, R.drawable.fruit_cup, R.drawable.dessert_cup, R.drawable.snack_pack,
			R.drawable.yogurt_parfait, R.drawable.cereal_cup, R.drawable.kettle_chips_7_5oz, R.drawable.kettle_chips_2oz, R.drawable.popcorners, R.drawable.snyders_pretzels,
			R.drawable.stacys_chips, R.drawable.cheezit_4_5oz, R.drawable.cheezit_3oz, R.drawable.cheezit_7oz, R.drawable.nabisco_ritz, R.drawable.kraft_velveeta,
			R.drawable.pringles_5_5oz, R.drawable.pringles_2_3oz, R.drawable.cup_noodles, R.drawable.hals_chips, R.drawable.regular_chips,
			R.drawable.blistex, R.drawable.chapstick, R.drawable.tums, R.drawable.advil_4pk, R.drawable.advil_20ct, R.drawable.aleve, R.drawable.allergy_24hr,
			R.drawable.sinus_multi, R.drawable.mucinex, R.drawable.dayquil, R.drawable.old_spice, R.drawable.trojan_condoms, R.drawable.dove_shampoo, R.drawable.irish_spring,
			R.drawable.suave_shampoo, R.drawable.alcohol_wipes, R.drawable.peroxide, R.drawable.always_10ct, R.drawable.always_pads, R.drawable.qtips, R.drawable.tampax,
			R.drawable.edge_shave_cream, R.drawable.tide, R.drawable.clorox, R.drawable.downy, R.drawable.bounce, R.drawable.toilet_paper, R.drawable.paper_towel,
			R.drawable.tide_pen, R.drawable.notebook, R.drawable.pens, R.drawable.mechanical_pencils
	);

	private EditText inputItem;
	private TextView bestPrice, pacePrice, wholeFoodsPrice, cvsPrice;
	private Button viewItemsButton;

	private Map<String, Double> paceItems, wholeFoodsItems, cvsItems;
	private String item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price_comparison);

		this.inputItem = findViewById(R.id.input_item);

		this.viewItemsButton = findViewById(R.id.view_items_button);

		this.bestPrice = findViewById(R.id.best_price);
		this.pacePrice = findViewById(R.id.pace_price);
		this.wholeFoodsPrice = findViewById(R.id.whole_foods_price);
		this.cvsPrice = findViewById(R.id.cvs_price);

		this.inputItem.setOnKeyListener((view, i, event) -> {
			if (event.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
				this.item = findItem(this.inputItem.getText().toString());

				if (!this.item.equalsIgnoreCase("None")) {
					String store = bestStore(this.item);

					this.bestPrice.setText(this.item + "\n\n" + "at " + store + " for $" + store(store).get(this.item));

					this.pacePrice.setText(price(this.item, "Pace"));
					this.wholeFoodsPrice.setText(price(this.item, "Whole Foods"));
					this.cvsPrice.setText(price(this.item, "CVS"));
				} else {
					this.bestPrice.setText("N/A");
					this.pacePrice.setText("N/A");
					this.wholeFoodsPrice.setText("N/A");
					this.cvsPrice.setText("N/A");
				}
				return true;
			}
			return false;
		});
		this.viewItemsButton.setOnClickListener(view -> startActivity(new Intent(PriceComparison.this, Items.class)));

		this.paceItems = new HashMap<>();
		this.wholeFoodsItems = new HashMap<>();
		this.cvsItems = new HashMap<>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.prices)));
			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				String[] split = line.split(",");
				double pacePrice = Double.valueOf(split[1]), wholeFoodsPrice = Double.valueOf(split[2]), cvsPrice = Double.valueOf(split[3]);

				ITEMS.add(Pair.of(split[0], R.drawable.grey_box));

				this.paceItems.put(split[0], pacePrice);
				this.wholeFoodsItems.put(split[0], wholeFoodsPrice);
				this.cvsItems.put(split[0], cvsPrice);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setItemToImage();
	}

	/**
	 * This function will attempt to create a list of items that match the input.
	 *
	 * @param input
	 * @return List<String> items of available items that match.
	 */
	private List<String> findItems(String input) {
		List<String> validItems = new ArrayList<>();

		String[] keywords = input.split(" ");

		for (Pair<String, Integer> pair : ITEMS) {
			String item = pair.getLeft();
			boolean containsKey = true;

			for (String key : keywords) {
				if (containsKey && !item.toLowerCase().contains(key.toLowerCase())) {
					containsKey = false;
					break;
				}
			}
			if (containsKey) {
				validItems.add(item);
			}
		}
		return validItems;
	}

	/**
	 * This function will return the item at the upmost priority of the list of matching items
	 * we obtain when we run the findItems function.
	 *
	 * @param input
	 * @return the most prioritized item in the data file, otherwise, None
	 */
	private String findItem(String input) {
		return findItems(input).size() > 0 ? findItems(input).get(0) : "None";
	}

	/**
	 * This function attempts to find the best store for the best price of the inputted item.
	 *
	 * Note: The items listed in the prices data file is organized in a way where the best item
	 * for best price is listed at the top of the priority if inputted size of item is not
	 * specified.
	 * @param item
	 * @return
	 */
	private String bestStore(String item) {
		double paceItem = this.paceItems.get(item), wfItem = this.wholeFoodsItems.get(item), cvsItem = this.cvsItems.get(item);
		boolean paceHas = paceItem > -1, wfHas = wfItem > -1, cvsHas = cvsItem > -1;

		boolean wfEqualToCVS = String.valueOf(wfItem).equalsIgnoreCase(String.valueOf(cvsItem));

		if (wfHas && cvsHas) {
			if (paceHas) {
				if (wfItem < paceItem && cvsItem < paceItem) {
					if (!wfEqualToCVS) {
						if (wfItem < paceItem && wfItem < cvsItem) {
							return "Whole Foods";
						}
					}
					return "CVS";
				} else if (wfItem < paceItem && cvsItem > paceItem) {
					return "Whole Foods";
				} else if (wfItem > paceItem && cvsItem < paceItem) {
					return "CVS";
				}
			} else {
				if (wfEqualToCVS) {
					return "CVS";
				}
			}
		} else if (!wfHas && cvsHas) {
			if (paceHas) {
				if (cvsItem < paceItem) {
					return "CVS";
				}
			} else {
				return "CVS";
			}
		} else if (wfHas && !cvsHas) {
			if (paceHas) {
				if (wfItem < paceItem) {
					return "Whole Foods";
				}
			} else {
				return "Whole Foods";
			}
		}
		return "Pace";
	}

	/**
	 * Updates each Pair within ITEMS so that the ImageView id for each item
	 * is correctly associated with it.
	 */
	private void setItemToImage() {
		for (int i = 0; i < ITEMS.size(); i++) {
			if (i == IMAGES.size()) {
				break;
			}
			ITEMS.get(i).setRight(IMAGES.get(i));
		}
	}

	/**
	 * This function converts a String version of a store to its HashMap.
	 *
	 * @param store
	 * @return Map<String, Double> store in which the String variant represents it.
	 */
	private Map<String, Double> store(String store) {
		if (store.equalsIgnoreCase("Pace")) {
			return this.paceItems;
		} else if (store.equalsIgnoreCase("Whole Foods")) {
			return this.wholeFoodsItems;
		} else if (store.equalsIgnoreCase("CVS")) {
			return this.cvsItems;
		}
		return null;
	}

	private String price(String item, String store) {
		return store(store).get(item) > -1 ? store(store).get(item) + "" : "None";
	}
}