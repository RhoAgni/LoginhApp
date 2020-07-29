package com.example.ifortechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    boolean EmailValid=false;
    boolean PasswordValid=false;

    CurrentData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        data = (CurrentData) Utility.loadSettings("Current.dat",this);
        if(data==null){
            data = new CurrentData();
            System.out.println("[Data read] Created New Data instead");
        }
        else{
            System.out.println("[Data read] Loaded, Current Session Key : "+data.SessionKey);
            if(data.RememberMe==true){
                CheckBox checkBox = (CheckBox)findViewById(R.id.rememberMe);
                checkBox.setChecked(true);
                EditText email = (EditText)findViewById(R.id.EmailBox);
                email.setText(data.Email);
            }
            if(data.StayConnected){
                //REUSE SESSION KEY
                Intent newAct = new Intent(LoginActivity.this, HomeActivity.class);
                newAct.putExtra("SessCode",data.SessionKey);
                startActivity(newAct);
            }
        }
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
        EditText email = (EditText) findViewById(R.id.EmailBox);
        EditText password = (EditText) findViewById(R.id.PasswordBox);

        ControlEmail();
        ControlPassword();

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ControlEmail();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ControlPassword();
            }
        });
    }

    public void Login(View view){
        EditText email = (EditText)findViewById(R.id.EmailBox);
        EditText password = (EditText)findViewById(R.id.PasswordBox);
        CheckBox rememberMe = (CheckBox)findViewById(R.id.rememberMe);
        CheckBox stayConnected = (CheckBox)findViewById(R.id.stayConnected);

        boolean passwordValid=false;
        boolean emailValid=false;

        String tempSessionKey = "";

        HashMap<String, String> loginDetail = new HashMap<String, String>();
        loginDetail.put("Name",email.getText().toString());
        loginDetail.put("Password",password.getText().toString());
        if(EmailValid && PasswordValid){
            Toast.makeText(this, "Logging In...",Toast.LENGTH_LONG).show();
            tempSessionKey = LoginUtility.performPostCall(loginDetail);
            System.out.println("Temp Session Key : "+tempSessionKey);
            if(tempSessionKey.equals("wrong") || tempSessionKey.equals("empty") || tempSessionKey.equals("Empty") || tempSessionKey.equals("") || tempSessionKey==null){
                Toast.makeText(this, "Invalid Credentials",Toast.LENGTH_LONG).show();
            }
            else{
                System.out.println("Current Session Key : "+tempSessionKey);
                Toast.makeText(this, "Logged In",Toast.LENGTH_LONG).show();
                if(rememberMe.isChecked()){
                    data.Email=email.getText().toString();
                    data.RememberMe=true;
                }
                else{
                    data.RememberMe=false;
                    data.Email="";
                }
                if(stayConnected.isChecked()){
                    data.SessionKey=tempSessionKey;
                    data.StayConnected=true;
                }
                else{
                    data.SessionKey="";
                    data.StayConnected=false;
                }
                Utility.saveSettings("Current.dat",data,this);
                System.out.println("Sessionkey log in : "+tempSessionKey);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        }
        else{
            Toast.makeText(this, "Some Fields Are Not Filled Correctly",Toast.LENGTH_LONG).show();
        }
    }
    public void ControlEmail(){

        EditText email = (EditText)findViewById(R.id.EmailBox);

        if(Utility.isEmailValid(email.getText().toString())){
            EmailValid=true;
            email.setBackgroundColor(Color.argb(30, 0, 0, 0));
        }
        else{
            EmailValid=false;
            email.setBackgroundColor(Color.argb(30, 255, 0, 0));
            email.setError("Invalid Email");
        }
    }

    public void ControlPassword(){
        EditText password = (EditText)findViewById(R.id.PasswordBox);
        boolean[] Checkers = Utility.isPasswordValid(password.getText().toString());
        if(Checkers[0] && Checkers[1] && Checkers[2]){
            PasswordValid=true;
            password.setBackgroundColor(Color.argb(30, 0, 0, 0));
        }
        else{
            String warning = "";
            if(!Checkers[0]){
                warning+=" No numbers ";
            }
            if(!Checkers[1]){
                warning+=" Not long enough ";
            }
            if(!Checkers[2]){
                warning+=" No lowercase ";
            }
            PasswordValid=false;
            password.setBackgroundColor(Color.argb(30, 255, 0, 0));
            password.setError(warning);
        }
    }
}
