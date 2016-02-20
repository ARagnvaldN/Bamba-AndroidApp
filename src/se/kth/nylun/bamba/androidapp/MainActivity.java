package se.kth.nylun.bamba.androidapp;

import se.kth.nylun.bamba.androidapp.model.db.ShoppingListDB;
import se.kth.nylun.bamba.androidapp.model.handler.ResourceHandler;
import se.kth.nylun.bamba.androidapp.model.handler.ShoppingListHandler;
import se.kth.nylun.bamba.androidapp.model.io.FileHandler;
import se.kth.nylun.bamba.androidapp.model.json.JsonParser;
import se.kth.nylun.bamba.model.Recipe;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static int QR_SCAN = 1;
	
	private ShoppingListHandler shoppingList;
	private ShoppingListDB shoppingDB;
	
	private Button shoppingListButton;
	private Button recipeButton;
	private Button ingredientButton;
	private Button scanButton;
	private Button statisticsButton;
	private Button settingsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Load lists
		ResourceHandler.initLists(getApplicationContext());
		
		//Define buttons
		shoppingListButton = (Button) findViewById(R.id.shoppinglist_btn);
		recipeButton = (Button) findViewById(R.id.recipe_btn);
		ingredientButton = (Button) findViewById(R.id.ingredients_btn);
		scanButton = (Button) findViewById(R.id.scan_btn);
		statisticsButton = (Button) findViewById(R.id.statistics_btn);
		settingsButton = (Button) findViewById(R.id.settings_btn);
		
		//Add listeners
		OnClickListener l = new MenuListener(getApplicationContext());
		shoppingListButton.setOnClickListener(l);
		recipeButton.setOnClickListener(l);
		ingredientButton.setOnClickListener(l);
		scanButton.setOnClickListener(l);
		statisticsButton.setOnClickListener(l);
		settingsButton.setOnClickListener(l);
		
		//Init database and shoppinglist
		shoppingDB = new ShoppingListDB(getApplicationContext());
		shoppingList = ShoppingListHandler.getInstance();
		
		//Load persistent data
				try{
					shoppingList.load(shoppingDB);
				}catch(Exception e){
					Log.e("MAIN:onCreate",""+e.getMessage());
					showToast("DB Error: Loading shopping list.");
				}
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent){
		if(requestCode == QR_SCAN){
			if(resultCode == RESULT_OK){
				
				//Start recipe activity for scanned activity
				//Get recipe id
				String qrCode = resultIntent.getStringExtra("SCAN_RESULT");
				String idHex = qrCode.substring(5);
				int id = Integer.decode(idHex);
				
				//Get recipe
				Recipe recipe = ResourceHandler.getRecipeById(id);
				
				//Start recipeactivity for recipe
				if(recipe != null){
					Intent intent = new Intent(this, RecipeActivity.class);
					String extra = JsonParser.recipeToJSON(recipe).toString();
					intent.putExtra("recipe", extra);
					startActivity(intent);
				}
				
			}else if(resultCode == RESULT_CANCELED){
				showToast("Skanning avbruten.");
			}
		}
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		shoppingList.save(shoppingDB);
		
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		FileHandler.saveAllLists(getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class MenuListener implements OnClickListener{

		Context context;
		
		public MenuListener(Context context){
			this.context = context;
		}
		
		@Override
		public void onClick(View v) {

			Class c = null;
			
			switch(v.getId()){
			
				case R.id.shoppinglist_btn:				//Start ShoppinglistActivity
					c = ShoppingListActivity.class;
					break;
					
				case R.id.recipe_btn:					//Start RecipeListActivity
					c = RecipeListActivity.class;
					break;
					
				case R.id.ingredients_btn:				//Start IngredientListActivity
					c = IngredientActivity.class;
					break;
					
				case R.id.statistics_btn:				//Start StatisticsActivity
					c = StatisticsActivity.class;
					break;
					
				case R.id.settings_btn:					//Start SettingsActivity
					c = SettingsActivity.class;
					break;
					
				case R.id.scan_btn:						//Start QRActivity (for result)
					c = null;
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, QR_SCAN);
					break;
			}

			//Create intent
			if(c != null){
				Intent intent = new Intent(context, c);
				startActivity(intent);
			}
				
		}
		
	}
	
	private void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
		}
}
