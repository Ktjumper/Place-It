/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 */
package login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import place_its.DatabaseHandler;
import place_its.MainActivity;
import place_its.PlaceItUtils;
import reminder.PlaceIt;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;



/**
 * storing the login username/password sets as Strings in SharedPreferences
 * example:
 * 			username:password
 * */
public class LoginUtils {
    
	public static final String NOTLOGGEDIN = "Please log in!";
	public static final String NOKEY = "NOKEY";
	
    /* save the last user logged in locally  */
    public static void login(Context context, String usernamePassword, String key) {
    	SharedPreferences settings = context.getSharedPreferences("LoggedInUser", 0);
		SharedPreferences.Editor editor = settings.edit();
        /* saving */
        editor.putString("LoggedIn",usernamePassword);
        editor.putString("Key", key);
        editor.commit();
        
        DatabaseHandler db = new DatabaseHandler(context);
		db.deleteDatabase();
    }
    
    public static void logout(Context context) {
    	SharedPreferences settings = context.getSharedPreferences("LoggedInUser", 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.clear();
    	editor.commit();
    }
    
    /* get the user logged in */
    public final static String getLoggedInUser (Context context) {
    	final SharedPreferences settings = context.getSharedPreferences("LoggedInUser", 0);
    	return settings.getString("LoggedIn", NOTLOGGEDIN);
    }
    
    public static String getLoggedInUserKey (Context context) {
    	final SharedPreferences settings = context.getSharedPreferences("LoggedInUser", 0);
    	return settings.getString("Key", NOKEY);
    }
    
    public static String getPlaceitNewKey() {
    	PlaceItUtils plUtils = PlaceItUtils.getInstance();
    	return String.valueOf(plUtils.getList().size());
    }
    
    
    /* get a new key for creating account */
    public static String getNewKey () {
    	return String.valueOf(getLogins().size());
    }
    
    /* get list of logins from datastore */
    public static ArrayList<String> getLogins() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String TAG = "loginactivity";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(MainActivity.PRODUCT_URI);
		ArrayList<String> logins= new ArrayList<String>();
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String data = EntityUtils.toString(entity);
			JSONObject myjson;

			try {
				myjson = new JSONObject(data);
				JSONArray array = myjson.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					logins.add(obj.get("description").toString());
					/* add item to the string */
					/*productsListString.append(obj.get("name").toString()
							+ " - " + obj.get("description").toString());*/
							//+ obj.get("product").toString() + "\n");
					// productsListString.append(obj.get("name").toString());
				}
			} catch (JSONException e) {
				Log.d(TAG, "Error in parsing JSON");
			}

		} catch (ClientProtocolException e) {
			Log.d(TAG,
					"ClientProtocolException thrown while trying to Connect to GAE");
		} catch (IOException e) {
			Log.d(TAG, "IOException thrown while trying to Connect to GAE");
		}
		return logins;
	}
    
    
    /* get all the placeits of the current loggedin user*/
    public static List<PlaceIt> getLoggedInUserPlaceits(Context context) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String TAG = "loginactivity";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(MainActivity.ITEM_URI);
		List<PlaceIt> placeits= new ArrayList<PlaceIt>();
		List<PlaceIt> pl = null;
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String data = EntityUtils.toString(entity);
			JSONObject myjson;

			try {
				myjson = new JSONObject(data);
				JSONArray array = myjson.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					if(getLoggedInUserKey(context).equals(obj.get("product").toString())) {
						
						placeits.add(PlaceItParser.StringToPlaceIt(obj.get("price").toString()));
						
					}
				}
				PlaceIt [] plA = new PlaceIt[placeits.size()];
				Log.d(""+placeits.size(),"SIZEOFLIST");
				for(PlaceIt place : placeits) {
					Log.d(""+place.getKeyId(),"SIZEOFkeyid");
					plA[(int) place.getKeyId() - 1] = place;
				}
				pl = new ArrayList<PlaceIt>(Arrays.asList(plA));
			} catch (JSONException e) {
				Log.d(TAG, "Error in parsing JSON");
			}

		} catch (ClientProtocolException e) {
			Log.d(TAG,
					"ClientProtocolException thrown while trying to Connect to GAE");
		} catch (IOException e) {
			Log.d(TAG, "IOException thrown while trying to Connect to GAE");
		}
		
		
		return pl;
	}
    
    
    
}