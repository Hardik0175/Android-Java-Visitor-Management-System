package com.studentshield.shield.visitor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import mehdi.sakout.fancybuttons.FancyButton;



public class OTP_verification extends AppCompatActivity {

    private int nofat=3;
    private ProgressDialog pD;

    private long countDown = 3000;

    private long interval = 1000;

    private CountDownTimer countDownTimer;

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private TextView otp;
    private TextView noof;

    private int caseno=0;

    dbHandler dbotp;
    String phonenumber;
    FirebaseAuth auth;
    FancyButton verify;
    int flag=555;
    String id;

    String URL_CHK="http://studentshield.in/visitor/checkexist.php?mobile=";
    String URL_DESIG="http://studentshield.in/visitor/syncdes.php";
    String URL_PURP="http://studentshield.in/visitor/syncpurp.php";
    String URL_STAFF="http://studentshield.in/visitor/syncstaff.php";

    dbHandler dbHandlerOTP;

    private PinLockListener mPinLockListener = new PinLockListener() {



        @Override
        public void onComplete(String pin) {
//
            pD.setMessage("Verifying ...");
            showDialog();

        jump();
//
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,pin);
//            signInWithPhone(credential);

        }

        @Override
        public void onEmpty() {
           // Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            //Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        caseno=1;

        mcallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
              //  verification_code = s;

                mResendToken = forceResendingToken;
                caseno=2;
                //   String phoneNumber = phoneInputLayout.getPhoneNumber();
                //  pD.show();
                pD.dismiss();
                Toast.makeText(getApplicationContext(),"Code is send",Toast.LENGTH_LONG).show();
              //  Intent i = new Intent(MainActivity.this,OTP_verification.class);
              //  i.putExtra("code",verification_code);
                //i.putExtra("phoneno",String.valueOf(phone.getText()));
                //pD.dismiss();
                //startActivity(i);

            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        Intent intent = getIntent();
        DisplayMetrics screen = new DisplayMetrics();
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        dbHandlerOTP = new dbHandler(getApplicationContext(), null, null, 9);
       // Toast.makeText(getApplicationContext(),dbHandlerOTP.getstringpurp(),Toast.LENGTH_SHORT).show();

        verify=(FancyButton) findViewById(R.id.verify);

        id = intent.getExtras().getString("code");
        auth=FirebaseAuth.getInstance();


       phonenumber=intent.getExtras().getString("phoneno");

        pD = new ProgressDialog(this);
        pD.setCancelable(false);

        pD.setMessage("Connecting ...");
        pD.show();

        getWindowManager().getDefaultDisplay().getMetrics(screen);
        // Toast.makeText(getApplicationContext(),timer,Toast.LENGTH_SHORT).show();

        int Width = screen.widthPixels;
        int height = screen.heightPixels;
        getWindow().setLayout((int) (Width * .7), (int) (height * .7));

       // auth=FirebaseAuth.getInstance();

        otp=(TextView) findViewById(R.id.otp);

        Typeface myface= Typeface.createFromAsset(getAssets(),"lato.light.ttf");
        otp.setTypeface(myface);


        noof=(TextView) findViewById(R.id.noofatt);
        noof.setTypeface(myface);

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);

        mPinLockView.setPinLockListener(mPinLockListener);


