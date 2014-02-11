package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import uk.ac.gla.apping.quartet.businesscardapp.data.ContactWithImages;
import uk.ac.gla.apping.quartet.businesscardapp.activities.OCR;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import CustomExceptions.OCRCreateDirError;
import CustomExceptions.OCRTestdataMissingFiles;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/*
 import com.googlecode.tesseract.android.TessBaseAPI;
 */

public class ImporterActivity extends Activity {
	private static final int CAMERA_REQUEST = 1888;
	private static final int GALLERY_REQUEST = 3;

	// OCR Variables
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/BusinessCard/";
	// public static final String lang = "eng";
	protected static final String PHOTO_TAKEN = "photo_taken";
	private static final String TAG = "BisinessCardApp.java";
	protected String _path = "/sdcard/" + "ocr.jpg";
	protected boolean _taken;
	// OCR ocr;

	private Button mButtonCamera;
	private Button mButtonGallery;
	private ImageView mImage;
	private Uri mImageCaptureUri;
	public String recogString;
	public Bitmap bm;

	public String getPath() {
		return _path;
	}

	public void setBitmap(Bitmap bitmap) {
		bm = bitmap;
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
				// OCR save test
				startCameraActivity();
				// startOCRActivity();

				// Nikis code
				// NikiStartCamera();

			}

		});

		mButtonGallery = (Button) findViewById(R.id.buttonGallery);
		mButtonGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
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

	protected void NikiStartCamera() {
		// TODO Auto-generated method stub
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// add check condition for security
		if (cameraIntent.resolveActivity(getPackageManager()) != null) {
			// create the file where the photo should go

			// uncomment for saving in the phone memory
			// if(NikiSavePic() != null){
			// cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			// Uri.fromFile(NikiSavePic()));
			// }

			// cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
			// mImageCaptureUri); ?!??!?!?!?
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("requestCode ", Integer.toString(requestCode));
		Log.i("resultCode ", Boolean.toString(resultCode == RESULT_OK));

		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_REQUEST) {

			} else if (requestCode == GALLERY_REQUEST) { // gallery returned
				// TODO: change Uri path if it is from galary.
				// picture
				// Uri selectedImage = data.getData();
				// Bitmap bitmap =
				// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
				// selectedImage);
			} else {
				// fail
			}

			Log.i("starting OCRTask :", "OK");
			OCRTask task = new OCRTask();
			task.execute();
		}

		// save in the db here
		/*
		 * Intent intent = new Intent(ImporterActivity.this,
		 * CardViewerActivity.class); intent.putExtra("id", 0); // passing the
		 * database id of the card to the CardViewerActivity activity
		 * startActivity(intent); finish(); // this activity must be terminated,
		 * so that user can't use back button to return to it
		 */
	}

	private void afterOCR() {
		Bitmap photo = bm;

		// ------------------------- TMP CODE FOR TESTING
		// -------------------------\\

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
		db.createContact(contact);

		// ------------------------- END [TMP CODE FOR TESTING]
		// -------------------------\\

		Log.e("Original   dimensions", photo.getRowBytes() * photo.getHeight()
				+ " ");
		Log.e("Compressed dimensions",
				decoded.getRowBytes() * decoded.getHeight() + " ");
	}

	private class OCRTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog = new ProgressDialog(
				ImporterActivity.this);

		/** progress dialog to show user that the backup is processing. */
		/** application context. */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Analysing ....");
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(final String... args) {
			Log.i("ImporterActivity", "async Pth: "
					+ ImporterActivity.this._path);
			OCR ocr = new OCR(ImporterActivity.this, getAssets());
			try {
				ocr.run();
				ImporterActivity.this.recogString = ocr.getRecognizedText();
				ImporterActivity.this.bm = ocr.getBitmap();
				Log.i("recognizedText", recogString);
				Log.i("Image ?", bm.toString());
			} catch (OCRTestdataMissingFiles e) {

				e.printStackTrace();
				ImporterActivity.this.recogString = null;
			} catch (OCRCreateDirError e) {

				e.printStackTrace();
				ImporterActivity.this.recogString = null;
			} catch (IOException e) {

				e.printStackTrace();
				ImporterActivity.this.recogString = null;
			}

			ocr = null;
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mImage.setImageBitmap(bm);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			ImporterActivity.this.afterOCR();
		}
	}
}