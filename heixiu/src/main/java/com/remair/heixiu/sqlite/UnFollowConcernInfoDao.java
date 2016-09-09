package com.remair.heixiu.sqlite;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wsk on 16/4/28.
 */
public class UnFollowConcernInfoDao {
    private Context context;
    private Dao<UnFollowConcernInfoDB, String> DaoOpe;
    private DatabaseHelper helper;

    public UnFollowConcernInfoDao(Context context){
        this.context = context;
        try{
            helper = DatabaseHelper.getHelper(context);
            DaoOpe = helper.getDao(UnFollowConcernInfoDB.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /***获取数据**/
    public UnFollowConcernInfoDB getUnFollowConcernInfoByUId(String user_id) throws Exception {
        if (null != user_id){
            UnFollowConcernInfoDB info=DaoOpe.queryForId(user_id);
            return info;
        }
        return null;
    }
    public void deleteUnFollowConcernInfoByUId(String user_id) throws SQLException {
        DaoOpe.deleteById(user_id);
    }
    /***获取所有数据**/
    public List<UnFollowConcernInfoDB> getConcernInfoAll() throws Exception {
        List<UnFollowConcernInfoDB> infoList=DaoOpe.queryForAll();
        return infoList;

    }
    /***添加数据**/
    public void addorupdate(UnFollowConcernInfoDB info,String user_id){
        try{

            UnFollowConcernInfoDB coninfo=getUnFollowConcernInfoByUId(user_id);
            if (null==coninfo){
                DaoOpe.create(info);
            }else{
//                DaoOpe.delete(coninfo);
                coninfo.setLastmessage(info.getLastmessage());
                coninfo.setUpdatetime(info.getUpdatetime());
                coninfo.setUserinfo(info.getUserinfo());
                DaoOpe.createOrUpdate(coninfo);
            }
//            DaoOpe.createOrUpdate(info);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public  void Modify(String user_id,String lastmessage){

        try {
            UpdateBuilder<UnFollowConcernInfoDB, String> updateBuilder = DaoOpe.updateBuilder();
            updateBuilder.where().eq("user_id", user_id);
            updateBuilder.updateColumnValue("lastmessage", lastmessage);
//            updateBuilder.updateColumnValue("Owner", uid);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void Modifyrrelation(String user_id,int relation){

        try {
            UpdateBuilder<UnFollowConcernInfoDB, String> updateBuilder = DaoOpe.updateBuilder();
            updateBuilder.where().eq("user_id", user_id);
            updateBuilder.updateColumnValue("relation", relation);
//            updateBuilder.updateColumnValue("Owner", uid);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
