/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: This is a holder class for the reminders to be used
 * in the Place-it app. 
 */
package reminder;

import com.google.android.gms.maps.model.LatLng;

public class Reminder extends PlaceIt {
	
	/*
	 * Default constructor for the Place-it
	 */
	public Reminder() {
		this("","","","",new LatLng(0,0),0);
	}

	/**
	 * @param mTitle
	 * @param mDescription
	 * @param mStartDate
	 * @param mEndDate
	 * @param mLocationOfPlaceIt
	 */
	public Reminder(String mTitle, String mDescription, String mStartDate,
			String mEndDate, LatLng mLocationOfPlaceIt, long keyId) {
		this.mTitle = mTitle;
		this.mDescription = mDescription;
		this.mStartDate = mStartDate;
		this.mEndDate = mEndDate;
		this.mLocationOfPlaceIt = mLocationOfPlaceIt;
		this.mKeyId = keyId;
	}

	/* Getters and setters */
	/**
	 * @return the title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.mTitle = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getTimeTriggered() {
		return mTimeTriggered;
	}

	public void setTimeTriggered(String timeTriggered) {
		this.mTimeTriggered = timeTriggered;
	}

	public long getKeyId() {
		return mKeyId;
	}

	public void setKeyId(long keyId) {
		this.mKeyId = keyId;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return mStartDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.mStartDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return mEndDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.mEndDate = endDate;
	}

	/**
	 * @return the frequencyOfRepeats
	 */
	public String getFrequencyOfRepeats() {
		return mFrequencyOfRepeats;
	}

	/**
	 * @param frequencyOfRepeats
	 *            the frequencyOfRepeats to set
	 */
	public void setFrequencyOfRepeats(String frequencyOfRepeats) {
		this.mFrequencyOfRepeats = frequencyOfRepeats;
	}

	/**
	 * @return the locationOfPlaceIt
	 */
	public LatLng getLocationOfPlaceIt() {
		return mLocationOfPlaceIt;
	}

	/**
	 * @param locationOfPlaceIt
	 *            the locationOfPlaceIt to set
	 */
	public void setLocationOfPlaceIt(LatLng locationOfPlaceIt) {
		this.mLocationOfPlaceIt = locationOfPlaceIt;
	}

	/**
	 * @return the distanceFromUser
	 */
	public Integer getDistanceFromUser() {
		return mDistanceFromUser;
	}

	/**
	 * @param distanceFromUser
	 *            the distanceFromUser to set
	 */
	public void setDistanceFromUser(Integer distanceFromUser) {
		this.mDistanceFromUser = distanceFromUser;
	}

	public int getPosted() {
		return mPosted;
	}

	public void setPosted(int posted) {
		this.mPosted = posted;
	}

	public int getPulled() {
		return mPulled;
	}

	public void setPulled(int pulled) {
		this.mPulled = pulled;
	}

	public int getDeleted() {
		return mDeleted;
	}

	public void setDeleted(int deleted) {
		this.mDeleted = deleted;
	}

	/**
	 * @return the mTypeOfPlaceIt
	 */
	public int getmTypeOfPlaceIt() {
		return this.mTypeOfPlaceIt;
	}

	/**
	 * @param mTypeOfPlaceIt the mTypeOfPlaceIt to set
	 */
	public void setmTypeOfPlaceIt(Integer mTypeOfPlaceIt) {
		this.mTypeOfPlaceIt = mTypeOfPlaceIt;
	}

	@Override
	public String getmCategoryOne() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setmCategoryOne(String mCategoryOne) {
		// TODO Auto-generated method stub
		/* Do nothing! */
	}

	@Override
	public String getmCategoryTwo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setmCategoryTwo(String mCategoryTwo) {
		// TODO Auto-generated method stub
		/* Do nothing! */
	}

	@Override
	public String getmCategoryThree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setmCategoryThree(String mCategoryThree) {
		// TODO Auto-generated method stub
		/* Do nothing! */
	}
}
