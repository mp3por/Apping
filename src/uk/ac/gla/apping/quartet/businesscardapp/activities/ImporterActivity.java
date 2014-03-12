package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import uk.ac.gla.apping.quartet.businesscardapp.CustomExceptions.FileNotDeletedException;
import uk.ac.gla.apping.quartet.businesscardapp.data.ContactWithImages;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/*
 import com.googlecode.tesseract.android.TessBaseAPI;
 */

// lets do some work, sorry for not working so many days

public class ImporterActivity extends Activity {
	private static final int CAMERA_REQUEST = 0;
	private static final int GALLERY_REQUEST = 3;
	private static final String FILE_NAME = "ocr.jpg";
	private static final String DIRECTORY = "/sdcard/";
	
	
	// OCR Variables
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/BusinessCard/";
	protected static final String PHOTO_TAKEN = "photo_taken";
	private static final String TAG = "BisinessCardApp.java";
	protected String _path = DIRECTORY + FILE_NAME;
	protected boolean _taken;
	// OCR ocr;
	
	private Intent mSenderIntent = null;

	private Button mButtonCamera;
	private Button mButtonGallery;
	private ImageView mImage;
	private Uri mImageCaptureUri;
	public String recogString;
	public Bitmap mBitmap;
	public Bitmap bmpGallery;

	public String getPath() {
		return _path;
	}

	public void setBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.i("ImporterActivity", "oncreate Pth: " + _path);

		setContentView(R.layout.activity_importer);

		mImage = (ImageView) findViewById(R.id.mImageViewCamera);
		mButtonCamera = (Button) findViewById(R.id.buttonCamera);
		mButtonCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startCameraActivity();
			}
		});

		mButtonGallery = (Button) findViewById(R.id.buttonGallery);
		mButtonGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			//	Intent intent = new Intent(Intent.ACTION_PICK, Media.INTERNAL_CONTENT_URI);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
				startActivityForResult(intent, GALLERY_REQUEST);
			}
		});
	}

	protected void startCameraActivity() {
		Log.i("startCameraActivity path", _path);
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		System.out.println("Intent made = true");
		if (intent.resolveActivity(getPackageManager()) != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			System.out.println("StartActivityForResult");
			startActivityForResult(intent, CAMERA_REQUEST);

		}
	}

	protected File NikiSavePic() {
		File photo = null;
		try {
			photo = createImageFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG, "ERROR: Saving image on sdcard failed");
		}
		return photo;
	}

	private File createImageFile() throws IOException {
		String mCurrentPhotoPath;
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		mSenderIntent = intent;
		
		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_REQUEST) {

			} else if (requestCode == GALLERY_REQUEST) { // gallery returned
				// TODO: change Uri path if it is from galary.
				// picture
			Uri selectedImage = mSenderIntent.getData();
				
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
				
                if(bmpGallery != null && !bmpGallery.isRecycled())
                {
                    bmpGallery = null;                
                }
                         
                bmpGallery = BitmapFactory.decodeFile(filePath);
                
               
                mImage.setImageBitmap(bmpGallery);
                //picturePath = getPath(selectedImage);
				// Bitmap bitmap =
				// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
				// selectedImage);
			
			} else {
				// fail
				 Log.d("Status:", "Photopicker canceled"); 
			}

			Log.i("starting OCRTask :", "OK");
			OCRTask task = new OCRTask();
			task.execute();
		}

		
	}
	
    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
            // just some safety built in 
            if( uri == null ) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            // this is our fallback here
            return uri.getPath();
    }



	private void afterOCR() {
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap photo = BitmapFactory.decodeFile(_path, options);

		deleteImageFile();
		
		// -------------------------  TMP CODE FOR TESTING  -------------------------\\

		// aspect ratio? never heard of it...
		photo = Bitmap.createScaledBitmap(photo, 200, 200, true);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		// png takes much more space then jpg
		// photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
		photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);
		byte[] byteThumbnail = stream.toByteArray();

		// add to image view
		Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(
				stream.toByteArray()));
		mImage.setImageBitmap(decoded);

		ContactWithImages contact = new ContactWithImages();
		contact.setName("Thissurname" + (new Random().nextInt(1000)));
		contact.setEmail("test@test.com");
		contact.setCompany("RIP APPING");
		contact.setNumber("+44711111");
		contact.setThumbnail(byteThumbnail);
		ContactHelper db = ContactHelper.getInstance(this);
		contact = db.createContact(contact);

		// ------------------------- END [TMP CODE FOR TESTING] -------------------------\\

		Log.e("Original   dimensions", photo.getRowBytes() * photo.getHeight()
				+ " ");
		Log.e("Compressed dimensions",
				decoded.getRowBytes() * decoded.getHeight() + " ");
		
		
		
		Intent intent = new Intent(ImporterActivity.this, CardViewerActivity.class); 
		intent.putExtra("id", contact.getId());
		intent.putExtra("New", true);
		
		startActivity(intent); 
		finish(); 
	}

	
	private void deleteImageFile() {
		try {
			File file = new File(DIRECTORY, FILE_NAME); 
			if (!file.delete()) {
				if(!this.deleteFile(FILE_NAME)) {
					throw new FileNotDeletedException("Could not delete file!");
				}
			}
		} catch (Exception e) {
			Log.e("BusinessCardApp", "Could not delete file: "+ e.getMessage());
		}
	}
	
	
	private class OCRTask extends AsyncTask<String, Void, Boolean> {
		private ImporterActivity mActivity = ImporterActivity.this;
		private ProgressDialog dialog = new ProgressDialog(mActivity);

		/** progress dialog to show user that the backup is processing. */
		/** application context. */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Analysing ....");
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(final String... args) {
			/*
			Log.i("ImporterActivity", "async Pth: "
					+ mActivity._path);
			OCR ocr = new OCR(mActivity, getAssets());
			try {
				ocr.run();
				mActivity.recogString = ocr.getRecognizedText();
				mActivity.mBitmap = ocr.getBitmap();
				Log.i("recognizedText", recogString);
				Log.i("Image ?", mBitmap.toString());
			} catch (OCRTestdataMissingFiles e) {

				e.printStackTrace();
				ImActivity.recogString = null;
			} catch (OCRCreateDirError e) {

				e.printStackTrace();
				mActivity.recogString = null;
			} catch (IOException e) {

				e.printStackTrace();
				mActivity.recogString = null;
			}

			ocr = null;
			*/
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mImage.setImageBitmap(mBitmap);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			mActivity.afterOCR();
		}
	}
}