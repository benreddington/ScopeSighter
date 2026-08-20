package com.reddington.scopesighter;

import java.util.ArrayList;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.content.Context;
/**
 * This is a child of View that displays the virtual
 * target for the user to interact with. Hits are registered
 * and when the user taps on this view.
 * @author Benjamin Reddington 2013
 */
public class TargetView extends View{

	private float x;        //y coord of center
	private float y;		//x coord of center
	private float ringIncrement;
	private ArrayList<Hit> hits = new ArrayList<Hit>();
	Paint ringsPaint = new Paint();
	Paint bullsEyePaint = new Paint();
	Paint hitPaint = new Paint();
	
	public TargetView(Context c){
		super(c);
		ringsPaint.setColor(Color.RED);
		ringsPaint.setStyle(Style.STROKE);		
		bullsEyePaint.setColor(Color.RED);
		bullsEyePaint.setStyle(Style.FILL);
		hitPaint.setColor(Color.BLACK);
		hitPaint.setStyle(Style.FILL);
	
	}

	public TargetView(float diameter, float x, float y, Context c){
		super(c);
		this.x = x;
		this.y = y;
		ringIncrement = diameter/6f;
		ringsPaint.setColor(Color.RED);
		ringsPaint.setStyle(Style.STROKE);
		bullsEyePaint.setColor(Color.RED);
		bullsEyePaint.setStyle(Style.FILL);
		hitPaint.setColor(Color.BLACK);
		hitPaint.setStyle(Style.FILL);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
	
		canvas.drawCircle(x,y,(ringIncrement*3),ringsPaint);	    //draw largest circle first
		canvas.drawCircle(x,y,(ringIncrement*2),ringsPaint);
				
		canvas.drawCircle(x,y,(ringIncrement),bullsEyePaint);
		
		for(Hit h:hits){
			canvas.drawCircle(h.getX(), h.getY(), 10f, hitPaint);
		}		
	}
	
	public void addHit(float x, float y){
		hits.add(new Hit(x,y));
	}
}