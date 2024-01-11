package com.wistaster.xapp.Classes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataHelper extends OrmLiteSqliteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME="XApp";
    private static int DATABASE_VERSION=8;
    private RuntimeExceptionDao<User,Integer> userRuntimeDao=null;
    private RuntimeExceptionDao<Post,Integer> postRuntimeDao=null;
    private RuntimeExceptionDao<Like,Integer> likeRuntimeDao=null;

    public DataHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource,User.class);
            TableUtils.createTable(connectionSource,Post.class);
            TableUtils.createTable(connectionSource,Like.class);
        }catch (SQLException e){
            e.printStackTrace();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(connectionSource,User.class,true);
            TableUtils.dropTable(connectionSource,Post.class,true);
            TableUtils.dropTable(connectionSource,Like.class,true);
            onCreate(database,connectionSource);
        }catch (SQLException e){
            e.printStackTrace();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public RuntimeExceptionDao<User,Integer> getUserRuntimeExceptionDao(){
        if(userRuntimeDao == null){
            userRuntimeDao = getRuntimeExceptionDao(User.class);
        }
        return userRuntimeDao;
    }
    public RuntimeExceptionDao<Post,Integer> getPostRuntimeExceptionDao(){
        if(postRuntimeDao == null){
            postRuntimeDao = getRuntimeExceptionDao(Post.class);
        }
        return postRuntimeDao;
    }
    public RuntimeExceptionDao<Like,Integer> getLikeRuntimeExceptionDao(){
        if(likeRuntimeDao == null){
            likeRuntimeDao = getRuntimeExceptionDao(Like.class);
        }
        return likeRuntimeDao;
    }
}