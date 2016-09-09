package com.remair.heixiu.common;

/**
 * 项目名称：Android
 * 类描述：一个类,它可以提供一个类型的对象。
 * 创建人：LiuJun
 * 创建时间：16/8/26 18:08
 * 修改人：LiuJun
 * 修改时间：16/8/26 18:08
 * 修改备注：
 */
public interface Supplier<T, V> {

    /**
     * 根据 V 创建一个 T
     */
    T get(V v);
}
