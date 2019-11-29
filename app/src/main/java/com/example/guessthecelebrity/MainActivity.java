package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class DownloadTast extends AsyncTask<String, Void , String>{

        @Override
        protected String doInBackground(String... strings) {
            String result  = "";
            URL url;
            try{
                url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while(data != -1)
                {
                    result += (char)data;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return "Failed";
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadTast task = new DownloadTast();
        try {
            String Result = task.execute("http://www.posh24.se/kandisar").get();
            String[] temp = Result.split("<div class=\"sidebarContainer\">");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
