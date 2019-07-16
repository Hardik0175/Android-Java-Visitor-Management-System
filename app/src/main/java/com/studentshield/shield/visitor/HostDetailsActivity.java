package com.studentshield.shield.visitor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import mehdi.sakout.fancybuttons.FancyButton;

public class HostDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ProgressDialog pD;

    int minteger = 0;

   // dbHandler dbHandlerOTP;
    private int apicheck=0;

    private TextView heading;
    private FancyButton nextBtn;

    private boolean mailsend=false;
    private boolean smssend=false;
    private boolean meetingcreated=false;


    private TextView stepfive;
    private String URL_SYNCSTAFF;

    private String tem;

    private FancyButton back;
    private  String id="";
    private String phonenumber;
    private String desig="";

    private String photoemail="";
    private String photoemailid="";

    private String numberofpp;
    private String purpose;
    private String nameofhost;

    private TextView no_of;
    private TextView ic_u;

    private String visiname="";

  //  private EditText numberofp;
    private Spinner Purpose;
    private Spinner whomtovisit;

    private ArrayAdapter<String> adapter2;
    private ArrayAdapter<String> adapter;

    dbHandler dbHandlerfinal;

    EditText otherpurpose;

    private int enable=0;

    int c=0;

    String mailurl="http://studentshield.in/visitor/newmail.php?phoneno=";
    String api1="https://enterprise.smsgupshup.com/GatewayAPI/rest?method=SendMessage&send_to=";
    String api2="%20is%20waiting%20to%20meet%20regarding%20";
    String api3="%20%0ADo%20you%20want%20to%20confirm%3F%0Ayes-%20http://studentshield.in/visitor/apiforaccepting.php%3Fphoneno%3D";
    String api4="%26status%3Dyes%0Ano-%20http://studentshield.in/visitor/apiforaccepting.php%3Fphoneno%3D";
    String api5="%26status%3Dno&msg_type=TEXT&userid=2000137573&auth_scheme=plain&password=kapila8143&v=1.1&format=text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_details);

        //dbHandlerOTP = new dbHandler(getApplicationContext(), null, null, 9);

        pD = new ProgressDialog(this);
        pD.setCancelable(false);
      //  numberofp=(EditText) findViewById(R.id.Number_of_persons);

        dbHandlerfinal = new dbHandler(getApplicationContext(), null, null, 9);



        otherpurpose=(EditText) findViewById(R.id.nameofpurpose);

        final Intent intent = getIntent();
        phonenumber=intent.getExtras().getString("phonenumber");
      //  desig=intent.getExtras().getString("designation");

        pD.setMessage("Synchronizing ...");
        showDialog();

        final ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        back=(FancyButton) findViewById(R.id.backhost);
        Purpose = (Spinner) findViewById(R.id.Purpose_view2);
        whomtovisit = (Spinner) findViewById(R.id.whomtovisit);
        nextBtn =(FancyButton) findViewById(R.id.nexttosign);

        no_of=(TextView) findViewById(R.id.no_of_stu);
        ic_u=(TextView) findViewById(R.id.incypu);


        URL_SYNCSTAFF="http://studentshield.in/visitor/syncstaff.php";

        Purpose.setOnItemSelectedListener(this);
        whomtovisit.setOnItemSelectedListener(this);


      //  pD.setMessage("Connecting ...");


       if( getvisi(phonenumber))
       {
           pD.dismiss();
       }

      //  showDialog();

        heading = (TextView) findViewById(R.id.hostheadin);
        Typeface myface = Typeface.createFromAsset(getAssets(), "lato.light.ttf");
        heading.setTypeface(myface);

        no_of.setTypeface(myface);
        ic_u.setTypeface(myface);

        if(MainActivity.enable_host_list==0)
        {
            whomtovisit.getLayoutParams().height=0;
        }

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
        String purp=dbHandlerfinal.getstringpurp();
       // Toast.makeText(getApplicationContext(),purp,Toast.LENGTH_SHORT).show();

        String[] purpeach=purp.split("#");
        for(int i=0;i<purpeach.length;i++)
        {
           // Toast.makeText(getApplicationContext(),purpeach[i],Toast.LENGTH_SHORT).show();
            adapter.add(purpeach[i]);
        }
        adapter.add("Other");
        adapter.add("Purpose"); // Hint for the spinner

       // Toast.makeText(getApplicationContext(),dbHandlerOTP.getstringstaff(),Toast.LENGTH_SHORT).show();


        adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_view) {

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


        adapter2.setDropDownViewResource(R.layout.dropdown_item);

        final String host=dbHandlerfinal.getstringstaff();

        // Toast.makeText(getApplicationContext(),purp,Toast.LENGTH_SHORT).show();

        String[] hosteach=host.split("#");

        for(int i=0;i<hosteach.length;i++)
        {
            adapter2.add(hosteach[i]);
        }

        adapter2.add("Whom to visit");

        Purpose.setAdapter(adapter);
        Purpose.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.
        Purpose.setOnItemSelectedListener(this);

        whomtovisit.setAdapter(adapter2);
        whomtovisit.setSelection(adapter2.getCount()); //set the hint the default selection so it appears on launch.
        whomtovisit.setOnItemSelectedListener(this);
        // pD.dismiss();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new CountDownTimer(2500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //  alertDialog.setMessage("00:"+ (millisUntilFinished/1000));
                    }

                    @Override
                    public void onFinish() {
                        enable=0;
                    }
                }.start();

                if(CheckFieldValidation() && enable==0) {
                    enable = -1;

                    pD.setMessage("Sending request ...");
                    pD.show();
                    numberofpp = String.valueOf(minteger);

                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //  alertDialog.setMessage("00:"+ (millisUntilFinished/1000));
                        }

                        @Override
                        public void onFinish() {
                            pD.dismiss();
                        }
                    }.start();

                    String purpose = adapter.getItem(Purpose.getSelectedItemPosition());

                    String gethost = adapter2.getItem(whomtovisit.getSelectedItemPosition());

                    String hostname = gethost.replaceAll("[ ]", "%20");

                    String name[] = gethost.split(" ");

                    String[] visifirstname = visiname.split(" ");

                    String visine = visiname.replaceAll("[ ]", "%20");

                    String purposee;


                    if (purpose.equals("Other")) {

                        purposee = otherpurpose.getText().toString().replaceAll("[ ]", "%20");
                        ;
                    } else {
                        purposee = purpose.replaceAll("[ ]", "%20");
                        //  visine=visiname.replaceAll("[ ]","%20");
                    }


                    String hostphone = dbHandlerfinal.getphonenumber(name[0] + " " + name[1]);

                    String hosteemail = dbHandlerfinal.gethostemail(hostphone);

                    String designame = dbHandlerfinal.getdesig(desig).replaceAll("[ ]", "%20");

                    //Toast.makeText(getApplicationContext(),dbHandlerfinal.getphonenumber(name[0]+" "+name[1]),Toast.LENGTH_LONG).show();

                    String url = "http://studentshield.in/visitor/createnewmeeting.php?did=" + desig + "&v_id=" + id + "&purposeid=" + dbHandlerfinal.getpurpid(purpose) + "&hostid=" + hostname;

                    String url2 = api1 + "91" + hostphone + "&msg=Hii%20" + hostname + "%21%0A" + visine + "%20%28" + phonenumber + "%29%20" + api2 + purposee + "%2E%0ADesignation-%20" + designame + "%0ANumber%20visitors-%20" + String.valueOf(minteger) + api3 + phonenumber + "%26personname%3D" + visifirstname[0] + "%26vid%3D" + id + api4 + phonenumber + "%26personname%3D" + visifirstname[0] + "%26vid%3D" + id + api5;

                    String url3 = mailurl + phonenumber + "&hostname=" + hostname + "&hostemail=" + hosteemail + "&personname=" + visine + "&vid=" + id + "&designation=" + designame + "&numberofvisit=" + String.valueOf(minteger) + "&purpose=" + purposee + "&photo=" + photoemail + "&photoid=jjjj";

                    //Toast.makeText(getApplicationContext(),url2,Toast.LENGTH_LONG).show();

