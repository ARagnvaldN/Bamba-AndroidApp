package se.kth.nylun.bamba.androidapp.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import se.kth.nylun.bamba.androidapp.R;
import se.kth.nylun.bamba.androidapp.R.id;
import se.kth.nylun.bamba.androidapp.R.layout;
import se.kth.nylun.bamba.model.Ingredient;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShoppingListAdapter extends ArrayAdapter<Ingredient> {

	private ArrayList<Ingredient> items;
	private SparseBooleanArray checkedItems;
	
	public ShoppingListAdapter( Context context,
								int textViewResourceId, 
								ArrayList<Ingredient> items){
		super(context, textViewResourceId, items);
		this.items = items;
		Collections.sort(items);
		checkedItems = new SparseBooleanArray();
	}
	
	public void toggleItem(int pos){

		checkedItems.put(pos, !checkedItems.get(pos));
		Log.i("LIST",Boolean.toString(checkedItems.get(pos)));
	}
	
	public ArrayList<Ingredient> getCheckedItems(){
		
		ArrayList<Ingredient> checked_items = new ArrayList<Ingredient>();
		
		for(int i=0;i<items.size();i++){
			if(checkedItems.get(i))
				checked_items.add(items.get(i));
		}
		
		return checked_items;
		
	}
	
	public ArrayList<Ingredient> getUnCheckedItems(){
		
		ArrayList<Ingredient> unchecked_items = new ArrayList<Ingredient>();
		
		for(int i=0;i<items.size();i++){
			if(!checkedItems.get(i))
				unchecked_items.add(items.get(i));
		}
		
		return unchecked_items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View view = convertView;
		
		//Inflate view
		if(view == null){
			LayoutInflater inflater = 
					(LayoutInflater) getContext()
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.shoppinglist_list_item_view, null);
		}
		
		//Fill the list view
		Ingredient i = items.get(position);
		
		if(i != null){
			
			TextView name = (TextView) view.findViewById(R.id.name_text);
			if(name != null)
				name.setText(i.getName());
			
			TextView desc = (TextView) view.findViewById(R.id.desc_text);
			if(desc != null)
				desc.setText(i.getDescription());
			
			TextView quantity = (TextView) view.findViewById(R.id.quantity_text);
			if(quantity != null)
				quantity.setText( Double.toString(i.getQuantity()) );
			
			TextView unit = (TextView) view.findViewById(R.id.unit_text);
			if(unit != null)
				unit.setText(i.getUnitString());
		}
		
		//Set background colour
		if(checkedItems.get(position))
			view.setBackgroundColor(Color.CYAN);
		else 
			view.setBackgroundColor(Color.WHITE);
		
		return view;
		
	}
	
	
	
}
