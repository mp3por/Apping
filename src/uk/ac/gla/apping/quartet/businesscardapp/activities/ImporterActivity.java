package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.googlecode.tesseract.android.TessBaseAPI;

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

import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImporterActivity extends Activity {
	private static final int CAMERA_REQUEST = 1888;
	private static final int GALLERY_REQUEST = 3;

	// OCR Variables
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
	public static final String lang = "eng";
	protected static final String PHOTO_TAKEN = "photo_taken";
	private static final String TAG = "BisinessCardApp.java";
	protected String _path;
	protected boolean _taken;

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
				OCRSaveTest();
				ViliStartCameraActivity();

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

	protected void ViliStartCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
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

	protected void OCRSaveTest() {
		// TODO Auto-generated method stub
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path
							+ " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata"))
				.exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang
						+ ".traineddata");
				// GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/"
						+ lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				// while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				// gin.close();
				out.close();

				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG,
						"Was unable to copy " + lang + " traineddata "
								+ e.toString());
			}
		}
		_path = DATA_PATH + "/ocr.jpg";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		String ocrText;
		// OCR
		if (resultCode == -1) {
			cameraGalleryResult(requestCode, resultCode, data);
			ocrText = onPhotoTaken();
		} else {
			Log.v(TAG, "User cancelled");
		}

		// save in the db here

		
		// Intent intent = new Intent(ImporterActivity.this,
		// CardViewerActivity.class);
		// intent.putExtra("id", 0); // passing the database id of the card to
		// the CardViewerActivity activity
		// startActivity(intent);
		// finish(); // this activity must be terminated, so that user can't use
		// back button to return to it
	}

	private void cameraGalleryResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");

			//Bitmap scaledBitmap = scaleBitmap(photo);
			
			//String pathToImage = mImageCaptureUri.getPath();
			
			//scaling
			int targetW = 480;
			int targetH = 320;
			//Bitmap b = decodeSampledBitmap(pathToImage, targetW, targetH);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 70, out);
			Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
			
			Log.e("Original   dimensions", photo.getRowBytes()*photo.getHeight() + " ");
			Log.e("Compressed dimensions", decoded.getRowBytes()*decoded.getHeight()+" ");
		    
			mImage.setImageBitmap(decoded);
		} else if (requestCode == GALLERY_REQUEST
				&& resultCode == Activity.RESULT_OK) { // gallery returned
			// picture
		
			
			File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
			 mImage = (ImageView) findViewById(R.id.mImageViewCamera);
			 mImage.setImageBitmap(photo);
			// mImage.setImageBitmap(decodeSampledBitmapFromFile(file.getAbsolutePath(), 50, 50));
		} else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) { // gallery returned picture

			// Uri selectedImage = data.getData();
			// Bitmap bitmap =
			// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
			// selectedImage);
		} else {
			// fail
		}
	}
	
	//scaling the bitmap (BAD QUALITY)
	private Bitmap scaleBitmap(Bitmap photo){
		final int maxSize = 960;
		int outWidth;
		int outHeight;
		int inWidth = photo.getWidth();
		int inHeight = photo.getHeight();
		if(inWidth > inHeight){
		    outWidth = maxSize;
		    outHeight = (inHeight * maxSize) / inWidth; 
		} else {
		    outHeight = maxSize;
		    outWidth = (inWidth * maxSize) / inHeight; 
		}

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(photo, outWidth, outHeight, false);
		return resizedBitmap;
	}
	
	// calculating the sample size
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	//doesnt Work for now
	public static Bitmap decodeSampledBitmap(String path, int reqWidth, int reqHeight){
		// First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(path, options);
	    
	}
	
	
	
	
	
	protected String onPhotoTaken() {
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		//Set Proper Orientation of the photo
		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}

		// _image.setImageBitmap( bitmap );
		
		Log.v(TAG, "Before baseApi");

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		
		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		return recognizedText;
		// Cycle done.
	}
	
}