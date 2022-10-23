package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CatImages req = new CatImages();
        req.execute("https://cataas.com/cat?json=true");

    }

    public class CatImages extends AsyncTask<String, Integer, Void> {
        ImageView imageView = findViewById(R.id.imageView);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        String baseUrl = "https://cataas.com";
        Bitmap bm = null;


        protected Void doInBackground(String ... urls) {

            while(true) {


                try {

                    //create a URL object of what server to contact:
                    URL url = new URL(urls[0]);

                    //open the connection
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    //wait for data:
                    InputStream response = urlConnection.getInputStream();

                    //JSON reading:
                    //Build the entire string response:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString(); //result is the whole string

                    // Convert string to JSON:
                    JSONObject cat = new JSONObject(result);

                    // Get json object data
                    String imageUrl = cat.getString("url");
                    String imageFile = cat.getString("file");
//                    imageUrl = "/cat/iGw6OApOjmJmDeoI";
//                    imageFile = "61009bf8caacc400184f6af5.jpeg";

                    File aFile = new File(getFilesDir(), imageFile);
                    // Use saved picture
                    if (aFile.exists()) {
                        bm = BitmapFactory.decodeFile(aFile.getPath());
                    }
                    // Save picture to storage
                    else {
                        FileOutputStream outputStream = openFileOutput(aFile.getName(), Context.MODE_PRIVATE);
                        boolean isPng = imageFile.toLowerCase(Locale.ROOT).contains("png");
                        bm = getBitmapFromURL(baseUrl + imageUrl);
                        bm.compress(isPng ? Bitmap.CompressFormat.PNG
                                : Bitmap.CompressFormat.JPEG, 100, outputStream);

                        outputStream.flush();
                        outputStream.close();
                    }

                    // Progress bar
                    for (int i = 0; i < 100; i++) {
                        try {
                            publishProgress(i);
                            Thread.sleep(30);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }

        protected void onProgressUpdate(Integer ... progress){
            if(progress[0] == 0){
                imageView.setImageBitmap(bm);
            }

            progressBar.setProgress(progress[0]);

        }

        protected void onPostExecute(){

        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


}