package com.reddington.scopesighter;

import java.io.Serializable;
/**
 * This is the parent of Range, Scope, and UnitStatus which
 * holds the name field and implements Serializable
 * for saving locally on the device. Also controls
 * whether or not the child object is "active" for 
 * use by the user and for calculations.
 * @author Benjamin Reddington 2013 *
 */
public class Savable implements Serializable {

	/**
	 * This is an Eclipse generated serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";	//Both Scopes and Ranges have names
	private boolean active = false;
	
	public Savable(){
		super();
	}
	
	public Savable(String n){
		super();
		name = n;
	}
	
	public String getName() {
		return name;
	}
/**
 * Sets the child object as "active" meaning that it's been
 * selected by the user for sighting calculations.
 * @param isActive
 */
	public void setActiveStatus(boolean isActive){
		if(isActive)
			active = true;
		else
			active = false;
	}	
	
	public boolean getActiveStatus(){
		return active;
	}	
	
}
