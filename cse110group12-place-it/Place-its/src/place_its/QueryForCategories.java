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

import java.util.List;

import com.example.place_its.R;
import com.google.android.gms.maps.model.LatLng;

import reminder.PlaceIt;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * @author Kristiyan
 * 
 */
public class QueryForCategories extends Service {
	GooglePlaces mGooglePlaces;
	GPSTracker mTracker;
	PlaceList mPlaceList = null;
	PlaceItUtils mPlaceItUtils = PlaceItUtils.getInstance();
	// boolean to check if the service containing this singleton is binded to
	// some activity
	static boolean mBound = false;
	static QueryForCategories mService;

	/*
	 * There is only one instance of PlaceItUtils present at all times. This is
	 * achieved using the Singleton DP.
	 */
	private static Object sInstanceLock = new Object();

	private static QueryForCategories mInstance = null;

	// delete this later
	// My attempt to make a singleton
	public static QueryForCategories getUniqueInstance() {
		synchronized (sInstanceLock) {
			if (mInstance == null) {
				mInstance = new QueryForCategories();
			}
			Log.d("Instance is " + mInstance == null ? "Null" : "not null",
					"Query getUniq...");
			return mInstance;
		}
	}

	/* 
	 * Called every time a place it is created in CreateCategoricalPlaceIt 
	 * (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("Start is " + intent == null ? "Null" : "not null",
				"Query onStartComm....");
		// HEATHER DID THIS -------
		mIsQueryDone = checkAgainstCategoricalPlaceIts();
		return Service.START_NOT_STICKY;
	}

	// helper for the activity
	static public ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
			Log.d("Service has connected", "onServiceConnected");
		}

		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
			Log.d("Service has disconnected", "onServiceConnected");
		}
	};

	public static Intent createIntent(Context context) {
		Log.d("Context is " + mInstance == null ? "Null" : "not null",
				"Query createInt....");
		Intent intent = new Intent(context, QueryForCategories.class);
		Log.d("Intent is " + mInstance == null ? "Null" : "not null",
				"Query createInt....");
		return intent;
	}

	@SuppressWarnings("unused")
	private final IBinder mBinder = new LocalBinder();
	@SuppressWarnings("unused")
	private boolean mIsQueryDone;

	public class LocalBinder extends Binder {
		QueryForCategories getService() {
			// Return this instance so clients can call public methods
			return mInstance;
		}
	}

	// Called only the very first time the QueryForCategories service is started
	public void onCreate() {
		super.onCreate();
		mGooglePlaces = new GooglePlaces();
		mTracker = new GPSTracker(getApplicationContext());
	}

	public void onStop() {
		Log.d("On stop...", "Service onStop...!");
		/* stop and release resources */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		mTracker = new GPSTracker(getBaseContext());
		mGooglePlaces = new GooglePlaces();

