package com.remair.heixiu.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.remair.heixiu.HXApp;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "heixiu.db";

    private Map<String, Dao> daos = new HashMap<String, Dao>();


    private DatabaseHelper(Context context) {
        super(context, HXApp.getInstance().getUserInfo().user_id +
                TABLE_NAME, null, 3);
        onOpen(getReadableDatabase());
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //			TableUtils.createTableIfNotExists(connectionSource, dataClass)
            TableUtils.createTable(connectionSource, ConcernInfoDB.class);
            TableUtils
                    .createTable(connectionSource, UnFollowConcernInfoDB.class);
            TableUtils.createTable(connectionSource, MessageInfoDB.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ConcernInfoDB.class, true);
            TableUtils
                    .dropTable(connectionSource, UnFollowConcernInfoDB.class, true);
            TableUtils.dropTable(connectionSource, MessageInfoDB.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static DatabaseHelper instance;


    /**
     * ������ȡ��Helper
     */
    public static synchronized DatabaseHelper getHelper(Context context) {

        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }

        return instance;
    }


    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }


    /**
     * �ͷ���Դ
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}

