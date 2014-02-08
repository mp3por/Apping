package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import uk.ac.gla.apping.quatret.businesscardapp.adapters.ContactAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ImageView mImageViewAdd;
	private ImageView mImageViewSearch;
	private Button mButtonCard;
	private EditText mEditTextSearch;
	private ContactAdapter mContactAdapter;
	private ListView mListViewContacts;
	private ArrayList<Contact> mArrayListContacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);	
		
		
		mEditTextSearch = (EditText) findViewById(R.id.editTextSearch);
		mEditTextSearch.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mContactAdapter.filter(mArrayListContacts, mEditTextSearch.getText().toString());
				mContactAdapter.notifyDataSetChanged();
			}
		});
		
		
		
		mImageViewAdd = (ImageView) findViewById(R.id.imageViewAdd);
		mImageViewAdd.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
				Intent intent = new Intent(MainActivity.this, ImporterActivity.class);
				startActivity(intent);
			}
		});
		
		mImageViewSearch = (ImageView) findViewById(R.id.imageViewSearch);
		mImageViewSearch.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
	        	CharSequence text = "YAY! I will be replaced soon!";
	        	Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
	        	toast.show();
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
	protected void onPause() {
		super.onPause();
		mArrayListContacts.clear();
		mArrayListContacts = null;
	}

	
	// update the adapter with contacts whenever the activity is brought to the front
	// TODO: may go for cleaner solution - check contact count and update adapter only if contact count has changed
	// provided that it is impossible to add one contact and delete another contact w/o opening MainActivity inbetween 
    @Override
	protected void onResume() {
		
    	ContactHelper db = ContactHelper.getInstance(this);
		
    	mArrayListContacts = (ArrayList<Contact>) db.getAllContacts();
				
		mListViewContacts = (ListView) findViewById(R.id.listViewContacts);
		 
		mContactAdapter = new ContactAdapter(this, mArrayListContacts, "");
		mContactAdapter.filter(mArrayListContacts, mEditTextSearch.getText().toString());
		
		mListViewContacts.setAdapter(mContactAdapter);
		
		super.onResume();
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
