package studio.archangel.toolkitv2.interfaces;

import android.content.Context;

import studio.archangel.toolkitv2.models.AngelException;

/**
 * Created by Michael on 2015/4/3.
 */
public interface NetProvider {
    String getServerRoot();

    String getReleaseServerRoot();

    String getDebugServerRoot();

    String getErrorMsg();

    String reportError(Context c, AngelException e);

    String getCookieField();

    String getCookieName();
}
