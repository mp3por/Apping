package uk.ac.gla.apping.quartet.businesscardapp.adapters;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.activities.CardViewerActivity;
import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactStaticAdapter extends ContactAdapter {

    public ContactStaticAdapter(Activity activity, ArrayList<Contact> contacts, String search) {
    	super(activity, contacts, search);
    }
  
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
    	
    	// reusing views to ensure that do not run out of memory
    	View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_activity_main, null);
            viewHolder = new ViewHolder();
            
            viewHolder.name = (TextView) view.findViewById(R.id.textViewContactName);
            viewHolder.number = (TextView) view.findViewById(R.id.textViewContactNumber);
            viewHolder.email = (TextView) view.findViewById(R.id.textViewContactEmail);
            viewHolder.company = (TextView) view.findViewById(R.id.textViewContactCompany);
            viewHolder.thumbnail = (ImageView) view.findViewById(R.id.imageViewLogo);
            viewHolder.actionCall = (ImageView) view.findViewById(R.id.imageViewCall);
            viewHolder.actionSms = (ImageView) view.findViewById(R.id.imageViewSms);
            viewHolder.actionEmail = (ImageView) view.findViewById(R.id.imageViewEmail);
            
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final Contact contact = mContacts.get(position);
 
        viewHolder.name.setText(highlightMatch(contact.getName()));
        
        if (!mMatch.equals("") && contact.getNumber().contains(mMatch)) {
        	viewHolder.number.setVisibility(View.VISIBLE);
        	viewHolder.number.setText(highlightMatch(contact.getNumber()));
        } else {
        	viewHolder.number.setVisibility(View.GONE);
        }      

        if (!mMatch.equals("") && contact.getCompany().contains(mMatch)) {
        	viewHolder.company.setVisibility(View.VISIBLE);
        	viewHolder.company.setText(highlightMatch(contact.getCompany()));
        } else {
        	viewHolder.number.setVisibility(View.GONE);
        }
        
        if (!mMatch.equals("") && contact.getEmail().contains(mMatch)) {
        	viewHolder.email.setVisibility(View.VISIBLE);
        	viewHolder.email.setText(highlightMatch(contact.getEmail()));
        } else {
        	viewHolder.email.setVisibility(View.GONE);
        }
          
        
        viewHolder.thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(contact.getThumbnail(), 0, contact.getThumbnail().length));
        viewHolder.thumbnail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {	
				Intent intent = new Intent(mActivity, CardViewerActivity.class);
				intent.putExtra("id", contact.getId()); // passing the database id of the card to the CardViewerActivity activity
				mActivity.startActivity(intent);
			}});  
        
        
        viewHolder.actionCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);          
	            callIntent.setData(Uri.parse("tel:" + contact.getNumber()));          
	            mActivity.startActivity(callIntent);
			}
		});
        
        
        viewHolder.actionSms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", contact.getNumber());
				mActivity.startActivity(smsIntent);
			}
        });
        
        
        viewHolder.actionEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String[] recipients = new String[]{contact.getEmail(), ""};  
				Intent emailIntent = new Intent(Intent.ACTION_SEND);  
				emailIntent.setType("message/rfc822");      
				emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);  
				mActivity.startActivity(emailIntent);  
			}
		});
        
        return view;
    }
}
