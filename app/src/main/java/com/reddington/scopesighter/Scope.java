package com.reddington.scopesighter;
/**
 * Holds all pertinent information for the user's scope.
 * is saved or deleted in ScopeSighterApplication class.
 * @author Benjamin Reddington 2013
 */
public class Scope extends Savable {

	/**
	 * This is an Eclipse generated serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The distance the shot placement is affected per increment of 
	 * the physical windage or elevation scope controls
	 */
	private double offsetPerClick;
	/**
	 * The number of yards specified on the user's scope for calculations
	 */
	private double distanceForAdjust;
	private boolean clockwiseOffsetsLeft;
	private boolean clockwiseOffsetsUp;
	
	public Scope(){
		super();
		offsetPerClick = 0;
		distanceForAdjust = 0;
		clockwiseOffsetsLeft = false;
		clockwiseOffsetsUp = false;
	}
	
	public Scope(String name, double offsetPerClick, double distanceForAdjust, boolean clockwiseOffsetsLeft, boolean clockwiseOffsetsUp){
		super(name);
		this.offsetPerClick = offsetPerClick;
		this.distanceForAdjust = distanceForAdjust;
		this.clockwiseOffsetsLeft = clockwiseOffsetsLeft;
		this.clockwiseOffsetsUp = clockwiseOffsetsUp;
	}
	
	
	public void setOffsetPerClick(double offsetPerClick){   //sets distance per click value, used when changing units
		this.offsetPerClick = offsetPerClick;
	}
	
	public double getOffsetPerClick(){
		return offsetPerClick;
	}
	
	public void setDistanceForAdjust(double distanceForAdjust){    //sets distance for adjusting, used when changing units
		this.distanceForAdjust = distanceForAdjust;
	}
	
	public double getDistanceForAdjust(){
		return distanceForAdjust;
	}
	
	public boolean getClockwiseOffsetsLeft(){
		return clockwiseOffsetsLeft;
	}
	
	public boolean getClockwiseOffsetsUp(){
		return clockwiseOffsetsUp;
	}
	
	public boolean equals(Scope s){
		if (s.getName().equals(this.getName()) && s.offsetPerClick == offsetPerClick && s.distanceForAdjust == distanceForAdjust && s.clockwiseOffsetsLeft == clockwiseOffsetsLeft && s.clockwiseOffsetsUp == clockwiseOffsetsUp)
			return true;
		return false;
	}
}
