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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import login.LoginUtils;
import login.PlaceItParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import reminder.PlaceIt;
import reminder.Reminder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.place_its.R;
import com.google.android.gms.maps.model.LatLng;

public class CreateRegularPlaceit extends FragmentActivity {

	public static final String TAG = "CreateRegularPlaceit";

	/*
	 * Variables to store the UI components for the activity
	 */
	private TextView mTitle;
	private TextView mDescription;
	private TextView mStartDate;
	private TextView mStartTime;
	private TextView mEndDate;
	private TextView mEndTime;
	private Button mOkBtn;
	private Button mCancelBtn;
	private double mPlaceItLatitute;
	private double mPlaceItLongitude;
	private Spinner spinner;

	private PlaceIt placeIt;

	/*
	 * The onCreate method is called every time the Activity is started by and
	 * intent. UI components are set up and listeners are added for the buttons.
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// createSpinner();
		Log.d("onCreate", "CreateRegularPlaceIt!");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_placeit);
		FieldTimeUpdater.setupStartDateAndTime(this, R.id.startDateField,
				R.id.startTimeField);
		FieldTimeUpdater.setupEndDateAndTime(this, R.id.endDateField,
				R.id.endTimeField);

		mTitle = (EditText) findViewById(R.id.titleField);
		mDescription = (EditText) findViewById(R.id.descriptionField);

		Bundle bundle = getIntent().getBundleExtra(
				PlaceItUtils.PLACE_IT_LOCATION);

		if (bundle != null) {
			mPlaceItLatitute = bundle.getDouble(PlaceItUtils.PLACE_IT_LATITUDE);
			mPlaceItLongitude = bundle
					.getDouble(PlaceItUtils.PLACE_IT_LONGITUDE);
			Log.d("onCreateLat", "" + mPlaceItLatitute);
			Log.d("onCreateLNG", "" + mPlaceItLongitude);
		}

		/* Add listener to startDate */
		mStartDate = (EditText) findViewById(R.id.startDateField);
		mStartDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createDatePicker().show(getSupportFragmentManager(),
						"startDateField");
			}
		});

		/* Add Listener to startTime */
		mStartTime = (EditText) findViewById(R.id.startTimeField);
		mStartTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createTimePicker().show(getSupportFragmentManager(),
						"startTimeField");
			}
		});

		/* Add Listener to endDate */
		mEndDate = (EditText) findViewById(R.id.endDateField);
		mEndDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createDatePicker().show(getSupportFragmentManager(),
						"endDateField");
			}
		});

		/* Add Listener to endTime */
		mEndTime = (EditText) findViewById(R.id.endTimeField);
		mEndTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createTimePicker().show(getSupportFragmentManager(),
						"endTimeField");
			}
		});

		/* Add a listener to the mOkButton */
		mOkBtn = (Button) findViewById(R.id.mOkButton);
		mOkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DatabaseHandler db = new DatabaseHandler(v.getContext());
				String title = mTitle.getText().toString();
				/* Validity checks */
				if (title.length() == 0) {
					Toast.makeText(getApplication(),
							"Invalid Form! Select a title!", Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (!validateDateInput()) {
					Toast.makeText(getApplication(),
							"Invalid Date! Please fix to proceed!",
							Toast.LENGTH_SHORT).show();
					return;
				}

				/* Pull info from UI components */
				String description = mDescription.getText().toString();
				String startDate = mStartDate.getText().toString() + " "
						+ mStartTime.getText().toString();
				String endDate = mEndDate.getText().toString() + " "
						+ mEndTime.getText().toString();
				LatLng location = new LatLng(mPlaceItLatitute,
						mPlaceItLongitude);

				// Instantiate a new Reminder for the decorator pattern
				// In this case, requires no decorators as place it is regular
				placeIt = new Reminder(title, description, startDate, endDate,
						location, 0);
				Log.d("Creating Place It", placeIt.toString());
				placeIt.setPosted(PlaceItUtils.PLACEIT_POSTED);

				placeIt.setFrequencyOfRepeats(spinner.getItemAtPosition(
						spinner.getSelectedItemPosition()).toString());
				/* Add the Place-it to the database */
				db.addToDatabase(placeIt);

				/* Create the bundle to be returned with the result */
				Bundle bundle = new Bundle();
				bundle.putString(PlaceItUtils.NEW_PLACE_IT_TITLE,
						placeIt.getTitle());
				bundle.putDouble(PlaceItUtils.PLACE_IT_LATITUDE,
						location.latitude);
				bundle.putDouble(PlaceItUtils.PLACE_IT_LONGITUDE,
						location.longitude);

				setResult(Activity.RESULT_OK, (new Intent()).putExtras(bundle));

				postdata();

				finish();
			}

			/* Function to validate what the user provided for time and date */
			private boolean validateDateInput() {
				// TODO Auto-generated method stub
				String fieldDate = mStartDate.getText().toString();
				String fieldTime = mStartTime.getText().toString();
				String[] time = fieldTime.split("[: ]");
				Log.d("Testing", "Time: " + time[0] + " - " + time[1] + " - "
						+ time[2]);
				String[] date = fieldDate.split("/");
				int startMonth = Integer.parseInt(date[0]);
				int startDay = Integer.parseInt(date[1]);
				int startYear = Integer.parseInt(date[2]);

				Calendar startDate = Calendar.getInstance();
				startDate.set(Calendar.DAY_OF_MONTH, startDay);
				startDate.set(Calendar.MONTH, startMonth);
				startDate.set(Calendar.YEAR, startYear);
				startDate.set(Calendar.HOUR, Integer.valueOf(time[0]));
				startDate.set(Calendar.MINUTE, Integer.valueOf(time[1]));
				if (time[2].equals("AM")) {
					startDate.set(Calendar.AM_PM, Calendar.AM);
				} else {
					startDate.set(Calendar.AM_PM, Calendar.PM);
				}

				fieldDate = mEndDate.getText().toString();
				fieldTime = mEndTime.getText().toString();
				time = fieldTime.split("[: ]");
				date = fieldDate.split("/");
				Calendar endDate = Calendar.getInstance();
				endDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
				endDate.set(Calendar.MONTH, Integer.parseInt(date[0]));
				endDate.set(Calendar.YEAR, Integer.parseInt(date[2]));
				endDate.set(Calendar.HOUR, Integer.valueOf(time[0]));
				endDate.set(Calendar.MINUTE, Integer.valueOf(time[1]));
				if (time[2].equals("AM")) {
					endDate.set(Calendar.AM_PM, Calendar.AM);
				} else {
					endDate.set(Calendar.AM_PM, Calendar.PM);
				}

				long diff = startDate.getTimeInMillis()
						- endDate.getTimeInMillis();

				Log.d("The time validity", "Validity -> " + diff);
				return diff <= 0;
			}
		});

		mCancelBtn = (Button) findViewById(R.id.mCancelButton);
		mCancelBtn.setText("Cancel");
		mCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED, (new Intent()));
				finish();
			}
		});
		createSpinner();
	}

	/*
	 * Creates the TimePicker fragment so that the user can add a time
	 */
	protected DialogFragment createTimePicker() {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.setCancelable(true);
		return newFragment;
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

	/*
	 * Create the DatePicker fragment for the user to pick their start and end
	 * time for the place-its.
	 */
	private DialogFragment createDatePicker() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.setCancelable(true);
		return newFragment;
	}

	/*
	 * Create the spinner object to be used by the UI to select repeatability.
	 */
	public void createSpinner() {
		spinner = (Spinner) findViewById(R.id.repeatSpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.spinner_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	/* add the placeit to datastore */
	private void postdata() {
		// final ProgressDialog dialog = ProgressDialog.show(this,
		// "Posting Data...", "Please wait...", false);
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(MainActivity.ITEM_URI);

				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs
							.add(new BasicNameValuePair(
									"name",
									LoginUtils
											.getLoggedInUserKey(getApplicationContext())
											+ "."
											+ String.valueOf(placeIt.getKeyId())));
					nameValuePairs.add(new BasicNameValuePair("price",
							PlaceItParser.PlaceItToString(placeIt)));
					nameValuePairs
							.add(new BasicNameValuePair(
									"product",
									LoginUtils
											.getLoggedInUserKey(getApplicationContext())));
					nameValuePairs.add(new BasicNameValuePair("action", "put"));

					Log.d("ASD",
							"KEY"
									+ LoginUtils
											.getLoggedInUserKey(getApplicationContext()));
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = client.execute(post);
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					String line = "";
					while ((line = rd.readLine()) != null) {
						Log.d(TAG, line);
					}

				} catch (IOException e) {
					Log.d(TAG, "IOException while trying to conect to GAE");
				}
				// dialog.dismiss();
			}
		};

		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// dialog.show();
	}
}
