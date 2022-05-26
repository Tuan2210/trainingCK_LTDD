package com.example.training_ktck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trainingCK_QLSach";

    private static final String TABLE_CONTACTS_BENHNHAN = "Sach";
    //    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TENSACH = "tensach";
    private static final String KEY_THELOAI = "theloai";
    private static final String KEY_GIA = "gia";

    private static final String create_table_lsk = "CREATE TABLE " + TABLE_CONTACTS_BENHNHAN + "("
            + KEY_EMAIL + " TEXT, "
            + KEY_TENSACH + " TEXT, "
            + KEY_THELOAI + " TEXT, "
            + KEY_GIA + " TEXT" + ")";

    public SQLiteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_lsk);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS_BENHNHAN);

        // Create tables again
        onCreate(db);
    }

    public void addBook(ThongTinChung thongTinChung) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, thongTinChung.getAccount().getEmail());

        values.put(KEY_TENSACH, thongTinChung.getBook().getTenSach());
        values.put(KEY_THELOAI, thongTinChung.getBook().getTheLoai());
        values.put(KEY_GIA, thongTinChung.getBook().getGia());

        db.insert(TABLE_CONTACTS_BENHNHAN, null, values);
        db.close();
    }

    public List<ThongTinChung> getAllInfo() {
        List<ThongTinChung> thongTinChungList=new ArrayList<ThongTinChung>();
        String selectQuery = "SELECT email, tensach, theloai, gia FROM " + TABLE_CONTACTS_BENHNHAN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ThongTinChung thongTinChung=new ThongTinChung();
                thongTinChung.getAccount().setEmail(cursor.getString(0));
                thongTinChung.getBook().setTenSach(cursor.getString(1));
                thongTinChung.getBook().setTheLoai(cursor.getString(2));
                thongTinChung.getBook().setGia(cursor.getString(3));

                thongTinChungList.add(thongTinChung);
            } while (cursor.moveToNext());
        }
        return thongTinChungList;
    }

    public void deleteBookInfo(ThongTinChung thongTinChung) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS_BENHNHAN, KEY_TENSACH + " = ?",
                new String[] { String.valueOf(thongTinChung.getBook().getTenSach()) });
        db.close();
    }

    public int updateBookInfo(ThongTinChung thongTinChung) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_THELOAI, thongTinChung.getBook().getTheLoai());
        values.put(KEY_GIA, thongTinChung.getBook().getGia());

        return db.update(TABLE_CONTACTS_BENHNHAN, values, KEY_TENSACH + " = ?",
                new String[] { String.valueOf(thongTinChung.getBook().getTenSach()) });
    }

}
