package ca.qc.cegepdrummondville.applicationdehockey;

/** Source http://hmkcode.com/android-simple-sqlite-database-tutorial/ */

/**
 * Created by 9565960 on 2016-10-31.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hockeyApp.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE penalties ("+
            "_id                   PRIMARY KEY NOT NULL," +
            "code          VARCHAR NOT NULL," +
            "time          INT     NOT NULL," +
            "player_number INT     NOT NULL," +
            "local         BOOLEAN NOT NULL;";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Mise à jour depuis la version " + oldVersion + " vers "
                        + newVersion + ", ce qui supprimera toutes les données.");
        db.execSQL("DROP TABLE IF EXISTS penalties");
        onCreate(db);
    }
//---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) penalty + get all penalties + delete all penalties
     */

    // penalties table name
    private static final String TABLE_PENALTIES = "penalties";

    // Books Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_CODE = "code";
    private static final String KEY_TIME = "time";
    private static final String KEY_LOCAL = "local";
    private static final String KEY_PLAYER_NUMBER = "player_number";

    private static final String[] COLUMNS = {KEY_ID,KEY_CODE,KEY_TIME,KEY_LOCAL,KEY_PLAYER_NUMBER};

    public void addPenalty(Penalty penalty){
        //Log.d("addBook", book.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CODE, penalty.getCode());
        values.put(KEY_TIME, penalty.getTime());
        values.put(KEY_LOCAL, penalty.isLocal());
        values.put(KEY_PLAYER_NUMBER, penalty.getPlayer_number());

        // 3. insert
        db.insert(TABLE_PENALTIES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Penalty getPenalty(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PENALTIES, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Penalty penalty = new Penalty();
        penalty.setId(Integer.parseInt(cursor.getString(0)));
        penalty.setCode(cursor.getString(1));
        penalty.setTime(cursor.getInt(2));
        penalty.setPlayer_number(cursor.getInt(3));
        penalty.setLocal(cursor.(4));

        Log.d("getBook("+id+")", book.toString());

        // 5. return book
        return book;
    }

    // Get All Books
    public List<Book> getAllBooks() {
        List<Book> books = new LinkedList<Book>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_BOOKS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Book book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));

                // Add book to books
                books.add(book);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", books.toString());

        // return books
        return books;
    }

    // Updating single book
    public int updateBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle()); // get title
        values.put("author", book.getAuthor()); // get author

        // 3. updating row
        int i = db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(book.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single book
    public void deleteBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_BOOKS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(book.getId()) });

        // 3. close
        db.close();

        Log.d("deleteBook", book.toString());

    }
}
