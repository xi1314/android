package com.remair.heixiu.bean;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/24 15:16
 * 修改人：LiuJun
 * 修改时间：16/8/24 15:16
 * 修改备注：
 */
public class HttpResult<T> {

    public String code;
    public String msg;
    public String token;
    public T data;
}
