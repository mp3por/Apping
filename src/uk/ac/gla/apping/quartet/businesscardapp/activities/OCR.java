package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import uk.ac.gla.apping.quartet.businesscardapp.CustomExceptions.OCRCreateDirError;
import uk.ac.gla.apping.quartet.businesscardapp.CustomExceptions.OCRTestdataMissingFiles;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

//import com.googlecode.tesseract.android.TessBaseAPI;

public class OCR {
/*
	//Path settings
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/BusinessCard/";
	public static final String lang = "eng";
	private static final String TAG = "BusinessCard.java";
	protected String _path = DATA_PATH + "/ocr.jpg";
	
	//Booleans
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";
	private boolean pathSet = false;
	
	//Fields
	private AssetManager assetManager;
	private ImporterActivity mActivity;
	private String recognizedText;
	private Bitmap bitmap;

	//Constructors
	public OCR(ImporterActivity activity, AssetManager assetManager) {
		// TODO Auto-generated constructor stub
		mActivity = activity;
		this._path = mActivity.getPath();
		this.assetManager = assetManager;
	}
	
	//Main run method - this has to be called after the 
	public void run() throws OCRTestdataMissingFiles, OCRCreateDirError, IOException {
		setPath(_path, assetManager);
		if (pathSet == false) {
			Log.e(TAG, "Path was not set");
			return ;
		}
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		bitmap = BitmapFactory.decodeFile(_path, options);

		//try {
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

			Log.v(TAG, "Rotation: " + rotate);

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

		//} catch (IOException e) {
		//	Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		//}

		// _image.setImageBitmap( bitmap );

		Log.v(TAG, "Before baseApi");
		
		//TODO: change bitmap brithness, but return the original

		recognizedText = runTess(bitmap);
		Log.v(TAG, "OCRED TEXT: " + recognizedText);

		if (lang.equalsIgnoreCase("eng")) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}

		recognizedText = recognizedText.trim();
		//return recognizedText;
		// Cycle done.
	}
	
	//Getter for the recognized text
	public String getRecognizedText() {
		return recognizedText;
	}
	
	//Calling the Tess API
	private String runTess(Bitmap bitmap) {
		// TODO Auto-generated method stub
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		Log.i("ocr.java",DATA_PATH);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);

		String recognizedText = baseApi.getUTF8Text();

		baseApi.end();
		return recognizedText;
	}

	
	public Bitmap getBitmap() {
		return bitmap;
	}

	//sets the pats and checks for the needed files
	private void setPath(String _path,AssetManager assetManager) throws OCRTestdataMissingFiles,OCRCreateDirError{
		pathSet = true;
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path
							+ " on sdcard failed");
					throw new OCRCreateDirError();
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		
		// this is to check if the training data is available since the TessApi can not work without them
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				//AssetManager assetManager = getAssets();
				Log.i("OCR.java setPath checking .traineddata in path", _path + " ; data_path: " + DATA_PATH );
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				Log.i("inputstream empty ?", Integer.toString(in.available()));
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/" + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
				throw new OCRTestdataMissingFiles();
				
			}
		}
		pathSet = true;
		this._path = _path;
	}

	*/
}

