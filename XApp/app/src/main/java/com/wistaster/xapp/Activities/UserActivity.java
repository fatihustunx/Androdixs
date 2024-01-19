package com.wistaster.xapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wistaster.xapp.Classes.User;
import com.wistaster.xapp.R;

public class UserActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");

        db = FirebaseFirestore.getInstance();

        //User user = userDao.queryForId(id);

        ImageView userAvatar = (ImageView) findViewById(R.id.userAvatar);
        TextView userEmails = (TextView) findViewById(R.id.userEmails);
        TextView userTxts = (TextView) findViewById(R.id.userTxts);
        Button button = (Button) findViewById(R.id.btnPosts);

        db.collection("Users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);

                        userAvatar.setImageResource(user.getAvatar());

                        userTxts.setText(String.valueOf(user.getFirstName()
                                + " " + user.getLastName()));

                        userEmails.setText(user.getEmail());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void posts(View v){

        Intent intent = new Intent(UserActivity.this,PostsActivity.class);

        startActivity(intent);
    }
}