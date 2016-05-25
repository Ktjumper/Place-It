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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import place_its.DatabaseHandler;
import place_its.MainActivity;
import reminder.PlaceIt;
import android.content.Context;
import android.util.Log;

public class Sync {
	
	
	public static void syncDatabases(double databaseTime, double datastoreTime) {
		
	}
	
	public static void DatabaseToDatastore (final ArrayList<PlaceIt> pilist) {
		
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(MainActivity.ITEM_URI);
 
			    try {
			      
			      for(PlaceIt pl : pilist) {
			    	  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			    	  nameValuePairs.add(new BasicNameValuePair("name",
			    		  LoginUtils.getLoggedInUserKey(MainActivity.getContext()) +"."+ String.valueOf(pl.getKeyId())));
			    	  nameValuePairs.add(new BasicNameValuePair("price",
			    		  PlaceItParser.PlaceItToString(pl)));
			    	  nameValuePairs.add(new BasicNameValuePair("product",
			    		  LoginUtils.getLoggedInUserKey(MainActivity.getContext())));
			    	  nameValuePairs.add(new BasicNameValuePair("action",
				          "put"));
			      
			    	  post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    
			    	  HttpResponse response = client.execute(post);
			    	  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			    	  String line ="";
			    	  while ((line = rd.readLine()) != null) {
			    		  Log.d("TEST", line);
			    	  }
			      }

			    } catch (IOException e) {
			    	Log.d("TEST", "IOException while trying to conect to GAE");
			    }
			}
		};

		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

		
	

	public static void DatastoreToDatabase (Context context) {
		/*database stuff*/
		DatabaseHandler db = new DatabaseHandler(context);
		db.deleteDatabase();
		List<PlaceIt> newPlaceits = new ArrayList<PlaceIt>();
		newPlaceits = LoginUtils.getLoggedInUserPlaceits(context);
		for(PlaceIt placeit : newPlaceits) {
			Log.d("Sync", "from datastore "+placeit.getTitle());

			db.addToDatabase(placeit);
		}
		db.close();
		
	}
	
}
