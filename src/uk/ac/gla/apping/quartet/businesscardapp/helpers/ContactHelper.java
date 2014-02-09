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
		values.put(ContactsDbHelper.COLUMN_EMAIL, contact.getEmail());
		values.put(ContactsDbHelper.COLUMN_NUMBER, contact.getNumber());
		values.put(ContactsDbHelper.COLUMN_COMPANY, contact.getCompany());
		values.put(ContactsDbHelper.COLUMN_THUMBNAIL, contact.getThumbnail());
		
		ContentValues values2 = new ContentValues();
		values2.put(ContactsDbHelper.COLUMN_FRONT_IMAGE, contact.getFrontImage());
		values2.put(ContactsDbHelper.COLUMN_BACK_IMAGE, contact.getBackImage());
		
		long insertId = -1, insertId2 = -1;
		try {
			dbHelper.getWritableDatabase().beginTransaction();
			insertId = dbHelper.getWritableDatabase().insertOrThrow(ContactsDbHelper.TABLE_CONTACTS, null, values);
			insertId2 = dbHelper.getWritableDatabase().insertOrThrow(ContactsDbHelper.TABLE_CONTACT_IMAGES, null, values2);
			
			if (insertId != insertId2) {
				Log.e("ContactHelper", "Table insert ids don't match!");
				dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACTS, ContactsDbHelper.COLUMN_ID
						+ " = " + insertId, null);
				dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACT_IMAGES, ContactsDbHelper.COLUMN_ID
						+ " = " + insertId2, null);	
			}
			
			dbHelper.getWritableDatabase().setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbHelper.getWritableDatabase().endTransaction();
		}

		Cursor cursor = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allContactColumns, ContactsDbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		
		Cursor cursor2 = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACT_IMAGES,
				ContactsDbHelper.allContactImageColumns, ContactsDbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor2.moveToFirst();
		
		ContactWithImages contactWithImages = cursorToContactWithImages(cursor, cursor2);
		cursor.close();
		cursor2.close();
		
		return contactWithImages;
	}

	public void updateContact(Contact contact) { // ContactWithImages
		ContentValues values = new ContentValues();
		values.put(ContactsDbHelper.COLUMN_NAME, contact.getName());
		values.put(ContactsDbHelper.COLUMN_EMAIL, contact.getEmail());
		values.put(ContactsDbHelper.COLUMN_NUMBER, contact.getNumber());
		values.put(ContactsDbHelper.COLUMN_COMPANY, contact.getCompany());

		dbHelper.getWritableDatabase().update(ContactsDbHelper.TABLE_CONTACTS, values, "id = ?", new String[]{Integer.toString(contact.getId())});
	}

	public void deleteContact(Contact contact) {
		deleteContact(contact.getId());
	}

	public void deleteContact(int id) {
		Log.i("Del", "Contact deleted with id: " + id);
		dbHelper.getWritableDatabase().beginTransaction();
		
		int affectedRows = dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACTS, ContactsDbHelper.COLUMN_ID
				+ " = " + id, null);
		int affectedRows2 = dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACT_IMAGES, ContactsDbHelper.COLUMN_ID
				+ " = " + id, null);
		
		if (affectedRows == affectedRows2) {
			dbHelper.getWritableDatabase().setTransactionSuccessful();
		}
		
		dbHelper.getWritableDatabase().endTransaction();
	}

	public List<Contact> getAllContacts() {
		// allocating the right arraylist size
		List<Contact> contacts = new ArrayList<Contact>(getContactCount() + 2);

		Cursor cursor = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allContactColumns, null, null, null, null, null);

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
				ContactsDbHelper.allContactColumns, ContactsDbHelper.COLUMN_ID + "=" + id, null, null, null, null);

		cursor.moveToFirst();
		Contact contact = cursorToContact(cursor);

		cursor.close();
		return contact;
	}
	
	public ContactWithImages getContactWithImagesById(int id) {
		Cursor cursor = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allContactColumns, ContactsDbHelper.COLUMN_ID + "=" + id, null, null, null, null);

		cursor.moveToFirst();
		
		Cursor cursor2 = dbHelper.getReadableDatabase().query(ContactsDbHelper.TABLE_CONTACTS,
				ContactsDbHelper.allContactImageColumns, ContactsDbHelper.COLUMN_ID + "=" + id, null, null, null, null);
		
		cursor.moveToFirst();
		
		ContactWithImages contact = cursorToContactWithImages(cursor, cursor2);

		cursor.close();
		cursor2.close();
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
	private ContactWithImages cursorToContactWithImages(Cursor cursor, Cursor cursor2) {
		ContactWithImages contact = new ContactWithImages();
		contact.setId(cursor.getInt(cursor.getColumnIndex(ContactsDbHelper.COLUMN_ID)));
		
		contact.setName(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_NAME)));
		contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_EMAIL)));
		contact.setNumber(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_NUMBER)));
		contact.setCompany(cursor.getString(cursor.getColumnIndex(ContactsDbHelper.COLUMN_COMPANY)));
		contact.setThumbnail(cursor.getBlob(cursor.getColumnIndex(ContactsDbHelper.COLUMN_THUMBNAIL)));
		
		contact.setFrontImage(cursor2.getBlob(cursor2.getColumnIndex(ContactsDbHelper.COLUMN_FRONT_IMAGE)));
		contact.setBackImage(cursor2.getBlob(cursor2.getColumnIndex(ContactsDbHelper.COLUMN_BACK_IMAGE)));
		
		return contact;
	}
}


