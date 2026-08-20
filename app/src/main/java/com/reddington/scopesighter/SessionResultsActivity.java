package com.reddington.scopesighter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * Displays the data in the results object in ssapp.
 * Provides navigation back to HomeActivity or to a
 * new sighting session with the same scope and range
 * as the one just performed.
 * @author Benjamin Reddington 2013
 */
public class SessionResultsActivity extends Activity {

	ScopeSighterApplication ssapp = (ScopeSighterApplication)getApplication();
	Result r;
	Button homeButton;
	Button newSessionButton;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultslayout);
		
		ssapp = (ScopeSighterApplication)getApplication();
		
		r = ssapp.getResult();
		
		TextView elevationClicks = (TextView)findViewById(R.id.elevationClicks);
		TextView elevationDirection = (TextView)findViewById(R.id.elevationDirection);
		TextView windageClicks = (TextView)findViewById(R.id.windageClicks);
		TextView windageDirection = (TextView)findViewById(R.id.windageDirection);
		
		elevationClicks.setText("" + r.getElevationClicks());		
		windageClicks.setText("" + r.getWindageClicks());
		elevationDirection.setText(r.getElevationRotationDirection());
		windageDirection.setText(r.getWindageRotationDirection());		
		
		homeButton = (Button)findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				homeButtonClicked();			
			}		
		});
      	
      	newSessionButton = (Button)findViewById(R.id.newSessionButton);
      	newSessionButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				newSessionButtonClicked();				
			}      		
      	});		
	}
	 
	public void homeButtonClicked(){
		startActivity(new Intent(this, HomeActivity.class));
	}
	
	public void newSessionButtonClicked(){
		ssapp.setTarget(new Target());
		startActivity(new Intent(this, SightingSessionActivity.class));
	}	
}