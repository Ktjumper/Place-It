/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 * Description: An activity for the List of Posted, Pulled-Down and Deleted.
 *
 * */

package place_its;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.place_its.R;

public class ViewListsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private int thisTabNum;

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	public static final String VIEWLISTS = "place_its.ViewListsActivity";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_lists);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText(R.string.posted)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.pulled_down)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.deleted)
				.setTabListener(this));
		
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}


	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		createListFragments(tab.getPosition());
		thisTabNum = tab.getPosition();
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onResume() {
		super.onResume();

		createListFragments(thisTabNum);
	}

	private void createListFragments(int tabNumber) {
		if (tabNumber == 0) {
			PlaceitsListFragment postedList = new PlaceitsListFragment(
					PlaceItUtils.POSTED_ITEM_VIEW);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, postedList).commit();
		} else if (tabNumber == 1) {
			PlaceitsListFragment pulleddownList = new PlaceitsListFragment(
					PlaceItUtils.PULLED_DOWN_ITEM_VIEW);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, pulleddownList).commit();
		} else {
			PlaceitsListFragment deletedList = new PlaceitsListFragment(
					PlaceItUtils.DELETED_ITEM_VIEW);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, deletedList).commit();
		}
	}
}