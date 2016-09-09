/**
 *
 */
package studio.archangel.toolkitv2.interfaces;

import studio.archangel.toolkitv2.adapters.CommonAdapterViewCache;


/**
 * 缓存生成监听器
 *
 * @param <T> 实体数据类型
 * @author Michael
 */
public interface OnCacheGeneratedListener<T> {
    /**
     * @param c        View缓存
     * @param t        实体数据
     * @param position
     */
    public void onCacheGenerated(CommonAdapterViewCache c, T t, int position);
}
