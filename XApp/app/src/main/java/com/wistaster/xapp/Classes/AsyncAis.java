package com.wistaster.xapp.Classes;

import android.content.Context;
import android.os.AsyncTask;

import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.wistaster.xapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncAis extends AsyncTask {

    private Context context;
    private String url = "https://api.openai.com/v1/chat/completions";
    private String apiKey = "Bearer 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";

    @Override
    protected Object doInBackground(Object[] objects) {

        Res res = new Res();

        context = (Context) objects[2];

        AiResponse aiResponse = null;

        AiRequest aiRequest = new AiRequest((String) objects[0]);

        try {
            aiResponse = ai(aiRequest);

            if(aiResponse!=null){

                res.aiRes = aiResponse;

                res.aiResTxt = (TextView) objects[1];

            }else{

                res.aiResTxt = (TextView) objects[1];
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    @Override
    protected void onPostExecute(Object o) {

        if (o != null) {
            if(((Res) o).aiRes != null){
                ((Res) o).aiResTxt.setText(((
                        Res) o).aiRes.choices.get(0).message.content);
            }else{
                ((Res) o).aiResTxt.setText("Error !");
                ((Res) o).aiResTxt.setTextColor(
                        ContextCompat.getColor(context,R.color.red));
            }
        }
    }

    public class Res{
        private AiResponse aiRes;
        private TextView aiResTxt;
    }

    public class AiRequest{
        private String model;
        private List<Message> messages;
        public AiRequest(String message){
            this.model = "gpt-3.5-turbo";
            this.messages = new ArrayList<>();
            this.messages.add(new Message("user",message));
        }
    }

    public class AiResponse{
        private List<Choice> choices;

        public class Choice{
            private int index;
            private Message message;
        }
    }
    public class Message{
        private String role;
        private String content;
        public Message(String role, String content){
            this.role=role;
            this.content=content;
        }
    }

    public AiResponse ai(AiRequest aiRequest) throws IOException {

        URL urls = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) urls.openConnection();

        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json");

        conn.setRequestProperty("Authorization", apiKey);

        conn.setDoOutput(true);

        Gson gson = new Gson();

        String json = gson.toJson(aiRequest);

        try (OutputStream os = conn.getOutputStream()) {

            byte[] input = json.getBytes("utf-8");

            os.write(input, 0, input.length);
        }

        int i = conn.getResponseCode();

        if(i == HttpURLConnection.HTTP_OK){
            System.out.println("Success!");
        }else{
            System.err.println("Error -> " + i);
            System.out.println(conn.getResponseMessage());
            return null;
        }

        AiResponse aiResponse;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine = null;
            StringBuilder response = new StringBuilder();
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            aiResponse = gson.fromJson(response.toString(), AiResponse.class);
        }

        conn.disconnect();

        return aiResponse;
    }
}