//                    Toast.makeText(getApplicationContext(),hosteemail,Toast.LENGTH_LONG).show();
//
//                    Toast.makeText(getApplicationContext(),url3,Toast.LENGTH_LONG).show();
                    if (MainActivity.enable_host_authentication == 0) {

                        Toast.makeText(getApplicationContext(), "Meeting is confirmed", Toast.LENGTH_LONG).show();
                        String approved="http://studentshield.in/visitor/apiforaccepting.php?phoneno="+phonenumber+"&personname="+visifirstname[0]+"&vid="+id +"&status=yes";
                        new GetMethodDemo().execute(approved);
                        pD.dismiss();
                        Intent i = new Intent(HostDetailsActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {


                        //  pD.dismiss();
                        //Toast.makeText(getApplicationContext(),dbHandlerfinal.getpurpid(purpose),Toast.LENGTH_LONG).show();
                        try {
                            String my = new GetMethodDemo().execute(url).get();
                            if (my.equals("success")) {
                                meetingcreated = true;
                                if (smssend == true && meetingcreated == true && mailsend == true && apicheck == 3) {
                                    // pD.dismiss();
                                    Intent i = new Intent(HostDetailsActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    pD.dismiss();
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        try {
                            String my = new GetMethodDemo().execute(url2).get();
                            if (my != null) {
                                smssend = true;
                                if (smssend == true && meetingcreated == true && mailsend == true && apicheck == 3) {
                                    // pD.dismiss();
                                    Intent i = new Intent(HostDetailsActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    pD.dismiss();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        try {
                            String my = new GetMethodDemo().execute(url3).get();
                            if (my.equals("success")) {
                                mailsend = true;
                                if (smssend == true && meetingcreated == true && mailsend == true && apicheck == 3) {
                                    //pD.dismiss();
                                    Intent i = new Intent(HostDetailsActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    pD.dismiss();
                                }

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                Toast.makeText(getApplicationContext(), "Request has been send to the host", Toast.LENGTH_LONG).show();
                                pD.dismiss();
                                Intent i = new Intent(HostDetailsActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                                // mTextField.setText("done!");
                            }
                        }.start();

                    }

//                emailid=visiemail.getText().toString();
//                address=visiaddr.getText().toString();
//


//                Intent i = new Intent(HostDetailsActivity.this,MainActivity.class);
//                startActivity(i);
//                finish();

//                CropImage.activity()
//                        .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
//                        .start(HostDetailsActivity.this);
                }

            }
        });
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//                Uri resultUri = result.getUri();//we get the result as Uri
//                File thumb_file_path = new File(resultUri.getPath());
//
//                Intent i = new Intent(HostDetailsActivity.this,SignatureActivity.class);
//                startActivity(i);
//                finish();
//
//            }}
//
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;

            //do this

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


            String purpose=adapter.getItem(Purpose.getSelectedItemPosition());

            if(purpose.equals("Other"))
            {
                otherpurpose.getLayoutParams().height=100;
                otherpurpose.setError("Required field");
            }
            else
            {
                otherpurpose.getLayoutParams().height=0;
                otherpurpose.setError("");
            }


           // purpose = adapterView.getItemAtPosition(i).toString();
       // Toast.makeText(getApplicationContext(),adapterView,Toast.LENGTH_SHORT).show();
    }






    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
      //  Toast.makeText(getApplicationContext(),"kdnfdkadndka",Toast.LENGTH_SHORT).show();
    }



    private void showDialog() {
        if (!pD.isShowing())
            pD.show();
    }

    private boolean getvisi(String phonenumber) {


       final boolean valid = true;



        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://studentshield.in/visitor/getiddesi.php?u_mobile="+phonenumber,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);
                                id+=product.getString("uid");
                                desig+=product.getString("u_desig");
                                visiname+=product.getString("u_name");
                                photoemailid+=product.getString("u_photoid");
                                photoemail+=product.getString("u_image");

                                ic_u.append(visiname);
                                ic_u.append(" !");

                                //Toast.makeText(getApplicationContext(),"Visitor  - "+"(Name:"+visiname+" usrId: "+ id+" "+"degid: "+desig+")",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();


                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

       // pD.dismiss();

        return valid;
        //ic_u.setText(visiname);

    }
    private boolean CheckFieldValidation() {



        boolean valid = true;
//        if (name.equals("")) {
//            valid = false;
//            visname.setError("Required field");
//        }
////        if (emailid.equals("")) {
////            valid = false;
////            visiemail.setError("Required field");
////        }
//        if (numberofpp.equals("")) {
//            valid = false;
//            //numberofp.setError("Required field");
//        }
//        else {
//            //Toast.makeText(getApplicationContext(),String.valueOf(numberofpp.length()),Toast.LENGTH_LONG).show();
//            if(!Pattern.matches("[a-zA-Z]+", numberofpp)) {
//                if(numberofpp.length() < 3) {
//                    // if(phone.length() != 10) {
//                    valid = true;
//
//                } else {
//                    valid = false;
//                    Toast.makeText(getApplicationContext(),"Max. number of visitor exceed",Toast.LENGTH_LONG).show();
//                }
//            }
//            else {
//                valid=false;
//                Toast.makeText(getApplicationContext(),"Please enter the number of visitors",Toast.LENGTH_LONG).show();
//            }
//        }

        if(MainActivity.enable_host_list==1) {
            if (String.valueOf(whomtovisit.getSelectedItemPosition()).equals(String.valueOf(adapter2.getCount()))) {
                valid = false;
                Toast.makeText(getApplicationContext(), "Please select one host", Toast.LENGTH_LONG).show();
            }
        }
        if (String.valueOf(Purpose.getSelectedItemPosition()).equals(String.valueOf(adapter.getCount()))) {
            valid = false;
            Toast.makeText(getApplicationContext(), "Please select one purpose of visit", Toast.LENGTH_LONG).show();

        }
        if (adapter.getItem(Purpose.getSelectedItemPosition()).equals("Other")) {
            if (otherpurpose.getText().toString().equals("")) {
            valid=false;
            }
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
                    // count++;
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return server_response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            apicheck++;

           // Intent i = new Intent(idphotoactivity.this, HostDetailsActivity.class);
           // i.putExtra("phonenumber",phonenumber);
            // i.putExtra("designation", dbHandlerid.getdesid(desig));

            //  new GetMethodDemo().execute("http://studentshield.in/visitor/addvisi.php?u_name="+name+"&u_mobile="+phonenumber+"&u_photoid=123&u_image=46&u_visitoremailID="+emailid+"&u_desig="+"&u_addr="+addrs);
            //startActivity(i);
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
    public void increaseInteger(View view) {
        if(minteger<9) {
            minteger = minteger + 1;
            display(minteger);
        }
    }public void decreaseInteger(View view) {
        if(minteger>=1) {
            minteger = minteger - 1;
            display(minteger);
        }
    }

    private void display(int number) {
        EditText displayInteger = (EditText) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
    }
}
