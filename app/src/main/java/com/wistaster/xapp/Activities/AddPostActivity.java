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
import com.wistaster.xapp.Classes.Post;
import com.wistaster.xapp.Classes.User;
import com.wistaster.xapp.R;

public class AddPostActivity extends AppCompatActivity {

    EditText title;
    EditText text;
    Button post;

    DataHelper dbHelper;

    RuntimeExceptionDao<Post,Integer> postDao;
    RuntimeExceptionDao<User,Integer> userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        title = (EditText) findViewById(R.id.editTextPostTitle);
        text = (EditText) findViewById(R.id.editTextPostText);
        post = (Button) findViewById(R.id.btnAddPost);

        dbHelper = (DataHelper) OpenHelperManager.getHelper(this,DataHelper.class);
        postDao = dbHelper.getPostRuntimeExceptionDao();
        userDao = dbHelper.getUserRuntimeExceptionDao();
    }

    public void addPost(View v){
        String titleTxt = title.getText().toString();
        String textTxt = text.getText().toString();

        if(titleTxt.isEmpty() || textTxt.isEmpty()){
            Toast.makeText(AddPostActivity.this,"Do Not Empty!",Toast.LENGTH_SHORT).show();
        }else{
            User user = userDao.queryForId(CurrentUser.getId());
            postDao.create(new Post(user,titleTxt,textTxt));

            Toast.makeText(AddPostActivity.this,"Success!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AddPostActivity.this, PostsActivity.class);

            startActivity(intent);
        }
    }

    public void posts(View v){
            Intent intent = new Intent(AddPostActivity.this,PostsActivity.class);

            startActivity(intent);
    }
}