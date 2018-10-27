package to_do_list.com.sudoajay.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sudoajay on 2/6/18.
 */

public class DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Database.db";
    public static  String DATABASE_TABLE_NAME = "Database_Table";
    public static final String col_1 = "ID";
    public static final String col_2 = "Task";
    public static final String col_3 = "Date";
    public static final String col_4 = "Time";
    public static final String col_5 = "Repeat";
    public static final String col_6 = "Done";
    public static final String col_7 = "Original_Time";



    public DataBase(Context context  )
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
    public Cursor Get_All_Date_And_ID_Done(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date ,ID ,Done from Database_Table ORDER BY Original_Time ASC " ,null);
    }
    public Integer Delete_Row(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME,"ID = ?",new String[] {id});
    }
    public Cursor Get_The_Value_From_Id(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table WHERE ID ="+id,null);
    }
    public Cursor Get_All_Data(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table",null);
    }
    public Cursor Specified_Row(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date from Database_Table ",null);
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
}
