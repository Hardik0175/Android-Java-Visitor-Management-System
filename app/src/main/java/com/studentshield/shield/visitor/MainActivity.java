package com.studentshield.shield.visitor;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.lamudi.phonefield.PhoneInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pD;
    private String verification_code;
    private TextView welcome;
    private TextView stepone;

    private RequestPermissionHandler mRequestPermissionHandler;

    private TextView inst;

    public static int take_photo_signup=0;
    public static  int take_new_pic=0;
    public static int kyc=0;
    public static  int kphone_verification=0;
    public static  int auto_sign_out=0;
    public static int enable_host_authentication=0;
    public static int enable_host_list=0;

    private EditText phone;
    FancyButton facebookLoginBtn;
    FirebaseAuth auth;

    PhoneAuthProvider.ForceResendingToken tokenn;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   Toast.makeText(getApplicationContext(),"OnCreate",Toast.LENGTH_LONG).show();

        mRequestPermissionHandler = new RequestPermissionHandler();
        handleButtonClicked();
        phone=(EditText) findViewById(R.id.phone_number);

        facebookLoginBtn = (FancyButton) findViewById(R.id.facebook_button);
        loadmysetting();
        welcome=(TextView) findViewById(R.id.welcome);
        auth=FirebaseAuth.getInstance();
        Typeface face= Typeface.createFromAsset(getAssets(),"lato.light.ttf");
        Typeface myface = Typeface.createFromAsset(getAssets(), "Reef.otf");
        welcome.setTypeface(face);

        //final PhoneInputLayout phoneInputLayout = (PhoneInputLayout) findViewById(R.id.phone_input_layout);
        //final Button button = (Button) findViewById(R.id.phone_check_in);

        pD = new ProgressDialog(this);
        pD.setCancelable(false);

        //cocphoneInputLayout.setHint();
        //showDialog();
        //inst=(TextView) findViewById(R.id.instruction);
        //inst.setTypeface(myface);
        //phoneInputLayout.setDefaultCountry("IN");

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
                verification_code = s;

         //String phoneNumber = phoneInputLayout.getPhoneNumber();
         //pD.show();

                Toast.makeText(getApplicationContext(),"Code is send",Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this,OTP_verification.class);
                i.putExtra("code",verification_code);
                i.putExtra("phoneno",String.valueOf(phone.getText()));
                pD.dismiss();
                startActivity(i);

            }
        };
        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // boolean valid = true;

                boolean valid=isValidMobile(String.valueOf(phone.getText()));
                // checks if the field is valid
//                if (phoneInputLayout.isValid()) {
//                    phoneInputLayout.setError(null);
//                } else {
//                    // set error message
//                    phoneInputLayout.setError("Invalid Phone Number");
//                    valid = false;
//                }
               // Toast.makeText(MainActivity.this, phone.getText(), Toast.LENGTH_LONG).show();
                if (valid) {

                   String phoneNumber = String.valueOf(phone.getText());

//                    pD.setMessage("Verifying ...");
//                  //  pD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//                  //  pD.getWindow().getColorMode(R.style.AppTheme_Customthemeforpopup);
//               //     pD.setProgressDrawable(new ColorDrawable(Color.BLACK));
//                 //   pD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    pD.show();

                    loadmysetting();

//                    Intent i = new Intent(MainActivity.this,OTP_verification.class);
//                    i.putExtra("code",verification_code);
//                    i.putExtra("phoneno",phoneNumber.trim());
//                    startActivity(i);

//                   // Toast.makeText(MainActivity.this, phoneNumber, Toast.LENGTH_LONG).show();

                    sendsmd("+91"+phoneNumber,mcallbacks);
                    pD.setMessage("Verifying ...");
                    showDialog();

                    // finish();

                } else {
                    phone.setError("Wrong phone number");
                    Toast.makeText(MainActivity.this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
       loadmysetting();
        // Toast.makeText(MainActivity.this, "OnResume", Toast.LENGTH_LONG).show();

    }

    public void sendsmd(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,30, TimeUnit.SECONDS,this,mCallbacks);
    }
    private void showDialog() {
        if (!pD.isShowing())
            pD.show();
    }
    private void handleButtonClicked(){
        mRequestPermissionHandler.requestPermission(this, new String[] {
                Manifest.permission.SEND_SMS, Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                // Toast.makeText(MainActivity.this, "request permission success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed() {
               // Toast.makeText(MainActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadmysetting()
    {
        String URL_SETTING="http://studentshield.in/visitor/syncsetting.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SETTING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                           // dbHandlerOTP.deletetabledesig();
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);

                                if(product.getString("option_value").equals("1"))
                                {
                                    if(product.getString("option_name").equals("take_photo_signup"))
                                    {
                                        take_photo_signup=1;
                                    }
                                    if(product.getString("option_name").equals("take_new_pic"))
                                    {
                                        take_new_pic=1;
                                    }
                                    if(product.getString("option_name").equals("kyc"))
                                    {
                                        kyc=1;
                                    }
                                    if(product.getString("option_name").equals("kphone_verification"))
                                    {
                                        kphone_verification=1;
                                    }
                                    if(product.getString("option_name").equals("auto_sign_out"))
                                    {
                                        auto_sign_out=1;
                                    }
                                    if(product.getString("option_name").equals("enable_host_authentication"))
                                    {
                                        enable_host_authentication=1;
                                    }
                                    if(product.getString("option_name").equals("enable_host_list"))
                                    {
                                        enable_host_list=1;
                                    }

                                }
                                else
                                {
                                  //  Toast.makeText(getApplicationContext(),product.getString("option_name"),Toast.LENGTH_LONG).show();
                                    if(product.getString("option_name").equals("take_photo_signup"))
                                    {
                                        take_photo_signup=0;
                                    }
                                    if(product.getString("option_name").equals("take_new_pic"))
                                    {
                                        take_new_pic=0;
                                    }
                                    if(product.getString("option_name").equals("kyc"))
                                    {
                                        kyc=0;
                                    }
                                    if(product.getString("option_name").equals("kphone_verification"))
                                    {
                                        kphone_verification=0;
                                    }
                                    if(product.getString("option_name").equals("auto_sign_out"))
                                    {
                                        auto_sign_out=0;
                                    }
                                    if(product.getString("option_name").equals("enable_host_authentication"))
                                    {
                                        enable_host_authentication=0;
                                    }
                                    if(product.getString("option_name").equals("enable_host_list"))
                                    {
                                        enable_host_list=0;
                                    }

                                }
                                //adddesi(product.getString("d_name"),product.getString("id"));

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

                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    private boolean isValidMobile(String phone) {

        boolean check=false;

        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 10 || phone.length() > 10) {
                // if(phone.length() != 10) {
                check = false;
            } else {
                check = true;

            }
        }
        else {
            check=false;

        }
        return check;
    }

}
