package com.wistaster.xapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wistaster.xapp.Classes.CurrentUser;
import com.wistaster.xapp.Classes.Post;
import com.wistaster.xapp.Classes.PostAdapter;
import com.wistaster.xapp.Classes.User;
import com.wistaster.xapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    int avatar;
    List<Post> posts;
    ImageView userImg;
    ListView listView;
    PostAdapter postAdapter;
    FirebaseFirestore db;
    DocumentReference docRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        userImg = (ImageView) findViewById(R.id.user);
        listView = (ListView) findViewById(R.id.posts);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        docRef = db.collection("Users").document(CurrentUser.getCurrUser().getUid());

        docRef.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                avatar = user.getAvatar();

                userImg.setImageResource(avatar);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostsActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //List<Post> posts = postDao.queryForAll();

        posts = new ArrayList<>();

        db.collection("Posts").get()
                .addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            posts.add(document.toObject(Post.class));
                        }

                        Collections.sort(posts, new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return o2.getCreationTimes().compareTo(o1.getCreationTimes());
                            }
                        });

                        postAdapter = new PostAdapter(PostsActivity.this,posts);

                        listView.setAdapter(postAdapter);
                    }
                }).addOnFailureListener(this,new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addPost(View v){
        Intent intent = new Intent(PostsActivity.this, AddPostActivity.class);

        startActivity(intent);
    }

    public void getUser(View v){
        Intent intent = new Intent(PostsActivity.this, UserActivity.class);
        intent.putExtra("id",CurrentUser.getCurrUser().getUid());

        startActivity(intent);
    }

    public void getAi(View v){
        Intent intent = new Intent(PostsActivity.this, AiChatActivity.class);

        startActivity(intent);
    }

    public void exit(View v){
        Intent intent = new Intent(PostsActivity.this,MainActivity.class);

        mAuth.signOut();

        startActivity(intent);
    }
}