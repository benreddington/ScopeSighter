package com.reddington.scopesighter;
/**
 * Stores the status of the user's choice of measurement system.
 * Only one of these objects per app exists. Is saved along with 
 * Scopes and Ranges in ssapp.savables.
 * @author Benjamin Reddington 2013
 */
public class UnitStatus extends Savable{
	
	/**
	 * added by Eclipse
	 */
	private static final long serialVersionUID = 5461954460263577064L;
	
	boolean isImperial;
	
	public UnitStatus(){
		super();
	}
	
	public UnitStatus(boolean isImperial){
		super();
		this.isImperial = isImperial;
	}
	
	public boolean getIsImperial(){
		return isImperial;
	}
	
	public void setUnitsImperial(){
		isImperial = true;
	}
	
	public void setUnitsMetric(){
		isImperial = false;
	}

}
