package com.wistaster.xapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.wistaster.xapp.Classes.DataHelper;
import com.wistaster.xapp.Classes.User;
import com.wistaster.xapp.R;

public class UserActivity extends AppCompatActivity {

    DataHelper dbHelper;

    RuntimeExceptionDao<User,Integer> userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id",-1);

        dbHelper = (DataHelper) OpenHelperManager.getHelper(this, DataHelper.class);

        userDao = dbHelper.getUserRuntimeExceptionDao();

        User user = userDao.queryForId(id);

        ImageView userAvatar = (ImageView) findViewById(R.id.userAvatar);
        TextView userEmails = (TextView) findViewById(R.id.userEmails);
        TextView userTxts = (TextView) findViewById(R.id.userTxts);

        Button button = (Button) findViewById(R.id.btnPosts);

        userAvatar.setImageResource(user.getAvatar());

        userTxts.setText(String.valueOf(user.getFirstName()
                + " " + user.getLastName()));

        userEmails.setText(user.getEmail());
    }

    public void posts(View v){

        Intent intent = new Intent(UserActivity.this,PostsActivity.class);

        startActivity(intent);
    }
}