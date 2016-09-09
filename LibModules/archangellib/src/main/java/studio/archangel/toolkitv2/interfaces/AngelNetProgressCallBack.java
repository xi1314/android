package studio.archangel.toolkitv2.interfaces;

import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;

/**
 * Collect and modify from http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0904/3416.html
 */
public abstract class AngelNetProgressCallBack extends AngelNetCallBack {
    public abstract void onProgress(long bytesRead, long contentLength, boolean done);
}