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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import login.LoginUtils;
import login.PlaceItParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import reminder.Categorical;
import reminder.PlaceIt;
import reminder.Reminder;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

/**
 * @author Kristiyan
 * 
 */
public class CreateCategoricalPlaceIt extends FragmentActivity {
	private Button mConfirmButton;

	public static final String TAG = "CreateCategoricalPlaceit";
	/*
	 * Variables to store the UI components for the activity
	 */
	private TextView mTitle;
	private TextView mDescription;
	private TextView mStartDate;
	private TextView mStartTime;
	private TextView mEndDate;
	private TextView mEndTime;
	private double mPlaceItLatitude;
	private double mPlaceItLongitude;
	private Spinner mRepeatSpinner;
	private Spinner mSpinnerOne;
	private Spinner mSpinnerTwo;
	private Spinner mSpinnerThree;
	private PlaceIt placeIt;

	private Button mCancelButton;

	private boolean validInput = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_categorical_placeit);

		FieldTimeUpdater.setupStartDateAndTime(this, R.id.startDateField,
				R.id.startTimeField);
		FieldTimeUpdater.setupEndDateAndTime(this, R.id.endDateField,
				R.id.endTimeField);

		mTitle = (EditText) findViewById(R.id.titleField);
		mDescription = (EditText) findViewById(R.id.descriptionField);

		Bundle bundle = getIntent().getBundleExtra(
				PlaceItUtils.PLACE_IT_LOCATION);

		if (bundle != null) {
			mPlaceItLatitude = bundle.getDouble(PlaceItUtils.PLACE_IT_LATITUDE);
			mPlaceItLongitude = bundle
					.getDouble(PlaceItUtils.PLACE_IT_LONGITUDE);
			Log.d("onCreateLat", "" + mPlaceItLatitude);
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

		mRepeatSpinner = (Spinner) findViewById(R.id.repeatSpinner);
		ArrayAdapter<CharSequence> mTypeOne = ArrayAdapter.createFromResource(
				this, R.array.spinner_array,
				android.R.layout.simple_spinner_item);
		mTypeOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mRepeatSpinner.setAdapter(mTypeOne);

		createCategoricalSpinner();
		
		/* Once the confirm button is clicked, get all the selected categories from the
		 * spinner, and pull all the data from the user-completed fields. Use these
		 * as parameters for creating a new place it and send that place it to the
		 * database.
		 */
		mConfirmButton = (Button) findViewById(R.id.ConfirmButton);
		mConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* Create a categorical placeit */
				String mSpinnerOneString = mSpinnerOne.getSelectedItem()
						.toString();
				String mSpinnerTwoString = mSpinnerTwo.getSelectedItem()
						.toString();
				String mSpinnerThreeString = mSpinnerThree.getSelectedItem()
						.toString();
				validateInput(mSpinnerOneString,
						mSpinnerTwoString, mSpinnerThreeString);
				if (validInput) {
					DatabaseHandler db = new DatabaseHandler(v.getContext());
					String title = mTitle.getText().toString();
					/* Validity checks */
					if (title.length() == 0) {
						Toast.makeText(getApplication(),
								"Invalid Form! Select a title!",
								Toast.LENGTH_SHORT).show();
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
					LatLng location = new LatLng(mPlaceItLatitude, System
							.currentTimeMillis());
					placeIt = new Reminder(title, description, startDate,
							endDate, location, 0);
					placeIt = new Categorical(placeIt);
					placeIt.setFrequencyOfRepeats(mRepeatSpinner
							.getSelectedItem().toString());
					placeIt.setmCategoryOne(mSpinnerOneString);
					placeIt.setmCategoryTwo(mSpinnerTwoString);
					placeIt.setmCategoryThree(mSpinnerThreeString);
					placeIt.setPosted(1);
					Log.d(placeIt.getTitle(), "Creating Categorical");
					Log.d("Value of type: " + placeIt.getmTypeOfPlaceIt(),
							"CreateCategoricalPlaceIt");
					db.addToDatabase(placeIt);

					showProgress(true);

					postdata();

					Log.d("Attempted the service "
							+ getApplicationContext() == null ? "null"
							: "not null", "CreateCategoricalPlaceIt");
					getApplicationContext().startService(
							QueryForCategories
									.createIntent(getApplicationContext()));
					Log.d("Attempted the service "
							+ QueryForCategories.getUniqueInstance() == null ? "blah"
							: "hello", "CreateCategoricalPlaceIt");
					finish();
				} else {
					Toast.makeText(getBaseContext(),
							"Please select a category!", Toast.LENGTH_SHORT)
							.show();
				}
			}

			@SuppressLint("UseSparseArrays")
			private void validateInput(String mSpinnerOneString,
					String mSpinnerTwoString, String mSpinnerThreeString) {
				int numberOfCategories = 0;
				// Hash the categories to a hash map to avoid duplicates
				Map<Integer, String> tester = new HashMap<Integer, String>();

				if (!mSpinnerOneString.equals(ProximityUtils.DEFAULT_CATEGORY)) {
					tester.put(1, mSpinnerOneString);
					numberOfCategories++;
				}
				if (!mSpinnerTwoString.equals(ProximityUtils.DEFAULT_CATEGORY)
						&& !tester.containsValue(mSpinnerTwoString)) {
					tester.put(2, mSpinnerTwoString);
					numberOfCategories++;
				}
				if (!mSpinnerThreeString
						.equals(ProximityUtils.DEFAULT_CATEGORY)
						&& !tester.containsValue(mSpinnerThreeString)) {
					tester.put(3, mSpinnerThreeString);
					numberOfCategories++;
				}

				if (numberOfCategories > 0)
					validInput = true;
			}
		});

		// Listener for the cancel button
		mCancelButton = (Button) findViewById(R.id.CancelButton);
		mCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Creating cancelled!",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		});
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
	 * Create the DatePicker fragment for the user to pick their start and end
	 * time for the place-its.
	 */
	private DialogFragment createDatePicker() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.setCancelable(true);
		return newFragment;
	}

	/* Function to validate what the user provided for time and date */
	private boolean validateDateInput() {
		String fieldDate = mStartDate.getText().toString();
		String fieldTime = mStartTime.getText().toString();
		String[] time = fieldTime.split("[: ]");
		Log.d("Testing", "Time: " + time[0] + " - " + time[1] + " - " + time[2]);
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

		long diff = startDate.getTimeInMillis() - endDate.getTimeInMillis();

		Log.d("The time validity", "Validity -> " + diff);
		return diff <= 0;
	}

	/* 
	 * Used to create three categorical spinners that the user can choose from
	 */
	private void createCategoricalSpinner() {
		mSpinnerOne = (Spinner) findViewById(R.id.typeSpinnerOne);
		ArrayAdapter<CharSequence> mTypeOne = ArrayAdapter.createFromResource(
				this, R.array.categories_array,
				android.R.layout.simple_spinner_item);
		mTypeOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerOne.setAdapter(mTypeOne);

		mSpinnerTwo = (Spinner) findViewById(R.id.typeSpinnerTwo);
		ArrayAdapter<CharSequence> mTypeTwo = ArrayAdapter.createFromResource(
				this, R.array.categories_array,
				android.R.layout.simple_spinner_item);
		mTypeTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerTwo.setAdapter(mTypeTwo);

		mSpinnerThree = (Spinner) findViewById(R.id.typeSpinnerThree);
		ArrayAdapter<CharSequence> mTypeThree = ArrayAdapter
				.createFromResource(this, R.array.categories_array,
						android.R.layout.simple_spinner_item);
		mTypeThree
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerThree.setAdapter(mTypeThree);
	}

	/* Add the place it to datastore */
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		findViewById(R.id.EVERYTHING).setVisibility(View.GONE);

		int shortAnimTime = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		final View mLoadStatusView = findViewById(R.id.load_status);

		mLoadStatusView.setVisibility(View.VISIBLE);
		mLoadStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mLoadStatusView.setVisibility(show ? View.VISIBLE
								: View.GONE);
					}
				});
	}

}
