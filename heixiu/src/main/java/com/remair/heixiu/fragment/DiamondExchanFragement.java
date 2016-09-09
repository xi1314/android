package com.remair.heixiu.fragment;

import android.support.v7.widget.RecyclerView;
import com.remair.heixiu.bean.Dimaondex;
import com.remair.heixiu.common.Supplier;
import com.remair.heixiu.fragment.base.RefreshListFragment;
import com.remair.heixiu.utils.LayoutManagerUtil;
import com.remair.heixiu.view.recycler.item.DiamondEchanItem;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.item.AdapterItem;

/**
 * 项目名称：heixu
 * 类描述：
 * 创建人：Liuyu
 * 创建时间：2016/8/31 18:52
 */
public class DiamondExchanFragement extends RefreshListFragment<Dimaondex>{
    List<Dimaondex> userList = new ArrayList<Dimaondex>();

   public int type;
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return LayoutManagerUtil.getGridLayoutManager(getActivity(),3);
    }


    @Override
    protected void requestData(boolean getMore) {


    }



 /*

      @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater
                .inflate(R.layout.act_fans1, container, false);

    }*/



    @Override
    protected Supplier<AdapterItem<Dimaondex>, Dimaondex> getAdapterItemSupplier() {
        return new Supplier<AdapterItem<Dimaondex>, Dimaondex>() {
            @Override
            public AdapterItem<Dimaondex> get(Dimaondex dimaondex) {

                return new DiamondEchanItem(getActivity());
            }
        };
    }
    @Override
    protected String getHint2Text() {
        return null;
    }

    @Override
    protected String getHint1Text() {
        return null;
    }

    @Override
    public void initData() {
        type= getArguments().getInt("type");
        // 1代表现金充值 //2代表存在感兑换钻石    //3代表钻石兑换黑豆//4代表存在感对黑豆
        if(type==1) {
            //现金充值
            userList.add(new Dimaondex(1, 20, type));
            userList.add(new Dimaondex(6, 60, type));
            userList.add(new Dimaondex(30, 300, type));
            userList.add(new Dimaondex(98, 1000, type));
            userList.add(new Dimaondex(298, 3100, type));
            userList.add(new Dimaondex(598, 6140, type));
            onDataSuccess(userList);
        }else if(type==2){
            userList.add(new Dimaondex(1000, 40, type));
            userList.add(new Dimaondex(5000, 200, type));
            userList.add(new Dimaondex(10000, 400, type));
            userList.add(new Dimaondex(25000, 1030, type));
            userList.add(new Dimaondex(50000, 2080, type));
            userList.add(new Dimaondex(100000, 4168, type));
            onDataSuccess(userList);
        }else if(type==3){

            userList.add(new Dimaondex(10, 1000, type));
            userList.add(new Dimaondex(60, 6000, type));
            userList.add(new Dimaondex(300, 30000, type));
            userList.add(new Dimaondex(980, 98000, type));
            userList.add(new Dimaondex(2980, 298000, type));
            userList.add(new Dimaondex(5880,588000, type));
            onDataSuccess(userList);
        }else if(type==4){
            userList.add(new Dimaondex(1000, 4000, type));
            userList.add(new Dimaondex(5000, 20000, type));
            userList.add(new Dimaondex(10000, 40000, type));
            userList.add(new Dimaondex(25000, 103000, type));
            userList.add(new Dimaondex(50000, 208800, type));
            userList.add(new Dimaondex(100000, 416800, type));
            onDataSuccess(userList);

        }



    }
}
