package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


import uk.ac.gla.apping.quartet.businesscardapp.data.ContactWithImages;
import uk.ac.gla.apping.quartet.businesscardapp.helpers.ContactHelper;
import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.style.SuperscriptSpan;
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
			.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
	//public static final String lang = "eng";
	protected static final String PHOTO_TAKEN = "photo_taken";
	private static final String TAG = "BisinessCardApp.java";
	protected String _path =  DATA_PATH + "/ocr.jpg";
	protected boolean _taken;
	OCR ocr;

	private Button mButtonCamera;
	private Button mButtonGallery;
	private ImageView mImage;
	private Uri mImageCaptureUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_importer);

		mImage = (ImageView) findViewById(R.id.mImageViewCamera);
		mButtonCamera = (Button) findViewById(R.id.buttonCamera);
		mButtonCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// OCR save test
			    startCameraActivity();

				// Nikis code
				NikiStartCamera();

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
		
		ocr = new OCR();
		ocr.setPath(_path,getAssets());
		
		
		File file = new File(_path);
		
		Uri outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		System.out.println("Intent made = true");
		if (intent.resolveActivity(getPackageManager()) != null){
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			System.out.println("StartActivityForResult");
			startActivityForResult(intent, 0);

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
			//if(NikiSavePic() != null){
			//	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(NikiSavePic()));
			//}
			
			//	cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri); ?!??!?!?!?
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

		String ocrText;
		// OCR
		if (resultCode == -1) {
			System.out.println("Activity return -1");
			cameraGalleryResult(requestCode, resultCode, data);
			ocrText = ocr.run();
			System.out.println("OCRText: "+ocrText);
		} else {
			Log.v(TAG, "User cancelled");
		}

		// save in the db here


		/*Intent intent = new Intent(ImporterActivity.this,
		CardViewerActivity.class);
		intent.putExtra("id", 0); // passing the database id of the card to the CardViewerActivity activity
		startActivity(intent);
		finish(); // this activity must be terminated, so that user can't use back button to return to it
		*/
	}

	private void cameraGalleryResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");

			
			
//------------------------- TMP CODE FOR TESTING -------------------------\\
			
			// aspect ratio? never heard of it... 
			photo = Bitmap.createScaledBitmap(photo, 200, 200, true);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			
			
			//png takes much more space then jpg
			//photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
			photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);
			byte[] byteThumbnail = stream.toByteArray();
			
			//add to image view
			Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()));
			mImage.setImageBitmap(decoded);

			ContactWithImages contact = new ContactWithImages();
			contact.setName("Thissurname"+ (new Random().nextInt(1000)));
			contact.setEmail("test@test.com");
			contact.setCompany("RIP APPING");
			contact.setNumber("+44711111");
			contact.setThumbnail(byteThumbnail);
			ContactHelper db = ContactHelper.getInstance(this);
			db.createContact(contact);
			
//------------------------- END [TMP CODE FOR TESTING] -------------------------\\			


			Log.e("Original   dimensions", photo.getRowBytes()*photo.getHeight() + " ");
			Log.e("Compressed dimensions", decoded.getRowBytes()*decoded.getHeight()+" ");

		} else if (requestCode == GALLERY_REQUEST
				&& resultCode == Activity.RESULT_OK) { // gallery returned
			// picture
			// Uri selectedImage = data.getData();
			// Bitmap bitmap =
			// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
			// selectedImage);
		} else {
			// fail
		}
	}

}