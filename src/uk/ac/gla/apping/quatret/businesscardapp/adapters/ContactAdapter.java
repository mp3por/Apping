package uk.ac.gla.apping.quatret.businesscardapp.adapters;

import java.util.ArrayList;

import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 
        TextView title = (TextView) vi.findViewById(R.id.text); // title
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.image); // artist name

        
        Contact contact = mContacts.get(position);
        
        title.setText(contact.getName());
        
        thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(contact.getThumbnail(), 0, contact.getThumbnail().length));
        
        thumbnail.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				CharSequence text = "Search will be implemented later";
	        	Toast toast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
	        	toast.show();
			}});
 
        // Setting all values in listview
        /*title.setText(song.get(CustomizedListView.KEY_TITLE));
        artist.setText(song.get(CustomizedListView.KEY_ARTIST));
        duration.setText(song.get(CustomizedListView.KEY_DURATION));
        imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        */
        return vi;
    }
}
