package com.studentshield.shield.visitor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class idphotoactivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static TextView change;
    TextView photoins;
    private static ImageView UImage;

    private FancyButton next;

    private ProgressDialog pD;

    private String phonenumber;
    private String name;
    private String emailid;
    private Bitmap imageBitmap;
    private String addrs;
    private String desig;
    private String image;

    private String name2="";

    dbHandler dbHandlerid;

    private String id="";

    private String UPLOAD_URL ="http://studentshield.in/visitor/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idphotoactivity);

        final ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        dbHandlerid = new dbHandler(getApplicationContext(), null, null, 9);

        final Intent intent = getIntent();
        phonenumber=intent.getExtras().getString("phoneno");
        name=intent.getExtras().getString("name");
        emailid=intent.getExtras().getString("email");
        addrs=intent.getExtras().getString("addr");
        desig=intent.getExtras().getString("designation");
        image=intent.getExtras().getString("image");

        photoins=(TextView) findViewById(R.id.idphoto);

        change=(TextView) findViewById(R.id.idchange_view);
        Typeface face= Typeface.createFromAsset(getAssets(),"lato.light.ttf");
        photoins.setTypeface(face);
        UImage = (ImageView) findViewById(R.id.iduserimage);
        change.setClickable(false);

        pD = new ProgressDialog(this);
        pD.setCancelable(false);

        pD.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        next=(FancyButton) findViewById(R.id.jumptohost);
       // Toast.makeText(getApplicationContext(),desig,Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"Please click a pic of your ID",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void takePicture(View view) {
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
            takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //  take_pic.setText("Change Picture");
            String udata = "Edit Picture";
            SpannableString content = new SpannableString(udata);
            content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
            change.setText(content);
            change.setClickable(true);
            Bundle extras = data.getExtras();
             imageBitmap = (Bitmap) extras.get("data");
            UImage.setScaleType(ImageView.ScaleType.FIT_XY);
            UImage.setImageBitmap(imageBitmap);
            UImage.setBackgroundResource(R.drawable.imageborder);
            UImage.setPadding(2, 2, 2, 2);
            UImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // <- set the scale
            UImage.setCropToPadding(true); // <- requires API 16 or more
            // SaveImage(imageBitmap);
        }
    }

    private boolean validity()
    {
        boolean cond=true;

        if(!change.isClickable())
        {
            cond=false;
        }
        return cond;
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

                    Intent i = new Intent(idphotoactivity.this, HostDetailsActivity.class);
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
                        if(!name2.equals(""))
                        {
                            addrs = addrs.replaceAll("[ ]","%20");
                            name = name.replaceAll("[ ]","%20");
                            emailid = emailid.replaceAll("[ ]","%20");

                            String iddesi=dbHandlerid.getdesid(desig);
                            if(iddesi.equals(""))
                            {
                                iddesi+="0";
                            }

                            String url="http://studentshield.in/visitor/addvisi.php?u_name="+name+"&u_mobile="+phonenumber+"&u_photoid="+name2+"&u_image="+image+"&u_visitoremailID="+emailid+"&u_desig=" + iddesi+"&u_addr="+addrs;

                            //Toast.makeText(getApplicationContext(),phonenumber,Toast.LENGTH_LONG).show();

                           // Toast.makeText(getApplicationContext(),name2,Toast.LENGTH_LONG).show();

                            new GetMethodDemo().execute(url);

//                            Intent i = new Intent(idphotoactivity.this,HostDetailsActivity.class);
//                            i.putExtra("phoneno",phonenumber);
//                            i.putExtra("name",name);
//                            i.putExtra("email",emailid);
//                            i.putExtra("addr",addrs);
//                            i.putExtra("designation",desig);
//                            i.putExtra("imageid",name2);
                            //pD.dismiss();
                           // startActivity(i);
//                            Toast.makeText(getApplicationContext(),s , Toast.LENGTH_LONG).show();
//                            Toast.makeText(getApplicationContext(),name2,Toast.LENGTH_LONG).show();

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
                name2 += phonenumber+"-"+timeStamp+"-id";

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name2);

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


}
