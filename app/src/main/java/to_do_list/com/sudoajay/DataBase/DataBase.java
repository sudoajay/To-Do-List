package to_do_list.com.sudoajay.DataBase;

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
    public static final String col_4 = "Repeat";

    public DataBase(Context context  )
    {
        super(context, DATABASE_NAME, null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "Task TEXT,Date Text,Repeat Text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }
    public void Fill_It(String Task , String Date ,String  Repeat ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,Task);
        contentValues.put(col_3,Date);
        contentValues.put(col_4,Repeat);
        sqLiteDatabase.insert(DATABASE_TABLE_NAME,null,contentValues);
    }
    public boolean check_For_Empty(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DATABASE_TABLE_NAME,null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count > 0) {
            return false;
            }
        return true;
    }
    public Cursor Get_All_Date(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date from Database_Table",null);
    }
    public Integer Delete_Row(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME,"ID = ?",new String[] {id});
    }
    public Cursor Get_The_Value_From_Id(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table WHERE ID ="+id,null);
    }
    public Cursor Get_All_Data(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from Database_Table",null);
    }
    public Cursor Specified_Row(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date from Database_Table ",null);
    }

    public void Update_The_Table(String id , String Task , String Date ,String  Repeat  ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_2,Task);
        contentValues.put(col_3,Date);
        contentValues.put(col_4,Repeat);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }


}
