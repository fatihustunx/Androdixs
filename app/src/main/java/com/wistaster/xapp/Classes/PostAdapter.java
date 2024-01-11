package com.wistaster.xapp.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.query.In;
import com.wistaster.xapp.Activities.UserActivity;
import com.wistaster.xapp.Classes.Post;
import com.wistaster.xapp.R;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PostAdapter extends BaseAdapter {
    private RuntimeExceptionDao<User,Integer> userDao;
    private RuntimeExceptionDao<Like,Integer> likeDao;
    private LayoutInflater layoutInflater;
    private DataHelper dbHelper;
    private List<Post> posts;
    private Context context;
    ImageView likeView, user;
    TextView title,text;
    TextView likesTxt;
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

        dbHelper = (DataHelper) OpenHelperManager.getHelper(context,DataHelper.class);

        userDao = dbHelper.getUserRuntimeExceptionDao();
        likeDao = dbHelper.getLikeRuntimeExceptionDao();

        if (view != null){
            user = (ImageView) view.findViewById(R.id.itemPostUser);
            title = (TextView) view.findViewById(R.id.itemPostTitle);
            text = (TextView) view.findViewById(R.id.itemPostText);

            likesTxt = (TextView) view.findViewById(R.id.itemPostTxtLikes);
            likeView = (ImageView) view.findViewById(R.id.itemPostBtnLikes);
            
            user.setImageResource(posts.get(i).getUser().getAvatar());

            title.setText(posts.get(i).getTitle());
            text.setText(posts.get(i).getText());

            List<Like> likes = likeDao.queryForEq("post_id",posts.get(i).getId());
            Optional<Like> c = likes.stream().filter
                    (l -> l.getUser().getId() == CurrentUser.getId()).findFirst();
            if(c.isPresent()){
                likeView.setImageResource(R.drawable.heart_on);
            }

            likesTxt.setText(String.valueOf(likes.size()));

            likeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<Like> likes = likeDao.
                            queryForEq("post_id",posts.get(i).getId());

                    ImageView likeW = (ImageView) view.findViewById(R.id.itemPostBtnLikes);

                    if(likes.isEmpty()){
                        User user = userDao.queryForId(CurrentUser.getId());
                        Like newLike = new Like(user,posts.get(i));
                        likeDao.create(newLike);
                        likes.add(newLike);

                        likeW.setImageResource(R.drawable.heart_on);
                    }else{
                        Optional<Like> isLike = likes.stream().filter
                                (l -> l.getUser().getId() == CurrentUser.getId()).findFirst();

                        if(isLike.isPresent()){
                            likeDao.delete(isLike.get());
                            likes.remove(isLike.get());

                            likeW.setImageResource(R.drawable.heart_off);
                        }else{
                            User user = userDao.queryForId(CurrentUser.getId());
                            Like newLike = new Like(user,posts.get(i));
                            likeDao.create(newLike);
                            likes.add(newLike);

                            likeW.setImageResource(R.drawable.heart_on);
                        }
                    }
                    TextView likesTxt = (TextView) view.findViewById(R.id.itemPostTxtLikes);
                    likesTxt.setText(String.valueOf(likes.size()));
                }
            });

            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserActivity.class);
                    intent.putExtra("id",posts.get(i).getUser().getId());

                    context.startActivity(intent);
                }
            });
        }

        return view;
    }
}