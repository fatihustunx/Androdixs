package com.wistaster.xapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wistaster.xapp.Classes.AsyncAis;
import com.wistaster.xapp.R;

import java.io.IOException;


public class AiChatActivity extends AppCompatActivity {

    EditText user; TextView ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        user = (EditText) findViewById(R.id.user);
        ai = (TextView) findViewById(R.id.ai);
    }

    public void G(View v) throws IOException {

        String req = String.valueOf(user.getText());

        new AsyncAis().execute(req,ai,this);

        user.setText(""); ai.setText("");

        ai.setHint("Generating...");
    }

    public void posts(View v){

        Intent intent = new Intent(AiChatActivity.this,
                PostsActivity.class);

        startActivity(intent);
    }
}