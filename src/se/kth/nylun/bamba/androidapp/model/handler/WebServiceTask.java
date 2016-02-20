package se.kth.nylun.bamba.androidapp.model.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONTokener;

import se.kth.nylun.bamba.androidapp.WSController;
import se.kth.nylun.bamba.androidapp.model.json.JsonParser;

import android.os.AsyncTask;
import android.util.Log;

public class WebServiceTask extends AsyncTask<Object,Void,Object>{
	
	private static WebServiceTask INSTANCE;
	
	private WebServiceTask(){
		super();
	}
	
	public static WebServiceTask getTask(){
		INSTANCE = new WebServiceTask();
		
		return INSTANCE;
	}

	@Override
	protected Object doInBackground(Object... params) {

		String method = (String) params[0];
		JsonParser parser = (JsonParser) params[1];
		
		try{
			
			URL url = new URL(WSController.WS_URL + method);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(20000);
			BufferedReader br = 
					new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line;
			while( (line = br.readLine()) != null  && INSTANCE.isRunning()){
				
				
				Object jo = new JSONTokener(line).nextValue();
				Object o = parser.parse(jo);
			
				return o;
			}
			
		}
		catch(SocketTimeoutException e){
			Log.e("SocketTimeout",""+e.getMessage());
		}
		catch(Exception e){
			Log.e("WebServiceTask",e.getMessage());
		}
		
		return null;
	}
	
	public static void stopTask(){
		INSTANCE.cancel(false);
	}
	
	public static boolean isRunning(){
		return !INSTANCE.isCancelled();
	}

	

}
