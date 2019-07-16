package com.studentshield.shield.visitor;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import java.util.Hashtable;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class photoactivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static TextView change;
    TextView photoins;
    private static ImageView UImage;

    private ProgressDialog pD;

    private static File image;

    private FancyButton next;

    private String phonenumber;
    private String name;
    private String emailid;
    private String addrs;
    private String desig;
    private Bitmap imageBitmap;
    private Bitmap mybitmap;
    private String name1="";

    dbHandler dbHandlerid;


    String mCurrentPhotoPath;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private String UPLOAD_URL ="http://studentshield.in/visitor/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoactivity);

        final Intent intent = getIntent();
        phonenumber=intent.getExtras().getString("phoneno");
        name=intent.getExtras().getString("name");
        emailid=intent.getExtras().getString("email");
        addrs=intent.getExtras().getString("addr");
        desig=intent.getExtras().getString("designation");

        dbHandlerid = new dbHandler(getApplicationContext(), null, null, 9);

       // Toast.makeText(getApplicationContext(),phonenumber+name+emailid+addrs+desig,Toast.LENGTH_LONG).show();

        next=(FancyButton) findViewById(R.id.nexttoid);
        photoins=(TextView) findViewById(R.id.photo);
        change=(TextView) findViewById(R.id.change_view);
        Typeface face= Typeface.createFromAsset(getAssets(),"lato.light.ttf");
        photoins.setTypeface(face);
        UImage = (ImageView) findViewById(R.id.userimage);
        change.setClickable(false);

        pD = new ProgressDialog(this);
        pD.setCancelable(false);

        pD.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validity())
                {
                    pD.setMessage("Uploading ...");
                    showDialog();

                    uploadImage();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please click a pic of yourself",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        int cameraCount = Camera.getNumberOfCameras();
//        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
//            Camera.getCameraInfo(camIdx, cameraInfo);
//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                try {
//                    cam = Camera.open(camIdx);
//                } catch (RuntimeException e) {
//                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
//                }
//            }
//        }



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
            //Uri photoUri = Uri.fromFile(getOutputPhotoFile());
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            //takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
           // takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }



//    public static Bitmap uriToBitmap(Context c, Uri uri) {
//        if (c == null && uri == null) {
//            return null;
//        }
//        try {
//            return MediaStore.Images.Media.getBitmap(c.getContentResolver(), uri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //  take_pic.setText("Change Picture");
            Uri filePath = data.getData();
//            try {
//                mybitmap =  MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
//            }catch (IOException e) {
//                e.printStackTrace();
//            }
            String udata = "Edit Picture";
            SpannableString content = new SpannableString(udata);
            content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
            change.setText(content);
            change.setClickable(true);
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
          //  mybitmap=(Bitmap) extras.get("data");
            UImage.setScaleType(ImageView.ScaleType.FIT_XY);
            UImage.setImageBitmap(imageBitmap);
            UImage.setBackgroundResource(R.drawable.imageborder);
            UImage.setPadding(2, 2, 2, 2);
            UImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // <- set the scale
            UImage.setCropToPadding(true); // <- requires API 16 or more
          //  SaveImage(imageBitmap);
        }
    }
    /*
    private void SaveImage(Bitmap bitmapImage) {


        File file = null;
        try {
            file = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast.makeText(photoactivity.this, "Error occurred while creating the File", Toast.LENGTH_SHORT).show();
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
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
    //              ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

    private boolean validity()
    {
        boolean cond=true;

        if(!change.isClickable())
        {
            cond=false;
        }
        return cond;
    }

    private void uploadImage(){
        //Showing the progress dialog
     //   final ProgressDialog loading = ProgressDialog.show(getApplicationContext(),"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                       // loading.dismiss();
                        //Showing toast message of the response
                        if(!name1.equals("") && MainActivity.kyc==1)
                        {

                            Intent i = new Intent(photoactivity.this,idphotoactivity.class);
                            i.putExtra("phoneno",phonenumber);
                            i.putExtra("name",name);
                            i.putExtra("email",emailid);
                            i.putExtra("addr",addrs);
                            i.putExtra("designation",desig);
                            i.putExtra("image",name1);
                            pD.dismiss();
                            startActivity(i);
                            Toast.makeText(getApplicationContext(),s , Toast.LENGTH_LONG).show();
                           // Toast.makeText(getApplicationContext(),name1,Toast.LENGTH_LONG).show();

                        }

                        else
                        {
                            addrs = addrs.replaceAll("[ ]","%20");
                            name = name.replaceAll("[ ]","%20");
                            emailid = emailid.replaceAll("[ ]","%20");

                            String iddesi=dbHandlerid.getdesid(desig);

                            if(iddesi.equals(""))
                            {
                                iddesi+="0";
                            }

                            String url="http://studentshield.in/visitor/addvisi.php?u_name="+name+"&u_mobile="+phonenumber+"&u_photoid="+"null"+"&u_image="+name1+"&u_visitoremailID="+emailid+"&u_desig=" + iddesi+"&u_addr="+addrs;

                            //Toast.makeText(getApplicationContext(),phonenumber,Toast.LENGTH_LONG).show();

                            // Toast.makeText(getApplicationContext(),name2,Toast.LENGTH_LONG).show();

                            new GetMethodDemo().execute(url);

//                            Intent i = new Intent(photoactivity.this,idphotoactivity.class);
//                            i.putExtra("phoneno",phonenumber);
//                            i.putExtra("name",name);
//                            i.putExtra("email",emailid);
//                            i.putExtra("addr",addrs);
//                            i.putExtra("designation",desig);
//                            i.putExtra("image",name1);

                            pD.dismiss();
//                            startActivity(i);
                            Toast.makeText(getApplicationContext(),s , Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                      //  loading.dismiss();
                        pD.dismiss();
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error of the internet", Toast.LENGTH_LONG).show();
                       // Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(imageBitmap);

                //Getting Image Name
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date());
                 name1 += phonenumber+"-"+timeStamp;

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name1);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
       //bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        int nh = (int) (bmp.getHeight() * (512.0 / bmp.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);
        scaled.compress(Bitmap.CompressFormat.JPEG, 75, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void showDialog() {
        if (!pD.isShowing())
            pD.show();
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

            Intent i = new Intent(photoactivity.this, HostDetailsActivity.class);
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
