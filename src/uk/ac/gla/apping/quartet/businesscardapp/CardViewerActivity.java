package uk.ac.gla.apping.quartet.businesscardapp;

import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CardViewerActivity extends Activity {
	
	private TextView mTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_card_viewer);
		
		Intent senderIntent = getIntent();
		int id = senderIntent.getIntExtra("id", -1); // will return the id, if if was specified, or will return -1, if no id was specified
		
		mTextView = (TextView) findViewById(R.id.textViewId);
		
		mTextView.setText("id: " + id);
	}
}
