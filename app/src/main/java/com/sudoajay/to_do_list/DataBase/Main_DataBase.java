package com.sudoajay.to_do_list.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sudoajay on 2/6/18.
 */

public class Main_DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Database.db";
    public static  String DATABASE_TABLE_NAME = "Database_Table";
    public static final String col_1 = "ID";
    public static final String col_2 = "Task";
    public static final String col_3 = "Date";
    public static final String col_4 = "Time";
    public static final String col_5 = "Repeat";
    public static final String col_6 = "Done";
    public static final String col_7 = "Original_Time";



    public Main_DataBase(Context context  )
    {
        super(context, DATABASE_NAME, null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "Task TEXT,Date Text,Time Text,Repeat Text , Done INTEGER , Original_Time INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }
    public void Fill_It(String Task , String Date ,String Time ,String  Repeat ,int Done, int Original_Time){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,Task);
        contentValues.put(col_3,Date);
        contentValues.put(col_4,Time);
        contentValues.put(col_5,Repeat);
        contentValues.put(col_6,Done);
        contentValues.put(col_7 ,Original_Time);
        sqLiteDatabase.insert(DATABASE_TABLE_NAME,null,contentValues);
    }
    public boolean check_For_Empty(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DATABASE_TABLE_NAME,null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count > 0) {
            return false;
            }
        return true;
    }
    public Cursor Get_All_Date_And_ID_Done_Week(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date ,ID ,Done,Repeat from Database_Table ORDER BY Original_Time ASC " ,null);
    }
    public Integer Delete_Row(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME,"ID = ?",new String[] {id});
    }
    public Cursor Get_The_Value_From_Id(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table WHERE ID = ?" ,new String []{ id+"" });
    }
    public Cursor Get_The_Repeat_From_Id(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Repeat from Database_Table  WHERE ID = ?" ,new String []{id+"" });
    }
    public Cursor Get_The_Id_From_Done(int done ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select ID from Database_Table  WHERE Done = ?" ,new String []{done+"" });
    }
    public Cursor Get_The_Date_From_Id(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date from Database_Table  WHERE ID = ?" ,new String []{id+""});
    }
    public Cursor Get_The_Data_From_Today_Time(int done, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table WHERE Date = ? AND Done = ?  ORDER BY Original_Time ASC "  ,new String []{date , done+"" });
    }
    public Cursor Get_The_Data_From_Done(int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table WHERE Done = ?  ORDER BY Original_Time ASC "  ,new String []{ done+"" });
    }
    public Cursor Get_All_Data_From_Date_Done_Time(String date,int time,int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table WHERE Date = ? And Original_Time >= ? AND Done = ? " ,new String []{ date, time+"" , done+"" });
    }
    public Cursor Get_The_Id_Name_Original_Time_From_Date_Done_OriginalTime( String date,int time,int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select ID ,Task ,Time,Original_Time from Database_Table  WHERE Date = ? And Original_Time >= ? AND Done = ? " +
                "ORDER BY Original_Time ASC " ,new String []{date,time+"",done+"" });
    }


    public void Update_The_Table(String id , String Task , String Date,String Time ,String  Repeat,int Done ,int Original_Time ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_2,Task);
        contentValues.put(col_3,Date);
        contentValues.put(col_4,Time);
        contentValues.put(col_5,Repeat);
        contentValues.put(col_6,Done);
        contentValues.put(col_7,Original_Time);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }

    public void Update_The_Table_For_Done(String id ,int Done  ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_6,Done);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }
    public void Update_The_Table_For_Date(String id ,String Date ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_3,Date);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }
    public void Update_The_Table_For_Repeat(String id ,String  Repeat ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_5,Repeat);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }

}
