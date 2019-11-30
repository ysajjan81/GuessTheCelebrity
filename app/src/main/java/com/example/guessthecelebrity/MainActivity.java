package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
    ImageView imageView;
    int locationOfCorrectAnswer ;
    String[] answers = new String[4];
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public void createQuestion()
    {

        Random random = new Random();
        choosenCeleb = random.nextInt(celebUrls.size());
        DownloadCelebImageTask downloadCelebImageTask = new DownloadCelebImageTask();

        Bitmap celebImage = null;
        try {
            celebImage = downloadCelebImageTask.execute(celebUrls.get(choosenCeleb)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(celebImage);

        locationOfCorrectAnswer  = random.nextInt(4);
        int incorrectAnswerLocation;
        for(int i = 0 ; i<4 ; i++)
        {
            if(i == locationOfCorrectAnswer)
                answers[i] = celebsNames.get(choosenCeleb);
            else
            {
                incorrectAnswerLocation = random.nextInt(celebsNames.size());
                while(incorrectAnswerLocation == locationOfCorrectAnswer)
                    incorrectAnswerLocation = random.nextInt(celebsNames.size());

                answers[i] = celebsNames.get(incorrectAnswerLocation);
            }
        }
        button0.setText(answers[0]);
        button1.setText(answers[1]);
        button2.setText(answers[2]);
        button3.setText(answers[3]);
    }
    public  void celebChosen(View view){
        if(view.getTag().equals(Integer.toString(locationOfCorrectAnswer))){
            Toast.makeText(getApplicationContext(), "Currect ", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Wrong! It was " + celebsNames.get(choosenCeleb), Toast.LENGTH_LONG).show();
        }
        createQuestion();
    }

    public  class DownloadCelebImageTask extends AsyncTask<String, Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url;
            try {
                url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
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
        imageView = findViewById(R.id.picBox);
        button0 = findViewById(R.id.option1);
        button1 = findViewById(R.id.option2);
        button2 = findViewById(R.id.option3);
        button3 = findViewById(R.id.option4);
        DownloadTast task = new DownloadTast();
        try {
            String Result = task.execute("http://www.posh24.se/kandisar").get();
            String[] temp = Result.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
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
            createQuestion();
//            Random random = new Random();
//            choosenCeleb = random.nextInt(celebUrls.size());
//            DownloadCelebImageTask downloadCelebImageTask = new DownloadCelebImageTask();
//
//                Bitmap celebImage = downloadCelebImageTask.execute(celebUrls.get(choosenCeleb)).get();
//                imageView.setImageBitmap(celebImage);
//
//                locationOfCorrectAnswer  = random.nextInt(4);
//                int incorrectAnswerLocation;
//                for(int i = 0 ; i<4 ; i++)
//                {
//                    if(i == locationOfCorrectAnswer)
//                        answers[i] = celebsNames.get(choosenCeleb);
//                    else
//                    {
//                        incorrectAnswerLocation = random.nextInt(celebsNames.size());
//                        while(incorrectAnswerLocation == locationOfCorrectAnswer)
//                            incorrectAnswerLocation = random.nextInt(celebsNames.size());
//                        answers[i] = celebsNames.get(incorrectAnswerLocation);
//                    }
//                }
//                button0.setText(answers[0]);
//                button1.setText(answers[1]);
//                button2.setText(answers[2]);
//                button3.setText(answers[3]);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
