package com.remair.heixiu.sqlite;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import java.util.List;

/**
 * Created by wsk on 16/4/28.
 */
public class ConcernInfoDao {
    private Context context;
    private Dao<ConcernInfoDB, String> DaoOpe;
    private DatabaseHelper helper;

    public ConcernInfoDao(Context context){
        this.context = context;
        try{
            helper = DatabaseHelper.getHelper(context);
            DaoOpe = helper.getDao(ConcernInfoDB.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /***获取数据**/
    public ConcernInfoDB getConcernInfoByUId(String user_id) throws Exception {
        if (null != user_id){
            ConcernInfoDB info=DaoOpe.queryForId(user_id);
            return info;
        }
        return null;
    }
    public void deleteConcernInfoByUId(String user_id) throws java.sql.SQLException {
        DaoOpe.deleteById(user_id);
    }
    /***获取所有数据**/
    public List<ConcernInfoDB> getConcernInfoAll() throws Exception {
        List<ConcernInfoDB> infoList=DaoOpe.queryForAll();
        return infoList;
    }
    /***添加数据**/
    public void addorupdate(ConcernInfoDB info,String user_id){
        try{
            ConcernInfoDB coninfo=getConcernInfoByUId(user_id);
            if (null==coninfo){
                DaoOpe.create(info);
            }else{
                coninfo.setLastmessage(info.getLastmessage());
                coninfo.setUpdatetime(info.getUpdatetime());
                coninfo.setUserinfo(info.getUserinfo());
                DaoOpe.createOrUpdate(coninfo);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void Modify(String user_id,String lastmessage){

        try {
            UpdateBuilder<ConcernInfoDB, String> updateBuilder = DaoOpe.updateBuilder();
            updateBuilder.where().eq("user_id", user_id);
            updateBuilder.updateColumnValue("lastmessage", lastmessage);
//            updateBuilder.updateColumnValue("Owner", uid);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void Modifyrelation(String user_id,int relation){

        try {
            UpdateBuilder<ConcernInfoDB, String> updateBuilder = DaoOpe.updateBuilder();
            updateBuilder.where().eq("user_id", user_id);
            updateBuilder.updateColumnValue("relation", relation);
//            updateBuilder.updateColumnValue("Owner", uid);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
