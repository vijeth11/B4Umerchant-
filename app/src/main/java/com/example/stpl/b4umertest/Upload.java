package com.example.stpl.b4umertest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by stpl on 29/3/18.
 */

public class Upload extends AsyncTask<Void, Void, Void> {
    private String url ;
    private Context mcontext;
    public Upload(Context c,String url )
    {
        mcontext=c;
        this.url=url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog

    }
    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
    }
    @Override
    protected Void doInBackground(Void... voids) {

        HttpHandler sh = new HttpHandler();

        Toast.makeText(mcontext,url,Toast.LENGTH_SHORT).show();
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node


                String error = jsonObj.getString("name");
                String msg=jsonObj.getString("message");


                // tmp hash map for single contact
                HashMap<String, String> result = new HashMap<>();

                // adding each child node to HashMap key => value

                result.put("error", error);
                result.put("message",msg);
                String output="";
                for(Map.Entry<String,String>mapEntry:result.entrySet())
                {
                    String key = mapEntry.getKey();
                    String value=mapEntry.getValue();
                    if(key=="error")
                        output+=key+"="+value;

                }
                Toast.makeText(mcontext,output,Toast.LENGTH_SHORT).show();
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                Toast.makeText(mcontext,
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();


            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");

                    Toast.makeText(mcontext,
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();


        }
        return null;
    }


}
