package com.example.inventorymanagement;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.net.ContentHandler;

public class SearchAdapter extends CursorAdapter {

    Context context;
    Cursor cursor;

    SearchAdapter(Context context, Cursor cursor){
        super(context,null);
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_search_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        String name = cursor.getString(cursor.getColumnIndex("PRODUCT_NAME"));
        int price = cursor.getInt(cursor.getColumnIndex("PRICE"));

        TextView product_id = view.findViewById(R.id.search_id);
        TextView product_name = view.findViewById(R.id.search_name);
        TextView product_price = view.findViewById(R.id.search_price);

        product_id.setText(id);
        product_name.setText(name);
        product_price.setText(String.valueOf(price));
    }


}
