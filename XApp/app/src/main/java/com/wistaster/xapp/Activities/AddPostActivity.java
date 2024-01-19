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
import com.google.firebase.firestore.FirebaseFirestore;
import com.wistaster.xapp.Classes.CurrentUser;
import com.wistaster.xapp.Classes.Post;
import com.wistaster.xapp.R;

import java.util.Date;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {

    EditText title;
    EditText text;
    Button post;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        title = (EditText) findViewById(R.id.editTextPostTitle);
        text = (EditText) findViewById(R.id.editTextPostText);
        post = (Button) findViewById(R.id.btnAddPost);

        db = FirebaseFirestore.getInstance();
    }

    public void addPost(View v){
        String titleTxt = title.getText().toString();
        String textTxt = text.getText().toString();

        if(titleTxt.isEmpty() || textTxt.isEmpty()){
            Toast.makeText(AddPostActivity.this,"Do Not Empty!",Toast.LENGTH_SHORT).show();
        }else{

            String id = UUID.randomUUID().toString();

            db.collection("Posts").document(id).set(new Post(id,CurrentUser.getCurrUser().getUid(),titleTxt,textTxt, new Date()))
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddPostActivity.this,"Success!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddPostActivity.this, PostsActivity.class);

                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPostActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void posts(View v){
            Intent intent = new Intent(AddPostActivity.this,PostsActivity.class);

            startActivity(intent);
    }
}