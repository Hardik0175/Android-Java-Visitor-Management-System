package com.studentshield.shield.visitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.view.ViewGroup.LayoutParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;



public class VisitorDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String temp;
    private ProgressDialog pD;

    dbHandler dbHandlerOTP;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView heading;


    private String name, emailid, address;

    private static EditText visname,visiemail,visiaddr;

    private FancyButton next;
    private FancyButton back;

    dbHandler dbHandlerid;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static String phonenumber;
    private static File image;
    String mCurrentPhotoPath;
    Spinner Purpose;

    EditText others;

    String designation;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_details);
         Purpose = (Spinner) findViewById(R.id.Purpose_view);

        dbHandlerOTP = new dbHandler(getApplicationContext(), null, null, 9);

        others=(EditText) findViewById(R.id.others);

        dbHandlerid = new dbHandler(getApplicationContext(), null, null, 9);

        Purpose.setOnItemSelectedListener(this);
        pD = new ProgressDialog(this);
        pD.setCancelable(false);
      //  heading = (TextView) findViewById(R.id.textView2);

        final Intent intent = getIntent();
        phonenumber=intent.getExtras().getString("phoneno");

       // Toast.makeText(getApplicationContext(),phonenumber,Toast.LENGTH_LONG).show();

        visname=(EditText) findViewById(R.id.visitor_name);
        visiemail=(EditText) findViewById(R.id.visitor_email);
        visiaddr=(EditText) findViewById(R.id.visitor_coming_from);

        back=(FancyButton) findViewById(R.id.verify);
        Typeface myface = Typeface.createFromAsset(getAssets(), "lato.light.ttf");
       // heading.setTypeface(myface);

        next = (FancyButton) findViewById(R.id.nexttocamera);

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_view) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.spinnerTarget)).setText("");
                    ((TextView) v.findViewById(R.id.spinnerTarget)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // Last item is not displayed here
            }

        };
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        String desg=dbHandlerOTP.getstringdesi();
        String[] desgeach=desg.split("#");
        for(int i=0;i<desgeach.length;i++)
        {
            adapter.add(desgeach[i]);

        }

        adapter.add("Other");
        adapter.add("Choose one"); // Hint for the spinner
         back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

        Purpose.setAdapter(adapter);
        Purpose.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.
        Purpose.setOnItemSelectedListener(this);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pD.show();
                name=visname.getText().toString();

                emailid=visiemail.getText().toString();
                address=visiaddr.getText().toString();
                //takePicture(view);
                if (CheckFieldValidation()) {

                    pD.dismiss();

                    if(MainActivity.take_photo_signup==1) {


                        Intent i = new Intent(VisitorDetailsActivity.this, photoactivity.class);
                        i.putExtra("phoneno", phonenumber);
                        i.putExtra("name", name);

                        if (emailid.equals("")) {
                            i.putExtra("email", "0");
                        } else {
                            i.putExtra("email", emailid);
                        }
                        i.putExtra("addr", address);
                        String designation;
                        if (temp.equals("Other")) {
                            designation = others.getText().toString();
                        } else {
                            designation = temp;
                        }
                        i.putExtra("designation", designation);
                        //Toast.makeText(getApplicationContext(),designation,Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }

                    else if(MainActivity.kyc==1)
                    {
                        Intent i = new Intent(VisitorDetailsActivity.this, idphotoactivity.class);
                        i.putExtra("phoneno", phonenumber);
                        i.putExtra("name", name);

                        if (emailid.equals("")) {
                            i.putExtra("email", "0");
                        } else {
                            i.putExtra("email", emailid);
                        }
                        i.putExtra("addr", address);
                        // designation;
                        if (temp.equals("Other")) {
                            designation = others.getText().toString();
                        } else {
                            designation = temp;
                        }
                        i.putExtra("designation", designation);
                        i.putExtra("image","null");
                        //Toast.makeText(getApplicationContext(),designation,Toast.LENGTH_LONG).show();
                        startActivity(i);

                    }

                    else
                    {
                        if (emailid.equals("")) {
                            emailid="0";
                        } else {
                            //i.putExtra("email", emailid);
                        }
                       // i.putExtra("addr", address);
                        // designation;
                        if (temp.equals("Other")) {
                            designation = others.getText().toString();
                        } else {
                            designation = temp;
                        }

                        address = address.replaceAll("[ ]","%20");
                        name = name.replaceAll("[ ]","%20");
                        emailid = emailid.replaceAll("[ ]","%20");

                        String iddesi=dbHandlerid.getdesid(designation);

                        if(iddesi.equals(""))
                        {
                            iddesi+="0";
                        }

                        String url="http://studentshield.in/visitor/addvisi.php?u_name="+name+"&u_mobile="+phonenumber+"&u_photoid="+"null"+"&u_image="+"null"+"&u_visitoremailID="+emailid+"&u_desig=" + iddesi+"&u_addr="+address;

                        //Toast.makeText(getApplicationContext(),phonenumber,Toast.LENGTH_LONG).show();

                        // Toast.makeText(getApplicationContext(),name2,Toast.LENGTH_LONG).show();

                        new GetMethodDemo().execute(url);
                    }

                }

                else {

                    pD.dismiss();
                }



//                CropImage.activity()
//                        .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
//                        .setActivityTitle("Profile picture")
//                        .start(VisitorDetailsActivity.this);
//                Intent i = new Intent(VisitorDetailsActivity.this,HostDetailsActivity.class);
//                startActivity(i);
                // finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String purpose=adapter.getItem(Purpose.getSelectedItemPosition());

        temp = adapterView.getItemAtPosition(i).toString();

        if(temp.equals("Other"))
        {
          // Toast.makeText(getApplicationContext(),"others",Toast.LENGTH_LONG).show();

            others.getLayoutParams().height=100;
            others.setError("Required field");
        }
        else
        {
            others.getLayoutParams().height=0;
            others.setError("");
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            pD.setMessage("Saving ...");
//            showDialog();
//            //  take_pic.setText("Change Picture");
//            String udata = "Edit Picture";
//            SpannableString content = new SpannableString(udata);
//            content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
//
//            /**for enable to edit the image*/
////          change.setText(content);
////          change.setClickable(true);
//
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            SaveImage(imageBitmap);
//
//            /**for display of image*/
////          UImage.setScaleType(ImageView.ScaleType.FIT_XY);
////          UImage.setImageBitmap(imageBitmap);
////          UImage.setBackgroundResource(R.drawable.imageborder);
////          UImage.setPadding(2, 2, 2, 2);
////          UImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // <- set the scale
////          UImage.setCropToPadding(true); // <- requires API 16 or more
//
//            Intent i = new Intent(VisitorDetailsActivity.this, idpic.class);
//            startActivity(i);
//            pD.dismiss();
//        }
//    }
//
//    //  invokes the intent to capture a photo
//    public void takePicture(View view) {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        // Ensure that there's a camera activity to handle the intent
//
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//
//            // Create the File where the photo should go
//            /*File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Toast.makeText(AddVisitors.this, "Error occurred while creating the File", Toast.LENGTH_SHORT).show();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.bennett.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/
//
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    // method to save the compressed bitmap image
//    private void SaveImage(Bitmap bitmapImage) {
//
//
//        File file = null;
//        try {
//            file = createImageFile();
//        } catch (IOException ex) {
//            // Error occurred while creating the File
//            Toast.makeText(VisitorDetailsActivity.this, "Error occurred while creating the File", Toast.LENGTH_SHORT).show();
//        }
//        if (file != null) ;
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
//            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
//            scaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    private void showDialog() {
        if (!pD.isShowing())
            pD.show();
    }


    private boolean CheckFieldValidation() {

        boolean valid = true;
        if (name.equals("")) {
            valid = false;
            visname.setError("Required field");
        }

//        if (emailid.equals("")) {
//            valid = false;
//            visiemail.setError("Required field");
//        }

        if(!emailid.equals("")) {
            if (emailid.matches(emailPattern) && emailid.length() > 0) {
                // Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                // or
            } else {
                valid = false;
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                //or

            }
        }

        if (address.equals("")) {
            valid = false;
            visiaddr.setError("Required field");
        }
        if(temp.equals("Choose one"))
        {
            valid = false;
            Toast.makeText(getApplicationContext(),"Choose one designation",Toast.LENGTH_SHORT).show();
        }
        else if(temp.equals("Other"))
        {
           // others.setError("Required field");

          //  others.getLayoutParams().height=100;

          //  others.setFocusableInTouchMode(true);
            //others.requestFocus();

           // valid = false;

            if(others.getText().toString().equals("")) {

                valid = false;
                others.setError("Required field");
            }


        }
        else
        {
            others.getLayoutParams().height=0;
        }


        return valid;

    }


    public class GetMethodDemo extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent i = new Intent(VisitorDetailsActivity.this, HostDetailsActivity.class);
            i.putExtra("phonenumber",phonenumber);
            pD.dismiss();
            // i.putExtra("designation", dbHandlerid.getdesid(desig));
            // Toast.makeText(getApplicationContext(),id+" "+dbHandlerid.getdesid(desig),Toast.LENGTH_LONG).show();
            //  new GetMethodDemo().execute("http://studentshield.in/visitor/addvisi.php?u_name="+name+"&u_mobile="+phonenumber+"&u_photoid=123&u_image=46&u_visitoremailID="+emailid+"&u_desig="+"&u_addr="+addrs);
            startActivity(i);

        }

    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }




}