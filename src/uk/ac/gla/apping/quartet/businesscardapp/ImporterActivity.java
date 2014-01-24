package uk.ac.gla.apping.quartet.businesscardapp;

import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ImporterActivity extends Activity {
	
	private Button mButtonCamera;
	private Button mButtonGallery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_importer);
		
		
		mButtonCamera = (Button) findViewById(R.id.buttonCamera);
		mButtonCamera.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
				// TODO: call camera
			}
				
		});
		
		
		mButtonGallery = (Button) findViewById(R.id.buttonGallery);
		mButtonGallery.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
				// TODO: call gallery
			}
				
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
