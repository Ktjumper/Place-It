/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: This is a utilities class for the Place-its. It has multiple constants,
 * and an array with all the Place-its in it. Furthermore, it is implemented
 * as Singleton, so that there is only one copy of the class. 
 */
package place_its;

import java.util.ArrayList;

import reminder.PlaceIt;
import android.util.Log;

public class PlaceItUtils {

	/* Constants that are used when passing information around */
	public static final String PLACE_IT_LOCATION = "place_its.placeItLocation";
	public static final String PLACE_IT_LATITUDE = "PLACE_IT_LATITUDE";
	public static final String PLACE_IT_LONGITUDE = "PLACE_IT_LONGITUDE";
	public static final String NEW_PLACE_IT_TITLE = "NEW_PLACE_IT_TITLE";
	public static final String PLACE_IT_IN_RANGE_TITLE = "PLACE_IT_IN_RANGE_TITLE";
	public static final String PLACE_IT_IN_RANGE_BUNDLE = "PLACE_IT_IN_RANGE_BUNDLE";
	public static final int PLACEIT_POSTED = 1;
	public static final int UNSET_PLACEIT_POSTED = 0;
	public static final int POSTED_ITEM_VIEW = 1;
	public static final int PULLED_DOWN_ITEM_VIEW = 2;
	public static final int DELETED_ITEM_VIEW = 3;
	public static final int ITEM_PULLED_DOWN = 1;
	public static final int ITEM_DELETED = 1;
	public static final String RADIUS_OF_PLACEIT = "804";// "804.67200";
	public static final String PLACE_IT_DESCRIPTION = "NEW_PLACE_IT_DESCRIPTION";
	public static final String KEY_ID = "KEY_ID";
	public static final int CATEGORICAL_PLACEIT = 1;
	public static final int REGULAR_PLACEIT = 0;
	public static final int LOCATIONS_VIEW = 1;
	public static final String CATEGORICAL_PLACEIT_RESULT = "name";
	public static final String CATEGORICAL_PLACEIT_ADDRESS = "address";
	public static final String CATEGORICAL_PLACEIT_PHONE_NUMBER = "phone";

	// Instance to acquire lock on so that Singleton can be achieved
	/*
	 * There is only one instance of PlaceItUtils present at all times. This is
	 * achieved using the Singleton DP.
	 */
	private static Object sInstanceLock = new Object();

	private final ArrayList<PlaceIt> placeItArray = new ArrayList<PlaceIt>();

	private static PlaceItUtils mInstance = null;

	public static PlaceItUtils getInstance() {
		synchronized (sInstanceLock) {
			if (mInstance == null) {
				mInstance = new PlaceItUtils();
			}
			return mInstance;
		}
	}

	/* Return a list of all placeits */
	public ArrayList<PlaceIt> getList() {
		return placeItArray;
	}

	/* Return a list of all placeits */
	public ArrayList<PlaceIt> getRegularList() {
		ArrayList<PlaceIt> regular = new ArrayList<PlaceIt>();
		for (PlaceIt pl : placeItArray) {
			Log.d("Value of type: " + pl.getmTypeOfPlaceIt(), "getRegularList");
//			if(pl.getmTypeOfPlaceIt() != (PlaceItUtils.CATEGORICAL_PLACEIT))
//				regular.add(pl);
			if (pl.getLocationOfPlaceIt().latitude != 0)
				regular.add(pl);
		}
		return regular;
	}
	
	public ArrayList<PlaceIt> getCategoricalList() {
		ArrayList<PlaceIt> categorical = new ArrayList<PlaceIt>();
		for (PlaceIt pl : placeItArray) {
			if(pl.getmTypeOfPlaceIt() == (PlaceItUtils.CATEGORICAL_PLACEIT))
				categorical.add(pl);
		}
		return categorical;
	}
	
	public PlaceIt getPlaceit(long keyId)
	{
		return placeItArray.get((int) (keyId - 1));
	}
	/*
	 * Method to add placeit to the list of palceits.
	 */
	public void addPlaceItsToList(PlaceIt pi) {
		Log.d("addPlaceItToList", "Trying to add: " + pi.getTitle());
		placeItArray.add(pi);
	}
	
	public void clearPlaceItsList() {
		placeItArray.clear();
	}
}
