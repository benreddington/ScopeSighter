package com.reddington.scopesighter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Displays UI controls for creating, modifying, and deleting Scope and Range
 * objects. Accesses and displays current state of collections of both object
 * types. Sets the last accessed scope and range to active for the next sighting
 * session.
 * 
 * @author Benjamin Reddington 2013
 */
public class SavableManagerActivity extends Activity {

	ScopeSighterApplication ssapp;

	Spinner scopeSpinner;
	Spinner rangeSpinner;
	Button saveScopeButton;
	Button deleteScopeButton;
	Button saveRangeButton;
	Button deleteRangeButton;
	Button backButton;
	Button clockwiseUpToggleButton;
	Button clockwiseLeftToggleButton;
	EditText scopeNameEditText;
	EditText oneClickEqualsEditText;
	EditText yardsForAdjustEditText;
	EditText rangeNameEditText;
	EditText feetToTargetEditText;
	EditText targetDiameterEditText;

	DecimalFormat df = new DecimalFormat("@####");

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ssapp = (ScopeSighterApplication) getApplication();
		setContentView(R.layout.saveablemanagerlayout);

		scopeSpinner = (Spinner) findViewById(R.id.scopeListSpinner);
		scopeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						ssapp.setActiveScope((String) parent
								.getItemAtPosition(pos));
						updateScopeControls();
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		rangeSpinner = (Spinner) findViewById(R.id.rangeListSpinner);
		rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
						ssapp.setActiveRange((String) parent.getItemAtPosition(pos));
						updateRangeControls();
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		clockwiseUpToggleButton = (Button) findViewById(R.id.clockwiseUpToggleButton);
		clockwiseUpToggleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (clockwiseUpToggleButton.getText().toString().equals("Up"))
					clockwiseUpToggleButton.setText("Down");
				else
					clockwiseUpToggleButton.setText("Up");
			}
		});

		clockwiseLeftToggleButton = (Button) findViewById(R.id.clockwiseLeftToggleButton);
		clockwiseLeftToggleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (clockwiseLeftToggleButton.getText().toString().equals("Left"))
					clockwiseLeftToggleButton.setText("Right");
				else
					clockwiseLeftToggleButton.setText("Left");
			}
		});

		saveScopeButton = (Button) findViewById(R.id.saveScopeButton);
		saveScopeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveScope();
			}
		});

		deleteScopeButton = (Button) findViewById(R.id.deleteScopeButton);
		deleteScopeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				deleteScope();
			}
		});

		saveRangeButton = (Button) findViewById(R.id.saveRangeButton);
		saveRangeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveRange();
			}
		});

		deleteRangeButton = (Button) findViewById(R.id.deleteRangeButton);
		deleteRangeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				deleteRange();
			}
		});

		backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				backButtonPressed();
				// must also intercept physical back button
			}
		});

		// getting references to the controls whose labels will be updated for
		// metric or imperial
		scopeNameEditText = (EditText) findViewById(R.id.scopeNameEditText);
		oneClickEqualsEditText = (EditText) findViewById(R.id.oneClickEqualsEditText);
		yardsForAdjustEditText = (EditText) findViewById(R.id.yardsForAdjustEditText);
		rangeNameEditText = (EditText) findViewById(R.id.rangeNameEditText);
		feetToTargetEditText = (EditText) findViewById(R.id.feetToTargetEditText);
		targetDiameterEditText = (EditText) findViewById(R.id.diameterOfTargetEditText);

		updateSpinners();
		setUnitLabels(); // since onResume() might not be called immediately after onCreate()
							
	}
