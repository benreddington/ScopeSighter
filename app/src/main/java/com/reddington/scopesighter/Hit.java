package com.reddington.scopesighter;

/** 
 * Hit stores two float values and provides
 * accessors for each. Hits are 
 * coordinates of user-indicated shot placements.
 * @author Benjamin Reddington 2013
 */
public class Hit {

	private float x;
	private float y;

	public Hit(float x, float y) {
		this.x = x;
		this.y = y;

	}

	public float getX() {
		return x;

	}

	public float getY() {
		return y;

	}

}
