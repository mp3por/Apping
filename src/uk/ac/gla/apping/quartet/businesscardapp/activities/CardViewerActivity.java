package uk.ac.gla.apping.quartet.businesscardapp.activities;

import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CardViewerActivity extends Activity {
	
	private TextView mTextView;
	private Button mButtonShare;
	private Button mButtonDelete;
	private Button mButtonSave;
	ContactHelper db = ContactHelper.getInstance(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_card_viewer);
		
		Intent senderIntent = getIntent();
		final int id = senderIntent.getIntExtra("id", -1); // will return the id, if if was specified, or will return -1, if no id was specified
		
		mTextView = (TextView) findViewById(R.id.textViewId);
		mTextView.setText("id: " + id);
		
		mButtonShare = (Button) findViewById(R.id.buttonShare);
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
		
		
		mButtonDelete = (Button) findViewById(R.id.buttonDelete);
		mButtonDelete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				//build dialog reaction to button clicks
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int button) {
				        //decide what to do according to the button pressed
				    	switch (button) {
					        case DialogInterface.BUTTON_POSITIVE:
					        	// delete contact
					        	db.deleteContact(id);
					        	
					        	// close this activity (screen) and indicate, that delete was performed
					        	Intent intent = new Intent(CardViewerActivity.this, MainActivity.class);
					        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					        	intent.putExtra("Action", MainActivity.RESULT_DELETE);
					        	startActivity(intent);
					            break;
					        case DialogInterface.BUTTON_NEGATIVE:
					            break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(CardViewerActivity.this);
				// define message and buttons
				builder.setMessage("Delete contact with id: "+ id +"?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
			}
		});
		
		
		mButtonSave = (Button) findViewById(R.id.buttonSave);
		mButtonSave.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Contact contact = new Contact();
				// populate contact fields here
				contact.setId(id);
				// ...
				
				
				// update contact in the database
				//db.updateContact(contact);
	        	
				// close this activity (screen) and indicate, that save was performed
				Intent intent = new Intent(CardViewerActivity.this, MainActivity.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	intent.putExtra("Action", MainActivity.RESULT_SAVE);
	        	startActivity(intent);
			}
		});
		
	}
}
