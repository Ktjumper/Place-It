/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: Deals with the main functions of the Map
 * and what happens when you place the place its
 */
package place_its;

import geocode.GeocodeJSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import reminder.PlaceIt;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.place_its.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnMyLocationButtonClickListener, OnMarkerClickListener,
		OnMarkerDragListener, OnInfoWindowClickListener, OnMapClickListener {

	// These settings are the same as the settings for the map. They will in
	// fact give you update at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private static final int CREATE_PLACE_IT_REQUEST = 0;

	private LocationClient mLocationClient;
	private boolean loaded = false;

	private GoogleMap mMap;
	private Marker mMarker = null;

	private Button mBtnFind;
	private EditText etPlace;

	private List<Marker> mSearchedMarkers = new ArrayList<Marker>();
	private ArrayList<PlaceIt> mPostedPlaceits = new ArrayList<PlaceIt>();

	LocationManager locationManager;

	PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (savedInstanceState == null) {
			mapFragment.setRetainInstance(true);
		} else {
			mMap = mapFragment.getMap();
		}
		setUpGeocoding();

		/*
		 * This has to make certain checks when the user opens the map for
		 * available place-its on the map within range.
		 */
		setUpMapIfNeeded();

		populateMapWithPlaceits();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		saveCameraSettings();
	}

	/* Obtain a map */
	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	@Override
	protected void onResume() {
		Log.d("MapActivity: onResume", "Went into onResume!");
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		QueryForCategories.getUniqueInstance().checkAgainstCategoricalPlaceIts();
		if (!mLocationClient.isConnected())
			mLocationClient.connect();

		populateMapWithPlaceits();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onDisconnected() {
		// Do nothing
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	@Override
	public void onPause() {
		super.onPause();
		/* location stay connection when paused */
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	/**
	 * This function ensures the map is not null. This function should only be
	 * called once.
	 */
	private void setUpMap() {
		/* push map stuff down to make space for searching address */
		mMap.setPadding(0, 110, 0, 0);

		mMap.setMyLocationEnabled(true);

		/* Listeners */
		mMap.setOnMyLocationButtonClickListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMapClickListener(this);
		mMap.setOnMarkerDragListener(this);
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "Current location set", Toast.LENGTH_SHORT).show();
		/* Took out this -> + mLocationClient.getLastLocation() */

		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (loaded == false) {
			loadCameraSettings(location);
			QueryForCategories.getUniqueInstance().checkAgainstCategoricalPlaceIts();
			loaded = true;
		}
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		/*
		 * only createplaceit if the marker is the one that the user just placed
		 * on the map
		 */
		if (marker.equals(mMarker)) {
			goCreatePlaceit(marker);
		} else if (mSearchedMarkers.contains(marker)) {
			/* marker is a searched marker */
			goCreatePlaceit(marker);
		} else {
			/* marker is existing placeit. View it */
			goViewPlaceit(marker);
		}
	}

	@Override
	public void onMapClick(LatLng position) {
		if (mMarker != null)
			mMarker.remove();

		mMarker = mMap.addMarker(new MarkerOptions().position(position)
				.title("New Place-it").snippet("Click here to create place-it")
				.draggable(true));
		mMarker.showInfoWindow();
	}

	@Override
	public void onMarkerDrag(Marker marker) {

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {

	}

	@Override
	public void onMarkerDragStart(Marker marker) {
	}

	private void goCreatePlaceit(Marker marker) {
		/* Go to CreateRegularPlaceit */
		Intent createPlaceItIntent = new Intent(getBaseContext(),
				CreateRegularPlaceit.class);
		Bundle bundle = new Bundle();
		bundle.putDouble(PlaceItUtils.PLACE_IT_LATITUDE,
				marker.getPosition().latitude);
		bundle.putDouble(PlaceItUtils.PLACE_IT_LONGITUDE,
				marker.getPosition().longitude);
		/* Log messages to ensure the right LATLNG is sent. */
		Log.d("goCreatePlaceitLat -> MapActivity", ""
				+ marker.getPosition().latitude);
		Log.d("goCreatePlaceitLng -> MapActivity", ""
				+ marker.getPosition().longitude);

		createPlaceItIntent.putExtra(PlaceItUtils.PLACE_IT_LOCATION, bundle);
		startActivityForResult(createPlaceItIntent, CREATE_PLACE_IT_REQUEST);
	}

	private void goViewPlaceit(Marker marker) {
		String title = null;
		double latitude = 0;
		double longitude = 0;
		String description = null;
		String start = null;
		String end = null;
		long keyId = 0;
		String frequency = null;

	
		
		Iterator<PlaceIt> iPostedPlaceits = mPostedPlaceits.iterator();
		Log.d("VIEWPLACEIT", "TESTINGMAP");
		while (iPostedPlaceits.hasNext()) {
			/* examine a placeit on map */
			PlaceIt iplaceit = iPostedPlaceits.next();
			title = iplaceit.getTitle();
			latitude = iplaceit.getLocationOfPlaceIt().latitude;
			longitude = iplaceit.getLocationOfPlaceIt().longitude;
			description = iplaceit.getDescription();
			start = iplaceit.getStartDate();
			end = iplaceit.getEndDate();
			keyId = iplaceit.getKeyId();
			frequency = iplaceit.getFrequencyOfRepeats();
			/* check if same placeit */
			
			
			Log.d("ITERATING " +title, "TESTINGMAP");
			Log.d("lat/long "+String.valueOf(latitude) +", "+ String.valueOf(longitude), "TESTINGMAP");
			Log.d("PLACEITLOCA " + iplaceit.getLocationOfPlaceIt().toString(), "TESTINGMAP");
			Log.d("MARKERTITLE "+marker.getPosition().toString(), "TESTINGMAP");
			Log.d("PLACEITTITLE "+iplaceit.getTitle() +"   MARKERTITLE " +marker.getTitle(), "TESTINGMAP");

			String placeitLat = String.valueOf(iplaceit.getLocationOfPlaceIt().latitude);
			String markerLat = String.valueOf(marker.getPosition().latitude);
			String placeitLong = String.valueOf(iplaceit.getLocationOfPlaceIt().longitude);
			String markerLong = String.valueOf(marker.getPosition().longitude);
			
			if(placeitLat.substring(0,10).equals(markerLat.substring(0,10)) &&
					placeitLong.substring(0,10).equals(markerLong.substring(0,10)))
			{
				Log.d("FOUND " + title, "TESTINGMAP");
				break;
			}
					
			
		}

		/*---go view placeit---*/
		Intent intent = new Intent(getBaseContext(), ViewPlaceitActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("description", description);
		intent.putExtra("start", start);
		intent.putExtra("end", end);
		intent.putExtra("keyId", keyId);
		intent.putExtra("frequency", frequency);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If the request went well (OK) and the request was
		if (resultCode == Activity.RESULT_OK
				&& requestCode == CREATE_PLACE_IT_REQUEST) {
			Toast.makeText(getBaseContext(), "Place-it added!",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getBaseContext(), "Place-it cancelled!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void populateMapWithPlaceits() {
		/*----clear map and redraw markers for placeits from database ----*/

		PlaceItUtils plUtils = PlaceItUtils.getInstance();
		/* Get list of placeits */
		ArrayList<PlaceIt> placeitsarray = plUtils.getRegularList();

		if(mMap != null)
			mMap.clear();

		/* Add marker for posted placeits in database */
		for (PlaceIt placeit : placeitsarray) {
			if (placeit.getPosted() == 1) {
				String placeittitle = placeit.getTitle();
				LatLng placeitLatLng = placeit.getLocationOfPlaceIt();
				mMap.addMarker(new MarkerOptions()
						.position(placeitLatLng)
						.title((placeittitle+"               ").substring(0, 14).trim())
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

				/* add posted placeit to list of posted placeits */
				mPostedPlaceits.add(placeit);
				addRangeDetect(placeit, Integer.valueOf(PlaceItUtils.RADIUS_OF_PLACEIT));
			}
		}
	}

	private void addRangeDetect(PlaceIt placeit, int range) {
		LatLng position = placeit.getLocationOfPlaceIt();
		
		/* add a circle at the location to show the range of detection */
		mMap.addCircle(new CircleOptions().center(position)
				.radius(range).strokeColor(Color.BLACK)
				.fillColor(0x30ff0000).strokeWidth(2));
		
		ProximityUtils proxy = ProximityUtils.getInstance();
		if (!proxy.hasBeenNotified("" + placeit.getKeyId())) {
			Intent proximityIntent = new Intent(getBaseContext(),
					ProximityActivity.class);
			Log.d(Long.toString(placeit.getKeyId()),
					"databaseCheck place it title");
			proximityIntent.putExtra(PlaceItUtils.KEY_ID, placeit.getKeyId());
			proximityIntent.putExtra(PlaceItUtils.PLACE_IT_IN_RANGE_TITLE, placeit.getTitle());
			pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
					proximityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

			locationManager.addProximityAlert(position.latitude,
					position.longitude, range, -1, pendingIntent);
		}
	}

	private void saveCameraSettings() {
		/* Get lat, long, and zoom level from cameraposition */
		CameraPosition mCameraPosition = mMap.getCameraPosition();
		double latitude = mCameraPosition.target.latitude;
		double longitude = mCameraPosition.target.longitude;
		float zoomLevel = mCameraPosition.zoom;

		/* save those settings */
		SharedPreferences settings = getSharedPreferences("CameraSettings", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("latitude", (float) latitude);
		editor.putFloat("longitude", (float) longitude);
		editor.putFloat("zoomlevel", zoomLevel);
		editor.commit();
	}

	private void loadCameraSettings(Location location) {
		/* use user's current location if there are no saved settings */
		double defaultLat = location.getLatitude();
		double defaultLong = location.getLongitude();

		/* retrieve lat, long, and zoom level settings */
		SharedPreferences settings = getSharedPreferences("CameraSettings", 0);
		double latitude = (double) settings.getFloat("latitude",
				(float) defaultLat);
		double longitude = (double) settings.getFloat("longitude",
				(float) defaultLong);
		float zoomLevel = settings.getFloat("zoomlevel", 17);

		/* make the LatLng for camera */
		LatLng loadedCameraLatLng = new LatLng(latitude, longitude);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(loadedCameraLatLng).zoom(zoomLevel).build();
		/* move the camera there */
		mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	private void setUpGeocoding() {
		etPlace = (EditText) findViewById(R.id.et_place);
		mBtnFind = (Button) findViewById(R.id.btn_show);
		mBtnFind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Getting the place entered
				String location = etPlace.getText().toString();
				if (location == null || location.equals("")) {
					Toast.makeText(getBaseContext(), "No address entered",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String url = "https://maps.googleapis.com/maps/api/geocode/json?";
				try {
					/*
					 * encoding special characters like space in the user input
					 * place
					 */
					location = URLEncoder.encode(location, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String address = "address=" + location;
				String sensor = "sensor=false";
				/* url , from where the geocoding data is fetched */
				url = url + address + "&" + sensor;
				/*
				 * Instantiating DownloadTask to get places from Google
				 * Geocoding service
				 */
				DownloadTask downloadTask = new DownloadTask();
				/* Start downloading the geocoding places */
				downloadTask.execute(url);
			}
		});
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);
			/* Creating an http connection to communicate with url */
			urlConnection = (HttpURLConnection) url.openConnection();
			/* Connecting to url */
			urlConnection.connect();
			/* Reading data from url */
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();
		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/** A class, to download Places from Geocoding webservice */
	private class DownloadTask extends AsyncTask<String, Integer, String> {
		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {
			// Instantiating ParserTask which parses the json data from
			// Geocoding webservice
			// in a non-ui thread
			ParserTask parserTask = new ParserTask();
			// Start parsing the places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}

	/** A class to parse the Geocoding Places in non-ui thread */
	class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {
		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {
			List<HashMap<String, String>> places = null;
			GeocodeJSONParser parser = new GeocodeJSONParser();
			try {
				jObject = new JSONObject(jsonData[0]);
				/** Getting the parsed data as a an ArrayList */
				places = parser.parse(jObject);
			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {
			for (int i = 0; i < list.size(); i++) {
				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);
				MarkerOptions markerOptions = new MarkerOptions();
				double lat = Double.parseDouble(hmPlace.get("lat"));
				double lng = Double.parseDouble(hmPlace.get("lng"));
				String name = hmPlace.get("formatted_address");
				LatLng latLng = new LatLng(lat, lng);
				markerOptions.position(latLng);
				markerOptions.title(name);
				markerOptions.snippet("Click here to create place-it");

				Marker searchedMarker = mMap.addMarker(markerOptions);
				mSearchedMarkers.add(searchedMarker);
				if (i == 0)
					mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			}
		}
	}

}
