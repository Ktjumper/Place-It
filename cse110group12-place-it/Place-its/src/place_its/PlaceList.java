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

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

/**
 * Implement this class from "Serializable" So that you can pass this class
 * Object to another using Intents Otherwise you can't pass to another actitivy
 * */
public class PlaceList implements Serializable {

	/**
	 * To avoid Eclipse from complaining
	 */
	private static final long serialVersionUID = 1L;

	@Key
	public String status;

	@Key
	public List<Place> results;

}