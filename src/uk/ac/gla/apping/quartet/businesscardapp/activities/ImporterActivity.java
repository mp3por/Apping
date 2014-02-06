package uk.ac.gla.apping.quartet.businesscardapp.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.gla.apping.quartet.businnesscardapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImporterActivity extends Activity {
	private static final int CAMERA_REQUEST = 1888;
	private static final int GALLERY_REQUEST = 3;
	
	
	private Button mButtonCamera;
	private Button mButtonGallery;
	private ImageView mImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_importer);
		
		//mImage = (ImageView) findViewById(R.id.mImageViewCamera);
		mButtonCamera = (Button) findViewById(R.id.buttonCamera);
		mButtonCamera.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// add check condition for security
				if(cameraIntent.resolveActivity(getPackageManager()) != null){
					// create the file where the photo should go
				/*	File photo = null;
					try{
						photo = createImageFile();
					}catch(IOException ex){
						ex.printStackTrace();
					}
					// continue only if the file was successfully created
					if(photo != null){
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
						startActivityForResult(cameraIntent, CAMERA_REQUEST);
					} */
					startActivityForResult(cameraIntent, CAMERA_REQUEST);
				}
			}
			
			String mCurrentPhotoPath;
			
			// saving the image
			private File createImageFile() throws IOException {
			    // Create an image file name
			    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			    String imageFileName = "JPEG_" + timeStamp + "_";
			    File storageDir = Environment.getExternalStoragePublicDirectory(
			            Environment.DIRECTORY_PICTURES);
			    File image = File.createTempFile(
			        imageFileName,  /* prefix */
			        ".jpg",         /* suffix */
			        storageDir      /* directory */
			    );

			    // Save a file: path for use with ACTION_VIEW intents
			    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
			    return image;
			}	
		
		});
		
		
		mButtonGallery = (Button) findViewById(R.id.buttonGallery);
		mButtonGallery.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {			
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(intent, GALLERY_REQUEST);
			}	
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) { // camera returned picture
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			 File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
			 mImage = (ImageView) findViewById(R.id.mImageViewCamera);
			 mImage.setImageBitmap(photo);
			// mImage.setImageBitmap(decodeSampledBitmapFromFile(file.getAbsolutePath(), 50, 50));
		} else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) { // gallery returned picture
			// Uri selectedImage = data.getData();
	        // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
		} else {
			// fail
		}
		
		
		// do OCR stuff here
		
		// save in the db here
		
		//Intent intent = new Intent(ImporterActivity.this, CardViewerActivity.class);
		//intent.putExtra("id", 0); // passing the database id of the card to the CardViewerActivity activity  
		//startActivity(intent);
		//finish(); // this activity must be terminated, so that user can't use back button to return to it
	}
	
	
}
