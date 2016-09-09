package com.remair.heixiu.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.base.HXBaseActivity;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.utils.Utils;
import org.json.JSONObject;
import rx.Subscription;
import studio.archangel.toolkitv2.util.Util;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/5/11 16:50
 * 修改人：JXH
 * 修改时间：2016/5/11 16:50
 * 修改备注：
 */
public class LevelActivity extends HXBaseActivity {
    @BindView(R.id.tv_grade) TextView tv_grade;
    @BindView(R.id.text_leve_lv2) TextView text_leve_lv2;
    @BindView(R.id.text_leve_lv1) TextView text_leve_lv1;
    @BindView(R.id.textview_proportion) TextView textview_proportion;
    @BindView(R.id.my_progress) ProgressBar my_progress;
    @BindView(R.id.distance_textview) TextView distance_textview;
    @BindView(R.id.title_txt) TextView mTitleTxt;
    @BindView(R.id.title_left_image) ImageButton mTitleLeftImage;
    private Intent mIntent;
    private int mProportion;
    int screen_widthx;


    @Override
    protected void initUI() {
        setContentView(R.layout.act_levl);
        ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        mTitleTxt.setText(getString(R.string.grade));
        int[] screenSize = Utils.getScreenSize(mActivity);
        screen_widthx = screenSize[0];
        mIntent = getIntent();
        int grade = mIntent.getIntExtra("grade", 0);
        tv_grade.setText(String.valueOf(grade));
        text_leve_lv1.setText(String.valueOf("LV" + grade));
        text_leve_lv2.setText(String.valueOf("LV" + (grade + 1)));
        getData();
    }


    private void getData() {
        Subscription subscribe = HXHttpUtil.getInstance().grade(mIntent
                .getIntExtra("user_id", 0))
                                           .subscribe(new HttpSubscriber<JSONObject>() {

                                               @Override
                                               public void onNext(JSONObject jsonObject) {
                                                   int grade = jsonObject
                                                           .optInt("grade");
                                                   mProportion = (int) (
                                                           jsonObject
                                                                   .optDouble("proportion") *
                                                                   100);
                                                   int differ_empiric = jsonObject
                                                           .optInt("differ_empiric");
                                                   textview_proportion
                                                           .setText(String
                                                                   .valueOf(
                                                                           mProportion +
                                                                                   "%"));
                                                   distance_textview
                                                           .setText(String
                                                                   .valueOf(
                                                                           getString(R.string.diff) +
                                                                                   differ_empiric));
                                                   if (grade !=
                                                           mIntent.getIntExtra("grade", 0)) {
                                                       tv_grade.setText(String
                                                               .valueOf(grade));
                                                       text_leve_lv1
                                                               .setText(String
                                                                       .valueOf(
                                                                               "LV" +
                                                                                       grade));
                                                       text_leve_lv2
                                                               .setText(String
                                                                       .valueOf(
                                                                               "LV" +
                                                                                       (grade +
                                                                                                1)));
                                                   }
                                                   startAnim();
                                               }
                                           });
        addSubscription(subscribe);
    }


    private void startAnim() {
        int px = Utils.getPX(216);
        int totlowidth = px * mProportion / 100;
        int datewidth = Util.getPX(14);

        if (datewidth < totlowidth) {
            ValueAnimator valueAnimator = ValueAnimator
                    .ofInt(datewidth, totlowidth);
            valueAnimator
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator p) {
                            ViewGroup.LayoutParams p1 = my_progress
                                    .getLayoutParams();
                            p1.width = (int) p.getAnimatedValue();
                            my_progress.setLayoutParams(p1);
                        }
                    });

            valueAnimator.setDuration(1500).start();
            int startx = (screen_widthx - px) / 2;
            ObjectAnimator objectAnimator = ObjectAnimator
                    .ofFloat(textview_proportion, "x", startx,
                            startx + totlowidth -
                                    textview_proportion.getWidth() / 2);

            objectAnimator.setDuration(1500).start();
        }
    }


    @OnClick(R.id.title_left_image)
    public void onClick() {
        finish();
    }
}
