package com.wistaster.xapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.wistaster.xapp.Classes.CurrentUser;
import com.wistaster.xapp.Classes.DataHelper;
import com.wistaster.xapp.Classes.Post;
import com.wistaster.xapp.Classes.PostAdapter;
import com.wistaster.xapp.Classes.User;
import com.wistaster.xapp.R;

import java.util.Collections;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    ImageView user;
    ListView listView;
    PostAdapter postAdapter;

    DataHelper dbHelper;

    RuntimeExceptionDao<Post,Integer> postDao;
    RuntimeExceptionDao<User,Integer> userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        user = (ImageView) findViewById(R.id.user);
        listView = (ListView) findViewById(R.id.posts);

        dbHelper = (DataHelper) OpenHelperManager.getHelper(this,DataHelper.class);

        postDao = dbHelper.getPostRuntimeExceptionDao();
        userDao = dbHelper.getUserRuntimeExceptionDao();

        user.setImageResource(userDao.queryForId
                (CurrentUser.getId()).getAvatar());

        List<Post> posts = postDao.queryForAll();

        Collections.reverse(posts);

        postAdapter = new PostAdapter(PostsActivity.this,posts);

        listView.setAdapter(postAdapter);
    }

    public void addPost(View v){
        Intent intent = new Intent(PostsActivity.this, AddPostActivity.class);

        startActivity(intent);
    }

    public void getUser(View v){
        Intent intent = new Intent(PostsActivity.this, UserActivity.class);
        intent.putExtra("id",CurrentUser.getId());

        startActivity(intent);
    }

    public void getAi(View v){
        Intent intent = new Intent(PostsActivity.this, AiChatActivity.class);

        startActivity(intent);
    }

    public void exit(View v){
        Intent intent = new Intent(PostsActivity.this,MainActivity.class);
        CurrentUser.setId(-1); startActivity(intent);
    }
}