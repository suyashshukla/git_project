package com.example.inventorymanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        listView = findViewById(R.id.txn_data);
        list = new ArrayList<>();

        list.add("Flour : 1kg");
        list.add("Fenugreek : 500 g");
        list.add("Cinnamon : 5 kg");
        list.add("Coriander : 250 g");
        list.add("Turmeric : 100 g");
        list.add("Maze : 652 g");
        list.add("Besan : 50 g");
        list.add("Sugar : 10 kg");
        list.add("Rice : 750 g");
        list.add("Wheat : 2 kg");

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        arrayAdapter.addAll(list);
        listView.setAdapter(arrayAdapter);

    }
}
