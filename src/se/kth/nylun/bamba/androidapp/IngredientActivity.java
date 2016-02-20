package se.kth.nylun.bamba.androidapp;

import java.util.ArrayList;

import se.kth.nylun.bamba.androidapp.model.db.ShoppingListDB;
import se.kth.nylun.bamba.androidapp.model.handler.ResourceHandler;
import se.kth.nylun.bamba.androidapp.model.handler.ShoppingListHandler;
import se.kth.nylun.bamba.androidapp.view.IngredientAdapter;
import se.kth.nylun.bamba.model.Ingredient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;


public class IngredientActivity extends Activity {
	
	private ShoppingListHandler shoppingList;
	private ShoppingListDB db;
	
	private ListView ingredientList;
	private ArrayList<Ingredient> ingredients;
	private IngredientAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingredient);
		
		shoppingList = ShoppingListHandler.getInstance();
		db = new ShoppingListDB(this);
		
		ingredientList = (ListView) findViewById(R.id.ingredientList);
		
	}
	
	
	public void addItemToShoppingList(Ingredient i){
		shoppingList.addItemAndSave(i, db);
		showToast("+ "+ Double.toString(i.getQuantity()) +" "+ i.getUnitString() +" "+ i.getName());
	}
	
	protected void onStart(){
		super.onStart();
		
		try {
			ingredients = ResourceHandler.getAllIngredients(true);
		} catch (Exception e) {
			Log.e("NET"," "+e.getMessage());
		}
		adapter = new IngredientAdapter(this,0,ingredients);
		
		ingredientList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingredient, menu);
		return true;
	}
	
	private void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
		}
	

}