package com.example.inventorymanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SellShowListActivity extends AppCompatActivity {


    ListView mListView;
    ArrayList<Model> mList;
    SellViewAdapter mAdapter;
    InventoryDataBase idb;
    TextView grandTotalText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_show_list);
        setTitle("Cart List");

        idb = new InventoryDataBase(this);
        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new SellViewAdapter(new SellShowListActivity(),R.layout.row_sell_view_card,mList);
        mListView.setAdapter(mAdapter);
        grandTotalText = findViewById(R.id.next_bill);

       //get all data from record table
        Cursor cursor = idb.dummyCursor("SELECT * FROM RECORD;");
        double grandTotal = 0;
        mList.clear();
        while (cursor.moveToNext()){
            String pid = cursor.getString(0);
            String name = cursor.getString(1);
            int quantity = cursor.getInt(2);
            int price = cursor.getInt(3);
            String date  = cursor.getString(4);
            double total = (double) quantity*price;
            grandTotal = total + grandTotal;
            //add to list
            mList.add(new Model(pid,name,quantity,price,total));
        }

        grandTotalText.setText("Grand Total: Rs "+grandTotal);

        mAdapter.notifyDataSetChanged();

        if (mList.size()==0){
           Toast.makeText(this,"No record found",Toast.LENGTH_LONG).show();
       }


       // Long click for action list
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                //alert dialog to take action
                final CharSequence[] item = {"Update","Delete"};

                //display update and delete option
                AlertDialog.Builder dialog = new AlertDialog.Builder(SellShowListActivity.this);

                dialog.setTitle("Chose an option...");
                dialog.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0 ){
                            //show update dialog
                            Model model= (Model) mAdapter.getItem(position);
                            showUpdateDialog(SellShowListActivity.this,model.getProductId());
                        }
                        if(which==1){
                            //show delete dialog
                            Model model = mList.get(position);
                            showDeleteDialog(model.getProductId());
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    //Update dialog Activity
    private void showUpdateDialog(Activity activity, final String productId) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");

        final EditText updateQty = dialog.findViewById(R.id.dialogUpdateQty);
        final Button updateFromDialog = dialog.findViewById(R.id.btnDialogUpdate);
        //width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels*0.99);
        //height of dialog box
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels*0.45);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        updateFromDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Cursor c = idb.dummyCursor("select inventory.product_id, inventory.QUANTITY, record.product_id , record.QUANTITY FROM inventory,record where inventory.product_id=record.product_id AND inventory.product_id = '"+productId+"'");

                    c.moveToNext();
                    String inventoryProduct_id = c.getString(0);
                    int inventoryQuantity = c.getInt(1);
                    String recordProduct_id = c.getString(2);
                    int recordQuantity = c.getInt(3);
                    int latestValue = Integer.parseInt(updateQty.getText().toString().trim());

                    int totalQuantity = inventoryQuantity + recordQuantity;

                    if (totalQuantity >= latestValue ) {

                        if (latestValue > 0){
                            int newQuantity = totalQuantity - latestValue;
                            idb.inventoryUpdate(inventoryProduct_id, newQuantity);
                            idb.updateDummy(recordProduct_id, latestValue);
                            Toast.makeText(SellShowListActivity.this,"Update Successfully",Toast.LENGTH_LONG).show();
                            updateRecordList();
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(SellShowListActivity.this,"Please Enter Valid Quantity",Toast.LENGTH_LONG).show();
                            updateQty.setText("1");
                        }
                    }

                    else {
                        Toast.makeText(SellShowListActivity.this,"You Can Sell Only "+totalQuantity+" Quantity of this Product",Toast.LENGTH_LONG).show();
                        updateQty.setText(""+totalQuantity);
                    }

                }
                catch (Exception error){
                    Log.e("Update Error",error.getMessage());
                }
            }
        });

    }


    //Show delete dialog Activity
    private void showDeleteDialog(final String productId){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(SellShowListActivity.this);
        dialogDelete.setTitle("Warning...");
        dialogDelete.setMessage("Are you sure to delete");

        dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Cursor c = idb.dummyCursor("select inventory.product_id,inventory.QUANTITY,record.product_id , record.QUANTITY FROM inventory,record where inventory.product_id=record.product_id AND inventory.product_id = '" + productId +"'");

                    String inventoryProduct_id, recordProduct_id;
                    int inventoryQuantity, recordQuantity;
                    c.moveToNext();
                    inventoryProduct_id = c.getString(0);
                    inventoryQuantity = c.getInt(1);
                    recordProduct_id = c.getString(2);
                    recordQuantity = c.getInt(3);

                    int totalQuantity = inventoryQuantity + recordQuantity;
                    idb.inventoryUpdate(inventoryProduct_id, totalQuantity);
                    idb.recordDelete(productId);

                    Toast.makeText(SellShowListActivity.this, "record delete successfully", Toast.LENGTH_LONG).show();

                }
                catch (Exception error){
                    Log.e("Error",error.getMessage());
                }
                updateRecordList();
                dialog.dismiss();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    //Show updated record in Card list
    private void updateRecordList() {
        Cursor cursor = idb.dummyCursor("SELECT * FROM RECORD;");
        double grandTotal = 0;
        mList.clear();
        while (cursor.moveToNext()){
            String pid = cursor.getString(0);
            String name = cursor.getString(1);
            int quantity = cursor.getInt(2);
            int price = cursor.getInt(3);
            String date  = cursor.getString(4);
            double total = quantity*price;
            grandTotal = total + grandTotal;
            //add to list
            mList.add(new Model(pid,name,quantity,price,total));
        }
        grandTotalText.setText("Grand Total: Rs "+grandTotal);
        mAdapter.notifyDataSetChanged();

    }
}
