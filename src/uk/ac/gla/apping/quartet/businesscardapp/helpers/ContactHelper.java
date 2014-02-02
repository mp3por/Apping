package uk.ac.gla.apping.quartet.businesscardapp.helpers;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gla.apping.quartet.businesscardapp.data.Contact;
import uk.ac.gla.apping.quartet.businesscardapp.data.ContactWithImages;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


public class ContactHelper {
	private static ContactHelper instance;
	private ContactsDbHelper dbHelper;

	public static ContactHelper getInstance(Context context) {
		if (instance == null) {
			instance = new ContactHelper(context);
		}
		return instance;
	}
	
	public ContactHelper(Context context) {
		dbHelper = new ContactsDbHelper(context);
	}

	public ContactWithImages createContact(ContactWithImages contact) {
		ContentValues values = new ContentValues();

		values.put(ContactsDbHelper.COLUMN_NAME, contact.getName());
		values.put(ContactsDbHelper.COLUMN_EMAIL, contact.getName());
		values.put(ContactsDbHelper.COLUMN_NUMBER, contact.getNumber());
		values.put(ContactsDbHelper.COLUMN_COMPANY, contact.getName());
		values.put(ContactsDbHelper.COLUMN_THUMBNAIL, contact.getThumbnail());
		values.put(ContactsDbHelper.COLUMN_FRONT_IMAGE, contact.getFrontImage());
		values.put(ContactsDbHelper.COLUMN_BACK_IMAGE, contact.getBackImage());
		
		long insertId = -1;
		try {
			insertId = dbHelper.getWritableDatabase().insertOrThrow(ContactsDbHelper.TABLE_CONTACTS, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Cursor cursor = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allColumns, ContactsDbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

		cursor.moveToFirst();
		ContactWithImages c = cursorToContactWithImages(cursor);
		cursor.close();
		return c;
	}

	public void updateContact(Contact contact) { // ContactWithImages
		ContentValues values = new ContentValues();
		values.put(ContactsDbHelper.COLUMN_NAME, contact.getName());
		values.put(ContactsDbHelper.COLUMN_EMAIL, contact.getName());
		values.put(ContactsDbHelper.COLUMN_NUMBER, contact.getNumber());
		values.put(ContactsDbHelper.COLUMN_COMPANY, contact.getName());
		// values.put(ContactsDbHelper.COLUMN_THUMBNAIL, contact.getThumbnail());
		// values.put(ContactsDbHelper.COLUMN_FRONT_IMAGE, contact.getFrontImage());
		// values.put(ContactsDbHelper.COLUMN_BACK_IMAGE, contact.getBackImage());

		dbHelper.getWritableDatabase().update(ContactsDbHelper.TABLE_CONTACTS, values, "id = ?", new String[]{Integer.toString(contact.getId())});
	}

	public void deleteContact(Contact contact) {
		int id = contact.getId();
		Log.i("Del", "Contact deleted with id: " + id);
		dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACTS, ContactsDbHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public void deleteContact(int id) {
		Log.i("Del", "Contact deleted with id: " + id);
		dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACTS, ContactsDbHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Contact> getAllContacts() {
		List<Contact> contacts = new ArrayList<Contact>();

		Cursor cursor = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contact contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}

		cursor.close();
		return contacts;
	}

	
	public Contact getContactById(int id) {
		Cursor cursor = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allColumns, ContactsDbHelper.COLUMN_ID + "=" + id, null, null, null, null);

		cursor.moveToFirst();
		Contact contact = cursorToContact(cursor);

		cursor.close();
		return contact;
	}
	
	public ContactWithImages getContactWithImagesById(int id) {
		Cursor cursor = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allColumns, ContactsDbHelper.COLUMN_ID + "=" + id, null, null, null, null);

		cursor.moveToFirst();
		ContactWithImages contact = cursorToContactWithImages(cursor);

		cursor.close();
		return contact;
	}

	public int getContactCount(){
		String sql = "SELECT COUNT(*) AS contact_count FROM " + ContactsDbHelper.TABLE_CONTACTS; 
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	/*
	 * Converting from cursor to Contact
	 */
	private Contact cursorToContact(Cursor cursor) {
		Contact contact = new Contact();
		
		contact.setId(cursor.getInt(cursor.getColumnIndex(ContactsDbHelper.COLUMN_ID)));
		contact.setName(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_NAME)));
		contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_EMAIL)));
		contact.setNumber(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_NUMBER)));
		contact.setCompany(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_COMPANY)));
		
		contact.setThumbnail(cursor.getBlob(cursor.getColumnIndex(ContactsDbHelper.COLUMN_THUMBNAIL)));
		
		return contact;
	}
	
	/*
	 * Converting from cursor to ContactWithImages
	 */
	private ContactWithImages cursorToContactWithImages(Cursor cursor) {
		ContactWithImages contact = new ContactWithImages();
		contact.setId(cursor.getInt(cursor.getColumnIndex(ContactsDbHelper.COLUMN_ID)));
		
		contact.setName(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_NAME)));
		contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_EMAIL)));
		contact.setNumber(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_NUMBER)));
		contact.setCompany(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_COMPANY)));
		
		contact.setThumbnail(cursor.getBlob(cursor.getColumnIndex(ContactsDbHelper.COLUMN_THUMBNAIL)));
		contact.setFrontImage(cursor.getBlob(cursor.getColumnIndex(ContactsDbHelper.COLUMN_FRONT_IMAGE)));
		contact.setBackImage(cursor.getBlob(cursor.getColumnIndex(ContactsDbHelper.COLUMN_BACK_IMAGE)));
		
		return contact;
	}
}


