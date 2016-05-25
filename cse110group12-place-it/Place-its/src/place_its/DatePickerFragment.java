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

import java.util.Calendar;

import com.example.place_its.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	/* Instance variables */
	private final static String START_DATE = "startDateField";
	private int mYear;
	private int mMonth;
	private int mDay;

	/*
	 * When the fragment is created it opens the onCreateDialog. (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
	}

	/*
	 * Callback method when the user has selected a date so that appropriate
	 * actions can be taken. (non-Javadoc)
	 * 
	 * @see
	 * android.app.DatePickerDialog.OnDateSetListener#onDateSet(android.widget
	 * .DatePicker, int, int, int)
	 */
	public void onDateSet(DatePicker view, int year, int month, int day) {

		FragmentManager fragmanager = getFragmentManager();

		if (fragmanager.findFragmentByTag(START_DATE) != null) {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.DAY_OF_MONTH, mDay);
			today.set(Calendar.MONTH, mMonth);
			today.set(Calendar.YEAR, mYear);

			Calendar newDate = Calendar.getInstance();
			newDate.set(Calendar.DAY_OF_MONTH, day);
			newDate.set(Calendar.MONTH, month);
			newDate.set(Calendar.YEAR, year);

			/*
			 * Validate the input for the start field
			 */
			long diff = today.getTimeInMillis() - newDate.getTimeInMillis();
			if (diff > 0) {
				Toast.makeText(getActivity(),
						"Invalid Date! Current date selected!",
						Toast.LENGTH_SHORT).show();
				FieldTimeUpdater.setDateForField(getActivity(),
						R.id.endDateField, (mMonth + 1), mDay, mYear);
			} else {
				FieldTimeUpdater.setDateForField(getActivity(),
						R.id.startDateField, (month + 1), day, year);
			}
		} else {
			TextView startView = (TextView) getActivity().findViewById(
					R.id.startDateField);
			String fieldDate = startView.getText().toString();
			String[] date = fieldDate.split("/");
			int startMonth = Integer.parseInt(date[0]);
			int startDay = Integer.parseInt(date[1]);
			int startYear = Integer.parseInt(date[2]);

			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.DAY_OF_MONTH, startDay);
			startDate.set(Calendar.MONTH, startMonth);
			startDate.set(Calendar.YEAR, startYear);

			Calendar newDate = Calendar.getInstance();
			newDate.set(Calendar.DAY_OF_MONTH, day);
			newDate.set(Calendar.MONTH, month + 1);
			newDate.set(Calendar.YEAR, year);

			/*
			 * Validate the input for the second field.
			 */
			long diff = startDate.getTimeInMillis() - newDate.getTimeInMillis();
			if (diff > 0) {
				Toast.makeText(getActivity(),
						"Invalid Date! Start date selected!",
						Toast.LENGTH_SHORT).show();
				FieldTimeUpdater.setDateForField(getActivity(),
						R.id.endDateField, (startMonth), startDay, startYear);
			} else {
				FieldTimeUpdater.setDateForField(getActivity(),
						R.id.endDateField, (month + 1), day, year);
			}
		}
	}

}