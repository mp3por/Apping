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
		
		ContentValues values2 = new ContentValues();
		values2.put(ContactsDbHelper.COLUMN_THUMBNAIL, contact.getThumbnail());
		
		ContentValues values3 = new ContentValues();
		values3.put(ContactsDbHelper.COLUMN_FRONT_IMAGE, contact.getFrontImage());
		values3.put(ContactsDbHelper.COLUMN_BACK_IMAGE, contact.getBackImage());
		
		long insertId = -1, insertId2 = -1, insertId3 = -1;
		try {
			dbHelper.getWritableDatabase().beginTransaction();
			insertId = dbHelper.getWritableDatabase().insertOrThrow(ContactsDbHelper.TABLE_CONTACTS, null, values);
			insertId2 = dbHelper.getWritableDatabase().insertOrThrow(ContactsDbHelper.TABLE_CONTACT_THUMBNAILS, null, values2);
			insertId3 = dbHelper.getWritableDatabase().insertOrThrow(ContactsDbHelper.TABLE_CONTACT_IMAGES, null, values3);
			
			if ((insertId != insertId2) || (insertId != insertId3)) {
				Log.e("ContactHelper", "Table insert ids don't match!");
				dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACTS, ContactsDbHelper.COLUMN_ID
						+ " = " + insertId, null);
				dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACT_THUMBNAILS, ContactsDbHelper.COLUMN_ID
						+ " = " + insertId2, null);
				dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACT_IMAGES, ContactsDbHelper.COLUMN_ID
						+ " = " + insertId3, null);
			}
			
			dbHelper.getWritableDatabase().setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbHelper.getWritableDatabase().endTransaction();
		}
		
		return getContactWithImagesById((int) insertId);
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
		int affectedRows2 = dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACT_THUMBNAILS, ContactsDbHelper.COLUMN_ID
				+ " = " + id, null);
		int affectedRows3 = dbHelper.getWritableDatabase().delete(ContactsDbHelper.TABLE_CONTACT_IMAGES, ContactsDbHelper.COLUMN_ID
				+ " = " + id, null);
		
		if (affectedRows == affectedRows2 || affectedRows == affectedRows3) {
			dbHelper.getWritableDatabase().setTransactionSuccessful();
		}
		
		dbHelper.getWritableDatabase().endTransaction();
	}

	public List<Contact> getAllContacts() {
		// allocating the right arraylist size
		List<Contact> contacts = new ArrayList<Contact>(getContactCount() + 2);

		String selectQuery = "SELECT "
			+ " contact_data." + ContactsDbHelper.COLUMN_ID + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_NAME + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_EMAIL + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_NUMBER + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_COMPANY + ", "
			+ " contact_thumbnail." + ContactsDbHelper.COLUMN_THUMBNAIL
			+ " FROM "+ ContactsDbHelper.TABLE_CONTACTS + " AS contact_data"
			+ " INNER JOIN " + ContactsDbHelper.TABLE_CONTACT_THUMBNAILS + " AS contact_thumbnail"
			+ " ON contact_data." + ContactsDbHelper.COLUMN_ID + "=" + "contact_thumbnail." + ContactsDbHelper.COLUMN_ID;

		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(selectQuery, null);
		
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
		String selectQuery = "SELECT "
			+ " contact_data." + ContactsDbHelper.COLUMN_ID + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_NAME + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_EMAIL + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_NUMBER + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_COMPANY + ", "
			+ " contact_thumbnail." + ContactsDbHelper.COLUMN_THUMBNAIL
			+ " FROM "+ ContactsDbHelper.TABLE_CONTACTS + " AS contact_data"
			+ " INNER JOIN " + ContactsDbHelper.TABLE_CONTACT_THUMBNAILS + " AS contact_thumbnail"
			+ " ON contact_data." + ContactsDbHelper.COLUMN_ID + "=" + "contact_thumbnail." + ContactsDbHelper.COLUMN_ID
			+ " WHERE contact_data." + ContactsDbHelper.COLUMN_ID + "=" + id;
			
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(selectQuery, null);

		cursor.moveToFirst();
		Contact contact = cursorToContact(cursor);

		cursor.close();
		return contact;
	}
	
	public ContactWithImages getContactWithImagesById(int id) {
		
		String selectQuery = "SELECT "
			+ " contact_data." + ContactsDbHelper.COLUMN_ID + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_NAME + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_EMAIL + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_NUMBER + ", "
			+ " contact_data." + ContactsDbHelper.COLUMN_COMPANY + ", "
			+ " contact_thumbnail." + ContactsDbHelper.COLUMN_THUMBNAIL + ", "
			+ " contact_images." + ContactsDbHelper.COLUMN_FRONT_IMAGE + ", "
			+ " contact_images." + ContactsDbHelper.COLUMN_BACK_IMAGE
			+ " FROM "+ ContactsDbHelper.TABLE_CONTACTS + " AS contact_data"
			+ " INNER JOIN " + ContactsDbHelper.TABLE_CONTACT_THUMBNAILS + " AS contact_thumbnail"
			+ " ON contact_data." + ContactsDbHelper.COLUMN_ID + "=" + "contact_thumbnail." + ContactsDbHelper.COLUMN_ID
			+ " INNER JOIN " + ContactsDbHelper.TABLE_CONTACT_IMAGES + " AS contact_images"
			+ " ON contact_data." + ContactsDbHelper.COLUMN_ID + "=" + "contact_images." + ContactsDbHelper.COLUMN_ID
			+ " WHERE contact_data." + ContactsDbHelper.COLUMN_ID + "=" + id;
			
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(selectQuery, null);
		cursor.moveToFirst();
		
		ContactWithImages contact = cursorToContactWithImages(cursor);
		
		cursor.close();
			
		return contact;
	}

	public int getContactCount() {
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