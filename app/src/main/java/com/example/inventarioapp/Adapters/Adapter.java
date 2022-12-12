package com.example.inventarioapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.inventarioapp.R;

public class Adapter extends CursorAdapter {
    LayoutInflater inflater;
    TextView textView;
    public Adapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item, parent, false);
         textView =  view.findViewById(R.id.item);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
       int cod =cursor.getColumnIndex("CODIGO");
       int nom =cursor.getColumnIndex("NOMBRE");

       // textView.setText(  cursor.getString(cod) + "-" + cursor.getString(nom));
    }
}
