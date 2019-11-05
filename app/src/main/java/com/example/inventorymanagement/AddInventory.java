package com.example.inventorymanagement;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddInventory extends AppCompatActivity {

    public EditText pid;
    EditText pname,qty,price,date;
    Button saveInventory;
    ImageView pressImage;
    InventoryDataBase idb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        setTitle("Storage");

        idb = new InventoryDataBase(this);
        pid = (EditText)findViewById(R.id.pid);
        pname = (EditText)findViewById(R.id.pname);
        qty = (EditText)findViewById(R.id.qty);
        price = (EditText)findViewById(R.id.price);
        date = (EditText)findViewById(R.id.Adddate);
        pressImage = findViewById(R.id.img_scan);
        saveInventory = (Button)findViewById(R.id.add_Inventory);

        /*if(getIntent().hasExtra("id"))
            pid.setText(getIntent().getStringExtra("id"));*/


        pressImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),ScannerHelper.class)
                .putExtra("activity","add"),45
                );
            }
        });

        saveInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addInventoryData();

                /*pid.setText("");
                pname.setText("");
                qty.setText("");
                price.setText("");
                date.setText("");
                pid.requestFocus();*/
            }
        });
    }

    public void addInventoryData(){
        int product_Quantity;
        double product_Price;
        String product_ID,product_name,product_Date;

        product_ID = pid.getText().toString().trim();
        product_name = pname.getText().toString().trim();
        product_Date = date.getText().toString().trim();
        String Str_qty = qty.getText().toString().trim();
        String Str_prc = price.getText().toString().trim();

        if(!Str_qty.isEmpty() && !Str_prc.isEmpty() &&  !product_ID.isEmpty() && !product_name.isEmpty()) {
            product_Quantity = Integer.parseInt(Str_qty);
            product_Price = Double.parseDouble(Str_prc);

            if(product_Quantity>0) {
                long isAdd = idb.addProductData(product_ID, product_name, product_Quantity, product_Price, product_Date);

                if (isAdd == -1)
                    Toast.makeText(AddInventory.this, "Item is not Save Successfully", Toast.LENGTH_LONG).show();

                else {
                    Toast.makeText(AddInventory.this, "Items  Saved Successfully", Toast.LENGTH_LONG).show();

                    pid.setText("");
                    pname.setText("");
                    qty.setText("");
                    price.setText("");
                    date.setText("");
                    pid.requestFocus();
                }
            }

            else{
                Toast.makeText(AddInventory.this, "Please Enter valid Quantity of product", Toast.LENGTH_LONG).show();
            qty.setText("1");
            }
        }

        else{

        }
        if(product_ID.isEmpty()){
            Toast.makeText(this,"Please Enter Product ID",Toast.LENGTH_LONG).show();
            pid.requestFocus();
        }
        else if(product_name.isEmpty()){
            Toast.makeText(this,"Please Enter Product Name",Toast.LENGTH_LONG).show();
           pname.requestFocus();
        }
        else if(Str_qty.isEmpty()) {
            Toast.makeText(this, "Please Enter Product Quantity", Toast.LENGTH_LONG).show();
            qty.setText("1");
            qty.requestFocus();
        }
        else if(Str_prc.isEmpty()){
            Toast.makeText(this,"Please Enter Product Price",Toast.LENGTH_LONG).show();
            price.requestFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 45){
            if (resultCode == RESULT_OK){
                pid.setText(data.getStringExtra("barCodeAdd"));
            }
        }
    }
}
