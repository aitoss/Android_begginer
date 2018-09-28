package com.example.root.guesstheceleb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {


    ArrayList<String> celeburl = new ArrayList<String>();
    ArrayList<String> celebname = new ArrayList<String>();
    int chosen = 0;
    ImageView imageView;
    int location ;
    String[] answers = new String[4];
    Button option1;
    Button option2;
    Button option3;
    Button option4;

    Bitmap celebImage;
    public void Chosen(View view){

        if (view.getTag().toString().equals(Integer.toString(location))){
            Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(MainActivity.this, "Wrong! It was "+ celebname.get(chosen), Toast.LENGTH_SHORT).show();
        }
        generator();
    }

    public class ImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.connect();

                InputStream in = httpURLConnection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class Downloadtask extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {

                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data  = inputStreamReader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;

                    data = inputStreamReader.read();
                }

                return  result;

            }
            catch (Exception e){
                e.printStackTrace();

                return null;
            }


        }
    }
    public void generator(){


        Random random = new Random();
        chosen = random.nextInt(celeburl.size());

        ImageDownloader imageTask = new ImageDownloader();

        Bitmap celebImage;

        try {

            celebImage = imageTask.execute(celeburl.get(chosen)).get();

            imageView.setImageBitmap(celebImage);

            location = random.nextInt(4);

            int incorrectAnswerLocation;

            for (int i=0; i<4; i++) {

                if (i == location) {

                    answers[i] = celebname.get(chosen);

                } else {

                    incorrectAnswerLocation = random.nextInt(celeburl.size());

                    while (incorrectAnswerLocation == chosen) {

                        incorrectAnswerLocation = random.nextInt(celeburl.size());

                    }

                    answers[i] = celebname.get(incorrectAnswerLocation);


                }


            }

           option1.setText(answers[0]);
            option2.setText(answers[1]);
            option3.setText(answers[2]);
            option4.setText(answers[3]);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Downloadtask Task = new Downloadtask();
        imageView = (ImageView)findViewById(R.id.imageView);
        option1 = (Button)findViewById(R.id.option1);
        option2 = (Button)findViewById(R.id.option2);
        option3 = (Button)findViewById(R.id.option3);
        option4 = (Button)findViewById(R.id.option4);

        String result = null;

        try {
            result = Task.execute("https://www.imdb.com/list/ls074596441").get();

            String[] split = result.split("<div class=\"lister-list\">");
            Pattern pattern = Pattern.compile("src=\"(.*?)\"");
            Matcher matcher  = pattern.matcher(split[1]);
            while (matcher.find()){
                celeburl.add(matcher.group(1));
            }

            pattern = Pattern.compile("img alt=\"(.*?)\"");
             matcher  = pattern.matcher(split[1]);
            while (matcher.find()){
                celebname.add(matcher.group(1));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        generator();

    }
}
