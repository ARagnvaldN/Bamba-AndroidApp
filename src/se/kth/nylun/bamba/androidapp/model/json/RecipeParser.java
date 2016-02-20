package se.kth.nylun.bamba.androidapp.model.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;


public class RecipeParser extends JsonParser {
	
	@Override
	public Object parse(Object o) throws JSONException {
		
		if(o instanceof JSONObject)
			return parse((JSONObject) o);
		else
			return parse((JSONArray) o);
	}
	
	public Object parse(JSONObject jo) throws JSONException {
		
			return parseRecipe(jo);
	}
	
	public Object parse(JSONArray jarr) throws JSONException {
		
		HashMap<Integer, Recipe> recipes = new HashMap<Integer, Recipe>();
		
		for(int i=0;i<jarr.length();i++){
			JSONObject jo = (JSONObject) jarr.get(i);
			Recipe recipe = (Recipe) parse(jo);
			recipes.put(recipe.getRecipeId(),recipe);
		}
		
		return recipes;
		
	}
	
	

}
