package uk.ac.gla.apping.quartet.businesscardapp.activities;

import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CardViewerActivity extends Activity {
	
	private TextView mTextView;
	private Button mButtonShare;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_card_viewer);
		
		Intent senderIntent = getIntent();
		int id = senderIntent.getIntExtra("id", -1); // will return the id, if if was specified, or will return -1, if no id was specified
		
		mTextView = (TextView) findViewById(R.id.textViewId);
		mButtonShare = (Button) findViewById(R.id.buttonShare);
		
		mTextView.setText("id: " + id);
		
		
		mButtonShare.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String[] recipients = new String[]{"service@google.com", ""};  
				Intent testIntent = new Intent(Intent.ACTION_SEND);  
				testIntent.setType("message/rfc822");  
				testIntent.putExtra(Intent.EXTRA_SUBJECT, "Haha no customer service from google!");    
				testIntent.putExtra(Intent.EXTRA_EMAIL, recipients);  
				startActivity(testIntent);  
				
			}
			
		});
		
	}
}
