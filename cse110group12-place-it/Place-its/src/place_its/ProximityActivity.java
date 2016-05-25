/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: This is the class that keeps track of all the Place-its
 * that are within range. It creates intents that are used by the Notification
 * class when a user walks in range. 
 */

package place_its;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import reminder.PlaceIt;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.place_its.R;

@SuppressLint("SimpleDateFormat")
public class ProximityActivity extends Activity {
	PlaceList placeList;
	String notificationTitle;
	String notificationContent;
	String tickerMessage;
	int notificationId = (int) System.currentTimeMillis();
	ProximityUtils mProximity = ProximityUtils.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean proximity_entering = getIntent().getBooleanExtra(
				LocationManager.KEY_PROXIMITY_ENTERING, true);
		DatabaseHandler db = new DatabaseHandler(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString(PlaceItUtils.PLACE_IT_IN_RANGE_TITLE);
		long keyId = bundle.getLong(PlaceItUtils.KEY_ID);
		Log.d(Long.toString(keyId), "Database Check KEYID: " + keyId
				+ " Title: " + title);
		if (proximity_entering) {
			mProximity.setNotified("" + keyId);
			Toast.makeText(getBaseContext(), "Entering the region",
					Toast.LENGTH_LONG).show();
			notificationTitle = "Place-It Proximity - Entry";
			notificationContent = "PlaceIt Title: " + title;
			tickerMessage = "Entered the region";
		} else {
			Toast.makeText(getBaseContext(), "Exiting the region",
					Toast.LENGTH_LONG).show();
			notificationTitle = "Proximity - Exit";
			notificationContent = "Exited the region";
			tickerMessage = "Exited the region";
		}

		Intent notificationIntent = new Intent(getApplicationContext(),
				NotificationView.class);
		notificationIntent.putExtra(PlaceItUtils.NEW_PLACE_IT_TITLE, title);
		notificationIntent.putExtra(PlaceItUtils.KEY_ID, keyId);

		Intent discardIntent = new Intent(getApplicationContext(),
				NotificationActionReceiver.class);
		discardIntent.putExtra(ProximityUtils.DISCARD_DELETE_FLAG,
				notificationId);
		discardIntent.putExtra(ProximityUtils.STATUS_FLAG,
				ProximityUtils.DISCARD_DELETE_FLAG);
		discardIntent.putExtra(PlaceItUtils.KEY_ID, keyId);
		PendingIntent piDeleteDiscard = PendingIntent.getBroadcast(
				this.getApplicationContext(), notificationId, discardIntent, 0);

		Intent repostIntent = new Intent(getApplicationContext(),
				NotificationActionReceiver.class);
		repostIntent.putExtra(ProximityUtils.REPOST_FLAG, notificationId);
		repostIntent.putExtra(ProximityUtils.STATUS_FLAG,
				ProximityUtils.REPOST_FLAG);
		repostIntent.putExtra(PlaceItUtils.KEY_ID, keyId);
		PendingIntent piRepost = PendingIntent.getBroadcast(
				this.getApplicationContext(), notificationId, repostIntent, 1);

		/**
		 * This is needed to make this intent different from its previous
		 * intents
		 */
		notificationIntent.setData(Uri.parse("tel:/" + notificationId));

		/**
		 * Creating different tasks for each notification. See the flag
		 * Intent.FLAG_ACTIVITY_NEW_TASK
		 */
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		/** Getting the System service NotificationManager */
		NotificationManager nManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		/** Configuring notification builder to create a notification */
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				getApplicationContext()).setWhen(System.currentTimeMillis())
				.setContentText(notificationContent)
				.setContentTitle(notificationTitle)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
				.addAction(R.drawable.notification_repost, "Repost", piRepost)
				.addAction(R.drawable.notification_discard, "Discard", piDeleteDiscard)
				.setTicker(tickerMessage).setContentIntent(pendingIntent);

		/** Creating a notification from the notification builder */
		Notification notification = notificationBuilder.build();
		PlaceItUtils plUtils = PlaceItUtils.getInstance();

