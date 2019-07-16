package com.studentshield.shield.visitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hhh on 9/4/2018.
 */

public class dbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;

    private static final String DATABASE_NAME = "studentDB.db";

    public static final String TABLE_VISITORS= "visitor_table";
    public static final String COLUMN_ursid="uid";
    public static final String COLUMN_u_id="_id";
    public static final String COLUMN_u_name="u_name";
    public static final String COLUMN_u_mobile="u_mobile";
    public static final String COLUMN_purp ="u_purp";
    public static final String COLUMN_img ="u_image";
    public static final String COLUMN_img_id ="u_image_id";
    public static final String COLUMN_email ="u_visitoremailid";
    public static final String COLUMN_u_deig ="u_desig";
    public static final String COLUMN_u_addr ="u_addr";

    public static final String TABLE_DESIGNATION= "desi_table";
    public static final String COLUMN_degna_id="desi_id";
    public static final String COLUMN_degna_usrid="desi_id_usr";
    public static final String COLUMN_degna_name="desi_name";

    public static final String TABLE_PURPOSE= "purp_table";
    public static final String COLUMN_purp_id="purp_id";
    public static final String COLUMN_purp_id_usr="purp_id_usr";
    public static final String COLUMN_purp_name="purp_name";

    public static final String TABLE_STAFF= "staff_table";
    public static final String COLUMN_staff_id="staff_id";
    public static final String COLUMN_staff_name="staff_name";
    public static final String COLUMN_staff_desig="staff_desig";
    public static final String COLUMN_staff_phone="staff_phone";
    public static final String COLUMN_staff_email="staff_email";

    public dbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_VISITORS + "(" +
                COLUMN_u_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ursid + " TEXT, " +
                COLUMN_u_name + " TEXT, " +
                COLUMN_u_mobile+ " TEXT, " +
                COLUMN_purp + " TEXT, " +
                COLUMN_img + " TEXT, " +
                COLUMN_img_id + " TEXT, " +
                COLUMN_email + " TEXT, " +
                COLUMN_u_deig+ " TEXT, " +
                COLUMN_u_addr+  " TEXT " +
                ");";

        String query2 = "CREATE TABLE " + TABLE_DESIGNATION + "(" +
                COLUMN_degna_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_degna_usrid+ " TEXT,"+
                COLUMN_degna_name +  " TEXT " +
                ");";

        String query3 = "CREATE TABLE " + TABLE_PURPOSE + "(" +
                COLUMN_purp_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_purp_id_usr + " TEXT,"+
                COLUMN_purp_name +  " TEXT " +
                ");";

        String query4 = "CREATE TABLE " + TABLE_STAFF + "(" +
                COLUMN_staff_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_staff_name + " TEXT, " +
                COLUMN_staff_desig+ " TEXT, " +
                COLUMN_staff_phone+ " TEXT, " +
                COLUMN_staff_email+  " TEXT " +
                ");";

        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURPOSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESIGNATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF);
        onCreate(db);
    }

    public void addvisitor(visitordb Student) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ursid, Student.getUid());
        values.put(COLUMN_u_name, Student.getVisitorname());
        values.put(COLUMN_u_mobile, Student.getVisitormobile());
        values.put(COLUMN_purp, Student.getVisitorpurp());
        values.put(COLUMN_img, Student.getVisitorimage());
        values.put(COLUMN_img_id, Student.getVisitorimageid());
        values.put(COLUMN_email, Student.getVisitoremailid());
        values.put(COLUMN_u_deig, Student.getVisitordesig());
        values.put(COLUMN_u_addr, Student.getVisitoraddr());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_VISITORS, null, values);
        db.close();
    }

    public void addstaff(staffdb staffdb) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_staff_name, staffdb.getStaffname());
        values.put(COLUMN_staff_desig, staffdb.getStaffdesig());
        values.put(COLUMN_staff_phone, staffdb.getStaffmobile());
        values.put(COLUMN_staff_email, staffdb.getStaffemail());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_STAFF, null, values);
        db.close();
    }

    public void adddesig(String desig,String id) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_degna_name, desig);
        values.put(COLUMN_degna_usrid, id);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_DESIGNATION, null, values);
        db.close();
    }
    public void addpurp(String purp,String id) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_purp_name, purp);
        values.put(COLUMN_purp_id_usr, id);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PURPOSE, null, values);
        db.close();
    }
    public void deletetablevisitor()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_VISITORS + " WHERE 1");
    }
    public void deletetablestaff()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STAFF + " WHERE 1");
    }
    public void deletetabledesig()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DESIGNATION + " WHERE 1");
    }
    public void deletetablespurpos()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PURPOSE + " WHERE 1");
    }

    public String getstringdesi()
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_DESIGNATION + " WHERE 1";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("desi_name")) != null) {
                dbString += recordSet.getString(recordSet.getColumnIndex("desi_name"));
                dbString += "#";
            }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }

    public String getdesig(String id)
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_DESIGNATION + " WHERE "+ COLUMN_degna_usrid + "=\"" + id + "\";";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("desi_name")) != null) {
                dbString = recordSet.getString(recordSet.getColumnIndex("desi_name"));

            }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }

    public String gethostemail(String phone)
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_STAFF + " WHERE "+ COLUMN_staff_phone + "=\"" + phone + "\";";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("staff_email")) != null) {
                dbString = recordSet.getString(recordSet.getColumnIndex("staff_email"));

            }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }

    public String getstringpurp()
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_PURPOSE + " WHERE 1";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("purp_name")) != null ) {
                dbString += recordSet.getString(recordSet.getColumnIndex("purp_name"));
                dbString += "#";
            }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }

    public String getstringstaff()
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_STAFF + " WHERE 1";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("staff_name")) != null ) {
                if(!recordSet.getString(recordSet.getColumnIndex("staff_name")).equals("000")) {
                    dbString += recordSet.getString(recordSet.getColumnIndex("staff_name"));
                    dbString += " (";
                    dbString +=recordSet.getString(recordSet.getColumnIndex("staff_desig"))+")";
                    dbString += "#";
                    }
                }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }

    public String getdesid(String value)
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_DESIGNATION + " WHERE " + COLUMN_degna_name + "=\"" + value + "\";";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("desi_id_usr")) != null ) {

                    dbString += recordSet.getString(recordSet.getColumnIndex("desi_id_usr"));

            }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }
    public String getpurpid(String value)
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_PURPOSE + " WHERE " + COLUMN_purp_name + "=\"" + value + "\";";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("purp_id_usr")) != null ) {

                dbString += recordSet.getString(recordSet.getColumnIndex("purp_id_usr"));

            }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }

    public String getphonenumber(String value)
    {
        String dbString = "";
        SQLiteDatabase db1 = getWritableDatabase();
        String queryclass1 = "SELECT * FROM " + TABLE_STAFF + " WHERE " + COLUMN_staff_name + "=\"" + value + "\";";
        Cursor recordSet = db1.rawQuery(queryclass1, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("staff_phone")) != null ) {

                dbString += recordSet.getString(recordSet.getColumnIndex("staff_phone"));

            }
            recordSet.moveToNext();
        }

        db1.close();
        return dbString;
    }
}
