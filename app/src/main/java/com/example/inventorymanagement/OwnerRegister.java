package com.example.inventorymanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OwnerRegister extends AppCompatActivity {
    EditText id,fullname,address,phone,email;
    Button register;
    InventoryDataBase idb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register);
        setTitle("Registration");

        idb = new InventoryDataBase(this);
        id = (EditText)findViewById(R.id.ownId);
        fullname = (EditText)findViewById(R.id.ownName);
        address = (EditText)findViewById(R.id.ownAddr);
        phone = (EditText)findViewById(R.id.ownPhno);
        email = (EditText)findViewById(R.id.ownEmail);
        register = (Button)findViewById(R.id.ownReg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOwnerData();
                finish();
            }
        });

    }

    public void registerOwnerData(){
        long res= idb.insertUserData(
                id.getText().toString().trim(),
                fullname.getText().toString().trim(),
                address.getText().toString().trim(),
                phone.getText().toString().trim(),
                email.getText().toString().trim()
        );
        if (res == -1)
            Toast.makeText(OwnerRegister.this,"Data is not Register",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(OwnerRegister.this,"Record Save Successfully",Toast.LENGTH_LONG).show();
    }
}
