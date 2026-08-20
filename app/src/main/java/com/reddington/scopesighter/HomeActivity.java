package com.reddington.scopesighter;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.*;
import android.widget.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/**
 * Starting Activity for the app. Displays logo, access to 
 * ScopeAndRangeManager, and spinners for choosing existing 
 * scopes and ranges for a new sighting session. Provides buttons
 * to begin a new session.
 * @author Benjamin Reddington 2013 *
 */
public class HomeActivity extends Activity {

	TextView title;
	Button newSessionButton, aboutButton, scopeAndRangeManagerButton, helpButton, unitsButton;
	Spinner scopeSpinner, rangeSpinner;

	ArrayList<Range> rangeList; 
	ArrayList<Scope> scopeList;
	private ScopeSighterApplication ssapp;
	
	Context context;
	
	/** 
	 * Called when the activity is first created. 
	 * Sets up all listeners, initializes variables
	 * from other classes. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		

		context = this;
		
		ssapp = (ScopeSighterApplication)getApplication();		
		setContentView(R.layout.scopesighterhomelayout);				
		
		aboutButton = (Button) findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new OnClickListener() { 
			public void onClick(View v) {
				lauchAboutActivity();
			} 
		});
		
		scopeAndRangeManagerButton = (Button) findViewById(R.id.scopeAndRangeManagerButton);
		scopeAndRangeManagerButton.setOnClickListener(new OnClickListener() { 
			public void onClick(View v) {
				launchScopeAndRangeManager();
			} 
		});		
		
		rangeSpinner = (Spinner) findViewById(R.id.rangeSpinner);
		rangeSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
				setSavableActive(rangeList.get(pos));				
			}
			public void onNothingSelected(AdapterView<?> arg0) {}      //required method definition			 
		});		
		
		scopeSpinner = (Spinner) findViewById(R.id.scopeSpinner);
		scopeSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){					
				setSavableActive(scopeList.get(pos));			
			}
			public void onNothingSelected(AdapterView<?> arg0) {}		 //required method definition	
		});		
		
		newSessionButton = (Button) findViewById(R.id.newSessionButton);
		newSessionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				launchNewSession();
			}
		});
		
		helpButton = (Button) findViewById(R.id.helpButton);
		helpButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.dialoglayout);		 
				Button closeButton = (Button) dialog.findViewById(R.id.closeButton);
				closeButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.setTitle(getString(R.string.dialogTitleText));
				dialog.show();
			}
		});
		
		unitsButton = (Button)findViewById(R.id.unitsButton);
		unitsButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(ssapp.isAppImperial()){ //if the app was in imperial when the button was pressed...
					unitsButton.setText(getString(R.string.unitsButtonMetricText));
					ssapp.setUnitsMetric();
				}
				else{ //if the app was in metric when the button was pressed...
					unitsButton.setText(getString(R.string.unitsButtonImperialText));
					ssapp.setUnitsImperial();
				}
			}
		});
				
		ssapp.ensurePopulated();			
		updateSpinners();			
	}	

	private void lauchAboutActivity(){ 
		startActivity(new Intent(this, AboutActivity.class));
	}
	/**
	 * Checks for valid scope and range, proceeds with
	 * a new target and SightingSessionActivity, or notifies user to 
	 * create a scope or range. Does not check each.
	 */
	public void launchNewSession() {
		if(!(ssapp.getActiveScope().getName().equals("Add a Scope")) && (!(ssapp.getActiveRange().getName().equals("Add a Range")))){		
			TableLayout t = (TableLayout) findViewById(R.id.mainTableLayout);
			ssapp.setDeviceWidth(t.getWidth());
			ssapp.setDeviceHeight(t.getHeight());
			ssapp.setTarget(new Target());
			startActivity(new Intent(this, SightingSessionActivity.class));
		}
		else{			
			Context context = getApplicationContext();
			CharSequence text = "Please choose or create your scope and range!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	private void setSavableActive(Savable s){
		if(s instanceof Range)
			ssapp.setActiveRange(s.getName());
		else
			ssapp.setActiveScope(s.getName());
	}
	/**
	 * Makes sure the spinners reflect the latest range and scope lists.
	 */
	private void updateSpinners(){
				
		rangeList = ssapp.getRanges();  
		scopeList = ssapp.getScopes();
		
		ArrayList<String> rangeNames = new ArrayList<String>();		
		for(Range r1:rangeList){
			rangeNames.add(r1.getName());
		}
		
		ArrayList<String> scopeNames = new ArrayList<String>();		
		for(Scope s1:scopeList){
			scopeNames.add(s1.getName());
		}
				
		ArrayAdapter<String> rangeDataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, rangeNames);
		rangeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rangeSpinner.setAdapter(rangeDataAdapter);
		rangeSpinner.setSelection((rangeNames.indexOf(ssapp.getActiveRange().getName())));
		rangeSpinner.invalidate();
				
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, scopeNames);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scopeSpinner.setAdapter(dataAdapter);	
		scopeSpinner.setSelection((scopeNames.indexOf(ssapp.getActiveScope().getName())));
		scopeSpinner.invalidate();
	}	
	
	private void launchScopeAndRangeManager(){		
		startActivity(new Intent(this, SavableManagerActivity.class));
	}
	
	public void onResume(){
		 super.onResume();
		 updateSpinners();	
		 setUnitStrings();				
	}
	/**
	 * Changes the text label of the unitToggleButton based on ssapp unit status.
	 */
	private void setUnitStrings(){
		if (ssapp.isAppImperial() == false)
			unitsButton.setText(getString(R.string.unitsButtonMetricText));			
		else
			unitsButton.setText(getString(R.string.unitsButtonImperialText));
	}
}