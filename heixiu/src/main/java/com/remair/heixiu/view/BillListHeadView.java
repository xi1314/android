package com.remair.heixiu.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.bean.FansInfoBean;
import com.remair.heixiu.utils.HXImageLoader;
import com.remair.heixiu.utils.Utils;
import com.remair.heixiu.utils.Xtgrade;
import java.util.List;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/8/30 11:37
 * 修改人：JXH
 * 修改时间：2016/8/30 11:37
 * 修改备注：
 */
public class BillListHeadView extends FrameLayout {

    @BindView(R.id.photo_two) SimpleDraweeView mPhotoTwo;
    @BindView(R.id.value_two) TextView mValueTwo;
    @BindView(R.id.part_two) LinearLayout mPartTwo;
    @BindView(R.id.photo_one) SimpleDraweeView mPhotoOne;
    @BindView(R.id.value_one) TextView mValueOne;
    @BindView(R.id.part_one) LinearLayout mPartOne;
    @BindView(R.id.photo_three) SimpleDraweeView mPhotoThree;
    @BindView(R.id.value_three) TextView mValueThree;
    @BindView(R.id.part_three) LinearLayout mPartThree;
    private List mList;
    private Context mContext;
    private int px;
    private int px1;


    public BillListHeadView(Context context, List list) {
        super(context);
        this.mContext = context;
        this.mList = list;
        px = Utils.getPX(118);
        px1 = Utils.getPX(70);
        initView();
        initData();
    }


    private void initData() {

        for (int i = 0; i < mList.size(); i++) {
            FansInfoBean fansInfoBean = (FansInfoBean) mList.get(i);
            if (i == 0) {

                HXImageLoader.loadImage(mPhotoOne, fansInfoBean.photo, px, px);
                mValueOne.setText(Xtgrade
                        .moneynumber(fansInfoBean.charm_value_sent + ""));
                mPartOne.setVisibility(View.VISIBLE);
                mPartOne.setTag(fansInfoBean);
            } else {
                if (i == 1) {
                    HXImageLoader
                            .loadImage(mPhotoTwo, fansInfoBean.photo, px1, px1);
                    mValueTwo.setText(Xtgrade
                            .moneynumber(fansInfoBean.charm_value_sent + ""));
                    mPartTwo.setVisibility(View.VISIBLE);
                    mPartTwo.setTag(fansInfoBean);
                } else if (i == 2) {
                    HXImageLoader
                            .loadImage(mPhotoThree, fansInfoBean.photo, px1, px1);
                    mValueThree.setText(Xtgrade
                            .moneynumber(fansInfoBean.charm_value_sent + ""));
                    mPartThree.setVisibility(View.VISIBLE);
                    mPartThree.setTag(fansInfoBean);
                }
            }
        }
    }


    private void initView() {
        View.inflate(mContext, R.layout.part_bill_list, this);
        ButterKnife.bind(this);
    }


    @OnClick({ R.id.part_two, R.id.part_one, R.id.part_three })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.part_two:
            case R.id.part_one:
            case R.id.part_three:
                FansInfoBean tag = (FansInfoBean) view.getTag();
                Intent intent = new Intent(mContext, FriendMessageActivity.class);
                intent.putExtra("viewed_user_id", tag.user_id);
                mContext.startActivity(intent);
                break;
        }
    }
}
