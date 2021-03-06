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

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
    // penalties table name
    private static final String TABLE_PENALTIES = "penalties";

    // Penalties Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_CODE = "code";
    private static final String KEY_TIME = "time";
    private static final String KEY_LOCAL = "local";
    private static final String KEY_PLAYER_NUMBER = "player_number";

    private static final String[] COLUMNS = {KEY_ID,KEY_CODE,KEY_TIME,KEY_LOCAL,KEY_PLAYER_NUMBER};
    private static final String DATABASE_NAME = "hockeyApp.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_PENALTIES + " ( "+
            KEY_ID + " PRIMARY KEY NOT NULL," +
            KEY_CODE +  " VARCHAR NOT NULL," +
            KEY_TIME + " INT NOT NULL," +
            KEY_PLAYER_NUMBER + " INT NOT NULL," +
            KEY_LOCAL + " BOOLEAN NOT NULL );";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENALTIES);
        onCreate(db);
    }
//---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) penalty + get all penalties + delete all penalties
     */

    public void addPenalty(Penalty penalty){
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

    /*
    public Penalty getPenalty(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PENALTIES, // a. table
                        COLUMNS, // b. column names
                        KEY_ID + " = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build penalty object
        Penalty penalty = new Penalty();
        penalty.setId(Integer.parseInt(cursor.getString(0)));
        penalty.setCode(cursor.getString(1));
        penalty.setTime(cursor.getInt(2));
        penalty.setPlayer_number(cursor.getInt(3));
        penalty.setLocal(cursor.getInt(4));

        // 5. return penalty
        return penalty;
    }
    */

    // Retourne la dernière pénalité de l'équipe (local ou visiteur)
    //
    // param local boolean
    public Penalty getLastPenalty(boolean isLocal){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_PENALTIES + " WHERE " + KEY_LOCAL + " = '" + String.valueOf(isLocal) + "'", null);

        // 3. if we got results get the first one
        Penalty penalty = null;
        if (cursor.getCount() > 0) {
            cursor.moveToLast();

            // 4. build penalty object
            penalty = new Penalty();
            penalty.setId(Integer.parseInt(cursor.getString(0)));
            penalty.setCode(cursor.getString(1));
            penalty.setTime(cursor.getInt(2));
            penalty.setPlayer_number(cursor.getInt(3));
            penalty.setLocal(cursor.getInt(4));
        }

        // 5. return penalty
        return penalty;
    }

    /*
    // Get All Penalties
    public List<Penalty> getAllPenalties() {
        List<Penalty> penalties = new LinkedList<Penalty>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PENALTIES;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build penalty and add it to list
        Penalty penalty = null;
        if (cursor.moveToFirst()) {
            do {
                penalty = new Penalty();
                penalty.setId(Integer.parseInt(cursor.getString(0)));
                penalty.setCode(cursor.getString(1));
                penalty.setTime(cursor.getInt(2));
                penalty.setPlayer_number(cursor.getInt(3));
                penalty.setLocal(cursor.getInt(4));

                // Add penalty to penalties
                penalties.add(penalty);
            } while (cursor.moveToNext());
        }

        // return penalties
        return penalties;
    }
    */

    // Updating single penalty
    public int updatePenalty(Penalty penalty) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", penalty.getCode()); // get title
        values.put("author", penalty.getTime()); // get author
        values.put("author", penalty.getPlayer_number()); // get author
        values.put("author", penalty.isLocal()); // get author

        // 3. updating row
        int i = db.update(TABLE_PENALTIES, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(penalty.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single penalty
    public void deletePenalty(Penalty penalty) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_PENALTIES,
                KEY_ID+" = ?",
                new String[] { String.valueOf(penalty.getId()) });

        // 3. close
        db.close();

        Log.d("deletePenalty", penalty.toString());

    }
}
