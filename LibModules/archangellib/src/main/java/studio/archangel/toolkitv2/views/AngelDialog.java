package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;

public class AngelDialog extends android.app.Dialog {

    String message;
    TextView tv_msg;
    String title;
    TextView tv_title;
    FrameLayout v_container;
    AngelMaterialButton b_ok;
    AngelMaterialButton b_cancel;
    AngelMaterialButton b_neutral;
    //    View v_custom;
    View.OnClickListener listener_ok;
    View.OnClickListener listener_cancel;
    View.OnClickListener listener_neutral;
    OnPostShowListener listener;
    int main_color;
    AngelMaterialProperties.DialogStyle button_style = AngelMaterialProperties.DialogStyle.ok_cancel;

    public AngelDialog(Context context, String title, String message) {
        super(context, R.style.AnimDialog);
        this.message = message;
        this.title = title;
    }

    public AngelDialog(Context context, String title, String message, int color) {
        this(context, title, message);
        main_color = context.getResources().getColor(color);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog);

        this.tv_title = (TextView) findViewById(R.id.view_dialog_title);
        setTitle(title);
        this.tv_msg = (TextView) findViewById(R.id.view_dialog_message);
        setMessage(message);
        v_container = (FrameLayout) findViewById(R.id.view_dialog_custom);
//        if (v_custom != null) {
//            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            p.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//            v_container.addView(v_custom, p);
//            v_container.setVisibility(View.VISIBLE);
//            tv_msg.setVisibility(View.GONE);
//        }

        this.b_ok = (AngelMaterialButton) findViewById(R.id.button_accept);
        b_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (listener_ok != null)
                    listener_ok.onClick(v);
            }
        });
        this.b_cancel = (AngelMaterialButton) findViewById(R.id.button_cancel);

        b_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (listener_cancel != null)
                    listener_cancel.onClick(v);
            }
        });
        b_neutral = (AngelMaterialButton) findViewById(R.id.button_neutral);
        b_neutral.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (listener_neutral != null)
                    listener_neutral.onClick(v);
            }
        });
        if (main_color != 0) {
            b_ok.setTextColor(main_color);
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        if (message == null || message.isEmpty())
            tv_msg.setVisibility(View.GONE);
        else {
            tv_msg.setVisibility(View.VISIBLE);
            tv_msg.setText(message);
        }
        tv_msg.setText(message);
    }

    public void setButtonStyle(AngelMaterialProperties.DialogStyle button_style) {
        this.button_style = button_style;
    }

    public void setOnPostShowListener(OnPostShowListener listener) {
        this.listener = listener;
    }

    public String getTitle() {
        return title;
    }

    public void setCustomView(View c) {
//        v_custom = c;
        if (c != null) {
            FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            p.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            v_container.removeAllViews();
            v_container.addView(c, p);
            v_container.setVisibility(View.VISIBLE);
            tv_msg.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        this.title = title;
        if (title == null || title.isEmpty())
            tv_title.setVisibility(View.GONE);
        else {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
    }

    @Override
    public void show() {
        super.show();
        afterShow();
    }

    private void afterShow() {
        if (listener != null) {
            listener.onPostShow(this);
        }
        setCancelable(false);
        switch (button_style) {
            case ok: {
                b_ok.setVisibility(View.VISIBLE);
                b_cancel.setVisibility(View.GONE);
                b_neutral.setVisibility(View.GONE);

                break;
            }
            case cancel: {
                b_ok.setVisibility(View.GONE);
                b_cancel.setVisibility(View.VISIBLE);
                b_neutral.setVisibility(View.GONE);
                setCancelable(true);
                break;
            }
            case ok_cancel: {
                b_ok.setVisibility(View.VISIBLE);
                b_cancel.setVisibility(View.VISIBLE);
                b_neutral.setVisibility(View.GONE);
                setCancelable(true);
                break;
            }
            case ok_neutral_cancel: {
                b_ok.setVisibility(View.VISIBLE);
                b_cancel.setVisibility(View.VISIBLE);
                b_neutral.setVisibility(View.VISIBLE);
                setCancelable(true);
                break;
            }
            default:
                break;
        }

    }

    public AngelMaterialButton getOkButton() {
        return b_ok;
    }

    public AngelMaterialButton getCancelButton() {
        return b_cancel;
    }

    public AngelMaterialButton getNeutralButton() {
        return b_neutral;
    }

    public void setOnOkClickedListener(View.OnClickListener onAcceptButtonClickListener) {
        this.listener_ok = onAcceptButtonClickListener;
    }

    public void setOnCancelClickedListener(View.OnClickListener onCancelButtonClickListener) {
        this.listener_cancel = onCancelButtonClickListener;
    }

    public void setOnNeutralClickedListener(View.OnClickListener onNeutralButtonClickListener) {
        this.listener_neutral = onNeutralButtonClickListener;
    }

    public interface OnPostShowListener {
        void onPostShow(AngelDialog d);
    }
}
