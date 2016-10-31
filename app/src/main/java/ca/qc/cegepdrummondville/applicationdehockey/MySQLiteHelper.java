package ca.qc.cegepdrummondville.applicationdehockey;

/** Source http://www.vogella.com/tutorials/AndroidSQLite/article.html */

/**
 * Created by 9565960 on 2016-10-31.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EQUIPES = "equipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOM = "nom";

    private static final String DATABASE_NAME = "hockeyApp.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_EQUIPES + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NOM
            + " text not null);";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPES);
        onCreate(db);
    }

}