/**
 * Ensures that if the units were changed or 
 * a different scope or range was made active,
 * that the proper labels and values are shown.
 */
	public void onResume() {
		super.onResume();
		setUnitLabels();
		updateScopeControls();
		updateRangeControls();
	}

	private void setUnitLabels() {

		if (ssapp.isAppImperial() == false) {

			TextView oneClickEquals = (TextView) findViewById(R.id.oneClickEqualsTextView);
			oneClickEquals.setText(getString(R.string.metricOneClickEqualsText));

			TextView inchesTextView = (TextView) findViewById(R.id.inchesTextView);
			inchesTextView.setText(getString(R.string.metricDistanceForScopeText));

			TextView feetToTarget = (TextView) (findViewById(R.id.feetToTargetTextView));
			feetToTarget.setText(getString(R.string.metersToTargetText));

			TextView targetDiameter = (TextView) (findViewById(R.id.diameterOfTargetTextView));
			targetDiameter.setText(getString(R.string.metricDiameterOfTargetText));
		}
	}

	private void updateScopeControls() {

		Scope active = ssapp.getActiveScope();
		scopeNameEditText.setText(active.getName());
		if (active.getClockwiseOffsetsLeft())
			clockwiseLeftToggleButton.setText(getString(R.string.toggleLeft));
		else
			clockwiseLeftToggleButton.setText(getString(R.string.toggleRight));
		if (active.getClockwiseOffsetsUp())
			clockwiseUpToggleButton.setText(getString(R.string.toggleUp));
		else
			clockwiseUpToggleButton.setText(getString(R.string.toggleDown));
		
		oneClickEqualsEditText.setText(df.format(active.getOffsetPerClick()));
		yardsForAdjustEditText.setText(df.format(active.getDistanceForAdjust()));
	}

	private void updateRangeControls() {

		Range active = ssapp.getActiveRange();
		rangeNameEditText.setText(active.getName());
		feetToTargetEditText.setText(df.format(active.getDistanceToTarget()));
		targetDiameterEditText.setText(df.format(active.getTargetDiameter()));
	}

	private void updateSpinners() {

		ArrayList<Range> rangeList = ssapp.getRanges();
		ArrayList<Scope> scopeList = ssapp.getScopes();

		ArrayList<String> rangeNames = new ArrayList<String>();
		for (Range r1 : rangeList) {
			rangeNames.add(r1.getName());
		}

		ArrayList<String> scopeNames = new ArrayList<String>();
		for (Scope s1 : scopeList) {
			scopeNames.add(s1.getName());
		}

		ArrayAdapter<String> rangeDataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, rangeNames);
		rangeDataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rangeSpinner.setAdapter(rangeDataAdapter);

		rangeSpinner.setSelection((rangeNames.indexOf(ssapp.getActiveRange()
				.getName())));
		updateRangeControls();

		ArrayAdapter<String> scopeDataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, scopeNames);
		scopeDataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scopeSpinner.setAdapter(scopeDataAdapter);

		scopeSpinner.setSelection((scopeNames.indexOf(ssapp.getActiveScope()
				.getName())));
		scopeDataAdapter.notifyDataSetChanged();
		updateScopeControls();
	}
