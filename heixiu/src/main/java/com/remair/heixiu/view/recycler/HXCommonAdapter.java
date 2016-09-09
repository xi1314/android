package com.remair.heixiu.view.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.remair.heixiu.common.Supplier;
import java.util.List;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：LiuJun
 * 创建时间：16/8/26 17:55
 * 修改人：LiuJun
 * 修改时间：16/8/26 17:55
 * 修改备注：
 */
public class HXCommonAdapter<T> extends CommonRcvAdapter<T> {

    protected Supplier<AdapterItem<T>, T> mItemSupplier;


    public HXCommonAdapter(@Nullable List<T> data, Supplier<AdapterItem<T>, T> itemSupplier) {
        super(data);
        mItemSupplier = itemSupplier;
    }


    @Override
    public T getItemType(T t) {
        return t;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public AdapterItem createItem(Object o) {
        return mItemSupplier.get((T) o);
    }
}
