package uk.ac.gla.apping.quatret.businesscardapp.adapters;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.activities.CardViewerActivity;
import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	private static LayoutInflater inflater = null;
	
    private Activity mActivity;
    private ArrayList<Contact> mContacts;
    private int mSize;
 
    public ContactAdapter(Activity activity, ArrayList<Contact> contacts) {
        mActivity = activity;
        mContacts = contacts;
        mSize = contacts.size();
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return mSize;
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.list_activity_main, null);
        }
          
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.image);
        TextView name = (TextView) vi.findViewById(R.id.textViewContactName);
        TextView number = (TextView) vi.findViewById(R.id.textViewContactNumber);
        TextView email = (TextView) vi.findViewById(R.id.textViewContactEmail);
        TextView company = (TextView) vi.findViewById(R.id.textViewContactCompany);

        final Contact contact = mContacts.get(position);
        
        name.setText(contact.getName());
        number.setText(contact.getNumber());
        email.setText(contact.getEmail());
        company.setText(contact.getCompany());
                
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
}
