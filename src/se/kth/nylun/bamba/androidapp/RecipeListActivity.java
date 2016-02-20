package se.kth.nylun.bamba.androidapp;

import java.util.ArrayList;

import se.kth.nylun.bamba.androidapp.model.db.ShoppingListDB;
import se.kth.nylun.bamba.androidapp.model.handler.ResourceHandler;
import se.kth.nylun.bamba.androidapp.model.handler.ShoppingListHandler;
import se.kth.nylun.bamba.androidapp.model.handler.WebServiceTask;
import se.kth.nylun.bamba.androidapp.model.json.JsonParser;
import se.kth.nylun.bamba.androidapp.view.RecipeAdapter;
import se.kth.nylun.bamba.model.Recipe;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class RecipeListActivity extends Activity {

	private ShoppingListHandler shoppingList;
	private ShoppingListDB db;
	
	private ListView listView;
	private ArrayList<Recipe> recipes;
	private RecipeAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_list);
		
		shoppingList = ShoppingListHandler.getInstance();
		db = new ShoppingListDB(this);
		
		listView = (ListView) findViewById(R.id.recipeList);
		recipes = new ArrayList<Recipe>();
		RecipeListener l = new RecipeListener(getApplicationContext());
		listView.setOnItemClickListener(l);
		listView.setOnItemLongClickListener(l);
		
	}
	
	private class RecipeListener implements OnItemClickListener, OnItemLongClickListener{

		private Context context;
		
		public RecipeListener(Context context){
			this.context = context;
		}
		
		@Override
		public void onItemClick(AdapterView<?> adapterView, View v, int pos,
				long id) {

			Intent intent = new Intent(context, RecipeActivity.class);
			String extra = JsonParser.recipeToJSON(recipes.get(pos)).toString();
			intent.putExtra("recipe", extra);
			startActivity(intent);
			
			
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> adapterView, View v,
				int pos, long id) {
			
			shoppingList.addRecipeAndSave(recipes.get(pos), db);
			showToast(recipes.get(pos).getName()+ " lades till i matlistan!");
			
			return true;
		}
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		//Get recipes
		recipes = ResourceHandler.getAllRecipes(true);
		adapter = new RecipeAdapter(this, R.id.recipeList, recipes);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		//Stop any task running in background
				if(WebServiceTask.isRunning())
					WebServiceTask.stopTask();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		
		//Stop any task running in background
		if(WebServiceTask.isRunning())
			WebServiceTask.stopTask();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipe_list, menu);
		return true;
	}

	private void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
		}
}