        mPinLockView.setPinLength(6);

        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);




        if (conMgr.getActiveNetworkInfo() != null) {
             //
            loaddesi();
            loadstaff();
            loadpurp();
            //do whatever you want to do
        } else {
            loaddesi();
            loadstaff();
            loadpurp();
            pD.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(OTP_verification.this).create();
                                alertDialog.setTitle("Info");
                                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                                //alertDialog.setIcon(R.drawable.alerticon);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();

                                    }
                                });

                    alertDialog.show();

        }
        setmycounter(30000);

        verify.setClickable(false);

        final String code=mPinLockView.toString();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verify.getText().equals("Resend OTP"))
                {
                    pD.show();

                    if(caseno==2)
                    {
                        resendVerificationCode(phonenumber,mResendToken);
                    }
                    else {
                        setmycounter(30000);

                        sendsmd("+91"+phonenumber,mcallbacks);
                    }

                   // Toast.makeText(getApplicationContext(),"jump",Toast.LENGTH_SHORT).show();
                    //myactiv.secondsms(phonenumber);
                }

           /*
               //
//                Toast.makeText(getApplicationContext(),dbHandlerOTP.getstringdesi(),Toast.LENGTH_SHORT).show();
//
//                Toast.makeText(getApplicationContext(),dbHandlerOTP.getstringstaff(),Toast.LENGTH_SHORT).show();
              //  Toast.makeText(getApplicationContext(),URL_CHK+phonenumber.trim(),Toast.LENGTH_SHORT).show();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHK+phonenumber.trim(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject product = array.getJSONObject(i);
                                       flag=Integer.valueOf(product.getString("u_mobile"));
                                    }
                                  //  Toast.makeText(getApplicationContext(),dbHandlerOTP.getstringpurp(),Toast.LENGTH_SHORT).show();
                                   // Toast.makeText(getApplicationContext(),"jump",Toast.LENGTH_SHORT).show();
                                    if(flag==000)
                                    {

                                        Intent i = new Intent(OTP_verification.this,HostDetailsActivity.class);
                                        i.putExtra("phonenumber","91"+phonenumber.trim());
                                    //    i.putExtra("phonenumber",phonenumber);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        Intent i = new Intent(OTP_verification.this,VisitorDetailsActivity.class);
                                        i.putExtra("phoneno","91"+phonenumber);
                                        startActivity(i);
                                        finish();
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




               // Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();*/
            }
        });


    }

    public void sendsmd(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,30, TimeUnit.SECONDS,this,mCallbacks);
    }

    public void signInWithPhone(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHK+"91"+phonenumber.trim(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {

                                                JSONArray array = new JSONArray(response);
                                                for (int i = 0; i < array.length(); i++) {

                                                    JSONObject product = array.getJSONObject(i);
                                                    flag=Integer.valueOf(product.getString("u_mobile"));
                                                }

                                                //  Toast.makeText(getApplicationContext(),dbHandlerOTP.getstringpurp(),Toast.LENGTH_SHORT).show();
                                                // Toast.makeText(getApplicationContext(),"jump",Toast.LENGTH_SHORT).show();
                                                if(flag==000)
                                                {
                                                    pD.dismiss();

                                                    Intent i = new Intent(OTP_verification.this,HostDetailsActivity.class);
                                                    i.putExtra("phonenumber","91"+phonenumber.trim());
                                                    // i.putExtra("phonenumber",phonenumber);
                                                    startActivity(i);
                                                    finish();
                                                }
                                                else
                                                {
                                                    pD.dismiss();
                                                    Intent i = new Intent(OTP_verification.this,VisitorDetailsActivity.class);
                                                    i.putExtra("phoneno","91"+phonenumber);
                                                    startActivity(i);
                                                    finish();
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


                        }
                        else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                        {
                            pD.dismiss();
                            Toast.makeText(getApplicationContext(),"User already exist",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            pD.dismiss();
                            Toast.makeText(getApplicationContext(),"Wrong code. please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void loaddesi() {

        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DESIG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            dbHandlerOTP.deletetabledesig();
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);
                                adddesi(product.getString("d_name"),product.getString("id"));

                            }
                           // Toast.makeText(getApplicationContext(),"deis",Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pD.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(OTP_verification.this).create();

                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("Please try again later (Server issue-103)");
                        //alertDialog.setIcon(R.drawable.alerticon);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });
                        alertDialog.show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void loadpurp() {
        dbHandlerOTP.deletetablespurpos();
        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PURP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);
                                addpurp(product.getString("purpose"),product.getString("id"));
                              // Toast.makeText(getApplicationContext(),product.getString("purpose")+" "+product.getString("id"),Toast.LENGTH_SHORT).show();
                            }


                          //
                        } catch (JSONException e) {

                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pD.dismiss();

                        AlertDialog alertDialog = new AlertDialog.Builder(OTP_verification.this).create();

                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("Please try again later (Server issue-102)");
                        //alertDialog.setIcon(R.drawable.alerticon);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });
                        alertDialog.show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
    private void loadstaff() {
        dbHandlerOTP.deletetablestaff();
        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_STAFF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {



                            String prm="";
                            String chairman="";
                            String viceprm="";
                            String admin="";
                            String tranhead="";

                           // dbHandlerOTP.deletetablespurpos();
                            JSONArray array = new JSONArray(response);
                            int x=0;
                            for (int i = 0; i < array.length(); i++) {
                                x++;

                               // Toast.makeText(getApplicationContext(),"0-",Toast.LENGTH_SHORT).show();
                                JSONObject product = array.getJSONObject(i);
                                //Toast.makeText(getApplicationContext(),product.getString("option_name"),Toast.LENGTH_SHORT).show();
                                if(product.getString("option_name").equals("principal")||product.getString("option_name").equals("p_phone")||product.getString("option_name").equals("p_email"))
                                {
                                    prm+=product.getString("option_value");
                                    if(product.getString("option_value").equals(""))
                                    {
                                        prm+="000";
                                    }
                                    prm+="#";
                                    if(product.getString("option_name").equals("principal"))
                                    {
                                        prm+="Principal";
                                        prm+="#";
                                    }

                                }
                                if(product.getString("option_name").equals("chairman")||product.getString("option_name").equals("c_phone")||product.getString("option_name").equals("c_email"))
                                {
                                    chairman+=product.getString("option_value");
                                    if(product.getString("option_value").equals(""))
                                    {
                                        chairman+="000";
                                    }
                                    chairman+="#";

                                    if(product.getString("option_name").equals("chairman"))
                                    {
                                        chairman+="Chairman";
                                        chairman+="#";
                                    }
                                }
                                if(product.getString("option_name").equals("viceprincipal")||product.getString("option_name").equals("vp_phone")||product.getString("option_name").equals("vp_email"))
                                {
                                    viceprm+=product.getString("option_value");
                                    if(product.getString("option_value").equals(""))
                                    {
                                        viceprm+="000";
                                    }
                                    viceprm+="#";
                                    if(product.getString("option_name").equals("viceprincipal"))
                                    {
                                        viceprm+="Vice principal";
                                        viceprm+="#";
                                    }
                                }
                                if(product.getString("option_name").equals("admin")||product.getString("option_name").equals("a_phone")||product.getString("option_name").equals("a_email"))
                                {
                                    admin+=product.getString("option_value");
                                    if(product.getString("option_value").equals(""))
                                    {
                                        admin+="000";
                                    }
                                    admin+="#";
                                    if(product.getString("option_name").equals("admin"))
                                    {
                                        admin+="Admin";
                                        admin+="#";
                                    }
                                }
                                if(product.getString("option_name").equals("tansportadmin")||product.getString("option_name").equals("ta_phone")||product.getString("option_name").equals("ta_email"))
                                {
                                    tranhead+=product.getString("option_value");
                                    if(product.getString("option_value").equals(""))
                                    {
                                        tranhead+="000";
                                    }
                                    tranhead+="#";
                                    if(product.getString("option_name").equals("tansportadmin"))
                                    {
                                        tranhead+="Transport head";
                                        tranhead+="#";
                                    }
                                }
                            }
                          //  Toast.makeText(getApplicationContext(),"staff",Toast.LENGTH_SHORT).show();
                            String[] princiinfo=prm.split("#");
                            addstaff(princiinfo[0],princiinfo[1],princiinfo[2],princiinfo[3]);

                            String[] staff2=chairman.split("#");
                            addstaff(staff2[0],staff2[1],staff2[2],staff2[3]);
                            String[] staff3=viceprm.split("#");
                            addstaff(staff3[0],staff3[1],staff3[2],staff3[3]);
                            String[] staff4=admin.split("#");
                            addstaff(staff4[0],staff4[1],staff4[2],staff4[3]);
                            String[] staff5=tranhead.split("#");
                            addstaff(staff5[0],staff5[1],staff5[2],staff5[3]);


//                           // Toast.makeText(getApplicationContext(),String.valueOf(x),Toast.LENGTH_SHORT).show();
//                           Toast.makeText(getApplicationContext(),prm,Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(),chairman,Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(),viceprm,Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(),admin,Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(),tranhead,Toast.LENGTH_SHORT).show();
                         //   Toast.makeText(getApplicationContext(),"staff",Toast.LENGTH_SHORT).show();
                            pD.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pD.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(OTP_verification.this).create();

                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("Please try again later (Server issue-101)");
                        //alertDialog.setIcon(R.drawable.alerticon);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });
                        alertDialog.show();

                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }


    void adddesi(String desi,String id)
    {
        dbHandlerOTP.adddesig(desi,id);

    }

    void addstaff(String name,String deisg,String mobile,String email)
    {
        staffdb newstadd = new staffdb(name,deisg,mobile,email);
        dbHandlerOTP.addstaff(newstadd);
    }


    void addpurp(String purp,String id)
    {
        dbHandlerOTP.addpurp(purp,id);

    }
    private void showDialog() {
        if (!pD.isShowing())
            pD.show();
    }

    void jump()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHK+"91"+phonenumber.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);
                                flag=Integer.valueOf(product.getString("u_mobile"));
                            }
                            //  Toast.makeText(getApplicationContext(),dbHandlerOTP.getstringpurp(),Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(),"jump",Toast.LENGTH_SHORT).show();
                            if(flag==000)
                            {

                                pD.dismiss();
                                Intent i = new Intent(OTP_verification.this,HostDetailsActivity.class);
                                i.putExtra("phonenumber","91"+phonenumber.trim());
                                //    i.putExtra("phonenumber",phonenumber);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                pD.dismiss();
                                Intent i = new Intent(OTP_verification.this,VisitorDetailsActivity.class);
                                i.putExtra("phoneno","91"+phonenumber);
                                startActivity(i);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pD.dismiss();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }
//


    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public void setmycounter(int value){
        noof.setText("No of attempts remaining: "+String.valueOf(nofat));
        if(nofat==-1)
        {

            finish();
        }

        nofat--;
        countDownTimer=new CountDownTimer(value,1000) {
            @Override
            public void onTick(long l) {
                verify.setText(String.valueOf(l/1000));
                //

            }

            @Override
            public void onFinish() {
                verify.setText("Resend OTP");

            }
        }.start();

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mcallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

        setmycounter(60000);
    }

}
