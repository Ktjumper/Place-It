package com.example.place_its.test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.robotium.solo.Solo;

import place_its.MainActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

public class CategoricalTest  extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	
	public CategoricalTest() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testButton() throws InterruptedException{
		solo.clickOnButton("Create Reminder");
		solo.clickOnButton("Create Regular");
		solo.assertCurrentActivity("MapActivity", "MapActivity");
	}
	
	public void testCategorical() throws InterruptedException{
		solo.clickOnButton("Create Reminder");
		solo.clickOnButton("Create Categorical");
		EditText mTitle = solo.getEditText("Title");
		EditText mDesc = solo.getEditText("Description");
		solo.clickOnView(mTitle);
		solo.clearEditText(mTitle);
		String strInput = "Victor Charlie"; //Title
		solo.typeText(mTitle, strInput);
		boolean actual = solo.searchEditText(strInput);
		assertEquals("Title does not match entered text",true, actual);
		strInput = "Koo"; //Desc
		solo.clickOnView(mDesc);
		solo.clearEditText(mDesc);
		solo.enterText(mDesc, strInput);
		actual = solo.searchEditText(strInput);
		assertEquals("Desc does not match entered text",true, actual);
		solo.assertCurrentActivity("Placeit not loaded","CreateCategoricalPlaceIt");
		
		assertTrue("Victor Charlie".equals(mTitle.getText().toString()));
		assertTrue("Koo".equals(mDesc.getText().toString()));
		
//		final Calendar c = Calendar.getInstance();
//		String date = (c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR);
//		int min = c.get(Calendar.MINUTE);
//		String minute = min+"";
//		if(min<10){
//			minute="0"+minute;
//		}
//		String time = "";//c.get(Calendar.HOUR)+":"+minute+" ";
//		if(c.get(Calendar.AM_PM)==Calendar.AM){
//			time+="AM";
//		}else{
//			time+="PM";
//		}
//		// StartDateField
//		solo.clickOnView((EditText) solo.getCurrentActivity().findViewById(0x7f050015));
//		solo.clickOnButton("Done");
//		//Leave our first day alone
//		//Leave our first hour alone
//		
//		//solo.clickOnText(composite,2);
//		// StartTimeField
//		solo.clickOnView((EditText) solo.getCurrentActivity().findViewById(0x7f050016));
//		/*Determine settings for second day*/
//	    c.add(Calendar.DAY_OF_YEAR, 4);
//	    c.add(Calendar.HOUR, 3);
//	    c.add(Calendar.MINUTE, 33);
//	    solo.setDatePicker(0, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//	    solo.clickOnButton("Done");
//	    
//	    //EndDateField
//		solo.clickOnView((EditText) solo.getCurrentActivity().findViewById(0x7f050018));
//		solo.setTimePicker(0, c.get(Calendar.HOUR), ((c.get(Calendar.MINUTE)-28))%60);
//		solo.clickOnButton("Done");
//		//EndDateField
//		solo.clickOnView((EditText) solo.getCurrentActivity().findViewById(0x7f050019));
//		solo.setTimePicker(0, c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
//		/*Determine settings for second hour*/
//		solo.clickOnButton("Done");
//		
//		date = (c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR);
//		min = c.get(Calendar.MINUTE);
//		minute = min+"";
//		if(min<10){
//			minute="0"+minute;
//		}
//		int hour = c.get(Calendar.HOUR);
//		if(hour==0){
//			hour=12;
//		}
//		time = hour+":"+minute+" ";
//		if(c.get(Calendar.AM_PM)==Calendar.AM){
//			time+="AM";
//		}else{
//			time+="AM";
//		}
//		String timeField = ((EditText) solo.getCurrentActivity().findViewById(0x7f050011)).getText().toString();
//		String dateField = ((EditText) solo.getCurrentActivity().findViewById(0x7f050010)).getText().toString();
//		Log.d("Allen","Allen "+time+" "+timeField);
//		Log.d("Allen","Allen "+date+" "+dateField);
//		assertTrue(solo.searchEditText(time));
//		assertTrue(solo.searchEditText(date));
//		
//		solo.hideSoftKeyboard();
		
		//solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		/*public static final int typeSpinnerOne=0x7f050012;
        public static final int typeSpinnerThree=0x7f050014;
        public static final int typeSpinnerTwo=0x7f050013;*/
		solo.pressSpinnerItem(0,1);
		solo.pressSpinnerItem(1,4);
		solo.pressSpinnerItem(2,7);
		solo.pressSpinnerItem(3,10);
		solo.clickOnButton("Confirm");
		
		TimeUnit.MILLISECONDS.sleep(3000);
		
		solo.goBack();
		
		solo.clickOnButton("View Reminders");
		solo.clickOnText("Victor Charlie");
		solo.clickOnButton("Delete");
		assert(!solo.searchText("Victor Charlie"));
	}
	
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