		ArrayList<PlaceIt> pl = plUtils.getList();
		Log.d(""+keyId,"proximityActivity keyid");
		PlaceIt currentPl = pl.get(((int) keyId) - 1);
		
		String startDate = currentPl.getStartDate();
		TimeToStringConverter converter = new TimeToStringConverter();
		converter.stringStartDateToTime(startDate);

		Time placeItTime = new Time();
		placeItTime.set(converter.getSeconds(), converter.getMinute(),
				converter.getHour(), converter.getDay(), converter.getMonth(),
				converter.getYear());

		Time now = new Time();
		now.setToNow();

		/**
		 * Sending the notification to system. The first argument ensures that
		 * each notification is having a unique id If two notifications share
		 * same notification id, then the last notification replaces the first
		 * notification
		 * */
		boolean triggered = false; // ensures place-it is triggered for
									// 45 minute check later
		if (Time.compare(placeItTime, now) <= 0 && !triggered) {
			triggered = true;
			nManager.notify(notificationId, notification);

			String timeNow = converter.timeToStringStartDate(now);
			// Put the time triggered into the database given the keyId

			db.setTimeTriggered(keyId, timeNow);

			currentPl.setTimeTriggered(timeNow);
		}

		// if the place-it was already triggered
		Date d1 = null, d2 = null;
		if (triggered) {
			String timeTriggered = currentPl.getTimeTriggered();

			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			try {
				d1 = df.parse(timeTriggered);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Time current = new Time();
			current.setToNow();
			try {
				d2 = df.parse(converter.timeToStringStartDate(current));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			long timeDiff = Math.abs(d1.getTime() - d2.getTime());

			// 2700000 ms = 45 minutes
			if (timeDiff > (ProximityUtils.SNOOZE_MILLS)) {
				// notify again

				nManager.notify(notificationId, notification);
			}

			db.setTime(keyId, currentPl.getStartDate(), currentPl.getEndDate());
			db.setPosted(keyId);
			db.close();
		}
		/** Finishes the execution of this activity */
		finish();
	}

	/*
	 * Repost method which informs the database of its intent to repost the 
	 * place it, while maintaining its original frequency
	 */
	public static void repostIfActive(String frequency, PlaceIt currentPl,
			DatabaseHandler db) {
		TimeToStringConverter converter = new TimeToStringConverter();
		String startDate = currentPl.getStartDate();

		if (frequency.equals("Repeat weekly")) {
			repeatTime(startDate, 7, 0, converter, currentPl, 0, db);
		}

		if (frequency.equals("Repeat bi-weekly")) {
			repeatTime(startDate, 14, 0, converter, currentPl, 0, db);
		}

		if (frequency.equals("Repeat tri-weekly")) {
			repeatTime(startDate, 21, 0, converter, currentPl, 0, db);
		}

		if (frequency.equals("Repeat monthly")) {
			repeatTime(startDate, 0, 1, converter, currentPl, 0, db);
		}
		db.setPosted(currentPl.getKeyId());
		currentPl.setPulled(0);
		currentPl.setPosted(1);
		currentPl.setDeleted(0);
//		if (frequency.equals("Never Repeat")) {
//			currentPl.setPulled(1);
//			currentPl.setPosted(0);
//			currentPl.setDeleted(0);
//			db.setPulled(currentPl.getKeyId());
//		}
	}

	/* Helper function for repostIfActive */
	public static void repeatTime(String startDate, int numDaysChange,
			int numMonthsChange, TimeToStringConverter converter,
			PlaceIt currentPl, int minutesChange, DatabaseHandler db) {
		Time oldTime = converter.stringStartDateToTime(startDate);
		Time newTime = new Time();
		newTime.set(oldTime.second, oldTime.minute + minutesChange,
				oldTime.hour, oldTime.monthDay + numDaysChange, oldTime.month
						+ numMonthsChange, oldTime.year);
		newTime.normalize(true);

		String newTimeString = converter.timeToStringStartDate(newTime);
		db.setTime(currentPl.getKeyId(), newTimeString, newTimeString);
		currentPl.setStartDate(newTimeString);
		currentPl.setEndDate(newTimeString);
	}
}
