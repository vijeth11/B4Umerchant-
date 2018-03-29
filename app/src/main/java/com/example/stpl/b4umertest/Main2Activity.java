package com.example.stpl.b4umertest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ListView lv;
    private String[] name;
    private Button update;
    private Boolean[] selecteditems;

    // URL to get contacts JSON
    private static String url = "https://vijeth11.000webhostapp.com/merchant.php?type=merchant";

    ArrayList<HashMap<String, String>> itemList;
    private JSONArray items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        itemList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
        update=(Button)findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 new Upload(Main2Activity.this,url).execute();
            }
        });

    }
    public void called(int pos,Boolean val)
    {
        selecteditems[pos+1]=val;
        Toast.makeText(Main2Activity.this,name[pos+1]+"is checked"+String.valueOf(selecteditems[pos+1]),Toast.LENGTH_SHORT).show();



    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    items = jsonObj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);
                        String name = c.getString("name");



                        // tmp hash map for single contact
                        HashMap<String, String> itemlist = new HashMap<>();

                        // adding each child node to HashMap key => value

                        itemlist.put("name", name);

                        // adding contact to contact list
                        itemList.add(itemlist);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            /**
             * Updating parsed JSON data into ListView
             * */
            name=new String[items.length()+1];
            selecteditems=new Boolean[items.length()+1];
            Log.e(TAG,"onPostExecute is running");
            int count=0;
            for(HashMap<String,String>map:itemList){
                count++;
                for(Map.Entry<String,String>mapEntry:map.entrySet())
                {
                    String key = mapEntry.getKey();
                    String value=mapEntry.getValue();
                    if(key=="name") {
                        name[count] = value;
                        selecteditems[count] = false;
                    }

                }
            }
         lv.setAdapter(new ImageAdapter(Main2Activity.this, Arrays.copyOfRange(name, 1, name.length)));
        }

    }
}
