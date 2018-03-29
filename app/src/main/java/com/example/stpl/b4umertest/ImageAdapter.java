package com.example.stpl.b4umertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array

    public String [] name  ;
    public Boolean[] checkBoxState;
    // Constructor
    public ImageAdapter(Context c,String[] names){
        mContext = c;
        name=names;
        checkBoxState=new Boolean[name.length];
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return name[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        textView.setText(name[position]);
        checkBoxState[position]=false;
        CheckBox checkBox=rowView.findViewById(R.id.check);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    checkBoxState[position] = true;
                    ischecked(position,true);
                }
                else {
                    checkBoxState[position] = false;
                    ischecked(position,false);
                }
            }
        });
        return rowView;
    }

    public void ischecked(int pos,Boolean val)
    {
        Main2Activity ma=(Main2Activity)mContext;
        ma.called(pos,val);
    }
}
