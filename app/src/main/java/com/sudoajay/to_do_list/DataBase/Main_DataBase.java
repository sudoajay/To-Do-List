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
    public static final String DATABASE_TABLE_NAME = "DATABASE_TABLE_NAME";
    public static final String col_1 = "ID";
    public static final String col_2 = "Task";
    public static final String col_3 = "Date";
    public static final String col_4 = "Time";
    public static final String col_5 = "Repeat";
    public static final String col_6 = "customWeekday";
    public static final String col_7 = "Endlessly";
    public static final String col_8 = "Done";
    public static final String col_9 = "Original_Time";



    public Main_DataBase(Context context  )
    {
        super(context, DATABASE_NAME, null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "Task TEXT,Date Text,Time Text,Repeat INTEGER ,customWeekday Text,Endlessly Text, Done INTEGER , Original_Time INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }
    public void Fill_It(String Task , String Date ,String Time ,int  Repeat ,String customWeekday ,String Endlessly ,int Done, int Original_Time){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,Task);
        contentValues.put(col_3,Date);
        contentValues.put(col_4,Time);
        contentValues.put(col_5,Repeat);
        contentValues.put(col_6,customWeekday);
        contentValues.put(col_7 ,Endlessly);
        contentValues.put(col_8,Done);
        contentValues.put(col_9,Original_Time);
        sqLiteDatabase.insert(DATABASE_TABLE_NAME,null,contentValues);
    }
    public boolean check_For_Empty(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DATABASE_TABLE_NAME,null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        return count <= 0;
    }
    public Cursor Get_All_Date_And_ID_Done_Week(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date ,ID ,Done,Repeat,customWeekday,Endlessly from DATABASE_TABLE_NAME ORDER BY Original_Time ASC "
                ,null);
    }
    public Integer deleteRow(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME,"ID = ?",new String[] {id});
    }
    public Cursor Get_The_Value_From_Id(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from DATABASE_TABLE_NAME WHERE ID = ?" ,new String []{ id+"" });
    }
    public Cursor Get_The_Id_From_Done(int done ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select ID from DATABASE_TABLE_NAME  WHERE Done = ?" ,new String []{done+"" });
    }
    public Cursor Get_The_Data_From_Today_Time(int done, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from DATABASE_TABLE_NAME WHERE Date = ? AND Done = ?  ORDER BY Original_Time ASC "  ,new String []{date , done+"" });
    }
    public Cursor Get_The_Data_From_Done(int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from DATABASE_TABLE_NAME WHERE Done = ?  ORDER BY Original_Time ASC "  ,new String []{ done+"" });
    }
    public Cursor Get_All_Data_From_Date_Done_Time(String date,int time,int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from DATABASE_TABLE_NAME WHERE Date = ? And Original_Time >= ? AND Done = ? " ,new String []{ date, time+"" , done+"" });
    }
    public Cursor Get_The_Id_Name_Original_Time_From_Date_Done_OriginalTime( String date,int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select ID ,Task ,Time,Original_Time from DATABASE_TABLE_NAME  WHERE Date = ? AND Done = ? " +
                "ORDER BY Original_Time ASC " ,new String []{date,done+"" });
    }
    public Cursor AlertNotificationDatabase(int done ,String date,int originalTime){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Task ,Time,Original_Time from DATABASE_TABLE_NAME  WHERE Done = ? AND Date = ? AND Original_Time >= ?" +
                "ORDER BY Original_Time ASC " ,new String []{done+"",date,originalTime+"" });
    }

    public void Update_The_Table(String id , String Task , String Date,String Time ,int  Repeat ,String customWeekday ,String Endlessly ,int Done ,int Original_Time ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_2,Task);
        contentValues.put(col_3,Date);
        contentValues.put(col_4,Time);
        contentValues.put(col_5,Repeat);
        contentValues.put(col_6,customWeekday);
        contentValues.put(col_7 ,Endlessly);
        contentValues.put(col_8,Done);
        contentValues.put(col_9,Original_Time);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }

    public void Update_The_Table_For_Done(String id ,int Done  ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_8,Done);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }
    public void UpdateTheTableAndClear(String id ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_5,0);
        contentValues.put(col_6,"");
        contentValues.put(col_7,"");
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }

}
