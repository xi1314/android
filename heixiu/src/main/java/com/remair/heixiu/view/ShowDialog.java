package com.remair.heixiu.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.remair.heixiu.R;
import studio.archangel.toolkitv2.views.AngelMaterialButton;

/**
 * Created by wsk on 16/5/13.
 */
public class ShowDialog extends Dialog {

    private Context context;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private ClickListenerInterface clickListenerInterface;
    private AngelMaterialButton btn_cecal;
    private AngelMaterialButton btn_sure;
    private RelativeLayout rl_back;
    private TextView tv_reminder, tv_message;
    private boolean isupdata = false;
    private View view;

    public interface ClickListenerInterface {
        void doConfirm();

        void doCancel();
    }


    public ShowDialog(Context context, String title, String confirmButtonText, String cacelButtonText) {
        super(context, R.style.dialog);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
        init();
    }


    public ShowDialog(Context context) {
        super(context);
        init();
    }


    public ShowDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }


    public ShowDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.show_dialg, null);
        //        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(Utils.getPX(240), ViewGroup.LayoutParams.WRAP_CONTENT);
        //        view.setLayoutParams(layoutParams);
        setContentView(view);

        tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_reminder = (TextView) view.findViewById(R.id.tv_reminder);
        rl_back = (RelativeLayout) view.findViewById(R.id.rl_back);
        btn_cecal = (AngelMaterialButton) view.findViewById(R.id.btn_cecal);
        btn_sure = (AngelMaterialButton) view.findViewById(R.id.btn_sure);

        tv_message.setText(title);
        btn_sure.setText(confirmButtonText);
        if (cacelButtonText.equals("")) {
            btn_cecal.setVisibility(view.GONE);
        }
        btn_cecal.setText(cacelButtonText);

        btn_cecal.setOnClickListener(new clickListener());
        btn_sure.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        //        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        int w = View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        lp.height = view.getMeasuredHeight();
        lp.width = view.getMeasuredWidth();
        dialogWindow.setAttributes(lp);
    }


    public void setCancelGone() {
        btn_cecal.setVisibility(View.GONE);
    }


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.btn_sure:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.btn_cecal:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }


    public void setbackGround() {
        rl_back.setBackgroundResource(R.drawable.upgrade_background);
        tv_reminder.setText("");
        RelativeLayout.LayoutParams searchParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        searchParams.topMargin = context.getResources()
                                        .getDimensionPixelSize(R.dimen.member_layout_updata_margin);
        tv_message.setLayoutParams(searchParams);
    }
}
