package com.example.inventorymanagement;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SellProduct extends AppCompatActivity {

   public EditText pid,qty;
    Button sellButton,showButton;
    ImageView img;
    TextView touch;
    InventoryDataBase idb;

    ArrayList<Model> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);
        setTitle("Sell Product");

        pid = (EditText)findViewById(R.id.pid);
        qty = (EditText)findViewById(R.id.Qty);
        sellButton = (Button) findViewById(R.id.btnSell);
        showButton =(Button) findViewById(R.id.btnShow);
        img = findViewById(R.id.img_scan);
        idb = new InventoryDataBase(this);
        /*touch = findViewById(R.id.tvHello);*/


        /*if(getIntent().hasExtra("id"))
            pid.setText(getIntent().getStringExtra("id"));*/


        img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            startActivityForResult(new Intent(SellProduct.this,ScannerHelper.class)
            .putExtra("activity","sell"),99);
           }
       });
        //create record table

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function to action
                sellItem();
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(SellProduct.this,SellShowListActivity.class);
            startActivity(intent);
            }
        });
    }

    public void sellItem() {

        String product_Id, product_Name, str_Qty;
        int quantity, dbQty, updatedQty;
        int price;

        product_Id = pid.getText().toString().trim();
        str_Qty = qty.getText().toString().trim();

        if (!str_Qty.isEmpty() && !product_Id.isEmpty()) {

            quantity = Integer.parseInt(str_Qty);

            Cursor cursor = idb.getProductDetails(product_Id);

            if(cursor.getCount() == 0)
                Toast.makeText(this,"Invalid Product Id",Toast.LENGTH_LONG).show();

            else {
                cursor.moveToNext();
                String pid = cursor.getString(0);
                product_Name = cursor.getString(1);
                dbQty = cursor.getInt(2);
                price = cursor.getInt(3);
                String product_Date = cursor.getString(4);

                if (dbQty < quantity){
                    Toast.makeText(this,"You Can Sell Only "+dbQty+" Quantity of this Product",Toast.LENGTH_LONG).show();
                    qty.setText("");
                    qty.requestFocus();
                }
                if (dbQty >= quantity && quantity > 0) {

                    updatedQty = dbQty - quantity;

                    try {
                        //create Record
                        idb.insertDummy(product_Id, product_Name, quantity, price, product_Date);

                        //update function
                        idb.updateAfterSell(product_Id, product_Name, updatedQty, price, product_Date);
                        Toast.makeText(SellProduct.this, quantity + " Item's are sold ", Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    qty.setText("");
                    this.pid.setText("");
                    this.pid.requestFocus();
                }

                else if (dbQty == 0){
                    Toast.makeText(SellProduct.this, "Please Enter Valid Quantity", Toast.LENGTH_LONG).show();
                    qty.setText("1");
                    qty.requestFocus();
                }

                else if (dbQty < 1) {
                    Toast.makeText(SellProduct.this, "All Item's are Sold Out", Toast.LENGTH_LONG).show();
                }
            }


        }

        else {
            if (product_Id.isEmpty())
                Toast.makeText(SellProduct.this, " Please Enter Valid Product ID ", Toast.LENGTH_LONG).show();

            else if (str_Qty.isEmpty()){

                Toast.makeText(SellProduct.this, " You can chose at least 1 Item ", Toast.LENGTH_LONG).show();

                qty.setText("1");
            }

            else
                Toast.makeText(SellProduct.this, " Please Enter Valid Input ", Toast.LENGTH_LONG).show();
        }
/*
    // function of create record Table  and insert data in table
    private void createRecord(String pid, String product_name, int quantity, int price, String date) {
        try {
            //record table insert
            idb.insertDummy(pid, product_name, quantity, price, date);
        }
        catch (Exception e){
            Log.e("insert error",e.getMessage());
        }
    }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99){
            if (resultCode == RESULT_OK){
                pid.setText(data.getStringExtra("barCodeSell"));
            }
        }
    }
}
