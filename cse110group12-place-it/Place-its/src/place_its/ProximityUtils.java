/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: This class is to be used as a utilities class for the proximity
 * activity. It is Singleton, so that it ensures one copy at all times. 
 */

package place_its;

import java.util.ArrayList;

public class ProximityUtils {

	public static final String DISCARD_DELETE_FLAG = "DISCARD_DELETE_FLAG";

	public static final String REPOST_FLAG = "REPOST_FLAG";

	public static final String STATUS_FLAG = "STATUS_FLAG";
	
	public static final String DEFAULT_CATEGORY = "Select a Category";

	/* This is constant for the duration of the snooze */
	protected static final int SNOOZE = 1;

	/* Repost in milliseconds. 45 mins == 2.7e+6 milliseconds. 1 min = 60000 */
	public static final long SNOOZE_MILLS = 60000;

	// Instance to acquire lock on
	private static Object sInstanceLock = new Object();

	private final ArrayList<String> statusArray = new ArrayList<String>();

	private static ProximityUtils mInstance = null;

	public static ProximityUtils getInstance() {
		synchronized (sInstanceLock) {
			if (mInstance == null) {
				mInstance = new ProximityUtils();
			}
			return mInstance;
		}
	}

	public boolean hasBeenNotified(String key) {
		return statusArray.contains(key);
	}

	public void unsetNotified(String elementToUnset) {
		statusArray.remove(elementToUnset);
	}

	public void setNotified(String elementToSet) {
		statusArray.add(elementToSet);
	}

}
