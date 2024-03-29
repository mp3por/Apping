package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.adapters.ContactAdapter;
import uk.ac.gla.apping.quartet.businesscardapp.adapters.ContactDynamicAdapter;
import uk.ac.gla.apping.quartet.businesscardapp.adapters.ContactStaticAdapter;
import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int RESULT_DELETE = 1;
	public static final int RESULT_SAVE = 2;
	public static final int RESULT_UPDATE = 3;
	private static final int CONTACT_ADAPTER_SWITCH_POINT = 500;
	
	private ImageView mImageViewAdd;
	private ImageView mImageViewSearch;
	private EditText mEditTextSearch;
	private ContactAdapter mContactAdapter;
	private ListView mListViewContacts;
	private ArrayList<Contact> mArrayListContacts;
	private ContactHelper db = ContactHelper.getInstance(this);
	private int mContactCount = -1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);		
		
		mEditTextSearch = (EditText) findViewById(R.id.editTextSearch);
		mEditTextSearch.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mContactCount != 0) {
					mContactAdapter.filter(mArrayListContacts, mEditTextSearch.getText().toString());
					mContactAdapter.notifyDataSetChanged();
				}
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
	}

	
    @Override
	protected void onResume() {
    	String text = "";
    	int result = getIntent().getIntExtra("Action", -1);
    	getIntent().removeExtra("Action");
    	
    	switch (result) {
    		case MainActivity.RESULT_DELETE:
    			text = "Deleted";
    			break;
    		case MainActivity.RESULT_SAVE:
    			text = "Saved";
    			break;
    		case MainActivity.RESULT_UPDATE:
    			text = "Updated";
    			break;
			default:
				break;
    	}
    	
    	if (!text.equals("")) {
	    	Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
	    	toast.show();
    	}
    	
    	// update the adapter with contacts whenever the activity is brought to the front
    	// check contact count and update adapter only if contact count has changed
    	// provided that it is impossible to add one contact and delete another contact w/o opening MainActivity 
    	if (mContactCount != db.getContactCount()) {
    		mContactCount = db.getContactCount();
    		updateAdapter();
    	}
    	
    	Toast toastContactCount = Toast.makeText(this, "Contact count:"+ mContactCount, Toast.LENGTH_LONG);
    	toastContactCount.show();
		
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
	
	
	private void updateAdapter() {
		mListViewContacts = (ListView) findViewById(R.id.listViewContacts);
		
		if (mContactCount < MainActivity.CONTACT_ADAPTER_SWITCH_POINT) {
			mArrayListContacts = (ArrayList<Contact>) db.getAllContacts();
			mContactAdapter = new ContactStaticAdapter(MainActivity.this, mArrayListContacts, "");
		} else {
			mArrayListContacts = (ArrayList<Contact>) db.getAllContactsWithoutThumbnails();
			mContactAdapter = new ContactDynamicAdapter(MainActivity.this, mArrayListContacts, "");
		}
		
		mContactAdapter.filter(mArrayListContacts, mEditTextSearch.getText().toString());
		mListViewContacts.setAdapter(mContactAdapter);
	}
}
