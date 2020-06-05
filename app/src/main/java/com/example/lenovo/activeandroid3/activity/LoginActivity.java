package com.example.lenovo.activeandroid3.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.activity.CreateAccountActivity;
import com.example.lenovo.activeandroid3.activity.v1.activity.V1MainActivity;
import com.example.lenovo.activeandroid3.util.Conversions;

public class LoginActivity extends AppCompatActivity
{

    //    TextInputLayout til_email , til_home_name ;
    Context context ;

    Button btn_create_account ,btn_login ;
    EditText et_email_address , et_password ;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState) ;
        setContentView( R.layout.activity_login) ;

        context = this ;

        et_email_address = ( EditText )findViewById( R.id.et_email_address ) ;
        et_password = ( EditText )findViewById( R.id.et_password ) ;


//        et_email_address.setText("kirankokate031@gmail.com");
//        et_password.setText("kiran");

        final SharedPreferences  sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        String    isTableCreated = sharedPref.getString("isTableCreated", "");

//        til_email = ( TextInputLayout )findViewById( R.id.til_email ) ;
//        til_home_name = ( TextInputLayout )findViewById( R.id.til_home_name ) ;


        btn_create_account = ( Button )findViewById( R.id.btn_create_account ) ;
        btn_login = ( Button )findViewById( R.id.btn_login ) ;

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent veriy = new Intent( LoginActivity.this, CreateAccountActivity.class ) ;
                startActivity( veriy ) ;
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                boolean cancel = false;
                View focusView = null;

                String email_address = et_email_address.getText().toString() ;
                String password = et_password.getText().toString() ;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if ( TextUtils.isEmpty( email_address ) )
                {
                    et_email_address.setError("Enter Email Address");
                    focusView = et_email_address;
                    cancel = true;
                }else if( ! email_address.matches(emailPattern) )
                {
                    et_email_address.setError("Enter Valid Email");
                    focusView = et_email_address ;
                    cancel = true;
                }else if ( TextUtils.isEmpty( password ) )
                {
                    et_password.setError("Enter Your Password");
                    focusView = et_password;
                    cancel = true;
                }
                if ( cancel )
                {
                    focusView.requestFocus();
                } else
                {

                    String  emailShared =   sharedPref.getString("email", "");
                    String  passwordShared = sharedPref.getString("password", "");
                    if(! email_address.equals( emailShared ) ) {
                        et_email_address.setError("incorrect email address");
                        et_email_address.requestFocus();
                    }else  if( ! passwordShared.equals( password )) {
                        et_password.setError("incorrect password");
                        et_password.requestFocus();
                    }else {

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("isPreviousLogin", "1") ;
                        editor.commit() ;
                        editor.apply() ;

                        Intent veriy = new Intent( LoginActivity.this, SplashScreen.class ) ;
                        startActivity( veriy ) ;
                    }


//                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putString("homeName", Conversions.makeFirstLeterCap(homeName ) ) ;
//                    editor.putString("email", email ) ;
//                    editor.commit() ;
//                    editor.apply() ;
                }
            }

        }) ;
    }
}
