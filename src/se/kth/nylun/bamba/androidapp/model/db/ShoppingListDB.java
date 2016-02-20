package se.kth.nylun.bamba.androidapp.model.db;

import java.util.Date;
import java.util.HashSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import se.kth.nylun.bamba.androidapp.WSController;
import se.kth.nylun.bamba.androidapp.model.handler.ResourceHandler;
import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;
import se.kth.nylun.bamba.model.ShoppingList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ShoppingListDB extends SQLiteOpenHelper{

	private final static int DATABASE_VER = 37;
	private static final String SHOPLIST_TBL = "ShoppingList",
								SHOPLIST2ITEMS = "Shop2Items",
								SHOPLIST2RECIPES = "Shop2Recipes",
								LIST_ID = "listId",
								RECIPE_ID = "recipeId",
								ITEM_ID = "itemId",
								DATE = "creationDate",
								QUANTITY = "quantity";
	
	public ShoppingListDB(Context context){
		super(context, SHOPLIST_TBL, null, DATABASE_VER);
	}
	
	public ArrayList<Date> getShoppingDates(){
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT " +DATE+ " FROM " +SHOPLIST_TBL+ " " +
								"ORDER BY " +DATE+ " DESC;", null);
		
		//Abort if empty
		if(!c.moveToFirst())
			return null;
		
		HashSet<Date> hashDates = new HashSet<Date>();
		do{
			String dateString = c.getString(0);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			Date d = null;
			try {
				d = sdf.parse(dateString);
			} catch (ParseException e) {
				Log.e("DB",""+e.getMessage());
			}
			hashDates.add(d);
		} while(c.moveToNext());
				
		
		db.close();
		
		ArrayList<Date> dates = new ArrayList<Date>(hashDates);
		return dates;
		
	}
	
	public ArrayList<Ingredient> getMostFrequentItems() throws InterruptedException, ExecutionException{
		//Gets the five items that appear most frequently in shoppinglists

		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT itemId, COUNT(itemId) AS count FROM "+ SHOPLIST2ITEMS +" " +
								"GROUP BY itemId " +
								"ORDER BY count DESC LIMIT 5;", null);
		
		//Abort if empty
		if(!c.moveToFirst())
			return null;
		
		//Get IDs of items
		ArrayList<Ingredient> items = new ArrayList<Ingredient>();
		do{
			Ingredient i = ResourceHandler.getIngredientById(c.getInt(0));
			items.add(i);
		} while(c.moveToNext());
		db.close();
		
		return items;
	}
	
	public ShoppingList mostRecentList() throws InterruptedException, ExecutionException{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM "+ SHOPLIST_TBL +
				" ORDER BY " + LIST_ID + " DESC LIMIT 1;", null);
		
		//Abort if empty DB
		if(!c.moveToFirst())
			return null;
		
		//Get ID of list
		int listId = c.getInt(0);
		ShoppingList sl = new ShoppingList();
		sl.setId(listId);
		
		//Get items in list
		Cursor itemC = db.rawQuery("SELECT * FROM " +SHOPLIST2ITEMS+ " " +
				"WHERE " +LIST_ID+" = " +listId+";", null);
		
		//List is empty, create new
		if(!itemC.moveToFirst()){
			db.close();
			return null;
		}
		
		ArrayList<Ingredient> items = new ArrayList<Ingredient>();
		do{
			Ingredient i = ResourceHandler.getIngredientById(itemC.getInt(1));
			i.setQuantity( itemC.getDouble(2) );
			items.add( i );
		}while(itemC.moveToNext());
		sl.setItems(items);
		
		//Get recipes in list
		Cursor recipeC = db.rawQuery("SELECT * FROM " +SHOPLIST2RECIPES+ " " +
				"WHERE "+LIST_ID+" = " +listId+ ";", null);
		
		if(recipeC.moveToFirst()){
			ArrayList<Recipe> recipes = new ArrayList<Recipe>();
			try {
				do{
					recipes.add( ResourceHandler.getRecipeById(recipeC.getInt(1)) );
				}while(recipeC.moveToNext());
				sl.setRecipes(recipes);
			} catch (Exception e) {
				Log.e("SQL",e.getMessage());
			}
		}
		
		db.close();
		
		return sl;
	}
	
	public String selectAll(){
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c1 = db.rawQuery("SELECT * FROM " +SHOPLIST_TBL, null);
		Cursor c2 = db.rawQuery("SELECT * FROM " +SHOPLIST2ITEMS, null);
		Cursor c3 = db.rawQuery("SELECT * FROM " +SHOPLIST2RECIPES, null);
		
		String s = "";
		String[] sarr = c1.getColumnNames();
		s+= sarr[0] + " ";
		s+= sarr[1] +"\n";
		if(c1.moveToFirst()){
			do{
			s += c1.getInt(0) + " ";
			s += c1.getString(1) + "\n";
			}while(c1.moveToNext());
		}
		
		sarr = c2.getColumnNames();
		s+= sarr[0] + " ";
		s+= sarr[1] +" ";
		s+= sarr[2] +"\n";
		if(c2.moveToFirst()){
			do{
			s += c2.getInt(0) + " ";
			s += c2.getInt(1) + " ";
			s += c2.getDouble(2) + "\n";
			}while(c2.moveToNext());
		}
		
		sarr = c3.getColumnNames();
		s+= sarr[0] + " ";
		s+= sarr[1] +"\n";
		if(c3.moveToFirst()){
			do{
			s += c3.getInt(0) + " ";
			s += c3.getString(1) + "\n";
			}while(c3.moveToNext());
		}
		
		db.close();
		
		return s;
	}
	
	public void insertShoppingList(ShoppingList sl){

		//Insert Shoppinglist in shoppingList table
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DATE, sl.getDateString());
		long id = db.insert(SHOPLIST_TBL, null, cv);
		
		//Insert items in itemTable
		cv.clear();
		for(Ingredient i:sl.getItems()){
			cv.put(LIST_ID, id);
			cv.put(ITEM_ID, i.getIngredientId());
			cv.put(QUANTITY, i.getQuantity());
			db.insert(SHOPLIST2ITEMS, null, cv);
		}
		
		//Insert recipes in recipeTable
		cv.clear();
		for(Recipe r:sl.getRecipes()){
			cv.put(LIST_ID, id);
			cv.put(RECIPE_ID, r.getRecipeId());
			db.insert(SHOPLIST2RECIPES, null, cv);
		}
		
		db.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		//Create a table for shoppinglists
		db.execSQL("CREATE TABLE " + SHOPLIST_TBL + " " +
				"(" +  LIST_ID + " INTEGER PRIMARY KEY NOT NULL, " +
				//RECIPE_ID + " INTEGER, " +
				//ITEM_ID + "INTEGER NOT NULL, " +
				DATE +" TEXT);");
		
		//Create a table for translating between shoppinglist and items
		db.execSQL("CREATE TABLE " + SHOPLIST2ITEMS + " (" +
				LIST_ID + " INTEGER NOT NULL, " +
				ITEM_ID + " INTEGER NOT NULL, " +
				QUANTITY +" REAL);");
		
		//Create a table for translating between shoppinglist and recipes
		db.execSQL("CREATE TABLE " + SHOPLIST2RECIPES +" (" +
				LIST_ID + " INTEGER NOT NULL, " +
				RECIPE_ID + " INTEGER);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

		Log.i("SQL","DB UPDATED TO VER: "+Integer.toString(newVer));
		
		db.execSQL("DROP TABLE IF EXISTS " + SHOPLIST_TBL);
		db.execSQL("DROP TABLE IF EXISTS " + SHOPLIST2ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + SHOPLIST2RECIPES);
		
		onCreate(db);
	}

	public void saveShoppingList(ShoppingList shoppingList) {
		int id = shoppingList.getId();
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " +SHOPLIST_TBL+ " " +
				"WHERE " +LIST_ID+ " = " +id+ ";");
		db.execSQL("DELETE FROM " +SHOPLIST2ITEMS+ " " +
				"WHERE " +LIST_ID+ " = " +id+ ";");
		db.execSQL("DELETE FROM " +SHOPLIST2RECIPES+ " " +
				"WHERE " +LIST_ID+ " = " +id+ ";");
		
		db.close();
		insertShoppingList(shoppingList);
	}
	
}
