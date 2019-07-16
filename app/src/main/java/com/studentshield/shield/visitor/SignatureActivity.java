package com.studentshield.shield.visitor;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.gcacace.signaturepad.views.SignaturePad;

public class SignatureActivity extends AppCompatActivity {

    private SignaturePad mSignaturePad;
    private Button clr;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        clr = (Button)findViewById(R.id.button_clr);
        done = (Button)findViewById(R.id.button_done);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
              // Bitmap b = mSignaturePad.getSignatureBitmap();
                //need to save the signature
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
               // mSignaturePad.clear();
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap b = mSignaturePad.getSignatureBitmap();
            }
        });
    }
}
