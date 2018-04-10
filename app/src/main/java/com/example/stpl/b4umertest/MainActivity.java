package com.example.stpl.b4umertest;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final String MoneyRecieved="Received Rs\\.[0-9]+";
    public static final String Transactionid="[0-9]{11}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button update = (Button) findViewById(R.id.button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        Button insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNewItem.class));
            }
        });

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                //From the received text string you may do string operations to get the required OTP
                //It depends on your SMS format
                Log.e("Message", messageText);

                Pattern pattern = Pattern.compile(MoneyRecieved);
                Matcher matcher = pattern.matcher(messageText);
                String money = "";
                while (matcher.find()) {
                    money = matcher.group();
                }
                pattern = Pattern.compile("[0-9]+");
                matcher = pattern.matcher(money);
                String RealMoney = "";
                while (matcher.find())
                    RealMoney = matcher.group();

                pattern = Pattern.compile(Transactionid);
                matcher = pattern.matcher(messageText);
                String txid = "";
                while (matcher.find())
                    txid = matcher.group();
                if (RealMoney != "" && txid != "") {
                    Toast.makeText(MainActivity.this, "Ammount: " + RealMoney + " transactionid" + txid, Toast.LENGTH_LONG).show();
                    AddOrder("http://vijeth11.000webhostapp.com/merchant.php?txid=" + txid + "&cost=" + RealMoney);
                }


            }
        });
    }


        public void AddOrder(final String JSON_URL)
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
                                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

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




}
