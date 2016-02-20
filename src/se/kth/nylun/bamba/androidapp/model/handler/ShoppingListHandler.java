package se.kth.nylun.bamba.androidapp.model.handler;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.util.Log;
import se.kth.nylun.bamba.androidapp.R;
import se.kth.nylun.bamba.androidapp.R.id;
import se.kth.nylun.bamba.androidapp.model.db.ShoppingListDB;
import se.kth.nylun.bamba.androidapp.view.ShoppingListAdapter;
import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;
import se.kth.nylun.bamba.model.ShoppingList;

public class ShoppingListHandler {

	private static final ShoppingListHandler instance = new ShoppingListHandler();
	public static ShoppingListHandler getInstance(){return instance;}
	
	private ShoppingList shoppingList;
	
	protected void add(Recipe r){
		shoppingList.addRecipe(r);
	}
	
	public void finishShopping(ArrayList<Ingredient> checked,
							   ArrayList<Ingredient> unchecked, ShoppingListDB db){
		
		Log.i("SHOPPINGLISTHANDLER:finishShopping","Shopping finished!");
		//Save checked items
		shoppingList.setItems(checked);
		save(db);
		
		//Create a new shoppingList
		shoppingList = new ShoppingList();
		shoppingList.setItems(unchecked);
		save(db);
		
	}
	
	public void save(ShoppingListDB db){
		if(shoppingList == null)
			return;
		
		Log.i("DB","DB Saved!");
		
		//Insert new/Save persistent copy of the shoppinglist
		if(shoppingList.getId() == 0)
			db.insertShoppingList(shoppingList);
		else
			db.saveShoppingList(shoppingList);
		
	}
	
	public void load(ShoppingListDB db) throws InterruptedException, ExecutionException{
		
		//Load persistent shoppinglist
		shoppingList = db.mostRecentList();
		
		Log.i("DB","DB Loaded!");
		
		//If DB is empty, create new list
		if(shoppingList == null){
			shoppingList = new ShoppingList();
			Log.i("","NEW SHOPPINGLIST");
		}

	}
	
	public ShoppingListAdapter getAdapter(Context context){
		return new ShoppingListAdapter(context, R.id.shoppingListView, shoppingList.getItems());
	}

	
	public void addRecipeAndSave(Recipe recipe, ShoppingListDB db) {
		shoppingList.addRecipe(recipe);
		save(db);
	}

	public void addItemAndSave(Ingredient i, ShoppingListDB db) {
		shoppingList.addIngredient(i);
		save(db);
	}

	public void removeItemAndSave(int pos, ShoppingListDB db) {
		shoppingList.removeItem(pos);
		save(db);
	}
	
}
