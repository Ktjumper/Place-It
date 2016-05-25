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
package reminder;

import com.google.android.gms.maps.model.LatLng;

/* Inherits from PlaceIt */
public abstract class PlaceItDecorator extends PlaceIt {
	public abstract String getTitle();
	public abstract void setTitle(String title);
	
	public abstract String getDescription();
	public abstract void setDescription(String description);
	
	public abstract String getTimeTriggered();
	public abstract void setTimeTriggered(String timeTriggered);
	
	public abstract long getKeyId();
	public abstract void setKeyId(long keyId);
	
	public abstract String getStartDate();
	public abstract void setStartDate(String startDate);
	
	public abstract String getEndDate();
	public abstract void setEndDate(String endDate);
	
	public abstract String getFrequencyOfRepeats();
	public abstract void setFrequencyOfRepeats(String frequencyOfRepeats);
	
	public abstract LatLng getLocationOfPlaceIt();
	public abstract void setLocationOfPlaceIt(LatLng locationOfPlaceIt);
	
	public abstract Integer getDistanceFromUser();
	public abstract void setDistanceFromUser(Integer distanceFromUser);
	
	public abstract int getPosted();
	public abstract void setPosted(int posted);
	
	public abstract int getPulled();
	public abstract void setPulled(int pulled);
	
	public abstract int getDeleted();
	public abstract void setDeleted(int deleted);
	
	public abstract int getmTypeOfPlaceIt();
	public abstract void setmTypeOfPlaceIt(Integer mCategoryThree);
	
	public abstract String getmCategoryOne();
	public abstract void setmCategoryOne(String mCategoryOne);
	
	public abstract String getmCategoryTwo();
	public abstract void setmCategoryTwo(String mCategoryTwo);
	
	public abstract String getmCategoryThree();
	public abstract void setmCategoryThree(String mCategoryThree);
}
