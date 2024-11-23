package com.example.stiregistration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Student_Data.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Students";

    //Column Names
    private static final String STUDENT_NUMBER = "StudentNumber"; //Primary Key
    private static final String LAST_NAME = "LastName";
    private static final String FIRST_NAME = "FirstName";
    private static final String MIDDLE_NAME = "MiddleName"; //Optional
    private static final String BIRTHDAY = "Birthday";
    private static final String AGE = "Age";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                STUDENT_NUMBER + " TEXT PRIMARY KEY, " +
                LAST_NAME + " TEXT NOT NULL, " +
                FIRST_NAME + " TEXT NOT NULL, " +
                MIDDLE_NAME + " TEXT, " +
                BIRTHDAY + " TEXT, " +
                AGE + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    // Insert a new student record
    public boolean insertData(String studentNumber, String lastName, String firstName, String middleName, String birthday, int age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STUDENT_NUMBER, studentNumber);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(MIDDLE_NAME, middleName);
        contentValues.put(BIRTHDAY, birthday);
        contentValues.put(AGE, age);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    //TO COMPLETE A DATABASE: BELOW LISTED THE CRUD OPERATIONS (Create, Read, Update, Delete Operations)

    // Retrieve all students data
    public Cursor getAlldata() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    // Update an existing student record
    public boolean updateData(String studentNumber, String lastName, String firstName, String middleName, String birthday, int age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STUDENT_NUMBER, studentNumber);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(MIDDLE_NAME, middleName);
        contentValues.put(BIRTHDAY, birthday);
        contentValues.put(AGE, age);

        int result = db.update(TABLE_NAME, contentValues, STUDENT_NUMBER + " = ?", new String[]{studentNumber});
        return result > 0;
    }

    // Delete a student record
    public boolean deleteData(String studentNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, STUDENT_NUMBER + " = ? ",new String[]{studentNumber});
        return  result > 0;
    }

    //Check if a student ID Exists
    public boolean studentExists(String studentNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE " + STUDENT_NUMBER + " = ?", new String[]{studentNumber});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
