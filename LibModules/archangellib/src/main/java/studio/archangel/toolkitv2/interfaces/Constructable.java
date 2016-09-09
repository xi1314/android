/**
 *
 */
package studio.archangel.toolkitv2.interfaces;

import android.os.Message;

/**
 * 实现了此接口的类必须要能通过setValue来赋值
 *
 * @author Michael
 */
public interface Constructable {
    /**
     * 通过msg的obj和what等字段来赋值
     *
     * @param msg
     */
    public void setValue(Message msg);
}
