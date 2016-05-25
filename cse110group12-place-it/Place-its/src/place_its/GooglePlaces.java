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

import java.io.IOException;

import org.apache.http.client.HttpResponseException;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

public class GooglePlaces {

	/* Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/* Google API Key */
	private static final String API_KEY = "AIzaSyCiGCrrQEXAyuAutHbWDwQ7o9Evm8JFwI8";
	
	/*
	 * Google Places search url's
	 * Example:
	 * https://maps.googleapis.com/maps/api/place/nearbysearch/json?location
	 * =-33.8670522
	 * ,151.1957362&radius=500&types=food&name=harbour&sensor=false&key
	 * =AddYourOwnKeyHere
	 */
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

	private double mLatitude;
	private double mLongitude;
	private double mRadius;
	private PlaceList mPlaceList;

	/**
	 * @return the mPlaceList
	 */
	public synchronized PlaceList getmPlaceList() {
		return mPlaceList;
	}

	/**
	 * @param mPlaceList
	 *            the mPlaceList to set
	 */
	public synchronized void setmPlaceList(PlaceList mPlaceList) {
		this.mPlaceList = mPlaceList;
	}

	@SuppressWarnings("unused")
	private GooglePlacesRequest mAuthTask;

	/**
	 * Searching places
	 * 
	 * @param latitude
	 *            - latitude of place
	 * @params longitude - longitude of place
	 * @param radius
	 *            - radius of searchable area
	 * @param types
	 *            - type of place to search
	 * @return list of places
	 * */
	public PlaceList search(double latitude, double longitude, double radius,
			String types) throws Exception {

		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.mRadius = radius;
		try {
			Log.d("lat: " + latitude + "Lon: " + longitude, "Google Places");
			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("location", mLatitude + "," + mLongitude);
			request.getUrl().put("radius", mRadius); // in meters
			request.getUrl().put("sensor", "false");
			if (types != null) {
				request.getUrl().put("types", types);
			}

			Log.d(request.getUrl().toString(), "This is the URL");

			/*mAuthTask = new GooglePlacesRequest();
			mAuthTask.execute(request);
			
			Log.d("List: " + mPlaceList, "Google Places 4");
			return mPlaceList;*/
			
			mPlaceList = request.execute().parseAs(PlaceList.class);
			return mPlaceList;
		} catch (HttpResponseException e) {
			Log.e("Error:", e.getMessage());
			return null;
		}

	}

	/**
	 * Getting the location that was used for this query.
	 * 
	 * @param latitude
	 *            - latitude of place
	 * @params longitude - longitude of place
	 * @param radius
	 *            - radius of searchable area
	 * @param types
	 *            - type of place to search
	 * @return list of places
	 * */
	public LatLng getLocation() {
		return new LatLng(mLatitude, mLongitude);
	}

	/**
	 * Creating HTTP request Factory
	 * */
	public static HttpRequestFactory createRequestFactory(
			final HttpTransport transport) {
		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				HttpHeaders headers = new HttpHeaders();
				headers.setUserAgent("Place-its");
				request.setHeaders(headers);
				JsonObjectParser parser = new JsonObjectParser(
						new JacksonFactory());
				request.setParser(parser);
			}
		});
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class GooglePlacesRequest extends
			AsyncTask<HttpRequest, Void, Boolean> {
		PlaceList pl;

		@Override
		protected Boolean doInBackground(HttpRequest... params) {
			try {
				Log.d("Executing GooglePlacesRequest with URL ", "GPR: "
						+ params[0].getUrl().toURL());
				// Execute request for Google Places
				pl = (params[0]).execute().parseAs(PlaceList.class);
				if (pl.results != null && pl.results.size() > 0) {
					Log.d("The result of pl is: " + pl.results
							+ " with status " + pl.status, "GPR after: ");
					setmPlaceList(pl);
					Log.d("The pl is now: " + getmPlaceList(), "in Google Places" );
				}
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				Log.e("Error:", ioe.getMessage());
			} catch (Exception e) {
				Log.e("Error:", e.getMessage());
			}
			return true;
		}

		// After the request is completed, set the place it list 
		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				setmPlaceList(pl);
				Log.d("onPostEx in Time: " + System.currentTimeMillis(), "QueryForC...");
				Log.d("onPostExecute" + mPlaceList.status + " " + pl.status
						+ " list " + mPlaceList.results + " " + pl.results.size(),
						"AsyncTask Google Places");
				
			}
			mAuthTask = null;
		}
	}

}