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

import com.google.api.client.util.Key;
 
/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another activity
* */
public class Place implements Serializable {
 
	private static final long serialVersionUID = 1L;

	@Key
    public String id;
     
    @Key
    public String name;
     
    @Key
    public String reference;
     
    @Key
    public String icon;
     
    @Key
    public String vicinity;
     
    @Key
    public Geometry geometry;
     
    @Key
    public String formatted_address;
     
    @Key
    public String formatted_phone_number;
 
    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }
     
    public static class Geometry implements Serializable
    {
        /**
		 * To avoid Eclipse from throwing a warning
		 */
		private static final long serialVersionUID = 1L;
		@Key
        public Location location;
    }
     
    public static class Location implements Serializable
    {
        /**
		 * To avoid Eclipse from throwing a warning
		 */
		private static final long serialVersionUID = 1L;

		@Key
        public double lat;
         
        @Key
        public double lng;
    }

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the vicinity
	 */
	public String getVicinity() {
		return vicinity;
	}

	/**
	 * @param vicinity the vicinity to set
	 */
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	/**
	 * @return the geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * @return the formatted_address
	 */
	public String getFormatted_address() {
		return formatted_address;
	}

	/**
	 * @param formatted_address the formatted_address to set
	 */
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	/**
	 * @return the formatted_phone_number
	 */
	public String getFormatted_phone_number() {
		return formatted_phone_number;
	}

	/**
	 * @param formatted_phone_number the formatted_phone_number to set
	 */
	public void setFormatted_phone_number(String formatted_phone_number) {
		this.formatted_phone_number = formatted_phone_number;
	}
     
}
