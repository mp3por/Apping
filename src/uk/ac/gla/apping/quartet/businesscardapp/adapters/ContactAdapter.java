package uk.ac.gla.apping.quartet.businesscardapp.adapters;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ContactAdapter extends BaseAdapter {
	protected static final String MESSAGE_NOCONTACTS = "No contacts";
	protected static final String MESSAGE_NOTHING_FOUND = "No contacts match search criteria";

	protected static LayoutInflater inflater = null;	
	
	protected static ListView listView;
	protected static TextView textViewNoContacts; 
	
    protected Activity mActivity;
    protected ArrayList<Contact> mContacts;
	protected String mMatch;
	
	
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
	
	protected Spanned highlightMatch(String text) {
		return Html.fromHtml(text.replace(mMatch, "<b><font color=red>" + mMatch + "</font></b>"));
	}
	
	protected static class ViewHolder {
		TextView name;
		TextView number;
		TextView email;
		TextView company;
		ImageView thumbnail;
		ImageView actionCall;
		ImageView actionSms;
		ImageView actionEmail;
	}
}
