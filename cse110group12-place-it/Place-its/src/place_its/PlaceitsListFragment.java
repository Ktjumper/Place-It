/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: This class is to be used when creating the Lists for the 
 * Posted, Pulled-down, and Deleted place-its. 
 */

package place_its;

import java.util.ArrayList;
import java.util.List;

import reminder.PlaceIt;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.place_its.R;
import com.google.android.gms.maps.model.LatLng;

@SuppressLint("ValidFragment")
public class PlaceitsListFragment extends ListFragment {

	/* Holds fields of a number of place its in their respective lists */
	private List<String> plTitles = new ArrayList<String>();
	private List<LatLng> plLatLng = new ArrayList<LatLng>();
	private List<String> plDescriptions = new ArrayList<String>();
	private List<String> plStart = new ArrayList<String>();
	private List<String> plEnd = new ArrayList<String>();
	private List<Long> plKeyId = new ArrayList<Long>();
	private List<String> plFrequency = new ArrayList<String>();

	public PlaceitsListFragment() {

	}

	public PlaceitsListFragment(int postedPlaceitsView) {
		populateList(postedPlaceitsView);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, plTitles);
		setListAdapter(listAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		/* go view placeit */
		Intent intent = new Intent(getActivity(), ViewPlaceitActivity.class);
		PlaceIt placeIt = PlaceItUtils.getInstance().getPlaceit(plKeyId.get(position));
		Log.d(placeIt.getKeyId()+" "+position,"KEYIDITEM");
		Log.d(placeIt.getTitle(),"KEYIDITEMNAME");
		intent.putExtra("title", placeIt.getTitle());
		intent.putExtra("latitude", placeIt.getLocationOfPlaceIt().latitude);
		intent.putExtra("longitude", placeIt.getLocationOfPlaceIt().longitude);
		intent.putExtra("description", placeIt.getDescription());
		intent.putExtra("start", placeIt.getStartDate());
		intent.putExtra("end", plEnd.get(position));
		intent.putExtra("keyId", placeIt.getKeyId());
		intent.putExtra("frequency", plFrequency.get(position));
		startActivity(intent);
	}

	private void populateList(int postedPlaceitsView) {
		PlaceItUtils plUtils = PlaceItUtils.getInstance();

		// populates list with placeit names
		ArrayList<PlaceIt> currPlaceits = plUtils.getList();
		Log.d("populateList", "Size: " + currPlaceits.size());
		for (PlaceIt pl : currPlaceits) {
			if (postedPlaceitsView == PlaceItUtils.POSTED_ITEM_VIEW
					&& pl.getPosted() == PlaceItUtils.PLACEIT_POSTED) {
				addItem(pl);
			} else if (postedPlaceitsView == PlaceItUtils.PULLED_DOWN_ITEM_VIEW
					&& pl.getPulled() == PlaceItUtils.ITEM_PULLED_DOWN) {
				addItem(pl);
			} else if (postedPlaceitsView == PlaceItUtils.DELETED_ITEM_VIEW
					&& pl.getDeleted() == PlaceItUtils.ITEM_DELETED) {
				addItem(pl);
			}
		}
	}
	// Add a single place it's data fields to its multiple respective lists
	private void addItem(PlaceIt item) {
		if (!plKeyId.contains(item.getKeyId())) {
			if (!plLatLng.contains(item.getLocationOfPlaceIt())) {
				plTitles.add(item.getTitle());
				plLatLng.add(item.getLocationOfPlaceIt());
				plDescriptions.add(item.getDescription());
				plStart.add(item.getStartDate());
				plEnd.add(item.getEndDate());
				plKeyId.add(item.getKeyId());
				plFrequency.add(item.getFrequencyOfRepeats());
			}
		}
	}

}
