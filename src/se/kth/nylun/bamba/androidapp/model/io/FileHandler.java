package se.kth.nylun.bamba.androidapp.model.io;

import java.io.FileOutputStream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import se.kth.nylun.bamba.androidapp.model.handler.ResourceHandler;
import se.kth.nylun.bamba.androidapp.model.json.IngredientParser;
import se.kth.nylun.bamba.androidapp.model.json.JsonParser;
import se.kth.nylun.bamba.androidapp.model.json.RecipeParser;
import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;

public class FileHandler{
	
	private final static String INGREDIENT_FILE_NAME = "ingredients";
	private final static String RECIPE_FILE_NAME = "recipes";
	
	
	public static HashMap<Integer, Ingredient> loadIngredients(Context context){
		
		HashMap<Integer, Ingredient> ingredients = null;
		
		FileInputStream in = null;
		try{
			in = context.openFileInput(INGREDIENT_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			
			StringBuilder sb = new StringBuilder();
			String line = "";
			while( (line = br.readLine()) != null){
				sb.append(line);
			}

			//File empty
			String jsonString = sb.toString();
			if(jsonString.equals("")){
				in.close();
				return null;
			}
			
			JSONObject json = new JSONObject(jsonString);
			
			IngredientParser parser = new IngredientParser();
			ingredients = (HashMap<Integer, Ingredient>) parser.parse(json);
			
			in.close();

		} catch(Exception e){
			Log.e("loadIngredients",""+e.getMessage());
			return null;
		}
		
		return ingredients;
	}
	
public static HashMap<Integer, Recipe> loadRecipes(Context context){
		
		HashMap<Integer, Recipe> recipes = null;
		
		FileInputStream in = null;
		try{
			in = context.openFileInput(RECIPE_FILE_NAME);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			
			StringBuilder sb = new StringBuilder();
			String line = "";
			while( (line = br.readLine()) != null){
				sb.append(line);
			}
			
			//File empty
			String jsonString = sb.toString();
			if(jsonString.equals("")){
				in.close();
				return null;
			}

			JSONObject json = new JSONObject(sb.toString());
			
			RecipeParser parser = new RecipeParser();
			recipes = (HashMap<Integer, Recipe>) parser.parse(json);
			
			in.close();

		} catch(Exception e){
			Log.e("loadIngredients",""+e.getMessage());
			return null;
		}
		
		return recipes;
	}
	
	public static void saveAllLists(Context context){
		saveRecipes(context);
		saveIngredients(context);
	}
	
	private static void saveRecipes(Context context){
		ArrayList<Recipe> recipes = ResourceHandler.getAllRecipes();
		
		//Parse list to JSON if there is one
		if(recipes != null){
			String json = JsonParser.recipesToJsonString(recipes);
					
			//Open and write to file
			FileOutputStream out = null;
			try{
				out = context.openFileOutput(RECIPE_FILE_NAME,Context.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(out);
				osw.write(json);
				
			} catch(Exception e){
				Log.e("saveRecipes",""+e.getMessage());
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	private static void saveIngredients(Context context){
		
		ArrayList<Ingredient> ingredients = ResourceHandler.getAllIngredients();
		
		if(ingredients != null){
			//Parse list to JSON	
			String json = JsonParser.ingredientsToJsonString(ingredients);
			//Open and write to file
			FileOutputStream out = null;
			try{
				out = context.openFileOutput(INGREDIENT_FILE_NAME, Context.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(out);
				osw.write(json);
				
			} catch(Exception e){
				Log.e("saveIngredients",""+e.getMessage());
			} finally{
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
	

}
