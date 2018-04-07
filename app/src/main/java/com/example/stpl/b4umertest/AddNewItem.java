package com.example.stpl.b4umertest;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class AddNewItem extends AppCompatActivity {

    private int max;
    EditText Name;
     EditText cost;
     EditText link;
     EditText discription;
     String[] yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        Name = (EditText) findViewById(R.id.edtname);
         cost = (EditText) findViewById(R.id.edtcost);
        link = (EditText) findViewById(R.id.edtlink);
         discription =(EditText) findViewById(R.id.editText);
        final Switch available = (Switch) findViewById(R.id.simpleSwitch);
        final Button insert = (Button) findViewById(R.id.insert);
         yes = new String[1];

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(available.isChecked()==true)
                    yes[0] ="yes";
                else
                    yes[0]="no";

                getid("http://vijeth11.000webhostapp.com/merchant.php?type=maximum");

            }
        });

    }


    public void insert(final String JSON_URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion


                     // Toast.makeText(AddNewItem.this,JSON_URL,Toast.LENGTH_SHORT).show();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            String error =obj.getString("error");
                            String message=obj.getString("message");
                            if(Objects.equals(error, "TRUE"))
                                Toast.makeText(AddNewItem.this,message,Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(AddNewItem.this, message, Toast.LENGTH_SHORT).show();
                                Name.setText("");
                                cost.setText("");
                                discription.setText("");
                                link.setText("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    public void getid(final String JSON_URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion


                        //Toast.makeText(AddNewItem.this,JSON_URL,Toast.LENGTH_SHORT).show();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            String error =obj.getString("error");
                            String message=obj.getString("message");
                            if(Objects.equals(error, "TRUE"))
                                Toast.makeText(AddNewItem.this,message,Toast.LENGTH_SHORT).show();


                            boolean numeric = true;

                            numeric = message.matches("-?\\d+");
                            if(numeric) {
                                max = Integer.parseInt(message);
                                //Toast.makeText(AddNewItem.this,String.valueOf(max),Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String name = Name.getText().toString().replace(" ","+");
                        name=name.replaceAll("(\\n)+", "\\\\n");
                        String Link = link.getText().toString().replace(" ","+");
                        Link=Link.replace("/","\\/");
                        String Discription = discription.getText().toString().replace(" ","+");
                        Discription=Discription.replaceAll("(\\n)+", "\\\\n");

                        insert("http://vijeth11.000webhostapp.com/merchant.php?id="+String.valueOf(max+1)+"&name="+name+"&cost="+cost.getText().toString()+"&link="+Link+"&discription="+Discription+"&available="+yes[0]);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

}
