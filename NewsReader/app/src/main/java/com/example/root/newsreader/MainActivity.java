package com.example.root.newsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();

    ArrayList<String> content = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteDatabase = this.openOrCreateDatabase("Articles" , MODE_PRIVATE,null);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY , articleid INTEGER , title VARCHAR , content VARCHAR)");

        Downloadtask downloadtask = new Downloadtask();

        try {
            downloadtask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        } catch (Exception e) {

        }


        ListView listview = (ListView) findViewById(R.id.listview);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Intent intent = new Intent(getApplicationContext() , ArticleActivity.class);

                intent.putExtra("content" , content.get(i));

                startActivity(intent);

            }
        });

        update();
    }

    public void update(){

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM  articles" , null);


        int contentindex = c.getColumnIndex("content");
        int titleindex = c.getColumnIndex("title");

        if (c.moveToFirst()){

            titles.clear();
            content.clear();

            do {

                titles.add(c.getString(titleindex));
                content.add(c.getString(contentindex));


            }while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }

    }

    public class Downloadtask extends AsyncTask<String ,Void , String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1){

                    char curr = (char)data;
                    result += curr;
                    data = inputStreamReader.read();
                }

                JSONArray jsonArray = new JSONArray(result);

                int  noi = 20;

                if (jsonArray.length() < 20){

                    noi = jsonArray.length();
                }

                sqLiteDatabase.execSQL("DELETE FROM articles");

                for (int i = 0; i < noi; i++ ){

                    String id = jsonArray.getString(i);
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/"+ id +".json?print=pretty");

                    urlConnection = (HttpURLConnection)url.openConnection();

                     inputStream = urlConnection.getInputStream();

                     inputStreamReader = new InputStreamReader(inputStream);

                     data = inputStreamReader.read();

                     String articleinfo = "";

                    while (data != -1){

                        char curr = (char)data;
                        articleinfo += curr;
                        data = inputStreamReader.read();
                    }

                    JSONObject jsonObject = new JSONObject(articleinfo);

                    if (!jsonObject.isNull("title") && !jsonObject.isNull("url")){

                        String articletitle = jsonObject.getString("title");

                        String articlesId = jsonObject.getString("id");

                        String articleturl = jsonObject.getString("url");

                        url = new URL(articleturl);

                        urlConnection = (HttpURLConnection) url.openConnection();

                        inputStream = urlConnection.getInputStream();

                        inputStreamReader = new InputStreamReader(inputStream);

                        data = inputStreamReader.read();

                        String articlecontent = "";

                        while (data != -1){

                            char curr = (char) data;
                            articlecontent += curr;
                            data = inputStreamReader.read();
                        }



                        String sql = "INSERT INTO articles( articleid , title, content) VALUES (?,?,?)";

                        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);

                        sqLiteStatement.bindString(1, id);
                        sqLiteStatement.bindString(2, articletitle);
                        sqLiteStatement.bindString(3, articlecontent);

                        sqLiteStatement.execute();

                    }

                }



                return result;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            update();
        }
    }
}
