package com.wistaster.xapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.wistaster.xapp.Classes.AvatarManager;
import com.wistaster.xapp.Classes.DataHelper;
import com.wistaster.xapp.R;
import com.wistaster.xapp.Classes.User;

import java.util.List;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

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

        DataHelper dbHelper = (DataHelper) OpenHelperManager.getHelper(this,DataHelper.class);
        final RuntimeExceptionDao<User,Integer> userDao = dbHelper.getUserRuntimeExceptionDao();

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
                    for (User user : userDao.queryForAll()) {
                        if (emailTxt.equals(user.getEmail())) {
                            Toast.makeText(RegisterActivity.this, "Email is Used!!", Toast.LENGTH_SHORT).show();

                            return;
                        }
                    }
                    int avatar;
                    Random random = new Random();
                    int r = random.nextInt(5) + 1;
                    if(gMan){ avatar = AvatarManager.getAvatarMan(r);}
                    else{ avatar = AvatarManager.getAvatarWoman(r);}
                    userDao.create(new User(firstNameTxt,lastNameTxt,emailTxt,passwordTxt,gMan,avatar));
                    //firstName.setText(""); lastName.setText(""); email.setText(""); password.setText("");
                    Toast.makeText(RegisterActivity.this,"Success!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                    startActivity(intent);
                }
            }
        });
    }
    public void x(View v){
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);

        startActivity(intent);
    }
}