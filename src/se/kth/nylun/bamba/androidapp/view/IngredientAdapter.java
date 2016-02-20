package se.kth.nylun.bamba.androidapp.view;

import java.util.List;

import se.kth.nylun.bamba.androidapp.IngredientActivity;
import se.kth.nylun.bamba.androidapp.R;
import se.kth.nylun.bamba.androidapp.R.id;
import se.kth.nylun.bamba.androidapp.R.layout;
import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IngredientAdapter extends ArrayAdapter<Ingredient>{

	private List<Ingredient> items;
	private IngredientActivity parent;

	public IngredientAdapter(Context context, int textViewResourceId,
			List<Ingredient> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		parent = (IngredientActivity) context; 	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View view = convertView;
		
		//Inflate view
		if(view == null){
			LayoutInflater inflater = 
					(LayoutInflater) getContext()
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.ingredient_list_item_view, null);
		}
		
		//Fill the list view
		Ingredient i = items.get(position);
		
		if(i != null){
			
			TextView name = (TextView) view.findViewById(R.id.ingredient_item_name);
			if(name != null)
				name.setText(i.getName());
			
			TextView desc = (TextView) view.findViewById(R.id.ingredient_item_desc);
			if(desc != null)
				desc.setText(i.getDescription());
			
			EditText quantity = (EditText) view.findViewById(R.id.ingredient_item_quantity);
			
			TextView unit = (TextView) view.findViewById(R.id.ingredient_item_unit);
			if(unit != null)
				unit.setText(i.getUnitString());
			
			Button add = (Button) view.findViewById(R.id.ingredient_item_add);
			if(add != null)
				add.setOnClickListener(new IngredientListener(i, quantity));
			
		}
		
		return view;
		
	}
	
	private class IngredientListener implements OnClickListener{

		private Ingredient ingredient;
		private EditText quantity;
		
		public IngredientListener(Ingredient i, EditText quantity){
			ingredient = i;
			this.quantity = quantity;
		}
		
		@Override
		public void onClick(View v) {
			String s = quantity.getText().toString();
			if(!s.isEmpty()){
				//Parse quantity
				double d = Double.parseDouble(s);
				ingredient.addQuantity(d);
				Log.i("Test", Double.toString(ingredient.getQuantity()));
				//Add to shoppinglist
				parent.addItemToShoppingList(ingredient);
				
				//Reset ingredient
				quantity.setText("");
				ingredient.setQuantity(0);
			}
			
			
		}
		
	}
	
}
