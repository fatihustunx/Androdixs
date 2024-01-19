package com.wistaster.xapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wistaster.xapp.Classes.CurrentUser;
import com.wistaster.xapp.R;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText email;
    EditText password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.btnLogin);

        email = (EditText) findViewById(R.id.editTextUserEmail);
        password = (EditText) findViewById(R.id.editTextUserPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();

        if(emailTxt.isEmpty() || passwordTxt.isEmpty()){
            Toast.makeText(LoginActivity.this,"Do Not Empty!",Toast.LENGTH_SHORT).show();
        }else{

            mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            CurrentUser.setCurrUser(mAuth.getCurrentUser());
                            Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, PostsActivity.class);

                            startActivity(intent);
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void shows(View v){

        String allUsers="abc@gmail.com" + " -> " + "passwords";

        /*
        List<User> users = userDao.queryForAll();
        for(User user:users){
            allUsers+="* : " + user.getId() + " " + user.getEmail() + " " + user.getPassword() +
                    " " + user.getGender() + " " + "\n";
        }*/
        Toast.makeText(LoginActivity.this, allUsers, Toast.LENGTH_SHORT).show();
    }

    public void x(View v){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);

        startActivity(intent);
    }
}