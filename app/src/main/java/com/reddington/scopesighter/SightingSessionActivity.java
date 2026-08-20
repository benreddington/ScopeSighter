package com.reddington.scopesighter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Displays the TargetView and processes screen touches.
 * Fills in data on the Target object for processing 
 * the result later. Provides a button to proceed when the 
 * user is ready.
 * @author Benjamin Reddington
 */
public class SightingSessionActivity extends Activity{

	private ScopeSighterApplication ssapp;
	private Target t;
	private Button dispResultButton;
	private float diameter;
	private TargetView targetView;
	private RelativeLayout targetLayout;
	private float screenHeight;
	private float screenWidth;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);                        
		setContentView(R.layout.sightingsessionlayout);
		targetView = new TargetView(this);		
		ssapp = (ScopeSighterApplication)getApplication();

		targetLayout = (RelativeLayout)findViewById(R.id.targetLayout);
		t = ssapp.getTarget();
		
		dispResultButton = (Button)findViewById(R.id.displayResultButton);
      	dispResultButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				dispResultButtonClicked();			
			}		
		});      	
      
      	screenWidth = ssapp.getDeviceWidth();
      	screenHeight = ssapp.getDeviceHeight();      	
      	
      	if (screenWidth < screenHeight)
			diameter = screenWidth;
		else
			diameter = screenHeight; 

		targetView = new TargetView(diameter,(screenWidth/2),(screenWidth/2),this);
		
		targetView.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent e) {
				if(e.getActionMasked() == MotionEvent.ACTION_DOWN){					
					t.addHit(new Hit(e.getX(),e.getY()));
					targetView.addHit(e.getX(),e.getY());
					targetView.invalidate();             
				}
				return true;				
			}			
		});
					
		t.setCenterX(screenWidth/2);
		t.setCenterY(targetLayout.getHeight()/2);
		t.setPixelDiameter(diameter);
		targetLayout.addView(targetView);		
		dispResultButton.bringToFront();
	}

	public void dispResultButtonClicked(){ 			
		
			ssapp.setTarget(t);
			ssapp.calculate();			
			startActivity(new Intent(this, SessionResultsActivity.class));		
		}
}