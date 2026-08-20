package com.reddington.scopesighter;

/**
 * Range holds the qualities of the shooting environments
 * that the user will be sighting in. Ranges are saved
 * to internal storage or deleted by ScopeSighterApplication.
 * Mutators are used when units are changed from imperial
 * to metric and back.
 * @author Benjamin Reddington 2013 *
 */
public class Range extends Savable {

	/**
	 * This is an Eclipse generated serialVersionUID.
	 */
	private static final long serialVersionUID = -5369995678678522030L;
	private float distanceToTarget;
	private float targetDiameter;
	
	public Range(){
		super();
		distanceToTarget = 0;
		targetDiameter = 0;
	}
	
	public Range(float distanceToTarget, float targetDiameter, String name){
		super(name);
		this.distanceToTarget = distanceToTarget;
		this.targetDiameter = targetDiameter;
	}

	public float getDistanceToTarget() {
		return distanceToTarget;
	}
	
	public void setDistanceToTarget(float distanceToTarget) { 
		this.distanceToTarget = distanceToTarget;         			
	}
	
	public float getTargetDiameter() {
		return targetDiameter;
	}
	
	public void setTargetDiameter(float targetDiameter) {
		this.targetDiameter = targetDiameter;         
	}
	/**
	 * .equals implementation for Range. Compares all values and
	 * returns true or false
	 * @param r
	 * @return Whether or not the objects are the same object for the purpose of the app
	 */
	public boolean equals(Range r){		
		if ((r.getName().equals(this.getName())) && (r.getDistanceToTarget() == distanceToTarget) && (r.getTargetDiameter() == targetDiameter))
			return true;
		return false;		
	}
}
