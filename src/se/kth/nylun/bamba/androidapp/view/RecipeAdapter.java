package se.kth.nylun.bamba.androidapp.view;

import java.util.List;


import se.kth.nylun.bamba.androidapp.R;
import se.kth.nylun.bamba.androidapp.R.id;
import se.kth.nylun.bamba.androidapp.R.layout;
import se.kth.nylun.bamba.model.Recipe;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecipeAdapter extends ArrayAdapter<Recipe>{
	
	private List<Recipe> recipes;

	public RecipeAdapter(Context context, int textViewResourceId,
			List<Recipe> recipes) {
		super(context, textViewResourceId, recipes);
		this.recipes = recipes;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View view = convertView;
		
		//Inflate view
		if(view == null){
			LayoutInflater inflater = 
					(LayoutInflater) getContext()
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.recipe_list_item_view, null);
		}
		
		//Fill the list view
		Recipe r = recipes.get(position);
		
		if(r != null){
			
			TextView name = (TextView) view.findViewById(R.id.name_text);
			if(name != null)
				name.setText(r.getName());
			
			TextView desc = (TextView) view.findViewById(R.id.desc_text);
			if(desc != null)
				desc.setText(r.getDescription());
			
		}
		
		return view;
		
	}

}
