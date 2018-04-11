package com.example.stpl.b4umertest;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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

public class OrderAdapter extends BaseAdapter {
    private Context mContext;
    public String txid[];
    public String orders[];

    public OrderAdapter(Context mc,String[] txid,String[] orders) {
        this.mContext=mc;
        this.txid=txid;
        this.orders=orders;
    }
    @Override
    public int getCount() {
        return txid.length;
    }

    @Override
    public Object getItem(int position) {
        return txid[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.order_list, parent, false);
        final TextView textView = (TextView) rowView.findViewById(R.id.ordernumber);
        textView.setText("Order :"+txid[position]);
        final TextView textView1 =(TextView) rowView.findViewById(R.id.orderitems);
        textView1.setText(orders[position]);
        final Button deliverd=(Button) rowView.findViewById(R.id.delivered);
        deliverd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrder("https://vijeth11.000webhostapp.com/merchant.php?txid="+txid[position]+"&delivered=yes");
                //Toast.makeText(mContext,"clicked delivery button https://vijeth11.000webhostapp.com/merchant.php?txid="+txid[position]+"&delivered=yes",Toast.LENGTH_SHORT).show();
            }
        });

        collapse(rowView.findViewById(R.id.descLayout));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout descriptionLayout = (RelativeLayout) v.findViewById(R.id.descLayout);
                if (descriptionLayout.isShown()) {
                    collapse(descriptionLayout);
                    return;
                }
                expand(descriptionLayout);
            }
        });
        return rowView;
    }



    public static void expand(final View v) {
        v.measure(-1, -2);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1.0f ? -2 : (int) (((float) targetHeight) * interpolatedTime);
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((long) ((int) (((float) targetHeight) / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    v.setVisibility(View.GONE);
                    return;
                }
                v.getLayoutParams().height = initialHeight - ((int) (((float) initialHeight) * interpolatedTime));
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((long) ((int) (((float) initialHeight) / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }



    public void updateOrder(final String JSON_URL)
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
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                MainActivity ma = (MainActivity) mContext;
                                ma.getOrderList("https://vijeth11.000webhostapp.com/merchant.php?type=orders");



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
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

}
