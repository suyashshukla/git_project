package com.example.inventorymanagement;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    EditText query;

    Button search;

    ListView listView;

    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search_trigger);
        query = findViewById(R.id.search_query);
        listView = findViewById(R.id.search_list);


        adapter = new SearchAdapter(SearchActivity.this,null);

        listView.setAdapter(adapter);


        InventoryDataBase dataBase = new InventoryDataBase(this);

        final SQLiteDatabase databaseContext = dataBase.getReadableDatabase();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String queryString = query.getText().toString();

                Cursor cursor = databaseContext.rawQuery("SELECT * FROM INVENTORY WHERE PRODUCT_NAME LIKE '%"+queryString+"%';",null);

                adapter.swapCursor(cursor);
                adapter.notifyDataSetChanged();

                if(cursor==null){
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.search_layout),"No Results Found!", BaseTransientBottomBar.LENGTH_INDEFINITE);
                            snackbar.setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                       snackbar.dismiss();
                                }
                            }).show();
                }

            }
        });


    }
}
