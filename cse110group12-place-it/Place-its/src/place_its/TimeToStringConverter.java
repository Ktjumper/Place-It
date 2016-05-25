/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: A converter from time to string. 
 */

package place_its;

import android.text.format.Time;

public class TimeToStringConverter {
	private int month, day, year, hour, minute, seconds;

	// Set all information to default values
	public TimeToStringConverter() {
		month = 0;
		day = 0;
		year = 0;
		hour = 0;
		minute = 0;
		seconds = 0;
	}

	/* 
	 * Converts a time (of type Time) to a usable string
	 */
	public String timeToStringStartDate(Time now) {
		String timeNow = "" + (now.month + 1) + "/" + now.monthDay + "/"
				+ now.year + " ";

		boolean flagPm = false;
		if (now.hour > 12) {
			flagPm = true;
			int nowHour = now.hour % 12;
			timeNow += nowHour + ":";
		} else {
			timeNow += now.hour + ":";
		}

		if (now.minute < 10) {
			timeNow += "0" + now.minute + " ";
		} else {
			timeNow += now.minute + " ";
		}

		if (flagPm) {
			timeNow += "PM";
		} else {
			timeNow += "AM";
		}

		return timeNow;
	}

	/*
	 * Converts a string representing a time to an actual time (of type Time)
	 */
	public Time stringStartDateToTime(String startDate) {
		String[] startDateAndTime = startDate.split(" ");
		String plDate = startDateAndTime[0];
		String plTime = startDateAndTime[1];
		String amPm = startDateAndTime[2];

		String[] mdy = plDate.split("/");
		month = Integer.parseInt(mdy[0]);
		day = Integer.parseInt(mdy[1]);
		year = Integer.parseInt(mdy[2]);

		String[] hourAndMinute = plTime.split(":");

		hour = Integer.parseInt(hourAndMinute[0]);
		minute = Integer.parseInt(hourAndMinute[1]);
		seconds = 0;

		// If the time is PM
		if (amPm.equals("PM") && !hourAndMinute[0].equals("12")) {
			hour = hour + 12;
		} else if (amPm.equals("AM") && hourAndMinute[0].equals("12")) {
			hour = 0; // 12 am is 00:00:00
		}

		Time placeItTime = new Time();
		placeItTime.set(seconds, minute, hour, day, --month, year);

		return placeItTime;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getYear() {
		return year;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSeconds() {
		return seconds;
	}

	public void reset() {
		month = 0;
		day = 0;
		year = 0;
		hour = 0;
		minute = 0;
		seconds = 0;
	}
}
