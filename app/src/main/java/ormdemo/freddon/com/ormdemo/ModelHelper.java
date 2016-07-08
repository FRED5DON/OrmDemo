package ormdemo.freddon.com.ormdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


/**
 * Created by FRED on 2016/7/8.
 */
public class ModelHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "model.db";
    private static final int DATABASE_VERSION = 2;
    private static ModelHelper helper;
    private Dao<Model, Long> logDao;

    public ModelHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ModelHelper getHelper(Context context) {
        if (helper == null) {
            synchronized (ModelHelper.class) {
                helper = new ModelHelper(context);
            }
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        //初始化建表
        try {
            TableUtils.createTableIfNotExists(connectionSource, Model.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            if (oldVersion == 1) {
                TableUtils.dropTable(connectionSource, Model.class, true);
                TableUtils.createTableIfNotExists(connectionSource, Model.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Model, Long> getModelDao() throws SQLException {
        if (logDao == null) {
            logDao = getDao(Model.class);
        }
        return logDao;
    }

}
