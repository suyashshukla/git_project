package com.example.inventorymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

public class InventoryDataBase extends SQLiteOpenHelper {

    private static final String DataBase_Name = "INVENTORY.db";
    private static final String User_Table = "SHOPKEEPER";
    private static final String Customer_Table = "CUSTOMER";
    private static final String Inventory_Table = "INVENTORY";
    private static final String Transaction_Table = "TRANSACTION";

    public static String[] User_Table_Columns = new String[]{"ID", "SHOPKEEPER_NAME", "ADDRESS", "PHONE_NO", "E_MAIL"};
    public static String[] Customer_Table_Columns = new String[]{"CID", "CUSTOMER_NAME", "ADDRESS", "PHONE_NO", "E_MAIL"};
    public static String[] Inventory_Table_Columns = new String[]{"_id",/*"PRODUCT_ID",*/ "PRODUCT_NAME", "QUANTITY", "PRICE", "DATE"};
    /*final static String[] Transaction_Table_Columns ={"TRANSACTION_ID","PRODUCT_ID","PRODUCT_NAME","QUANTITY","PRICE","CID","ID"};*/

    public static String[] Query = new String[]{
            String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT,%s TEXT);", User_Table, User_Table_Columns[0], User_Table_Columns[1], User_Table_Columns[2], User_Table_Columns[3], User_Table_Columns[4]),

            String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT,%s TEXT);", Customer_Table, Customer_Table_Columns[0], Customer_Table_Columns[1], Customer_Table_Columns[2], Customer_Table_Columns[3], Customer_Table_Columns[4]),

            String.format("CREATE TABLE %s(%s VARCHAR(2) PRIMARY KEY,%s TEXT,%s INTEGER,%s INTEGER,%s TEXT);", Inventory_Table, Inventory_Table_Columns[0], Inventory_Table_Columns[1], Inventory_Table_Columns[2], Inventory_Table_Columns[3], Inventory_Table_Columns[4])
    };

    public static String[] DropQuery = new String[]{String.format("DROP TABLE IF EXISTS %s;", User_Table),
            String.format("DROP TABLE IF EXISTS %s;", Customer_Table),
            String.format("DROP TABLE IF EXISTS %s;", Inventory_Table)
    };

    public InventoryDataBase(Context context) {
        super(context, DataBase_Name, null, 1);
    }

    int i;

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (i = 0; i < Query.length; i++) {
            db.execSQL(Query[i]);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (i = 0; i < DropQuery.length; i++) {
            db.execSQL(DropQuery[i]);
        }
        onCreate(db);
    }

    public long insertUserData(String id, String Name, String addr, String phno, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User_Table_Columns[0], id);
        contentValues.put(User_Table_Columns[1], Name);
        contentValues.put(User_Table_Columns[2], addr);
        contentValues.put(User_Table_Columns[3], phno);
        contentValues.put(User_Table_Columns[4], email);
        long in = db.insert(User_Table, null, contentValues);
        return in;
    }

    public long insertCustomerData(String id, String Name, String addr, String phno, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Customer_Table_Columns[0], id);
        contentValues.put(Customer_Table_Columns[1], Name);
        contentValues.put(Customer_Table_Columns[2], addr);
        contentValues.put(Customer_Table_Columns[3], phno);
        contentValues.put(Customer_Table_Columns[4], email);
        long in = db.insert(Customer_Table, null, contentValues);
        return in;
    }

    public long addProductData(String pid, String pname, int qty, double price, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Inventory_Table_Columns[0], pid);
        contentValues.put(Inventory_Table_Columns[1], pname);
        contentValues.put(Inventory_Table_Columns[2], qty);
        contentValues.put(Inventory_Table_Columns[3], price);
        contentValues.put(Inventory_Table_Columns[4], date);
        long isAdd = db.insert(Inventory_Table, null, contentValues);
        return isAdd;
    }
    public Cursor getProductDetails(String pid/*,int qty*/ ){
        //int pQty,rQty;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *FROM "+Inventory_Table+" where _id='"+pid+"'",null);
       /* cursor.moveToNext();
        pQty = cursor.getInt(2);
        if(pQty>qty) {
            rQty = pQty - qty;
            //update fuction
            updateAfterSell(pid, cursor.getString(1), rQty, cursor.getInt(3), cursor.getString(4));
        }*/
        return cursor;
    }

    public void updateAfterSell(String pid,String pname,int uQty,int price,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //contentValues.put(Inventory_Table_Columns[0],pid);
        //contentValues.put(Inventory_Table_Columns[1],pname);
        contentValues.put(Inventory_Table_Columns[2],uQty);
        //contentValues.put(Inventory_Table_Columns[3],price);
        //contentValues.put(Inventory_Table_Columns[4],date);

        db.update(Inventory_Table,contentValues,"_id = ?",new String[]{pid});
    }

    public  void dummyTable(String qry){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(qry);
    }
    public Cursor dummyCursor ( String sql){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql,null);
    }
    //INSERT DUMMY DATA
    public  void insertDummy(String pid, String pname, int qty, int price, String date){
        int recordQty= 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor =  dummyCursor("select * from record where PRODUCT_ID='"+pid+"'");
        if(cursor.getCount() != 0 ) {
            cursor.moveToNext();
            recordQty = cursor.getInt(2);
            int updateQty = recordQty + qty;
            //price = cursor.getInt(3);
            contentValues.put("PRODUCT_ID", pid);
            contentValues.put("PRODUCT_NAME", pname);
            contentValues.put("QUANTITY", updateQty);
            contentValues.put("PRICE", price);
            contentValues.put("DATE", date);
            db.update("RECORD",contentValues,"PRODUCT_ID = ?",new String[]{pid});
        }
        else{
            contentValues.put("PRODUCT_ID", pid);
            contentValues.put("PRODUCT_NAME", pname);
            contentValues.put("QUANTITY", qty);
            contentValues.put("PRICE", price);
            contentValues.put("DATE", date);
            db.insert("RECORD",null,contentValues);
        }
    }


    public void inventoryUpdate(String inventoryProduct_id, int totalQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Inventory_Table_Columns[2],totalQuantity);

        db.update(Inventory_Table,contentValues,"_id = ?",new String[]{inventoryProduct_id});
    }

    public void updateDummy(String recordProduct_id, int updatedQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

            //price = cursor.getInt(3);
            /*contentValues.put("PRODUCT_ID", pid);
            contentValues.put("PRODUCT_NAME", pname);*/
            contentValues.put("QUANTITY", updatedQty);
           /* contentValues.put("PRICE", price);
            contentValues.put("DATE", date);*/
            db.update("RECORD",contentValues,"PRODUCT_ID = ?",new String[]{recordProduct_id});
        }

    public void recordDelete(String productID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.delete("RECORD","PRODUCT_ID = ?",new String[]{String.valueOf(productID)});
    }
}