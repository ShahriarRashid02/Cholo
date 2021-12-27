package com.example.cholov2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class reg_page extends AppCompatActivity {
    EditText eFname,eemail,ephonenum,epassword;
    Button rbtn;
    ProgressBar progressBar;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_page);
        eFname =findViewById(R.id.fname);
        eemail = findViewById(R.id.email);
        ephonenum=findViewById(R.id.pnum);
        epassword=findViewById(R.id.password);
        //progressBar=findViewById(R.id.progressBar);
        rbtn=findViewById(R.id.subbtn);

        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =eFname.getText().toString();
                String email =eemail.getText().toString();
                String phonenum =ephonenum.getText().toString();
                String password =epassword.getText().toString();

                if (name.isEmpty()){
                    eFname.setError("Name Required!");
                    return;
                }
                String namePattern="[a-zA-Z]";
                if (!name.matches("^[A-Za-z]+$")){
                    eFname.setError("Name should be A-Z and a-z");
                    return;
                }
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (email.isEmpty()){
                    eemail.setError("Email Required");
                    return;
                }
                if (!email.matches(emailPattern)){
                    eemail.setError("Enter Valid Email! ");
                    return;
                }
                if(phonenum.length()<10 || phonenum.length()>11){
                    ephonenum.setError("Number Should be 11 degit");
                    return;
                }
                if (password.isEmpty()){
                    epassword.setError("Password Required ! ");
                    return;
                }
                if (password.length()<8){
                    epassword.setError("Password Should be greater than 8 Character !");
                    return;
                }

                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("user");
                regHelper helperClass= new regHelper(name,phonenum,email,password);
                reference.child(phonenum).setValue(helperClass);
                Toast.makeText(reg_page.this,"Registration Sucess",Toast.LENGTH_LONG).show();

                Intent intent =new Intent(reg_page.this,login.class);
                startActivity(intent);
            }
        });

    }
}