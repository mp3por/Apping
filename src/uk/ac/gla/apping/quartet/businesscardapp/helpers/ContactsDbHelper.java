package uk.ac.gla.apping.quartet.businesscardapp.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDbHelper extends SQLiteOpenHelper {

	public static final String TABLE_CONTACTS = "contacts";
	public static final String TABLE_CONTACT_IMAGES = "contact_images";
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_COMPANY = "company";
	public static final String COLUMN_THUMBNAIL = "thumbnail";
	public static final String COLUMN_FRONT_IMAGE = "front_image";
	public static final String COLUMN_BACK_IMAGE = "back_image";
	
	public static String[] allContactColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_NUMBER, COLUMN_COMPANY, COLUMN_THUMBNAIL};
	public static String[] allContactImageColumns = {COLUMN_ID, COLUMN_FRONT_IMAGE, COLUMN_BACK_IMAGE};

	private static final String DATABASE_NAME = "contacts.db";
	private static final int DATABASE_VERSION = 3;

	private static final String DATABASE_CREATE_CONTACTS = "create table "
			+ TABLE_CONTACTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " 
			+ COLUMN_NAME + " character null,"
			+ COLUMN_EMAIL + " character null,"
			+ COLUMN_NUMBER + " character null,"
			+ COLUMN_COMPANY + " character null,"
			+ COLUMN_THUMBNAIL + " BLOB null"
			+ ");";

			
	private static final String DATABASE_CREATE_CONTACT_IMAGES = "create table "
			+ TABLE_CONTACT_IMAGES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_FRONT_IMAGE + " BLOB null,"
			+ COLUMN_BACK_IMAGE + " BLOB null"
			+ ");";

	public ContactsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * Creating the database
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_CONTACTS);
		database.execSQL(DATABASE_CREATE_CONTACT_IMAGES);
	}

	/*
	 * Upgrading the database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ContactsDbHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_IMAGES);
		onCreate(db);
	}
} 