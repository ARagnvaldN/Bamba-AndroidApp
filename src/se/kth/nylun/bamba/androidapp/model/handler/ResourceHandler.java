package se.kth.nylun.bamba.androidapp.model.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import se.kth.nylun.bamba.androidapp.WSController;
import se.kth.nylun.bamba.androidapp.model.io.FileHandler;
import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;

public class ResourceHandler {
	
	private static HashMap<Integer, Ingredient> ingredients;
	private static HashMap<Integer, Recipe> recipes;
	
	public static void initLists(Context context){

			//Download refreshed lists of the server
			refreshLists();
			
			//If network error or other reason use lists from file
			if(ingredients == null || recipes == null){
				ingredients = FileHandler.loadIngredients(context);
				recipes = FileHandler.loadRecipes(context);
			}
			
			//If file is not available either, create empty lists
			if(ingredients == null || recipes == null){
				ingredients = new HashMap<Integer, Ingredient>();
				recipes = new HashMap<Integer, Recipe>();
			}

	}
	
	public static void refreshLists(){
		refreshRecipeMap();
		refreshIngredientMap();
	}
	
	private static void refreshRecipeMap(){
		HashMap<Integer, Recipe> newRecipes = null;
		try{
			newRecipes = WSController.getAllRecipes();
			
		} catch(Exception e){
			Log.e("refreshRecipeMap",""+e.getMessage());
		}
		
		if(newRecipes != null)
			recipes = newRecipes;
	}
	
	private static void refreshIngredientMap() {
		HashMap<Integer, Ingredient> newIngredients = null;
		try {
			newIngredients = WSController.getAllIngredients();
			
		} catch (Exception e) {
			Log.e("refreshIngredientMap",""+e.getMessage());
		}
		
		if(newIngredients != null)
			ingredients = newIngredients;
	}
	
	public static Ingredient getIngredientById(int id){
		if(ingredients == null)		
			getAllIngredients();
		
		return ingredients.get(id);
	}
	
	public static Recipe getRecipeById(int id){
		if(recipes == null)		
			getAllRecipes();
		
		return recipes.get(id);
	}
	
	public static ArrayList<Ingredient> getAllIngredients(){
		return getAllIngredients(false);
	}
	
	public static ArrayList<Ingredient> getAllIngredients(boolean forceRefresh){
		
		if(forceRefresh)				//Force a refreshed list from the server
			refreshIngredientMap();
		
		if(ingredients == null)
			return null;
		
		return new ArrayList<Ingredient>(ingredients.values());
	}
	
	
	public static ArrayList<Recipe> getAllRecipes(){
		return getAllRecipes(false);
	}
	
	public static ArrayList<Recipe> getAllRecipes(boolean forceRefresh){
		
		if(forceRefresh)//Force a refreshed list from the server
			refreshRecipeMap();
		
		if(recipes == null)
			return null;
		
		return new ArrayList<Recipe>(recipes.values());
		
	}

	public static void setResources(
			HashMap<Integer, Ingredient> ings,
			HashMap<Integer, Recipe> recs, Context c) {
		ingredients = ings;
		recipes = recs;
	}
}

