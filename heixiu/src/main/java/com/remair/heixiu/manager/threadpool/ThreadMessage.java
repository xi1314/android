package com.remair.heixiu.manager.threadpool;
import java.util.LinkedHashMap;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：Jw
 * 创建时间：16/8/30 16:10
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadMessage extends LinkedHashMap<String,Object>{
    private int what;

    public void setWhat(int what) {
        this.what = what;
    }
    public int getWhat() {
        return what;
    }

    public int getInt(String key){
        if(key != null){
            Object o = get(key);
            if(o != null){
                try{
                    return Integer.parseInt(o.toString());
                }catch (Exception e){
                    return -1;
                }
            }
        }
        return -1;
    }
    public String getString(String key){
        if(key != null){
            Object o = get(key);
            if(o != null){
                return o.toString();
            }
        }
        return null;
    }
    public float getFloat(String key){
        if(key != null){
            Object o = get(key);
            if(o != null){
                try{
                    return Float.parseFloat(o.toString());
                }catch (Exception e){
                    return -1;
                }
            }
        }
        return -1;
    }
    public double getDouble(String key){
        if(key != null){
            Object o = get(key);
            if(o != null){
                try{
                    return Double.parseDouble(o.toString());
                }catch (Exception e){
                    return -1;
                }
            }
        }
        return -1;
    }
    public long getLong(String key){
        if(key != null){
            Object o = get(key);
            if(o != null){
                try{
                    return Long.parseLong(o.toString());
                }catch (Exception e){
                    return -1;
                }
            }
        }
        return -1;
    }
}
