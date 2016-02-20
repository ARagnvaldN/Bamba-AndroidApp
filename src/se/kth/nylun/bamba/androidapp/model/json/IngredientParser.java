package se.kth.nylun.bamba.androidapp.model.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.kth.nylun.bamba.model.Ingredient;

public class IngredientParser extends JsonParser {

	@Override
	public Object parse(Object o) throws JSONException {
		
		if(o instanceof JSONObject)
			return parse((JSONObject)o);
		else
			return parse((JSONArray)o);
		
	}
	
	public Object parse(JSONObject jo) throws JSONException {
		
		return parseIngredient(jo);
	}
	
	public Object parse(JSONArray jarr) throws JSONException {
		
		HashMap<Integer,Ingredient> ingredients = new HashMap<Integer,Ingredient>();
		
		for(int i=0;i<jarr.length();i++){
			JSONObject jo = (JSONObject) jarr.get(i);
			Ingredient ingredient = parseIngredient(jo);
			ingredients.put(ingredient.getIngredientId(),ingredient);
		}
		
		return ingredients;
		
	}

}
