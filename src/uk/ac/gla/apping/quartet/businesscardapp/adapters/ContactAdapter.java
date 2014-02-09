package uk.ac.gla.apping.quartet.businesscardapp.adapters;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.activities.CardViewerActivity;
import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	private static LayoutInflater inflater = null;

	private Spanned highlightMatch(String text) {
		return Html.fromHtml(text.replace(mMatch, "<b>" + mMatch + "</b>"));
	}
	
    private Activity mActivity;
    private ArrayList<Contact> mContacts;

	private String mMatch;
 
    public ContactAdapter(Activity activity, ArrayList<Contact> contacts, String search) {
        mActivity = activity;
        mContacts = contacts;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
          
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.image);
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
                
        thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(contact.getThumbnail(), 0, contact.getThumbnail().length));
        thumbnail.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {	
				Intent intent = new Intent(mActivity, CardViewerActivity.class);
				intent.putExtra("id", contact.getId()); // passing the database id of the card to the CardViewerActivity activity
				mActivity.startActivity(intent);
			}});
        
        return vi;
    }

	public void filter(ArrayList<Contact> allContacts, String needle) {
		
		
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
		
		mMatch = needle;
		notifyDataSetChanged(); 
	}
}
