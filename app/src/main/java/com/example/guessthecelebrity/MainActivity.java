package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebUrls = new ArrayList<String>();
    ArrayList<String> celebsNames = new ArrayList<String>();
    int choosenCeleb;
    public  class DownloadCelebImageTask extends AsyncTask<String, Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url;
            try {
                url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                Bitmap bitmap ;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
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

            Pattern p = Pattern.compile("src=\"(.*?)\"");
            Matcher m = p.matcher(temp[0]);
            while ((m.find()))
            {
//                System.out.println(m.group(1));
                celebUrls.add(m.group(1));
            }
             p = Pattern.compile("alt=\"(.*?)\"");
             m = p.matcher(temp[0]);
            while ((m.find())){
//                System.out.println(m.group(1));
                  celebsNames.add(m.group(1));
            }
            Random random = new Random();
            choosenCeleb = random.nextInt(celebUrls.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
