/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: This is the Fragment class that allows the user to pick a 
 * start and end time for their place-its. 
 */

package place_its;

import java.util.Calendar;

import com.example.place_its.R;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		OnTimeSetListener {
	/* Instance variable */
	private final static String START_TIME = "startTimeField";

	/*
	 * When the fragment is created it opens the onCreateDialog. (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	/*
	 * The callback method that is used when the user selects time to populate
	 * fields. (non-Javadoc)
	 * 
	 * @see
	 * android.app.TimePickerDialog.OnTimeSetListener#onTimeSet(android.widget
	 * .TimePicker, int, int)
	 */
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
		FragmentManager fragmanager = getFragmentManager();

		if (fragmanager.findFragmentByTag(START_TIME) != null) {
			FieldTimeUpdater.setTimeForField(getActivity(),
					R.id.startTimeField, hourOfDay, minute);
		} else {
			FieldTimeUpdater.setTimeForField(getActivity(), R.id.endTimeField,
					hourOfDay, minute);
		}
	}
}
