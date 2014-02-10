package uk.ac.gla.apping.quartet.businesscardapp.adapters;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.activities.CardViewerActivity;
import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	private static final String MESSAGE_NOCONTACTS = "No contacts";
	private static final String MESSAGE_NOTHING_FOUND = "No contacts match search criteria";

	private static LayoutInflater inflater = null;	
	
	private static ListView listView;
	private static TextView textViewNoContacts; 
	
    private Activity mActivity;
    private ArrayList<Contact> mContacts;
	private String mMatch;
 
    public ContactAdapter(Activity activity, ArrayList<Contact> contacts, String search) {
        mActivity = activity;
        mContacts = contacts;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView = (ListView) mActivity.findViewById(R.id.listViewContacts);
        textViewNoContacts = (TextView) mActivity.findViewById(R.id.textViewNoContacts);
    }
 
    public int getCount() {
        return mContacts.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
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
        thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(contact.getThumbnail(), 0, contact.getThumbnail().length));
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
        
        return vi;
    }

	public void filter(ArrayList<Contact> allContacts, String needle) {
		if (allContacts.size() == 0) {
			listView.setVisibility(View.GONE);
			textViewNoContacts.setText(MESSAGE_NOCONTACTS);
			textViewNoContacts.setVisibility(View.VISIBLE);
		} else {
			
			if (!needle.equals("")) {	
				ArrayList<Contact> filtered = new ArrayList<Contact>();
				for (Contact contact : allContacts) {
					if(contact.getName().contains(needle) || contact.getCompany().contains(needle) || contact.getEmail().contains(needle)) {
						filtered.add(contact);
					}
				}
			
				mContacts = filtered;
			} else {
				mContacts = allContacts;
			}
			
			
			if (mContacts.size() == 0) {
				listView.setVisibility(View.GONE);
				textViewNoContacts.setText(MESSAGE_NOTHING_FOUND);
				textViewNoContacts.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.VISIBLE);
				textViewNoContacts.setVisibility(View.GONE);
			}
		}
		mMatch = needle;
		notifyDataSetChanged(); 
	}
	
	private Spanned highlightMatch(String text) {
		return Html.fromHtml(text.replace(mMatch, "<b><font color=red>" + mMatch + "</font></b>"));
	}
}