/**
 * Ensures minimum input has been provided for the
 * app to create a new scope object.
 * @return
 */
	private boolean validateScopeAttributes() {

		if ((!(scopeNameEditText.getText().toString().equals("")))
				&& (!(yardsForAdjustEditText.getText().toString().equals("")))
				&& (!(oneClickEqualsEditText.getText().toString().equals("")))) {
			return true;
		}

		Context context = getApplicationContext();
		CharSequence text = "Check Scope Inputs";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		return false;
	}

	/**
	 * Ensures minimum input has been provided by
	 * the user to create a new range object.
	 * @return
	 */
	private boolean validateRangeAttributes() {

		if ((!(rangeNameEditText.getText().toString().equals("")))
				&& (!(feetToTargetEditText.getText().toString().equals("")))
				&& (!(targetDiameterEditText.getText().toString().equals("")))) {
			return true;
		}

		Context context = getApplicationContext();
		CharSequence text = "Check Range Inputs";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		return false;
	}

	private void backButtonPressed() {
		startActivity(new Intent(this, HomeActivity.class));
	}

	
	/**
	 * Following ~200 lines save and delete scopes and ranges
	 * with some validation to prevent duplicates, etc.
	 */
	private void saveScope() {

		if (validateScopeAttributes()) {
			boolean clockLeft = false;
			boolean clockUp = false;

			if (clockwiseUpToggleButton.getText().equals("Up"))
				clockUp = true;
			if (clockwiseLeftToggleButton.getText().equals("Left"))
				clockLeft = true;

			Scope s = new Scope(scopeNameEditText.getText().toString(), Double.parseDouble(oneClickEqualsEditText.getText().toString()), Double.parseDouble(yardsForAdjustEditText.getText().toString()), clockLeft, clockUp);

			ArrayList<Scope> existingScopes = ssapp.getScopes();
			boolean existing = false;
			for (Scope es : existingScopes) {
				if (s.equals(es))
					existing = true;
			}

			if (existing) {
				Context context = getApplicationContext();
				CharSequence text = "Scope Saved";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return;
			}

			ssapp.updateSavables(s);
			updateSpinners();

			Context context = getApplicationContext();
			CharSequence text = "Scope Saved";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	/**
	 * Original operation was to check for an existing scope that matched all of
	 * the attribute fields. If a user accidentally modified the name or a
	 * value, delete would fail and a toast would instruct the user to reselect
	 * the scope from the list so that it's attributes would be re-populated.
	 * Since Spinners only fire events on changed selections, that won't work.
	 * Hence the updateScopeControls(); and then proceeding, rather than
	 * checking for valid attributes, etc.
	 */
	private void deleteScope() {

		updateScopeControls(); //this essentially causes the method to delete the active scope

		if (validateScopeAttributes()) {
			boolean clockLeft = false;
			boolean clockUp = false;

			if (clockwiseUpToggleButton.getText().equals("Up"))
				clockUp = true;
			if (clockwiseLeftToggleButton.getText().equals("Left"))
				clockLeft = true;

			Scope s = new Scope(scopeNameEditText.getText().toString(),
					Double.parseDouble(oneClickEqualsEditText.getText()
							.toString()),
					Double.parseDouble(yardsForAdjustEditText.getText()
							.toString()), clockLeft, clockUp);

			ArrayList<Scope> existingScopes = ssapp.getScopes();
			boolean existing = false;
			for (Scope es : existingScopes) {
				if (s.equals(es))
					existing = true;
			}

			if (!existing) {
				Context context = getApplicationContext();
				CharSequence text = "No scope with specified attributes exists. Reselect scope and press delete.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return; // is there a more readable way to do this with an
						// "else" statement? Not intuitive.
			}

			ssapp.updateSavables(s);
			updateSpinners();

			Context context = getApplicationContext();
			CharSequence text = "Scope Deleted";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	/**
	 * Saves a Range object in ssapp by creating an instance from the user
	 * specified values. If the Range != to any existing range, it is added to
	 * the main list of savables in ssapp.
	 */
	private void saveRange() {

		if (validateRangeAttributes()) {
			Range r = new Range(Float.parseFloat(feetToTargetEditText.getText()
					.toString()), Float.parseFloat(targetDiameterEditText
					.getText().toString()), rangeNameEditText.getText()
					.toString());

			ArrayList<Range> existingRanges = ssapp.getRanges();
			boolean existing = false;
			for (Range er : existingRanges) {
				if (r.equals(er))
					existing = true;
			}

			if (existing) {
				Context context = getApplicationContext();
				CharSequence text = "Range Saved";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return;
			}

			ssapp.updateSavables(r);
			updateRangeControls();
			updateSpinners();

			Context context = getApplicationContext();
			CharSequence text = "Range Saved";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	/**
	 * See deleteScope() above
	 */
	private void deleteRange() {

		updateRangeControls();

		if (validateRangeAttributes()) {
			Range r = new Range(Float.parseFloat(feetToTargetEditText.getText()
					.toString()), Float.parseFloat(targetDiameterEditText
					.getText().toString()), rangeNameEditText.getText()
					.toString());

			ArrayList<Range> existingRanges = ssapp.getRanges();
			boolean existing = false;
			for (Range er : existingRanges) {
				if (r.equals(er))
					existing = true;
			}

			if (!existing) {
				Context context = getApplicationContext();
				CharSequence text = "No range with specified attributes exists. Reselect range and press delete.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return;
			}

			ssapp.updateSavables(r);
			updateSpinners();

			Context context = getApplicationContext();
			CharSequence text = "Range Deleted";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
}
