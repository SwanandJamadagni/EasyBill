package sj_infotech.easybill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Swanand on 24-04-2019.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_Name = "easybill.sqlite";
    public static final String Userinfo_Table = "users";
    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Userinfo_Table + " (ID INTEGER PRIMARY KEY,EMAIL TEXT,PASS TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS"+Userinfo_Table);
        onCreate(db);

    }

    public boolean insertintouserinfo(String email,String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL",email);
        contentValues.put("PASS", pass);
        long result = db.insert(Userinfo_Table,null,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getalldata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from users",null);
        return res;
    }

    public void truncate_table(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Userinfo_Table);
    }
}
