/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: Class that deals with the main functionality
 * of the application. 
 */
package place_its;

import login.LoginUtils;
import login.Sync;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.place_its.R;

public class MainActivity extends Activity {
	
	/* URIs for google app engine*/
	public static final String PRODUCT_URI = "http://yogurtworldyummy.appspot.com/product";
	public static final String ITEM_URI = "http://yogurtworldyummy.appspot.com/item";
	
	/* possible to get to these activities from main */
	public static final String VIEWLISTS = "place_its.ViewListsActivity";
	public static final String CREATEPLACEIT = "place_its.CreatePlaceItReminders";
	public static final String SHOWMAP = "place_its.MapActivity";
	public static final String LOGIN = "place_its.LoginActivity";


	private static Button mViewListsButton;
	private static Button mCreatePlaceIt;
	private static Button mShowMapButton;
	private static Button mLoginButton;
	private static TextView mUser;
	
	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = getApplicationContext();
	}
	
	public static Context getContext() {
		return context;
	}
	
	@Override
	public void onResume() {
	    super.onResume(); 
	    
	    
	    
	    /* check if user connected to internet */
	    ConnectionDetector cd = new ConnectionDetector(getBaseContext());

	    if (cd.isConnectingToInternet()) {
	    	Log.d("MainActivity", "CONNECTED TO INTERNET. UPDATE FROM DATASTORE");
	    	
	    	/* store any offline session to datastore */
	    	DatabaseHandler db = new DatabaseHandler(this);
	    	getEverythingFromDatabase(db);

	    	PlaceItUtils plUtil = PlaceItUtils.getInstance();
	    	Sync.DatabaseToDatastore(plUtil.getList());
	    	
	    	
	    	/* get stuff from datastore */
            Sync.DatastoreToDatabase(getContext());
            
            
	    } else {
	    	Log.d("MainActivity", "NOT CONNECTED TO INTERNET");
	    	
	    	/* let user continue whatever they have in database and store it to datastore
	    	 * later */
		    DatabaseHandler db = new DatabaseHandler(this);
	    	getEverythingFromDatabase(db);
	    	
	    	
	    	
	    }


	    
		
		
	    
		setUpButtons();
		
		mUser = (TextView) findViewById(R.id.LoggedInUser);
		
		String[] user = LoginUtils.getLoggedInUser(getBaseContext()).split(":");
		mUser.setText(user[0]);
	}

	

	public static void getEverythingFromDatabase(DatabaseHandler db) {
		Cursor cursor = db.getReadableDatabase().rawQuery(
				"select * from PLACEITS_TABLE", null);
		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		plUtils.clearPlaceItsList();
		if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				
			    /* add database plcceits to list*/
				plUtils.addPlaceItsToList(db.getPlaceItFromDatabase(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
	}
	
	/* adding listeners to buttons */
	private void setUpButtons () {
		mViewListsButton = (Button) findViewById(R.id.ViewListsButton);
		mViewListsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonEvent(VIEWLISTS);
			}
		});
		
		mCreatePlaceIt = (Button) findViewById(R.id.CreatePlaceIts);
		mCreatePlaceIt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonEvent(CREATEPLACEIT);
			}
		});
		
		mShowMapButton = (Button) findViewById(R.id.ShowMapButton);
		mShowMapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonEvent(SHOWMAP);
			}
		});
		
		mLoginButton = (Button) findViewById(R.id.LoginButton);
		
		/* disable buttons exacept Login if user not logged in*/
		if(LoginUtils.getLoggedInUser(getApplicationContext()).equals(LoginUtils.NOTLOGGEDIN)) {
			mViewListsButton.setVisibility(View.INVISIBLE);
			mShowMapButton.setVisibility(View.INVISIBLE);
			mCreatePlaceIt.setVisibility(View.INVISIBLE);
			

			/* enable LOGIN button */
			mLoginButton.setText("Login");
			mLoginButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					buttonEvent(LOGIN);
				}
			});
			
		}
		else {
			mViewListsButton.setVisibility(View.VISIBLE);
			mShowMapButton.setVisibility(View.VISIBLE);
			mCreatePlaceIt.setVisibility(View.VISIBLE);
			
			/* enable LOGOUT button*/
			mLoginButton.setText("Logout");
			mLoginButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LoginUtils.logout(getBaseContext());
					/* reload this activity to update view */
					finish();
					startActivity(getIntent());
				}
			});
			
		}
	}
	
	/* getting the class for the intents */
	private void buttonEvent(String activity) {
		if (activity.equals(VIEWLISTS)) {
			goToActivity(VIEWLISTS);
		} else if (activity.equals(SHOWMAP)) {
			goToActivity(SHOWMAP);
		} else if (activity.equals(LOGIN)) {
			/* only can login if there's internet */
			ConnectionDetector cd = new ConnectionDetector(getBaseContext());
		    if (cd.isConnectingToInternet()) {
		    	goToActivity(LOGIN);
		    }
		    else {
		    	Toast.makeText(getBaseContext(),
						"Please connect to internet to login!",
						Toast.LENGTH_SHORT).show();
		    }
		} else if (activity.equals(CREATEPLACEIT)){
			goToActivity(CREATEPLACEIT);
		} else {
			throw new IllegalArgumentException("No Activity to run!");
		}
	}
	
	/* actually going to the next acitivty*/
	private void goToActivity(String activity) {
		Intent intent;
		try {
			intent = new Intent(this, Class.forName(activity));
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			Log.d("MainActivity", "can't find class "+activity);
			e.printStackTrace();
		}
	}


}