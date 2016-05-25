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
package login;

import place_its.PlaceItUtils;
import reminder.Categorical;
import reminder.PlaceIt;
import reminder.Reminder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


public class PlaceItParser {
	
	
	public static PlaceIt StringToPlaceIt (String PlaceItString) {
		String[] placeitProp = PlaceItString.split("\\|\\|\\|");
				
		long mKeyId = Long.valueOf(placeitProp[0]);
		String mTitle = placeitProp[1];
		String mDescription = placeitProp[2];
		String mStartDate = placeitProp[3];
		String mEndDate = placeitProp[4];
		String mFrequencyOfRepeats = placeitProp[5];
		String[] location = placeitProp[6].split(",");
		LatLng mLocationOfPlaceIt = new LatLng(Double.valueOf(location[0]), Double.valueOf(location[1]));
		Integer mDistanceFromUser = Integer.valueOf(placeitProp[7]);
		int mPosted = Integer.valueOf(placeitProp[8]);
		int mPulled = Integer.valueOf(placeitProp[9]);
		int mDeleted = Integer.valueOf(placeitProp[10]);
		String mTimeTriggered = placeitProp[11];
		int category = Integer.valueOf(placeitProp[12]);
		String mCategory1 = placeitProp[13];
		String mCategory2 = placeitProp[14];
		String mCategory3 = placeitProp[15];
	
		PlaceIt placeit = new Reminder(mTitle, mDescription, mStartDate, mEndDate,
				mLocationOfPlaceIt, mKeyId);
		
		if(category == PlaceItUtils.CATEGORICAL_PLACEIT)
		{
			placeit = new Categorical(placeit);
			placeit.setmTypeOfPlaceIt(category);
			placeit.setmCategoryOne(mCategory1);
			placeit.setmCategoryTwo(mCategory2);
			placeit.setmCategoryThree(mCategory3);
		}
//		PlaceIt placeit = new RegularPlaceIt(mTitle, mDescription, mStartDate,
//				mEndDate, mLocationOfPlaceIt, mKeyId);
		placeit.setFrequencyOfRepeats(mFrequencyOfRepeats);
		placeit.setDistanceFromUser(mDistanceFromUser);
		placeit.setPosted(mPosted);
		placeit.setPulled(mPulled);
		placeit.setDeleted(mDeleted);
		placeit.setTimeTriggered(mTimeTriggered);
		
		Log.d("TEST","TESTING"+placeit.getTitle() + placeitProp[6]);
		return placeit;
	}
	
	
	public static String PlaceItToString (PlaceIt placeit) {
		String mKeyId = String.valueOf(placeit.getKeyId());// this value should never change
		Log.d(mKeyId,"KEYID");
		String mTitle = placeit.getTitle();
		String mDescription = placeit.getDescription();
		String mStartDate = placeit.getStartDate();
		String mEndDate = placeit.getEndDate();
		String mFrequencyOfRepeats = placeit.getFrequencyOfRepeats();
		String mLocationOfPlaceIt = String.valueOf(placeit.getLocationOfPlaceIt().latitude)+","+
				String.valueOf(placeit.getLocationOfPlaceIt().longitude);
		String mDistanceFromUser = String.valueOf(placeit.getDistanceFromUser());
		String mPosted = String.valueOf(placeit.getPosted());
		String mPulled = String.valueOf(placeit.getPulled());
		String mDeleted = String.valueOf(placeit.getDeleted());
		String mTimeTriggered = placeit.getTimeTriggered();
		String mCategory = ""+placeit.getmTypeOfPlaceIt();
		String mCategory1 = placeit.getmCategoryOne();
		String mCategory2 = placeit.getmCategoryTwo();
		String mCategory3 = placeit.getmCategoryThree();
		Log.d(mCategory1,"categorical");
		final String DE = "|||";
		
		String placeitString = 
				mKeyId + DE +
				mTitle + DE +
				mDescription + DE +
				mStartDate + DE +
				mEndDate + DE +
				mFrequencyOfRepeats + DE + 
				mLocationOfPlaceIt + DE +
				mDistanceFromUser + DE +
				mPosted + DE +
				mPulled + DE +
				mDeleted + DE +
				mTimeTriggered + DE +
				mCategory + DE +
				mCategory1 + DE +
				mCategory2 + DE +
				mCategory3;
				
		return placeitString;
	}

}
