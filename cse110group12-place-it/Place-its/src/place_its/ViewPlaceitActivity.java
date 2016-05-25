/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 2/16/14
 * Description: This is the activity for viewing a place-it. It handles user
 * interaction by adding button listeners. 
 */
package place_its;

import reminder.PlaceIt;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.place_its.R;

public class ViewPlaceitActivity extends Activity {

	private static double latitude = 0;
	private static double longitude = 0;
	private static String title;
	private static String description;
	private static String start;
	private static String end;
	private static String frequency;
	private static Button mDeleteBtn;
	private static Button mSnoozeBtn;
	private static Button mRepostBtn;
	private long keyId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_placeit);

		/*
		 * Get latitude and longitude from list or marker Used to identify the
		 * placeit
		 */
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			title = bundle.getString("title");
			description = bundle.getString("description");
			start = bundle.getString("start");
			end = bundle.getString("end");
			latitude = bundle.getDouble("latitude");
			longitude = bundle.getDouble("longitude");
			keyId = bundle.getLong("keyId");
			frequency = bundle.getString("frequency");
		}

		TextView titleText = (TextView) findViewById(R.id.PlaceitTitle);
		TextView descriptionText = (TextView) findViewById(R.id.PlaceitDescription);
		TextView startText = (TextView) findViewById(R.id.PlaceitStart);
		TextView endText = (TextView) findViewById(R.id.PlaceitEnd);
		TextView longitudeText = (TextView) findViewById(R.id.PlaceitLongitude);
		TextView latitudeText = (TextView) findViewById(R.id.PlaceitLatitude);
		TextView frequencyText = (TextView) findViewById(R.id.PlaceitFrequency);
		TextView locationnameText = (TextView) findViewById(R.id.PlaceitLocationName);

		titleText.setText((title + "               ").substring(0, 14).trim());
		descriptionText.setText(description);
		startText.setText("Start: " + start);
		endText.setText("End: " + end);
		latitudeText.setText("Latitude: " + String.valueOf(latitude));
		longitudeText.setText("Longitude: " + String.valueOf(longitude));
		frequencyText.setText(frequency);
		locationnameText.setText("Location: ");

		mDeleteBtn = (Button) findViewById(R.id.deleteButton);
		mDeleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deletePlaceit(v);
				finish();
			}
		});

		mSnoozeBtn = (Button) findViewById(R.id.snoozeButton);
		mSnoozeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlaceIt currPl = PlaceItUtils.getInstance().getList()
						.get((int) keyId - 1);
				if (currPl.getPosted() == 0 && currPl.getDeleted() != 1) {
					DatabaseHandler db = new DatabaseHandler(getBaseContext());
					TimeToStringConverter converter = new TimeToStringConverter();
					ProximityActivity.repeatTime(currPl.getStartDate(), 0, 0,
							converter, currPl, 45, db);
					currPl.setPosted(1);
					currPl.setPulled(0);
					currPl.setDeleted(0);
					db.setPosted(currPl.getKeyId());
					db.close();
					

				} else {
					Toast.makeText(getBaseContext(),
							"Invalid action. Already posted or deleted!",
							Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});

		mRepostBtn = (Button) findViewById(R.id.repostButton);
		mRepostBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlaceIt currPl = PlaceItUtils.getInstance().getList()
						.get((int) keyId - 1);
				if (currPl.getPosted() == 0) {
					ProximityActivity.repostIfActive(
							currPl.getFrequencyOfRepeats(), currPl,
							new DatabaseHandler(getBaseContext()));
				} else {
					Toast.makeText(getBaseContext(),
							"Invalid action. Already posted!",
							Toast.LENGTH_SHORT).show();
				}
				finish();

				/* richard add stuff in here for OK button */
			}
		});
	}

	public void deletePlaceit(View view) {
		DatabaseHandler db = new DatabaseHandler(view.getContext());

		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt thisPlaceit = plUtils.getPlaceit(keyId);
		Log.d("FLAGS", "FLAGS" + thisPlaceit.getPosted() + thisPlaceit.getPulled() + thisPlaceit.getDeleted());
		thisPlaceit.setPosted(0);
		thisPlaceit.setDeleted(1);
		thisPlaceit.setPulled(0);

		db.setDeleted(thisPlaceit.getKeyId());
		

		db.close();
		

	}

}
