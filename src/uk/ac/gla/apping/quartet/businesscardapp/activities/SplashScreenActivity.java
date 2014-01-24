package uk.ac.gla.apping.quartet.businesscardapp.activities;

import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {
	private static final int SLEEP_TIME = 3000; // sleep time in milliseconds 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the layout 
		setContentView(R.layout.activity_splash_screen);
		
		Thread thread = new Thread(){

			@Override
			public void run() {
				try {
					Thread.sleep(SplashScreenActivity.SLEEP_TIME);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Intent intent = new Intent("uk.ac.gla.apping.quartet.businesscardapp.MAINACTIVITY");
					startActivity(intent);

					// end the activity, so user can't access it by pressing back button
					finish();
				}
			}
			
		};
		
		thread.start();
	}
	
}
