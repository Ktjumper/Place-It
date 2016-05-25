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

import reminder.PlaceIt;

import com.example.place_its.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CategoricalNotificationView extends FragmentActivity {
	private long keyId = 0; // ID of PlaceIt
	private Button mRepostBtn;
	private Button mSnoozeBtn;
	private Button mDeleteBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categorical_view);

		final DatabaseHandler db = new DatabaseHandler(getBaseContext());

		Bundle data = getIntent().getExtras();

		// Find all the text fields
		TextView titleText = (TextView) findViewById(R.id.PlaceitTitle);
		TextView descriptionText = (TextView) findViewById(R.id.PlaceitDescription);
		TextView locationnameText = (TextView) findViewById(R.id.PlaceitLocationName);
		TextView location = (TextView)findViewById(R.id.PlaceitLocation);

		keyId = (Long) data.get(PlaceItUtils.KEY_ID);

		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt pl = plUtils.getPlaceit(keyId);
		
		// Set the text fields
		titleText.setText(pl.getTitle());
		descriptionText.setText(pl.getDescription());
		locationnameText.setText(data.getString(PlaceItUtils.CATEGORICAL_PLACEIT_RESULT));
		location.setText("Address: " + String.valueOf(data.getString(PlaceItUtils.CATEGORICAL_PLACEIT_ADDRESS)));
		
		/* Action buttons */
		mDeleteBtn = (Button) findViewById(R.id.DeleteButton);
		mDeleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deletePlaceit(v);
				finish();
			}
		});

		mSnoozeBtn = (Button) findViewById(R.id.SnoozeButton);
		mSnoozeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlaceIt currPl = PlaceItUtils.getInstance().getPlaceit(keyId);
				// Only snooze if the place it is not already posted and not deleted
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
		
		mRepostBtn = (Button) findViewById(R.id.RepostButton);
		mRepostBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlaceIt currPl = PlaceItUtils.getInstance().getPlaceit(keyId);
				// If the place it is not already posted
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

		// Otherwise, set the categorical place it to pulled
		plUtils.getPlaceit(keyId).setDeleted(0);
		plUtils.getPlaceit(keyId).setPosted(0);
		plUtils.getPlaceit(keyId).setPulled(1);
		db.setPulled(pl.getKeyId());
	}

	public void deletePlaceit(View view) {
		// Instantiate a database handler so that the place it can be removed
		// from the database
		DatabaseHandler db = new DatabaseHandler(view.getContext());

		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt thisPlaceit = plUtils.getPlaceit(keyId);
		Log.d("FLAGS", "FLAGS in Notification " + thisPlaceit.getPosted()
				+ thisPlaceit.getPulled() + thisPlaceit.getDeleted());
		thisPlaceit.setPosted(0);
		thisPlaceit.setDeleted(1);
		thisPlaceit.setPulled(0);

		db.setDeleted(thisPlaceit.getKeyId());

		db.close();
	}
}
