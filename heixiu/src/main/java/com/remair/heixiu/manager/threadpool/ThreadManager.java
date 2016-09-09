package com.remair.heixiu.manager.threadpool;

import com.apkfuns.logutils.LogUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称：Android
 * 类描述：异步线程池管理
 * 创建人：Jw
 * 创建时间：16/8/30 16:10
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadManager  {
    private static ExecutorService executorService;
    public static ThreadManager tm;
    private ThreadManager(){}

    public static ThreadManager getInstance(){
        if(tm == null){
            tm = new ThreadManager();
            if(executorService != null){
                if(!executorService.isShutdown()){
                    executorService.shutdown();
                }
            }
            executorService =  Executors.newCachedThreadPool();
        }
        return tm;
    }
    public void execute(Runnable runnable){
        if(runnable != null){
            executorService.execute(runnable);
        }else{
            LogUtils.i("runnable is null ...");
        }
    }
}
