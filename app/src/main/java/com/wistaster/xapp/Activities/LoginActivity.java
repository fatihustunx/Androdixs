package com.wistaster.xapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.wistaster.xapp.Classes.CurrentUser;
import com.wistaster.xapp.Classes.DataHelper;
import com.wistaster.xapp.R;
import com.wistaster.xapp.Classes.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText email;
    EditText password;

    DataHelper dbHelper;
    RuntimeExceptionDao<User,Integer> userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.btnLogin);

        email = (EditText) findViewById(R.id.editTextUserEmail);
        password = (EditText) findViewById(R.id.editTextUserPassword);

        dbHelper = (DataHelper) OpenHelperManager.getHelper(this,DataHelper.class);
        userDao = dbHelper.getUserRuntimeExceptionDao();
    }

    public void login(View view) {
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();

        if(emailTxt.isEmpty() || passwordTxt.isEmpty()){
            Toast.makeText(LoginActivity.this,"Do Not Empty!",Toast.LENGTH_SHORT).show();
        }else{
            for (User user : userDao.queryForAll()) {
                if (emailTxt.equals(user.getEmail())) {
                    if (passwordTxt.equals(user.getPassword())) {
                        CurrentUser.setId(user.getId());
                    }
                }
            }

            if (userDao.idExists(CurrentUser.getId())) {
                // email.setText(""); password.setText("");

                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, PostsActivity.class);

                startActivity(intent);

            } else {
                Toast.makeText(LoginActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shows(View v){
        String allUsers="";
        List<User> users = userDao.queryForAll();
        for(User user:users){
            allUsers+="* : " + user.getId() + " " + user.getEmail() + " " + user.getPassword() +
                    " " + user.getGender() + " " + "\n";
        }
        Toast.makeText(LoginActivity.this, allUsers, Toast.LENGTH_SHORT).show();
    }

    public void x(View v){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);

        startActivity(intent);
    }
}