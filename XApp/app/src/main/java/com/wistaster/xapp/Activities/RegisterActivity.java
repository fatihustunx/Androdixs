package com.wistaster.xapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wistaster.xapp.Classes.AvatarManager;
import com.wistaster.xapp.R;
import com.wistaster.xapp.Classes.User;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = (Button) findViewById(R.id.btnRegister);
        EditText firstName = (EditText) findViewById(R.id.editTextUserFirstName);
        EditText lastName = (EditText) findViewById(R.id.editTextUserLastName);
        EditText email = (EditText) findViewById(R.id.editTextUserEmail);
        EditText password = (EditText) findViewById(R.id.editTextUserPassword);
        RadioButton rWoman = (RadioButton) findViewById(R.id.rWoman);
        RadioButton rMan = (RadioButton) findViewById(R.id.rMan);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstNameTxt = firstName.getText().toString();
                String lastNameTxt = lastName.getText().toString();
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();

                boolean gMan = rMan.isChecked();

                if(TextUtils.isEmpty(firstNameTxt) ||
                        TextUtils.isEmpty(lastNameTxt) ||
                        TextUtils.isEmpty(emailTxt) ||
                        TextUtils.isEmpty(passwordTxt) ||
                        (!rMan.isChecked() && !rWoman.isChecked())){
                    Toast.makeText(RegisterActivity.this,"Do Not Empty !!",Toast.LENGTH_SHORT).show();
                }else{
                    int avatar;
                    Random random = new Random();
                    int r = random.nextInt(5) + 1;
                    if(gMan){ avatar = AvatarManager.getAvatarMan(r);}
                    else{ avatar = AvatarManager.getAvatarWoman(r);}

                    mAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        mUser = mAuth.getCurrentUser();

                                        user = new User(mUser.getUid(),firstNameTxt,lastNameTxt,emailTxt,passwordTxt,gMan,avatar);

                                        db.collection("Users").document(mUser.getUid()).set(user)
                                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(RegisterActivity.this,"Success!", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                                            startActivity(intent);
                                                        }else{
                                                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    public void x(View v){
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);

        startActivity(intent);
    }
}