		return null;
	}

	/* 
	 * Check all categorical place its to see if the user is within range of 
	 * a place category
	 */
	public boolean checkAgainstCategoricalPlaceIts() {
		List<PlaceIt> categorical = mPlaceItUtils.getCategoricalList();
		boolean status = false;
		for (PlaceIt pl : categorical) {
			Log.d("PlaceIt being queried: " + pl, "checkAgainst...");
			status = queryGooglePlaces(pl, pl.getKeyId());
		}
		return status;
	}

	/* 
	 * Helper function for checkAgainstCategoricalPlaceIts
	 */
	private boolean queryGooglePlaces(PlaceIt placeIt, final long keyId) {
		boolean status = false;
		try {
			StringBuilder queryString = new StringBuilder();
			String cat1, cat2, cat3;
			if ((cat1 = placeIt.getmCategoryOne()) != null) {
				queryString.append(cat1 + "|");
				status = true;
			}

			if ((cat2 = placeIt.getmCategoryTwo()) != null) {
				queryString.append(cat2 + "|");
				status = true;
			}

			if ((cat3 = placeIt.getmCategoryThree()) != null) {
				queryString.append(cat3);
				status = true;
			}
			mPlaceList = mGooglePlaces.search(mTracker.getLatitude(),
					mTracker.getLongitude(),
					Double.valueOf(PlaceItUtils.RADIUS_OF_PLACEIT),
					queryString.toString());
			Log.d("" + mPlaceList, "Testing QueryForCategories search");
			Log.d("QueryFor in Time: " + System.currentTimeMillis(),
					"QueryForC...");
			Thread t = new Thread() {

				public void run() {
					notifyIfThere(keyId);
				}
			};
			t.start();
			t.wait(1500);

			Log.d("" + mPlaceList.results, "Result of GP");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;
	}

	/* 
	 * Check all the Google Places around and compare it to the current place it's
	 * category to determine whether or not to trigger place it 
	 */
	void notifyIfThere(long keyId) {
		float[] results = new float[1]; /* Go figure! That is really cool */
		Place closest = null;
		float closestDist = 0;
		mPlaceList = mGooglePlaces.getmPlaceList();
		Log.d("Notifying if there " + mPlaceList + " " + keyId,
				"notifyIfThere!");
		if (mPlaceList.results != null) {
			if (mPlaceList.results.size() > 0) {
				for (Place pl : mPlaceList.results) {
					// Find the closest place by distance
					calculateDistance(pl, mGooglePlaces.getLocation(), results);
					if (closest == null) {
						closest = pl;
					} else if (results[0] < closestDist) {
						closest = pl;
						closestDist = results[0];
					}
				}
				// Send a notification with the closest place to the user
				PlaceIt pl = PlaceItUtils.getInstance().getPlaceit(keyId);
				if(pl.getPosted() == PlaceItUtils.PLACEIT_POSTED)
					sendNotification(keyId, closest);
			}
		}
	}

	/**
	 * Sends a notification to the user with the closest place that matches
	 * the category they specified for their place it
	 * @param keyId
	 * @param closest
	 */
	private void sendNotification(long keyId, Place closest) {

		Log.d("Sending a notification", "sendNotification!");

		int notificationId = (int) System.currentTimeMillis();
		Intent notificationIntent = new Intent(getApplicationContext(),
				CategoricalNotificationView.class);
		notificationIntent.putExtra(PlaceItUtils.KEY_ID, keyId);
		notificationIntent.putExtra(PlaceItUtils.CATEGORICAL_PLACEIT_RESULT,
				closest.getName());
		notificationIntent.putExtra(PlaceItUtils.CATEGORICAL_PLACEIT_ADDRESS,
				closest.getVicinity());
		notificationIntent.putExtra(
				PlaceItUtils.CATEGORICAL_PLACEIT_PHONE_NUMBER,
				closest.getFormatted_phone_number());

		Intent repostIntent = new Intent(getApplicationContext(),
				NotificationActionReceiver.class);
		repostIntent.putExtra(ProximityUtils.REPOST_FLAG, notificationId);
		repostIntent.putExtra(ProximityUtils.STATUS_FLAG,
				ProximityUtils.REPOST_FLAG);
		repostIntent.putExtra(PlaceItUtils.KEY_ID, keyId);
		PendingIntent piRepost = PendingIntent.getBroadcast(
				this.getApplicationContext(), notificationId, repostIntent, 1);

		Intent discardIntent = new Intent(getApplicationContext(),
				NotificationActionReceiver.class);
		discardIntent.putExtra(ProximityUtils.DISCARD_DELETE_FLAG,
				notificationId);
		discardIntent.putExtra(ProximityUtils.STATUS_FLAG,
				ProximityUtils.DISCARD_DELETE_FLAG);
		discardIntent.putExtra(PlaceItUtils.KEY_ID, keyId);
		PendingIntent piDeleteDiscard = PendingIntent.getBroadcast(
				this.getApplicationContext(), notificationId, discardIntent, 0);

		/** Getting the System service NotificationManager */
		NotificationManager nManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		/** Configuring notification builder to create a notification */
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				getApplicationContext())
				.setWhen(System.currentTimeMillis())
				.setContentText("Categorical Place-It")
				.setContentTitle(closest.getName())
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.addAction(R.drawable.notification_repost, "Repost", piRepost)
				.addAction(R.drawable.notification_discard, "Discard", piDeleteDiscard)
				.setContentIntent(pendingIntent);

		/** Creating a notification from the notification builder */
		Notification notification = notificationBuilder.build();

		nManager.notify(notificationId, notification);
	}

	/* Calculates distance between location of Google Place and current place it */
	private void calculateDistance(Place pl, LatLng location, float[] results) {
		Location.distanceBetween(pl.getGeometry().location.lat,
				pl.getGeometry().location.lng, location.latitude,
				location.longitude, results);

	}
}
