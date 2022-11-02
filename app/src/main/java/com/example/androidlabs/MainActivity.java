package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String ITEM_NAME = "NAME";
    public static final String ITEM_HEIGHT = "HEIGHT";
    public static final String ITEM_MASS = "MASS";
    ArrayList<ListItem> elements = new ArrayList<>();
    MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView theList = findViewById(R.id.listView);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        Starwars req = new Starwars();
        req.execute("https://swapi.dev/api/people/?format=json");

        System.out.println(elements.toString());

        adapter = new MyListAdapter();

        theList.setAdapter(adapter);

        theList.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            ListItem listItem = elements.get(position);
            dataToPass.putString(ITEM_NAME, listItem.getName());
            dataToPass.putInt(ITEM_HEIGHT, listItem.getHeight());
            dataToPass.putInt(ITEM_MASS, listItem.getWeight());

            if (isTablet) {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(MainActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

    }

    private class MyListAdapter extends BaseAdapter{

        public int getCount() { return elements.size();}

        public ListItem getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.row_layout, parent, false);

            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.textGoesHere);
            tView.setText( getItem(position).getName() );

            //return it to be put in the table
            return newView;
        }
    }

    public class Starwars extends AsyncTask<String, Integer, ArrayList<ListItem>> {

        @Override
        protected  ArrayList<ListItem> doInBackground(String... urls) {
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
                JSONObject json = new JSONObject(result);

                JSONArray results = json.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject object = results.getJSONObject(i);

                    elements.add(new ListItem(object.getString("name"), object.getInt("height"), object.getInt("mass")));
                }

                return elements;
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(ArrayList<ListItem> arrayList){
            adapter.notifyDataSetChanged();
        }

    }

    private class ListItem {
        private String name;
        private int height, weight;

        public ListItem(String name, int height, int weight) {
            this.name = name;
            this.height = height;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public int getHeight() {
            return height;
        }

        public int getWeight() {
            return weight;
        }

    }


//    public class CatImages extends AsyncTask<String, Integer, Void> {
//        ImageView imageView = findViewById(R.id.imageView);
//        ProgressBar progressBar = findViewById(R.id.progressBar);
//        String baseUrl = "https://cataas.com";
//        Bitmap bm = null;
//
//
//        protected Void doInBackground(String ... urls) {
//
//            while(true) {
//
//
//                try {
//
//                    //create a URL object of what server to contact:
//                    URL url = new URL(urls[0]);
//
//                    //open the connection
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                    //wait for data:
//                    InputStream response = urlConnection.getInputStream();
//
//                    //JSON reading:
//                    //Build the entire string response:
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    String result = sb.toString(); //result is the whole string
//
//                    // Convert string to JSON:
//                    JSONObject cat = new JSONObject(result);
//
//                    // Get json object data
//                    String imageUrl = cat.getString("url");
//                    String imageFile = cat.getString("file");
////                    imageUrl = "/cat/iGw6OApOjmJmDeoI";
////                    imageFile = "61009bf8caacc400184f6af5.jpeg";
//
//                    File aFile = new File(getFilesDir(), imageFile);
//                    // Use saved picture
//                    if (aFile.exists()) {
//                        bm = BitmapFactory.decodeFile(aFile.getPath());
//                    }
//                    // Save picture to storage
//                    else {
//                        FileOutputStream outputStream = openFileOutput(aFile.getName(), Context.MODE_PRIVATE);
//                        boolean isPng = imageFile.toLowerCase(Locale.ROOT).contains("png");
//                        bm = getBitmapFromURL(baseUrl + imageUrl);
//                        bm.compress(isPng ? Bitmap.CompressFormat.PNG
//                                : Bitmap.CompressFormat.JPEG, 100, outputStream);
//
//                        outputStream.flush();
//                        outputStream.close();
//                    }
//
//                    // Progress bar
//                    for (int i = 0; i < 100; i++) {
//                        try {
//                            publishProgress(i);
//                            Thread.sleep(30);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//            }
//        }
//
//        protected void onProgressUpdate(Integer ... progress){
//            if(progress[0] == 0){
//                imageView.setImageBitmap(bm);
//            }
//
//            progressBar.setProgress(progress[0]);
//
//        }
//
//        protected void onPostExecute(){
//
//        }
//
//    }


}