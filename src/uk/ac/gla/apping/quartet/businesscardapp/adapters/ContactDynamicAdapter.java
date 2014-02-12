package uk.ac.gla.apping.quartet.businesscardapp.adapters;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.activities.CardViewerActivity;
import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactDynamicAdapter extends ContactAdapter {
	private ContactHelper db = ContactHelper.getInstance(mActivity);
 
    public ContactDynamicAdapter(Activity activity, ArrayList<Contact> contacts, String search) {
        super(activity, contacts, search);
    }
 
 
    public View getView(int position, View convertView, ViewGroup parent) {
        // reusing views to ensure that do not run out of memory
    	View vi = convertView;
        if(convertView == null) {
            vi = inflater.inflate(R.layout.list_activity_main, null);
        }
          
        TextView name = (TextView) vi.findViewById(R.id.textViewContactName);
        TextView number = (TextView) vi.findViewById(R.id.textViewContactNumber);
        TextView email = (TextView) vi.findViewById(R.id.textViewContactEmail);
        TextView company = (TextView) vi.findViewById(R.id.textViewContactCompany);

        final Contact contact = mContacts.get(position);
 
        name.setText(highlightMatch(contact.getName()));
        
        if (!mMatch.equals("") && contact.getNumber().contains(mMatch)){
        	number.setVisibility(View.VISIBLE);
        	number.setText(highlightMatch(contact.getNumber()));
        } else {
        	number.setVisibility(View.GONE);
        }      

        if (!mMatch.equals("") && contact.getCompany().contains(mMatch)){
        	company.setVisibility(View.VISIBLE);
        	company.setText(highlightMatch(contact.getCompany()));
        } else {
        	number.setVisibility(View.GONE);
        }
        
        if (!mMatch.equals("") && contact.getEmail().contains(mMatch)){
        	email.setVisibility(View.VISIBLE);
        	email.setText(highlightMatch(contact.getEmail()));
        } else {
        	email.setVisibility(View.GONE);
        }
          
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.imageViewLogo);
        
        thumbnail.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
				Intent intent = new Intent(mActivity, CardViewerActivity.class);
				intent.putExtra("id", contact.getId()); // passing the database id of the card to the CardViewerActivity activity
				mActivity.startActivity(intent);
			}});  
        
        ImageView actionCall = (ImageView) vi.findViewById(R.id.imageViewCall);
        actionCall.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);          
	            callIntent.setData(Uri.parse("tel:" + contact.getNumber()));          
	            mActivity.startActivity(callIntent);
			}
		});
        
        ImageView actionSms = (ImageView) vi.findViewById(R.id.imageViewSms);
        actionSms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", contact.getNumber());
				mActivity.startActivity(smsIntent);
			}
        });
        
        ImageView actionEmail = (ImageView) vi.findViewById(R.id.imageViewEmail);
        actionEmail.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String[] recipients = new String[]{contact.getEmail(), ""};  
				Intent emailIntent = new Intent(Intent.ACTION_SEND);  
				emailIntent.setType("message/rfc822");      
				emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);  
				mActivity.startActivity(emailIntent);  
			}
		});
        
        (new ImageUpdater(thumbnail, contact.getId())).execute();
        
        return vi;
    }
	
	private class ImageUpdater extends AsyncTask<String, Void, Boolean> {
		private ImageView mThumbnail;
		private int id;
		private byte[] mBytes;
		
		public ImageUpdater(ImageView imageView, int contactId) {
			mThumbnail = imageView;
			id = contactId;
		}

		@Override
		protected void onPreExecute() {}

		@Override
		protected Boolean doInBackground(final String... args) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				//supress this shit
			}
			mBytes = db.getContactThumbnailById(id);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mThumbnail.setImageBitmap(BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length));
		}
	}
}
