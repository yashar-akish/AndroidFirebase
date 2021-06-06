package com.company.androidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneSignActivity extends AppCompatActivity {

    EditText phoneNumber, smsCode;
    Button sendCode, signPhone;
    String sentCode;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sign);
        getSupportActionBar().setTitle("Phone Number sign In");

        phoneNumber = findViewById(R.id.phoneEt);
        smsCode = findViewById(R.id.codeEt);
        sendCode = findViewById(R.id.smsBtn);
        signPhone = findViewById(R.id.phoneSignInBtn);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhoneNumber = phoneNumber.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhoneNumber,
                        60, TimeUnit.SECONDS, PhoneSignActivity.this, mCallBack);
            }
        });

        signPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signWithPhoneCode();
            }
        });
    }

    public void signWithPhoneCode(){
        String enterUserCode = smsCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sentCode, enterUserCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(PhoneSignActivity.this, main_menu.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(PhoneSignActivity.this, "The Code is not correct!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            sentCode = s;
        }
    };
}