package se.kth.nylun.bamba.androidapp;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import se.kth.nylun.bamba.androidapp.model.db.ShoppingListDB;
import se.kth.nylun.bamba.androidapp.model.handler.ShoppingListHandler;
import se.kth.nylun.bamba.androidapp.model.json.JsonParser;
import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RecipeActivity extends Activity {
	
	SharedPreferences sharedPreferences;
	ShoppingListHandler shoppingList;
	ShoppingListDB db;
	
	TextView recipeName;
	TextView recipeDescription;
	TextView recipeIngredients;
	TextView recipeInstructions;
	
	Button addRecipe;
	Button favoriteRecipe;
	
	Recipe recipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		shoppingList = ShoppingListHandler.getInstance();
		db = new ShoppingListDB(this);
		
		//Set textfields
		recipeName = (TextView) findViewById(R.id.recipe_activity_name);
		recipeDescription = (TextView) findViewById(R.id.recipe_activity_desc);
		recipeIngredients = (TextView) findViewById(R.id.recipe_activity_ingredients);
		recipeInstructions = (TextView) findViewById(R.id.recipe_activity_instructions);
		
		//Set buttons
		RecipeButtonListener l = new RecipeButtonListener();
		addRecipe = (Button) findViewById(R.id.recipe_activity_add_btn);
		addRecipe.setOnClickListener(l);
		favoriteRecipe = (Button) findViewById(R.id.recipe_activity_favorite_btn);
		favoriteRecipe.setOnClickListener(l);
		
		//Fill textviews
		fillTextViews();
	}
	
	private void fillTextViews(){
		
		//Get recipe in extras
		Intent intent = this.getIntent();
		Bundle b = intent.getExtras();
		String extra = b.getString("recipe");
		Object o;
		try {
			o = new JSONTokener(extra).nextValue();
			if(o instanceof JSONObject){
				JSONObject jo = (JSONObject) o;
				recipe = JsonParser.parseRecipe(jo);
			}
		} catch (JSONException e) {
			Log.e("JSON",""+e.getMessage());
		}
		
		if(recipe != null){
			recipeName.setText(recipe.getName());
			recipeDescription.setText(recipe.getDescription());
			
			//Make recipe conform to preferenced portions
			double portions = Double.parseDouble( sharedPreferences.getString("portions", "4") );
			if(portions != 4.0)
				recipe.setPortions(portions);
			
			
			//Parse instructions # for bullet
			String instructions = recipe.getInstructions();
			String[] bullets = instructions.split("#");
			instructions = "";
			for(int i=1;i<bullets.length;i++){
				instructions += (i) + ": ";
				instructions += bullets[i];
				instructions += "\n";
			}
			recipeInstructions.setText(instructions);
			
			//Parse ingredients
			ArrayList<Ingredient> ingredients = recipe.getIngredients();
			String ingredientList = "";
			for(Ingredient i: ingredients){
				ingredientList += " - ";
				ingredientList += i.getName() + " ";
				ingredientList += i.getReadableString();
				ingredientList += "\n";
			}
			recipeIngredients.setText(ingredientList);
		}
	}
	
	private class RecipeButtonListener implements OnClickListener{
	
		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case R.id.recipe_activity_add_btn:
				shoppingList.addRecipeAndSave(recipe, db);
				finish();
				return;
			case R.id.recipe_activity_favorite_btn:
				break;
			}
			
		}
	}
}
