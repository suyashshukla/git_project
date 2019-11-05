package com.example.inventorymanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerHelper extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private final int CAMERA_REQUEST_CODE = 999;
    ZXingScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setTitle("Scanner");

        //check the sdk version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            //no need to required permission
            setContentView(scannerView);
        } else {

            //check the permission
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                setContentView(scannerView);

            } else {
                //ask for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:

                for (int i=0; i<permissions.length; i++){
                    String permission  = permissions[i];

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                        boolean showRational = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                    } else {
                        setContentView(scannerView);
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void handleResult(Result result) {
        if(getIntent().getStringExtra("activity").equals("sell")){
            setResult(RESULT_OK,new Intent().putExtra("barCodeSell",result.getText()));
        }
            /*startActivity(new Intent(ScannerHelper.this,SellProduct.class)
                    .putExtra("id",result.getText()));*/

        else
            setResult(RESULT_OK,new Intent().putExtra("barCodeAdd",result.getText()));

            /*startActivity(new Intent(ScannerHelper.this,AddInventory.class)
            .putExtra("id",result.getText()));*/

        onBackPressed();
        finish();

    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
