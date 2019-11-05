package com.example.inventorymanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomerRegister extends AppCompatActivity {

    EditText cid,cname,caddress,cphone,cemail;
    Button cregister;
    InventoryDataBase idb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        idb = new InventoryDataBase(this);
        cid = (EditText)findViewById(R.id.cid);
        cname = (EditText)findViewById(R.id.cname);
        caddress = (EditText)findViewById(R.id.caddr);
        cphone = (EditText)findViewById(R.id.cphone);
        cemail = (EditText)findViewById(R.id.cmail);
        cregister = (Button)findViewById(R.id.cregister);

        cregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCustomerData();
                finish();
            }
        });
    }

    public void registerCustomerData(){
        long res= idb.insertCustomerData(
                cid.getText().toString().trim(),
                cname.getText().toString().trim(),
                caddress.getText().toString().trim(),
                cphone.getText().toString().trim(),
                cemail.getText().toString().trim()
        );
        if (res == -1)
            Toast.makeText(CustomerRegister.this,"Data is not Register",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(CustomerRegister.this,"Record Save Successfully",Toast.LENGTH_LONG).show();
    }

}
