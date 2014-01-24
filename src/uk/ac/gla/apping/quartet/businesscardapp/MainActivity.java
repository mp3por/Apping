package uk.ac.gla.apping.quartet.businesscardapp;

import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button mButtonAdd;
	private Button mButtonCard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);	
		
		mButtonAdd = (Button) findViewById(R.id.buttonAdd);
		mButtonAdd.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
				Intent intent = new Intent(MainActivity.this, ImporterActivity.class);
				startActivity(intent);
			}
				
		});
		
		
		mButtonCard = (Button) findViewById(R.id.buttonCard);
		mButtonCard.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {			
				Intent intent = new Intent(MainActivity.this, CardViewerActivity.class);
				startActivity(intent);
			}
				
		});
		
	}
}
