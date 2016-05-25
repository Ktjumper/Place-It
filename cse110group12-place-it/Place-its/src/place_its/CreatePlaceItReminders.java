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
package place_its;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.place_its.R;

/**
 * @author Kristiyan
 * 
 */
public class CreatePlaceItReminders extends FragmentActivity {
	private Button mRegularPlaceit;
	private Button mCategoricalPlaceit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("onCreate", "CreatePlaceItReminders!");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_placeit_reminder);

		// Listener for regular place it button on UI
		mRegularPlaceit = (Button) findViewById(R.id.CreateRegularPlaceIt);
		mRegularPlaceit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						MapActivity.class);
				Toast.makeText(getBaseContext(), "Press on Map to create Place-it",
						Toast.LENGTH_SHORT).show();
				startActivity(intent);
			}
		});

		// Listener for categorical place it button on UI
		mCategoricalPlaceit = (Button) findViewById(R.id.CreateCategoricalPlaceit);
		mCategoricalPlaceit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						CreateCategoricalPlaceIt.class);
				Toast.makeText(getBaseContext(), "Testing the google places",
						Toast.LENGTH_SHORT).show();
				startActivity(intent);
			}
		});

	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_placeit, menu);
		return true;
	}
}
