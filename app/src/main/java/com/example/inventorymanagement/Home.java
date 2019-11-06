package com.example.inventorymanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    InventoryDataBase idb;
    Button reg,customer,inventory,sell;

    Button bill,search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        idb = new InventoryDataBase(this);
        reg  =(Button)findViewById(R.id.btn_Reg);
        /*customer  =(Button)findViewById(R.id.btn_Reg2);*/
        inventory  =(Button)findViewById(R.id.btn_Reg3);
        sell = (Button)findViewById(R.id.btnSell);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Home.this,OwnerRegister.class);
                startActivity(intent);
                //finish();
            }
        });

        /*customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,CustomerRegister.class);
                startActivity(intent);
            }
        });*/

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,AddInventory.class);
                startActivity(intent);
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idb.dummyTable("CREATE TABLE IF NOT EXISTS RECORD(PRODUCT_ID VARCHAR(2), PRODUCT_NAME VARCHAR, QUANTITY INTEGER,PRICE INTEGER, DATE VARCHAR,RID INTEGER DEFAULT 1 PRIMARY KEY AUTOINCREMENT)");

                Intent intent = new Intent(Home.this,SellProduct.class);
                startActivity(intent);
            }
        });

        billButton();
        searchButton();

    }

    public void billButton(){

        bill = findViewById(R.id.bill_button);

        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,BillActivity.class));
            }
        });

    }

    public void searchButton(){

        search = findViewById(R.id.search_button);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,SearchActivity.class));
            }
        });
    }
}
