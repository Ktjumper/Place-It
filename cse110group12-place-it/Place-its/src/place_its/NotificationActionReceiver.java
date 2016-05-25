/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: Deals with what happens when you discard and
 * repost a place it. 
 */
package place_its;

import reminder.PlaceIt;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationActionReceiver extends BroadcastReceiver {

	/*
	 * This is a notification action receiver that handled pending Intents if
	 * the user discards or reposts a Place-it. It handles both of them by
	 * adjusting flags. (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("NotificationDismiss", "In notification receiver");
		DatabaseHandler db = new DatabaseHandler(context);
		Bundle bundle = intent.getExtras();

		long keyId = bundle.getLong(PlaceItUtils.KEY_ID);
		String status = bundle.getString(ProximityUtils.STATUS_FLAG);
		Log.d("NotificationDismiss", "Key ID: " + keyId + " with status "
				+ status);

		PlaceIt thisPlaceit = PlaceItUtils.getInstance().getList()
				.get((int) keyId - 1);
		int notificationID;
		/* Discarded the place it. Therefore get deleted flag set. */
		if (status.equals(ProximityUtils.REPOST_FLAG)) {
			notificationID = bundle.getInt(ProximityUtils.REPOST_FLAG);
			ProximityActivity.repostIfActive(
					thisPlaceit.getFrequencyOfRepeats(), thisPlaceit, db);
			db.close();
		} else {
			notificationID = bundle.getInt(ProximityUtils.DISCARD_DELETE_FLAG);
			db.setDeleted(keyId);
			thisPlaceit.setDeleted(1);
			thisPlaceit.setPosted(0);
			thisPlaceit.setPulled(0);
			db.close();
		}

		((NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE))
				.cancel(notificationID);
		/* Your code to handle the event here */
	}
}