package com.reddington.scopesighter;

/**
 * Holds the result of Calculate() and is accessed
 * by SessionResultsActivity. Stored in the 
 * SightingSessionApplication. No mutators 
 * because each Result is used only once.
 * @author Benjamin Reddington 2013
 */
public class Result {

	private int windageClicks;
	private int elevationClicks;
	private String windageRotation;
	private	String elevationRotation;	
	
	public Result(){
	
	}
	
	public Result(int horizontalClicks, int verticalClicks, String horizontalRotationDirection, String verticalRotationDirection){		
		windageClicks = horizontalClicks;
		elevationClicks = verticalClicks;
		windageRotation = horizontalRotationDirection;
		elevationRotation = verticalRotationDirection;		
	}
	
	public int getWindageClicks() {
		return windageClicks;
	}

	public int getElevationClicks() {
		return elevationClicks;
	}

	public String getWindageRotationDirection() {
		return windageRotation;
	}

	public String getElevationRotationDirection() {
		return elevationRotation;
	}
}
