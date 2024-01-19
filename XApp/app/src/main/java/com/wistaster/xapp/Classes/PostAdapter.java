package com.wistaster.xapp.Classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wistaster.xapp.Activities.UserActivity;
import com.wistaster.xapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostAdapter extends BaseAdapter {
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private LayoutInflater layoutInflater;
    private List<Post> posts;
    private Context context;
    public PostAdapter(Context context, List<Post> posts) {

        this.context = context;
        this.posts = posts;

        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.post_item,null);

        db = FirebaseFirestore.getInstance();

        if (view != null){
            ImageView user = (ImageView) view.findViewById(R.id.itemPostUser);
            TextView title = (TextView) view.findViewById(R.id.itemPostTitle);
            TextView text = (TextView) view.findViewById(R.id.itemPostText);

            TextView likesTxt = (TextView) view.findViewById(R.id.itemPostTxtLikes);
            ImageView likeView = (ImageView) view.findViewById(R.id.itemPostBtnLikes);

            docRef = db.collection("Users").document(posts.get(i).getUserId());

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    user.setImageResource(documentSnapshot.toObject(User.class).getAvatar());

                    //user.setImageResource(posts.get(i).getUser().getAvatar());

                    title.setText(posts.get(i).getTitle());
                    text.setText(posts.get(i).getText());

                    //List<Like> likes = likeDao.queryForEq("post_id",posts.get(i).getId());

                    List<Like> likes = new ArrayList<>();

                    db.collection("Likes").whereEqualTo("postId",posts.get(i).getId()).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                                        likes.add(document.toObject(Like.class));
                                    }
                                    Optional<Like> c = likes.stream().filter
                                            (l -> l.getUserId().equals(CurrentUser.getCurrUser().getUid())).findFirst();
                                    if(c.isPresent()){
                                        likeView.setImageResource(R.drawable.heart_on);
                                    }
                                    likesTxt.setText(String.valueOf(likes.size()));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            likeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<Like> likes = new ArrayList<>();

                    db.collection("Likes").whereEqualTo("postId",posts.get(i).getId()).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                                        likes.add(document.toObject(Like.class));
                                    }

                                    ImageView likeW = (ImageView) view.findViewById(R.id.itemPostBtnLikes);

                                    if(likes.isEmpty()){

                                        String id = UUID.randomUUID().toString();

                                        Like newLike = new Like(id,CurrentUser.getCurrUser().getUid(),posts.get(i).getId());

                                        db.collection("Likes").document(id).set(newLike)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        likes.add(newLike);

                                                        likeW.setImageResource(R.drawable.heart_on);

                                                        //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                                        TextView likesTxt = (TextView) view.findViewById(R.id.itemPostTxtLikes);
                                                        likesTxt.setText(String.valueOf(likes.size()));
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        Optional<Like> isLike = likes.stream().filter
                                                (l -> l.getUserId().equals(CurrentUser.getCurrUser().getUid())).findFirst();

                                        if(isLike.isPresent()){
                                            db.collection("Likes").document(isLike.get().getId())
                                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            likes.remove(isLike.get());

                                                            likeW.setImageResource(R.drawable.heart_off);

                                                            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                                            TextView likesTxt = (TextView) view.findViewById(R.id.itemPostTxtLikes);
                                                            likesTxt.setText(String.valueOf(likes.size()));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }else{
                                            String id = UUID.randomUUID().toString();

                                            Like newLike = new Like(id,CurrentUser.getCurrUser().getUid(),posts.get(i).getId());

                                            db.collection("Likes").document(id).set(newLike)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            likes.add(newLike);

                                                            likeW.setImageResource(R.drawable.heart_on);

                                                            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                                            TextView likesTxt = (TextView) view.findViewById(R.id.itemPostTxtLikes);
                                                            likesTxt.setText(String.valueOf(likes.size()));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserActivity.class);
                    intent.putExtra("id",posts.get(i).getUserId());

                    context.startActivity(intent);
                }
            });
        }

        return view;

    }
}