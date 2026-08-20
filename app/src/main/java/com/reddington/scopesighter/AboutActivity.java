package com.reddington.scopesighter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Explains the history of the app, offers users the ability to donate via
 * PayPal, and provides a button to return to the home screen.
 * 
 * @author Benjamin Reddington 2013
 */

public class AboutActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutlayout);

		Button homeButton = (Button) findViewById(R.id.aboutHomeButton);
		//Button donateButton = (Button) findViewById(R.id.donateButton);

		homeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				homeButtonClicked();
			}
		});
		/**
		donateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				donateButtonClicked();
			}
		});
 		*/
	}

	/**
	 * Launches an intent asking the phone to launch a browser and navigate to
	 * the URL saved in the intent. Takes user to a paypal donate screen.
	 */
	private void donateButtonClicked() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(Uri
				.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=6FDKJT9CS7J6L"));
		startActivity(intent);
	}

	public void homeButtonClicked() {
		startActivity(new Intent(this, HomeActivity.class));
	}

}
