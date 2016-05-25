/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: Deals with what happens when you
 * click on the notification of place it.  
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

public class NotificationView extends Activity {

	private long keyId = 0;
	private static Button mDeleteBtn;
	private static Button mSnoozeBtn;
	private static Button mRepostBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_placeit);

		final DatabaseHandler db = new DatabaseHandler(getBaseContext());

		Bundle data = getIntent().getExtras();

		/* Find all the correct text fields to be set later */
		TextView titleText = (TextView) findViewById(R.id.PlaceitTitle);
		TextView descriptionText = (TextView) findViewById(R.id.PlaceitDescription);
		TextView startText = (TextView) findViewById(R.id.PlaceitStart);
		TextView endText = (TextView) findViewById(R.id.PlaceitEnd);
		TextView frequencyText = (TextView) findViewById(R.id.PlaceitFrequency);
		TextView latText = (TextView) findViewById(R.id.PlaceitLatitude);
		TextView longText = (TextView) findViewById(R.id.PlaceitLongitude);
		TextView locationnameText = (TextView) findViewById(R.id.PlaceitLocationName);

		keyId = (Long) data.get(PlaceItUtils.KEY_ID);

		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		
		// Set the text fields to their corresponding data
		titleText.setText((plUtils.getPlaceit(keyId).getTitle() + "               ").substring(0, 14).trim());
		descriptionText.setText(plUtils.getPlaceit(keyId).getDescription());
		frequencyText.setText(plUtils.getPlaceit(keyId).getFrequencyOfRepeats());
		latText.setText(String.valueOf("Latitude: " + plUtils.getPlaceit(keyId).getLocationOfPlaceIt().latitude));
		longText.setText(String.valueOf("Longitude: " +plUtils.getPlaceit(keyId).getLocationOfPlaceIt().longitude));
        locationnameText.setText("Location: ");
		
		
		/* Listener for the delete button */
		mDeleteBtn = (Button) findViewById(R.id.deleteButton);
		mDeleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deletePlaceit(v);
				finish();
			}
		});

		startText.setText("Start: " + plUtils.getPlaceit(keyId).getStartDate());
		endText.setText("End: " +plUtils.getPlaceit(keyId).getEndDate());

		/* 
		 * Listener for the snooze button 
		 * Only allow if the current place it isn't already posted and if it is not
		 * deleted
		 */
		mSnoozeBtn = (Button) findViewById(R.id.snoozeButton);
		mSnoozeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlaceIt currPl = PlaceItUtils.getInstance().getList()
						.get((int) keyId - 1);
				if (currPl.getPosted() == 0 && currPl.getDeleted() != 1) {
					TimeToStringConverter converter = new TimeToStringConverter();
					ProximityActivity.repeatTime(currPl.getStartDate(), 0, 0,
							converter, currPl, ProximityUtils.SNOOZE, db);
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
		
		/* Listener for the repost button */
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
			}
		});
		
		// Set the status of the place it and set its status in the database
		plUtils.getPlaceit(keyId).setDeleted(0);
		plUtils.getPlaceit(keyId).setPosted(0);
		plUtils.getPlaceit(keyId).setPulled(1);
		db.setPulled(plUtils.getPlaceit(keyId).getKeyId());
	}

	/* Delete the place it from the database and update its status */
	public void deletePlaceit(View view) {
		DatabaseHandler db = new DatabaseHandler(view.getContext());

		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt thisPlaceit = plUtils.getPlaceit(keyId);
		Log.d("FLAGS", "FLAGS in Notification " + thisPlaceit.getPosted() + thisPlaceit.getPulled() + thisPlaceit.getDeleted());
		thisPlaceit.setPosted(0);
		thisPlaceit.setDeleted(1);
		thisPlaceit.setPulled(0);

		db.setDeleted(thisPlaceit.getKeyId());

		db.close();
	}
}
