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

import login.Sync;
import reminder.Categorical;
import reminder.PlaceIt;
import reminder.Reminder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class DatabaseHandler extends SQLiteOpenHelper {

	/* column/labels for database */
	private static final int version = 1;// database version
	private static final String dbName = "Place-its";// database name
	private static final String KEY_ID = "id";
	private static final String CATEGORY = "category";
	private static final String CATEGORY1 = "category1";
	private static final String CATEGORY2 = "category2";
	private static final String CATEGORY3 = "category3";
	private static final String TITLE = "Title";
	private static final String DESCRIPTION = "Description";
	private static final String LATITUDE = "Latitude";
	private static final String LONGITUDE = "Longitude";
	private static final String START_TIME = "Start";
	private static final String END_TIME = "End";
	private static final String FREQUENCY = "Frequency";// how often it repeats
	private static final String DISTANCE = "Distance";
	private static final String POSTED = "Posted";
	private static final String PULLED = "Pulled";
	private static final String DELETED = "Deleted";
	private static final String TIMETRIGGERED = "TimeTriggered";

	/* fields used for adding to database */
	private static long keyId;
	private static int category = 0;
	private static String category1 = "";
	private static String category2 = "";
	private static String category3 = "";
	private static String title = "";
	private static String description = "";
	private static double latitude = 0;
	private static double longitude = 0;
	private static String start = "";
	private static String end = "";
	private static String frequency = "";
	private static int distance = 0;
	private static int posted = 0;
	private static int pulled = 0;
	private static int deleted = 0;
	private static String timeTriggered = "";

	// Contacts table name
	private static final String TABLE_NAME = "PLACEITS_TABLE";

	// table column names

	public DatabaseHandler(Context context) {
		super(context, dbName, null, version);
	}

	/*
	 * creates a string to execute an SQL command CREATE TABLE is main command
	 * TABLE_NAME is the name of the table After the "(", the KEY_ID is used as
	 * the unique identifier for each row all the fields are then stored as text
	 * or integers in the database db.execSQL runs the string as an SQL command
	 */
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + TITLE + " TEXT," + DESCRIPTION
				+ " TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT,"
				+ START_TIME + " TEXT," + END_TIME + " TEXT," + FREQUENCY
				+ " TEXT," + DISTANCE + " INTEGER," + POSTED + " INTEGER,"
				+ PULLED + " INTEGER," + DELETED + " INTEGER," + TIMETRIGGERED
				+ " TEXT," + CATEGORY + " INTEGER," + CATEGORY1 + " TEXT," 
				+ CATEGORY2 + " TEXT," + CATEGORY3 + " TEXT)";
				
		db.execSQL(CREATE_TABLE);
	}

	/*
	 * checks if a table exists, if it does, it drops the table and creates a
	 * new one if necessary
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}
	
	public void deleteDatabase()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteSQL = "DROP table IF EXISTS " + TABLE_NAME;

		db.execSQL(deleteSQL);
		onCreate(db);
		PlaceItUtils pl = PlaceItUtils.getInstance();
		pl.clearPlaceItsList();
		
		Log.d("ASD", "TEST123");
		db.close();
	}

	/*
	 * sets the local variables that hold the fields of a place it that is sent
	 * in so that they can be used to send into the database
	 */
	public void setValues(PlaceIt placeIt) {
		title = placeIt.getTitle();
		description = placeIt.getDescription();
		latitude = placeIt.getLocationOfPlaceIt().latitude;
		longitude = placeIt.getLocationOfPlaceIt().longitude;
		start = placeIt.getStartDate();
		end = placeIt.getEndDate();
		frequency = placeIt.getFrequencyOfRepeats();
		distance = placeIt.getDistanceFromUser();
		posted = placeIt.getPosted();
		pulled = placeIt.getPulled();
		deleted = placeIt.getDeleted();
		timeTriggered = placeIt.getTimeTriggered();
		category = placeIt.getmTypeOfPlaceIt();
		category1 = placeIt.getmCategoryOne();
		category2 = placeIt.getmCategoryTwo();
		category3 = placeIt.getmCategoryThree();
	}

	/*
	 * extracts the fields from a placeit parameter by calling setValues. The
	 * method then creates a contentvalues and pushes the values into the
	 * content values which is then inserted into the database
	 */
	public void addToDatabase(PlaceIt placeIt) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		setValues(placeIt);
		Log.d(placeIt.toString(),"add to database");
		/* add parameters to the database */
		values.put(TITLE, title);
		values.put(DESCRIPTION, description);
		values.put(LATITUDE, latitude);
		values.put(LONGITUDE, longitude);
		values.put(START_TIME, start);
		values.put(END_TIME, end);
		values.put(FREQUENCY, frequency);
		values.put(DISTANCE, distance);
		values.put(POSTED, posted);
		values.put(PULLED, pulled);
		values.put(DELETED, deleted);
		values.put(TIMETRIGGERED, timeTriggered);
		values.put(CATEGORY, category);
		values.put(CATEGORY1, category1);
		values.put(CATEGORY2, category2);
		values.put(CATEGORY3, category3);
		// Inserting Row
		PlaceItUtils.getInstance().addPlaceItsToList(placeIt); /* THATS BAD */
		keyId = db.insert(TABLE_NAME, null, values);// sets the key id to be
		Log.d(""+keyId,"WTF IS THE KEYID");
		Log.d(category1,"category");
		placeIt.setKeyId(keyId);
		db.close();
	}

	/* takes in the key id value and sets the deleted flag to 1 for true */
	public void setDeleted(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(POSTED, 0);
		values.put(PULLED, 0);
		values.put(DELETED, 1);
		db.update(TABLE_NAME, values, KEY_ID + " = " + id, null);
		db.close();		
		
		/* update changes to datastore */
		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt pl = plUtils.getPlaceit(id);
		pl.setPosted(0);
		pl.setPulled(0);
		pl.setDeleted(1);
		Sync.DatabaseToDatastore(plUtils.getList());
	}

	public void setPulled(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(POSTED, 0);
		values.put(PULLED, 1);
		values.put(DELETED, 0);
		db.update(TABLE_NAME, values, KEY_ID + " = " + id, null);
		Log.d(Long.toString(id), "databaseCheckPULLEDID");
		db.close();		
		
		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt pl = plUtils.getPlaceit(id);
		pl.setPosted(0);
		pl.setPulled(1);
		pl.setDeleted(0);
		Sync.DatabaseToDatastore(plUtils.getList());
	}

	public void setPosted(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(POSTED, 1);
		values.put(PULLED, 0);
		values.put(DELETED, 0);
		db.update(TABLE_NAME, values, KEY_ID + " = " + id, null);
		db.close();
		
		/* update changes to datastore */
		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt pl = plUtils.getPlaceit(id);
		pl.setPosted(1);
		pl.setPulled(0);
		pl.setDeleted(0);
		Sync.DatabaseToDatastore(plUtils.getList());
	}

	public void setTime(long id, String start, String end) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(START_TIME, start);
		values.put(END_TIME, end);
		db.update(TABLE_NAME, values, KEY_ID + " = " + id, null);
		db.close();
		
		/* update changes to datastore */
		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		Sync.DatabaseToDatastore(plUtils.getList());
	}

	public void setTimeTriggered(long id, String timeTriggered) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TIMETRIGGERED, timeTriggered);
		db.update(TABLE_NAME, values, KEY_ID + " = " + id, null);
		db.close();		
		
		/* update changes to datastore */
		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		PlaceIt pl = plUtils.getPlaceit(id);
		pl.setTimeTriggered(timeTriggered);
		Sync.DatabaseToDatastore(plUtils.getList());
	}

	/*
	 * gets place it from the database. sets all the fields of the place it from
	 * the values extracted from the database
	 */
	public PlaceIt getPlaceItFromDatabase(Cursor cursor) {

		/*placeIt.setmTypeOfPlaceIt(cursor.getInt(cursor.getColumnIndex("category")));
		placeIt.setmCategoryOne(cursor.getString(cursor.getColumnIndex("category1")));
		placeIt.setmCategoryTwo(cursor.getString(cursor.getColumnIndex("category2")));
		placeIt.setmCategoryThree(cursor.getString(cursor.getColumnIndex("category3")));
		*/
		int check = cursor.getInt(cursor.getColumnIndex("category"));
		PlaceIt placeIt = new Reminder();
		if(check == PlaceItUtils.CATEGORICAL_PLACEIT)
		{
			placeIt = new Categorical(placeIt);
			placeIt.setmCategoryOne(cursor.getString(cursor.getColumnIndex("category1")));
			placeIt.setmCategoryTwo(cursor.getString(cursor.getColumnIndex("category2")));
			placeIt.setmCategoryThree(cursor.getString(cursor.getColumnIndex("category3")));
		}
		
		
		placeIt.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
		placeIt.setDescription(cursor.getString(cursor
				.getColumnIndex("Description")));
		placeIt.setStartDate(cursor.getString(cursor.getColumnIndex("Start")));
		placeIt.setEndDate(cursor.getString(cursor.getColumnIndex("End")));
		double latitude = Double.valueOf(cursor.getDouble(cursor
				.getColumnIndex("Latitude")));
		double longitude = Double.valueOf(cursor.getDouble(cursor
				.getColumnIndex("Longitude")));
		placeIt.setLocationOfPlaceIt(new LatLng(latitude, longitude));
		placeIt.setFrequencyOfRepeats(cursor.getString(cursor
				.getColumnIndex("Frequency")));
		placeIt.setPosted(Integer.valueOf(cursor.getString(cursor
				.getColumnIndex(POSTED))));
		placeIt.setKeyId(Long.parseLong(cursor.getString(0)));
		placeIt.setDeleted(cursor.getInt(cursor.getColumnIndex("Deleted")));
		placeIt.setPulled(cursor.getInt(cursor.getColumnIndex("Pulled")));
		placeIt.setTimeTriggered(cursor.getString(cursor
				.getColumnIndex("TimeTriggered")));
		Log.d(placeIt.toString(),"ddatabasePlaceItTotal");
		Log.d(placeIt.getTitle(), "ddatabaseCheckTitle");
		Log.d(Long.toString(placeIt.getKeyId()), "ddatabaseCheckKeyID");
		Log.d(Integer.toString(placeIt.getPosted()), "ddatabaseCheckPosted");
		Log.d(Integer.toString(placeIt.getPulled()), "ddatabaseCheckPulled");
		Log.d(Integer.toString(placeIt.getDeleted()), "ddatabaseCheckDeleted");
		Log.d(placeIt.getStartDate(), "ddatabaseCheckTime");
		Log.d(placeIt.getTimeTriggered(), "ddatabaseCheckTimeTriggered");
		Log.d(placeIt.getFrequencyOfRepeats(),
				"ddatabaseCheckFrequencyOfRepeats");
		Log.d(Integer.toString(placeIt.getmTypeOfPlaceIt()),"ddatabaseTypeOfPlaceit");
		Log.d(placeIt.getmCategoryOne(),"ddatabaseCategory1");
		Log.d(placeIt.getmCategoryTwo(),"ddatabaseCategory2");
		Log.d(placeIt.getmCategoryThree(),"ddatabaseCategory3");
		return placeIt;
	}
		
	
}


