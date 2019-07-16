package com.studentshield.shield.visitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class idpic extends AppCompatActivity {
    String mCurrentPhotoPath;
    private ProgressDialog pD;
    private static File image;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idpic);
        pD = new ProgressDialog(this);
        pD.setCancelable(false);
//        CropImage.activity()
//                .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
//                .setActivityTitle("Government id")
//                .start(idpic.this);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            /*File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(AddVisitors.this, "Error occurred while creating the File", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.bennett.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            pD.setMessage("Saving ...");
            showDialog();
            //  take_pic.setText("Change Picture");
            String udata = "Edit Picture";
            SpannableString content = new SpannableString(udata);
            content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);

            /**for enable to edit the image*/
//          change.setText(content);
//          change.setClickable(true);

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            SaveImage(imageBitmap);

            /**for display of image*/
//          UImage.setScaleType(ImageView.ScaleType.FIT_XY);
//          UImage.setImageBitmap(imageBitmap);
//          UImage.setBackgroundResource(R.drawable.imageborder);
//          UImage.setPadding(2, 2, 2, 2);
//          UImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // <- set the scale
//          UImage.setCropToPadding(true); // <- requires API 16 or more

            Intent i = new Intent(idpic.this, HostDetailsActivity.class);
            startActivity(i);
            pD.dismiss();
        }
    }
    private void showDialog() {
        if (!pD.isShowing())
            pD.show();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // method to save the compressed bitmap image
    private void SaveImage(Bitmap bitmapImage) {


        File file = null;
        try {
            file = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast.makeText(idpic.this, "Error occurred while creating the File", Toast.LENGTH_SHORT).show();
        }
        if (file != null) ;
        try {
            FileOutputStream out = new FileOutputStream(file);
            int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
            scaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
