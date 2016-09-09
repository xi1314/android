package com.remair.heixiu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nineoldandroids.animation.ObjectAnimator;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.adapters.ReplayAdapter;
import com.remair.heixiu.bean.ReplayBean;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.Util;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/3/2 15:59
 * 修改人：JXH
 * 修改时间：2016/3/2 15:59
 * 修改备注：
 */
public class MineLiveActivity extends HXBaseActivity {
    ReplayAdapter liveAdapter;
    ArrayList<ReplayBean> showslive;
    @BindView(R.id.tV_hot) TextView tv_hot;
    @BindView(R.id.tv_new) TextView tv_new;
    @BindView(R.id.include) LinearLayout include;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_right_text) TextView mTitleRightText;
    @BindView(R.id.tv_huifang) TextView mTvHuifang;
    @BindView(R.id.friend_list_live) RecyclerView mFriendListLive;
    @BindView(R.id.frag_show_list_hint) TextView mFragShowListHint;
    @BindView(R.id.tv_delete) TextView mTvDelete;
    protected boolean isExpanded = false;
    protected LinearLayoutManager linearLayoutManager;
    protected ImageView imageView;
    int userId;
    int type = 0;
    float x, x1, x2;
    protected Context mContext;

    ReplayAdapter.DeleteListener mDeleteListener = new ReplayAdapter.DeleteListener() {
        @Override
        public void deleteItem(RecyclerView.ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            HXHttpUtil.getInstance()
                      .deleteRecordMovie(showslive.get(adapterPosition).roomNum)
                      .subscribe(new HttpSubscriber<Object>() {
                          @Override
                          public void onNext(Object o) {
                              showslive.remove(adapterPosition);
                              mTvHuifang.setText(String.valueOf(
                                      showslive.size() +
                                              getString(R.string.count_replay)));
                              liveAdapter.notifyItemRemoved(adapterPosition);
                              if (showslive.size() < 1) {
                                  mFragShowListHint.setVisibility(View.VISIBLE);
                              }
                          }
                      });
        }
    };

    CommonRecyclerAdapter.OnItemToClickListener mOnItemToClickListener = new CommonRecyclerAdapter.OnItemToClickListener() {
        @Override
        public void onItemClick(View view, Object data) {
            ReplayBean replayBean = (ReplayBean) data;
            Intent intent = new Intent(MineLiveActivity.this, PlayerActivity.class);
            intent.putExtra("type", 0);
            intent.putExtra("url", replayBean.url);
            startActivity(intent);
            Subscription subscribe = HXHttpUtil.getInstance()
                                               .editRecordCount(Integer
                                                       .parseInt(replayBean.roomNum))
                                               .subscribe(new HttpSubscriber<Object>() {
                                                   @Override
                                                   public void onNext(Object o) {

                                                   }
                                               });
            addSubscription(subscribe);
        }
    };


    @Override
    protected void initUI() {
        setContentView(R.layout.act_live);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        UserInfo userInfo = HXApp.getInstance().getUserInfo();
        if (userInfo == null) {
            finish();
            return;
        }
        mContext = getApplication();
        userId = getIntent().getIntExtra("user_id", 0);
        type = getIntent().getIntExtra("type", 0);
        String count = getIntent().getStringExtra("count");
        if (count.equals("0")) {
            include.setVisibility(View.GONE);
        }
        mTvHuifang.setText(String
                .valueOf(count + getString(R.string.count_replay)));
        if (userId == userInfo.user_id) {
            mTitleTxt.setText(getString(R.string.my_live));
            mTitleRightText.setText(getString(R.string.edit));
        } else {
            String userName = getIntent().getStringExtra("userName");
            mTitleTxt.setText(String
                    .valueOf(userName + getString(R.string.live_other)));
        }
        showslive = new ArrayList<>();
        liveAdapter = new ReplayAdapter(MineLiveActivity.this, showslive, mDeleteListener,
                userInfo.user_id == userId);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        mFriendListLive.setLayoutManager(linearLayoutManager);
        mFriendListLive.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = Util.getPX((float) 0.5);
            }
        });
        mFriendListLive.setAdapter(liveAdapter);
        liveAdapter.setOnItemToClickListener(mOnItemToClickListener);
        toggleType(tv_new, tv_hot);
    }


    private void requestData() {
        Subscription subscribe = HXHttpUtil.getInstance()
                                           .recordLiveinfo(getIntent()
                                                   .getIntExtra("user_id", 0), type)
                                           .subscribe(new HttpSubscriber<List<ReplayBean>>() {
                                               @Override
                                               public void onNext(List<ReplayBean> replayBeen) {
                                                   showslive.clear();
                                                   if (replayBeen.size() < 1) {
                                                       mFragShowListHint
                                                               .setVisibility(View.VISIBLE);
                                                       return;
                                                   } else {
                                                       mFragShowListHint
                                                               .setVisibility(View.GONE);
                                                   }
                                                   showslive.addAll(replayBeen);
                                                   liveAdapter
                                                           .notifyDataSetChanged();
                                               }
                                           });
        addSubscription(subscribe);
    }


    public void toggleImage(ImageView imageView) {
        Object tag = imageView.getTag();
        if (tag.equals("true")) {
            imageView.setImageResource(R.drawable.select);
            imageView.setTag("false");
        } else {
            imageView.setImageResource(R.drawable.selected);
            imageView.setTag("true");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExpanded) {
                isExpanded = false;
                tv_hot.setEnabled(true);
                tv_new.setEnabled(true);
                mTvDelete.setVisibility(View.GONE);
                liveAdapter.setSlideState(new ReplayAdapter.ItemEnableSlide() {
                    @Override
                    public boolean setItemSlide() {
                        return true;
                    }
                });
                int itemCount = liveAdapter.getItemCount();
                for (int i = 0; i < itemCount; i++) {
                    ViewGroup childAt = (ViewGroup) linearLayoutManager
                            .getChildAt(i);
                    ViewGroup view = (ViewGroup) childAt.getChildAt(0);
                    ImageView selectView = (ImageView) view.getChildAt(0);
                    selectView.setImageResource(R.drawable.select);
                    View childAt1 = view.getChildAt(1);
                    View childAt2 = view.getChildAt(2);
                    ObjectAnimator
                            .ofFloat(selectView, "x", selectView.getX(), x)
                            .setDuration(250).start();
                    ObjectAnimator.ofFloat(childAt1, "x", childAt1.getX(), x1)
                                  .setDuration(250).start();
                    ObjectAnimator.ofFloat(childAt2, "x", childAt2.getX(), x2)
                                  .setDuration(250).start();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({ R.id.title_left_image, R.id.title_right_text, R.id.tv_new,
                     R.id.tV_hot })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.title_right_text:
                if (isExpanded) {
                    mTitleRightText.setText(getString(R.string.edit));
                    isExpanded = false;
                    tv_hot.setEnabled(true);
                    tv_new.setEnabled(true);
                    mTvDelete.setVisibility(View.GONE);
                    liveAdapter.itemSlideHelper.setEnableSlide(true);
                    liveAdapter
                            .setSlideState(new ReplayAdapter.ItemEnableSlide() {
                                @Override
                                public boolean setItemSlide() {
                                    return true;
                                }
                            });
                    int itemCount = liveAdapter.getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        ViewGroup childAt = (ViewGroup) linearLayoutManager
                                .getChildAt(i);
                        ViewGroup childView = (ViewGroup) childAt.getChildAt(0);
                        ImageView selectView = (ImageView) childView
                                .getChildAt(0);
                        selectView.setImageResource(R.drawable.select);
                        selectView.setTag("false");
                        View childAt1 = childView.getChildAt(1);
                        View childAt2 = childView.getChildAt(2);
                        ObjectAnimator
                                .ofFloat(selectView, "x", selectView.getX(), x)
                                .setDuration(250).start();
                        ObjectAnimator
                                .ofFloat(childAt1, "x", childAt1.getX(), x1)
                                .setDuration(250).start();
                        ObjectAnimator
                                .ofFloat(childAt2, "x", childAt2.getX(), x2)
                                .setDuration(250).start();
                    }
                    liveAdapter
                            .setOnItemToClickListener(mOnItemToClickListener);
                } else {
                    if (showslive.size() < 1) {
                        Notifier.showShortMsg(mContext, getString(R.string.replay_hint));
                        return;
                    }
                    mTitleRightText.setText(getString(R.string.cancel));
                    isExpanded = true;
                    tv_hot.setEnabled(false);
                    tv_new.setEnabled(false);
                    mTvDelete.setVisibility(View.VISIBLE);
                    liveAdapter.itemSlideHelper.setEnableSlide(false);
                    liveAdapter
                            .setSlideState(new ReplayAdapter.ItemEnableSlide() {
                                @Override
                                public boolean setItemSlide() {
                                    return false;
                                }
                            });
                    final int itemCount = liveAdapter.getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        ViewGroup childAt = (ViewGroup) linearLayoutManager
                                .getChildAt(i);
                        ViewGroup childView = (ViewGroup) childAt.getChildAt(0);
                        ImageView imageView = (ImageView) childView
                                .getChildAt(0);
                        View childAt1 = childView.getChildAt(1);
                        View childAt2 = childView.getChildAt(2);
                        x = imageView.getX();
                        x1 = childAt1.getX();
                        x2 = childAt2.getX();
                        ObjectAnimator
                                .ofFloat(imageView, "x", x, Util.getPX(40f) + x)
                                .setDuration(250).start();
                        ObjectAnimator.ofFloat(childAt1, "x", x1,
                                Util.getPX(40f) + x1).setDuration(250).start();
                        ObjectAnimator.ofFloat(childAt2, "x", x2,
                                Util.getPX(40f) + x2).setDuration(250).start();
                    }
                    mTvDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String roomNum = "";
                            final ArrayList<Integer> list = new ArrayList<>();
                            for (int i = 0; i < itemCount; i++) {
                                ViewGroup childAt = (ViewGroup) linearLayoutManager
                                        .getChildAt(i);
                                ViewGroup view = (ViewGroup) childAt
                                        .getChildAt(0);
                                imageView = (ImageView) view.getChildAt(0);
                                Object tag = imageView.getTag();
                                if (tag.equals("true")) {
                                    RecyclerView.ViewHolder viewHolder = liveAdapter
                                            .getChildViewHolder(childAt);
                                    int adapterPosition = viewHolder
                                            .getAdapterPosition();
                                    list.add(adapterPosition);
                                    String room = showslive
                                            .get(adapterPosition).roomNum;
                                    roomNum = roomNum + room;
                                }
                            }
                            if (!roomNum.equals("")) {
                                roomNum = roomNum
                                        .substring(0, roomNum.length() - 1);

                                Subscription subscribe = HXHttpUtil
                                        .getInstance()
                                        .deleteRecordMovie(roomNum)
                                        .subscribe(new HttpSubscriber<Object>() {
                                            @Override
                                            public void onNext(Object o) {
                                                Notifier.showShortMsg(mContext, "删除成功");
                                                for (int i = 0; i < list.size();
                                                     i++) {
                                                    Integer integer = list
                                                            .get(i);
                                                    showslive.remove(integer
                                                            .intValue());
                                                }
                                                mTvHuifang.setText(String
                                                        .valueOf(showslive
                                                                .size() +
                                                                getString(R.string.count_replay)));
                                                liveAdapter
                                                        .notifyDataSetChanged();
                                                if (showslive.size() < 1) {
                                                    mFragShowListHint
                                                            .setVisibility(View.VISIBLE);
                                                }
                                            }
                                        });
                                addSubscription(subscribe);
                            } else {
                                Notifier.showShortMsg(mContext, "请选择要删除的回放");
                            }
                        }
                    });
                    liveAdapter
                            .setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
                                @Override
                                public void onItemClick(View view, Object data) {
                                    ViewGroup viewGroup = (ViewGroup) view;
                                    ViewGroup viewgroup = (ViewGroup) viewGroup
                                            .getChildAt(0);
                                    toggleImage((ImageView) viewgroup
                                            .getChildAt(0));
                                }
                            });
                }
                break;
            case R.id.tv_new:
                type = 1;
                toggleType(tv_new, tv_hot);
                break;
            case R.id.tV_hot:
                type = 0;
                toggleType(tv_hot, tv_new);
                break;
        }
    }


    protected void toggleType(TextView view1, TextView view2) {
        Drawable drawable1 = getResources()
                .getDrawable(R.drawable.icon_best_hot);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1
                .getMinimumHeight());
        view1.setCompoundDrawables(drawable1, null, null, null);
        view2.setCompoundDrawables(null, null, null, null);
        requestData();
    }
}
