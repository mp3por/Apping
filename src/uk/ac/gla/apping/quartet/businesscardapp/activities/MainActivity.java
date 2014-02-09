package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.gla.apping.quartet.businesscardapp.adapters.ContactAdapter;
import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businesscardapp.data.ContactWithImages;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
	public static final int RESULT_DELETE = 1;
	public static final int RESULT_SAVE = 2;
	
	private ImageView mImageViewAdd;
	private ImageView mImageViewSearch;
	private EditText mEditTextSearch;
	private ContactAdapter mContactAdapter;
	private ListView mListViewContacts;
	private ArrayList<Contact> mArrayListContacts;
	private ContactHelper db = ContactHelper.getInstance(this);
	private int mContactCount;
	
	
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
		
		Button fuckingCrashMe = (Button) findViewById(R.id.buttonCrashMe);
		fuckingCrashMe.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
				PopulateDatabase thread = new PopulateDatabase();
				
				thread.execute();
				
				
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
    		updateAdapter();
    		mContactCount = db.getContactCount();
    	}
		
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
		mArrayListContacts = (ArrayList<Contact>) db.getAllContacts();

		mListViewContacts = (ListView) findViewById(R.id.listViewContacts);

		mContactAdapter = new ContactAdapter(MainActivity.this, mArrayListContacts, "");
		mContactAdapter.filter(mArrayListContacts, mEditTextSearch.getText().toString());

		mListViewContacts.setAdapter(mContactAdapter);
	}
	
	
	
	private class PopulateDatabase extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

		/** progress dialog to show user that the backup is processing. */
		/** application context. */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Please wait, duplicating contacts....");
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(final String... args) {
			ContactHelper db = ContactHelper.getInstance(getApplicationContext());
        	
        	int copyCount = Integer.parseInt(((EditText) findViewById(R.id.editTextCopyCount)).getText().toString());
        	for (int i = 0; i < copyCount; i++) {
        		ContactWithImages contact = new ContactWithImages();
    			contact.setName("Thissurname"+ (new Random().nextInt(1000)));
    			contact.setEmail("test@test.com");
    			contact.setCompany("RIP APPING");
    			contact.setNumber("+44711111");
    			contact.setThumbnail(db.getContactById(1).getThumbnail());	
        		db.createContact(contact);
        	}
			
			
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			MainActivity.this.updateAdapter();
		}
	}

}
