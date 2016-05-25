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

import place_its.PlaceItUtils;

import com.google.android.gms.maps.model.LatLng;

/* 
 * Categorical place it decorator that can decorate a Reminder 
 */
public class Categorical extends PlaceItDecorator {
	PlaceIt placeIt;

	/* Store the three possible categories that the user has specified */
	String mFirstCategory = "";
	String mSecondCategory = "";
	String mThirdCategory = "";

	public Categorical(PlaceIt placeIt) {
		this.placeIt = placeIt;
		placeIt.mTypeOfPlaceIt = PlaceItUtils.CATEGORICAL_PLACEIT;
	}
	
	@Override
	public String getTitle() {
		return placeIt.getTitle();
	}

	@Override
	public void setTitle(String title) {
		placeIt.setTitle(title);
	}

	@Override
	public String getDescription() {
		return placeIt.getDescription();
	}

	@Override
	public void setDescription(String description) {
		placeIt.setDescription(description);
	}

	@Override
	public String getTimeTriggered() {
		return placeIt.getTimeTriggered();
	}

	@Override
	public void setTimeTriggered(String timeTriggered) {
		placeIt.setTimeTriggered(timeTriggered);
	}

	@Override
	public long getKeyId() {
		return placeIt.getKeyId();
	}

	@Override
	public void setKeyId(long keyId) {
		placeIt.setKeyId(keyId);
	}

	@Override
	public String getStartDate() {
		return placeIt.getStartDate();
	}

	@Override
	public void setStartDate(String startDate) {
		placeIt.setStartDate(startDate);
	}

	@Override
	public String getEndDate() {
		return placeIt.getEndDate();
	}

	@Override
	public void setEndDate(String endDate) {
		placeIt.setEndDate(endDate);
	}

	@Override
	public String getFrequencyOfRepeats() {
		return placeIt.getFrequencyOfRepeats();
	}

	@Override
	public void setFrequencyOfRepeats(String frequencyOfRepeats) {
		placeIt.setFrequencyOfRepeats(frequencyOfRepeats);
	}

	@Override
	public LatLng getLocationOfPlaceIt() {
		return placeIt.getLocationOfPlaceIt();
	}

	@Override
	public void setLocationOfPlaceIt(LatLng locationOfPlaceIt) {
		placeIt.setLocationOfPlaceIt(locationOfPlaceIt);
	}

	@Override
	public Integer getDistanceFromUser() {
		return placeIt.getDistanceFromUser();
	}

	@Override
	public void setDistanceFromUser(Integer distanceFromUser) {
		placeIt.setDistanceFromUser(distanceFromUser);
	}

	@Override
	public int getPosted() {
		return placeIt.getPosted();
	}

	@Override
	public void setPosted(int posted) {
		placeIt.setPosted(posted);
	}

	@Override
	public int getPulled() {
		return placeIt.getPulled();
	}

	@Override
	public void setPulled(int pulled) {
		placeIt.setPulled(pulled);
	}

	@Override
	public int getDeleted() {
		return placeIt.getDeleted();
	}

	@Override
	public void setDeleted(int deleted) {
		placeIt.setDeleted(deleted);
	}

	@Override
	public int getmTypeOfPlaceIt() {
		return placeIt.getmTypeOfPlaceIt();
	}

	@Override
	public void setmTypeOfPlaceIt(Integer mTypeOfPlaceIt) {
		placeIt.setmTypeOfPlaceIt(mTypeOfPlaceIt);
	}

	/*
	 * Only methods that differ from Reminder.java
	 * (non-Javadoc)
	 * @see reminder.PlaceItDecorator#getmCategoryOne()
	 */
	@Override
	public String getmCategoryOne() {
		return mFirstCategory;
	}

	@Override
	public void setmCategoryOne(String mCategoryOne) {
		this.mFirstCategory = mCategoryOne;
	}

	@Override
	public String getmCategoryTwo() {
		return mSecondCategory;
	}

	@Override
	public void setmCategoryTwo(String mCategoryTwo) {
		this.mSecondCategory = mCategoryTwo;
	}

	@Override
	public void setmCategoryThree(String mCategoryThree) {
		this.mThirdCategory = mCategoryThree;
	}

	@Override
	public String getmCategoryThree() {
		return mThirdCategory;
	}
}
