package uk.ac.gla.apping.quartet.businesscardapp.activities;

import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button mButtonAdd;
	private Button mButtonCard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
				intent.putExtra("id", 0); // passing the database id of the card to the CardViewerActivity activity
				startActivity(intent);
			}	
		});
		
	}

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.menuItemAdvancedSearch:
	        	// do something with the advanced search... maybe, maybe not.
	        	CharSequence text = "Pending...";
	        	Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
	        	toast.show();
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
}
