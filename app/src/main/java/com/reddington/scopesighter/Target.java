package com.reddington.scopesighter;

import java.util.ArrayList;
/**
 * Represents the physical target the user has fired at.
 * Contains a list of hits added by the user in 
 * SightingSessionActivity.
 * @author Benjamin Reddington 2013
 */
public class Target {

	private String name;
	private ArrayList<Hit> hits = new ArrayList<Hit>();
	private float centerX;
	private float centerY;
	private float pixelDiameter;

	public Target() {

	}
	public Target(String n) {
		name = n;
	}
	public void setName(String n) {
		name = n;
	}
	public String getName() {
		return name;
	}
	public void addHit(Hit h) {
		hits.add(h);
	}
	public ArrayList<Hit> getHits() {
		return hits;
	}	
	public float getCenterX(){
		return centerX;
	}
	public void setCenterX(float x){
		centerX = x;
	}
	public float getCenterY(){
		return centerY;
	}
	public void setCenterY(float y){
		centerY = y;
	}
	
	public float getPixelDiameter(){
		return pixelDiameter;
	}
	public void setPixelDiameter(float pd){
		pixelDiameter = pd;
	}
}