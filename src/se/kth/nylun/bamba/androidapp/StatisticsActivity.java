package se.kth.nylun.bamba.androidapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import se.kth.nylun.bamba.androidapp.model.db.ShoppingListDB;
import se.kth.nylun.bamba.model.Ingredient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class StatisticsActivity extends Activity{
	
	private ShoppingListDB db;
	
	private ArrayList<Ingredient> frequencyItems;
	private ArrayList<Date> shoppingDates;
	
	private TextView frequencyList;
	private TextView averageShoppingDates;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_statistics);
		
		db = new ShoppingListDB(this.getApplicationContext());
		
		frequencyList = (TextView) findViewById(R.id.statistics_frequency_list);
		averageShoppingDates = (TextView) findViewById(R.id.statistics_avg_days_shopping_txt);
		
		try {
			frequencyItems = db.getMostFrequentItems();
			shoppingDates = db.getShoppingDates();
		} catch (Exception e) {
			Log.e("DB",""+e.getMessage());
		}
		
		//Fill list with items
		String s = "";
		for(int i = 0; i<frequencyItems.size(); i++){
			s += i+1 +": ";
			s += frequencyItems.get(i).getName();
			s += "\n";
		}
		frequencyList.setText(s);
		
		//Figure out intervall of shopping
		final long ONE_DAY = 24*60*60*1000;
		Collections.sort(shoppingDates);
		long totalTimeDiff = shoppingDates.get(shoppingDates.size()-1).getTime() - shoppingDates.get(0).getTime();
		double averageTimeDiff = totalTimeDiff / shoppingDates.size();

		averageShoppingDates.setText(Double.toString(averageTimeDiff / ONE_DAY));
		
	}

}
