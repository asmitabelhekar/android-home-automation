package com.example.lenovo.activeandroid3.activity.v1.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.LoginActivity;
import com.example.lenovo.activeandroid3.util.Conversions;

public class CreateAccountActivity extends AppCompatActivity {

    EditText et_first_name , et_last_name , et_email_address ,et_password ,et_gender, et_dob ;
    Button btn_create_account ;
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        context = this ;

        et_first_name = (EditText )findViewById( R.id.et_first_name );
        et_last_name = (EditText )findViewById( R.id.et_last_name ) ;
        et_email_address = (EditText )findViewById( R.id.et_email_address ) ;
        et_password = (EditText )findViewById( R.id.et_password ) ;
        et_gender = (EditText )findViewById( R.id.et_gender ) ;
//        et_dob = (EditText )findViewById( R.id.et_dob ) ;
        btn_create_account = ( Button )findViewById( R.id.btn_create_account );


//        et_first_name.setText("kiran");
//        et_last_name.setText("kokate");
//        et_email_address.setText("kirankokate031@gmail.com");
//        et_password.setText("kiran");
//        et_gender.setText("Male");


        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean cancel = false;
                View focusView = null;


                String firstName = et_first_name.getText().toString() ;
                String lastName = et_last_name.getText().toString() ;
                String emailAddress = et_email_address.getText().toString() ;
                String password = et_password.getText().toString() ;
                String gender = et_gender.getText().toString() ;
//                String dob = et_dob.getText().toString() ;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if ( TextUtils.isEmpty( firstName ) )
                {
                    et_first_name.setError("Enter First Name");
                    focusView = et_first_name;
                    cancel = true;
                }else if ( TextUtils.isEmpty( lastName ) )
                {
                    et_last_name.setError("Enter Last Name");
                    focusView = et_last_name;
                    cancel = true;
                }else if( ! emailAddress.matches(emailPattern) )
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
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("firstName", Conversions.makeFirstLeterCap(firstName ) ) ;
                    editor.putString("lastName", Conversions.makeFirstLeterCap(lastName ) ) ;
                    editor.putString("email", emailAddress ) ;
                    editor.putString("password", password ) ;
                    editor.putString("gender", gender ) ;
//                    editor.putString("dob", dob ) ;
                    editor.commit() ;
                    editor.apply() ;

                    Intent veriy = new Intent( CreateAccountActivity.this, LoginActivity.class ) ;
                    startActivity( veriy ) ;

                }

            }
        });
    }
}
