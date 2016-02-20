package se.kth.nylun.bamba.androidapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.json.JSONTokener;

import se.kth.nylun.bamba.androidapp.model.handler.WebServiceTask;
import se.kth.nylun.bamba.androidapp.model.json.IngredientParser;
import se.kth.nylun.bamba.androidapp.model.json.RecipeParser;
import se.kth.nylun.bamba.model.Ingredient;
import se.kth.nylun.bamba.model.Recipe;

import android.os.AsyncTask;
import android.util.Log;

public class WSController {
	
	//public static String WS_URL = "http://192.168.0.105/BambaService/rest";
	public static String WS_URL = "http://zvenneshomies.dlinkddns.com/BambaService/rest";
	private URL url;
	
	public WSController(){
		
		try {
			url = new URL(WS_URL);
			
		} catch (MalformedURLException e) {
			Log.e("WSController",e.getMessage());
		}
	}
	
	public static HashMap<Integer, Recipe> getAllRecipes() throws InterruptedException, ExecutionException{
		
		String path = "/hello/allRecipes";
		
		WebServiceTask WSTask = WebServiceTask.getTask();
		Object o = WSTask.execute(path,new RecipeParser()).get();
		
		//If there's only one recipe in the database
		if(o instanceof Recipe){
			HashMap<Integer, Recipe> recipes = new HashMap<Integer, Recipe>();
			Recipe r = (Recipe) o;
			recipes.put(r.getRecipeId(),r);
			return recipes;
		}
			
		return (HashMap<Integer, Recipe>) o;
	}
	
	public static HashMap<Integer, Ingredient> getAllIngredients() throws InterruptedException, ExecutionException{
		
		String path = "/hello/allIngredients";
		
		WebServiceTask WSTask = WebServiceTask.getTask();
		Object o =  WSTask.execute(path,new IngredientParser()).get();

		//In case there's only one ingredient encapsulate it
		if(o instanceof Ingredient){
			HashMap<Integer, Ingredient> ingredients = new HashMap<Integer, Ingredient>();
			Ingredient i = (Ingredient) o;
			ingredients.put(i.getIngredientId(), i);
			return ingredients;
		}
		
		return (HashMap<Integer, Ingredient>) o;
	}
	
	public static Ingredient getIngredientById(int id) throws InterruptedException, ExecutionException{
		
		String path = "/hello/ingredient?id=" + id;

		WebServiceTask WSTask = WebServiceTask.getTask();
		Ingredient i = (Ingredient) WSTask.execute(path,new IngredientParser()).get();
		
		return i;
	}
	
	public static Recipe getRecipeById(int id) throws Exception{
		
		String path = "/hello/recipe?id=" + id;

		WebServiceTask WSTask = WebServiceTask.getTask();
		Recipe r = (Recipe) WSTask.execute(path, new RecipeParser()).get();
		
		return r;
	}
	
	
	/*private static class WebServiceTask extends AsyncTask<Object,Void,Object>{

		@Override
		protected Object doInBackground(Object... params) {

			String method = (String) params[0];
			JsonParser parser = (JsonParser) params[1];
			
			try{
				
				URL url = new URL(WS_URL + method);
				URLConnection conn = url.openConnection();

				BufferedReader br = 
						new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				String line;
				while( (line = br.readLine()) != null ){
					
					Object jo = new JSONTokener(line).nextValue();
					Object o = parser.parse(jo);
				
					return o;
				}
				
			}
			catch(Exception e){
				Log.e("JSON",e.toString());
			}
			
			return null;
		}

	}*/
}
