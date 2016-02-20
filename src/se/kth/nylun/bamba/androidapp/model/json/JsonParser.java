package se.kth.nylun.bamba.androidapp.model.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;

public abstract class JsonParser {

	protected static final String ING_ID = "ingredientId",
						 ING_NAME = "name",
						 ING_DESCR = "description",
						 CATEGORY = "category",
						 MLT = "meanLifeTime",
						 QUANTITY = "quantity",
						 UNIT = "unit",
						 REC_ID = "recipeId",
						 AUTH_ID = "creatorId",
						 DATE = "creationDate",
						 REC_NAME = "name",
						 REC_DESCR = "description",
						 INSTRUCTIONS = "instructions",
						 INGREDIENTS = "ingredients";
	
	protected boolean single = false;
	
	public abstract Object parse(Object o) throws JSONException;

	public static Ingredient parseIngredient(JSONObject jo) throws JSONException{
		
		return new Ingredient(
				jo.getInt(ING_ID),
				jo.getString(ING_NAME),
				jo.getString(ING_DESCR),
				jo.getInt(CATEGORY),
				jo.getInt(MLT),
				jo.getDouble(QUANTITY),
				jo.getInt(UNIT)
				);
		
	}
	
	public static Recipe parseRecipe(JSONObject jo) throws JSONException{
		
		Date date = null;
		try{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		date = sdf.parse(jo.getString(DATE));
		}catch(ParseException e){}
		
		Recipe recipe = new Recipe(
				jo.getInt(REC_ID),
				jo.getInt(AUTH_ID),
				date,
				jo.getString(REC_NAME),
				jo.getString(REC_DESCR),
				jo.getString(INSTRUCTIONS),
				null);
		
		JSONArray jarr = (JSONArray) jo.get(INGREDIENTS);
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		
		for(int i=0;i<jarr.length();i++){
			JSONObject o = (JSONObject) jarr.get(i);
			Ingredient ingredient = parseIngredient(o);
			ingredients.add(ingredient);
		}
		
		recipe.setIngredients(ingredients);
		
		return recipe;
	}

	public static JSONObject ingredientToJSON(Ingredient i){
		JSONObject jo = new JSONObject();
		
		try{
			jo.put(ING_ID, i.getIngredientId());
			jo.put(ING_NAME, i.getName());
			jo.put(ING_DESCR, i.getDescription());
			jo.put(CATEGORY, i.getCategory());
			jo.put(MLT, i.getMeanLifeTime());
			jo.put(QUANTITY, i.getQuantity());
			jo.put(UNIT, i.getUnit());
		}catch(JSONException e)
			{Log.e("ingredientToJson",""+e.getMessage());}
		
		return jo;
	}
	
	
	public static JSONObject recipeToJSON(Recipe r) {
		JSONObject jo = new JSONObject();
		try{
			jo.put(REC_ID, r.getRecipeId());
			jo.put(AUTH_ID, r.getCreatorId());
			jo.put(REC_NAME, r.getName());
			jo.put(REC_DESCR, r.getDescription());
			jo.put(INSTRUCTIONS, r.getInstructions());
			jo.put(DATE, r.getCreationDate().toString());
			ArrayList<Ingredient> items = r.getIngredients();
			JSONArray jarr = new JSONArray();
			for(Ingredient i : items){
				JSONObject o = ingredientToJSON(i);
				jarr.put(o);
			}
			jo.put(INGREDIENTS, jarr);
		}catch(JSONException e)
			{Log.e("JSON",""+e.getMessage());}
		
		return jo;
	}
	
	public static String ingredientsToJsonString(ArrayList<Ingredient> ingredients){
		JSONArray jarr = new JSONArray();
		
		for(Ingredient i: ingredients){
			jarr.put(ingredientToJSON(i));
		}
		
		return jarr.toString();
	}
	
	public static String recipesToJsonString(ArrayList<Recipe> recipes){
		
		JSONArray jarr = new JSONArray();
		for(Recipe r: recipes){
			jarr.put(recipeToJSON(r));
		}
		
		return jarr.toString();
	}
	
}
