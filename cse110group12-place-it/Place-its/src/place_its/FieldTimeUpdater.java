/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: Class to deal with converting from 
 * 24 hrs to 12 hrs and AM/PM 
 */
package place_its;

import com.example.place_its.R;

import android.app.Activity;
import android.text.format.Time;
import android.widget.EditText;


public class FieldTimeUpdater {

	private static final int OFFSET = 1;

	/*
	 *  Used to convert 24hr format to 12hr format with AM/PM values
	 */
	private static String updateTime(int hours, int mins) {

		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);

		// Append in a StringBuilder
		String aTime = new StringBuilder().append(hours).append(':')
				.append(minutes).append(" ").append(timeSet).toString();

		return aTime;
	}

	/* 
	 * Method used to setup the date and time fields with the right values
	 * for the start date and time.
	 */
	public static void setupStartDateAndTime(Activity currentActivity,
			int startDateField, int startTimeField) {
		Time now = new Time();
		now.setToNow();
		((EditText) currentActivity.findViewById(R.id.startDateField))
				.setText((now.month + OFFSET) + "/" + now.monthDay + "/"
						+ now.year);

		String updatedTime = updateTime(now.hour, now.minute);
		((EditText) currentActivity.findViewById(R.id.startTimeField))
				.setText(updatedTime);
	}

	/*
	 * Method to setup the end date and time for the text fields
	 */
	public static void setupEndDateAndTime(Activity currentActivity,
			int endDateField, int endTimeField) {
		Time now = new Time();
		now.setToNow();
		((EditText) currentActivity.findViewById(endDateField))
				.setText((now.month + OFFSET) + "/" + now.monthDay + "/"
						+ now.year);

		String updatedTime = updateTime(now.hour, now.minute);
		((EditText) currentActivity.findViewById(R.id.endTimeField))
				.setText(updatedTime);
	}

	public static void setTimeForField(Activity currentActivity, int fieldId,
			int hour, int minute) {
		String updatedTime = updateTime(hour, minute);
		((EditText) currentActivity.findViewById(fieldId)).setText(updatedTime);
	}

	public static void setDateForField(Activity currentActivity,
			int fieldId, int month, int day, int year) {
		((EditText) currentActivity.findViewById(fieldId))
		.setText((month) + "/" + day + "/" + year);
	}
}
