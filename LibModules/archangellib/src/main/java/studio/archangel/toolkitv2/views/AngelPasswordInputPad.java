package studio.archangel.toolkitv2.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import studio.archangel.toolkitv2.R;

/**
 * Created by Michael on 2015/7/1.
 */
public class AngelPasswordInputPad extends FrameLayout {
    View pad_1, pad_2, pad_3, pad_4, pad_5, pad_6, pad_7, pad_8, pad_9, pad_0, pad_backspace, bar;
    OnPadClickListener listener;
    boolean hidden = false;

    public AngelPasswordInputPad(Context context) {
        this(context, null);
    }

    public AngelPasswordInputPad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public AngelPasswordInputPad(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        inflate(context, R.layout.view_angel_password_input_pad, this);
        pad_1 = findViewById(R.id.view_angel_password_input_pad_1);
        pad_2 = findViewById(R.id.view_angel_password_input_pad_2);
        pad_3 = findViewById(R.id.view_angel_password_input_pad_3);
        pad_4 = findViewById(R.id.view_angel_password_input_pad_4);
        pad_5 = findViewById(R.id.view_angel_password_input_pad_5);
        pad_6 = findViewById(R.id.view_angel_password_input_pad_6);
        pad_7 = findViewById(R.id.view_angel_password_input_pad_7);
        pad_8 = findViewById(R.id.view_angel_password_input_pad_8);
        pad_9 = findViewById(R.id.view_angel_password_input_pad_9);
        pad_0 = findViewById(R.id.view_angel_password_input_pad_0);
        pad_backspace = findViewById(R.id.view_angel_password_input_pad_backspace);
        bar = findViewById(R.id.view_angel_password_input_pad_top);
        pad_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("1");
                }
            }
        });
        pad_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("2");
                }
            }
        });
        pad_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("3");
                }
            }
        });
        pad_4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("4");
                }
            }
        });
        pad_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("5");
                }
            }
        });
        pad_6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("6");
                }
            }
        });
        pad_7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("7");
                }
            }
        });
        pad_8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("8");
                }
            }
        });
        pad_9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("9");
                }
            }
        });
        pad_0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPadClicked("0");
                }
            }
        });
        pad_backspace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBackspaceClicked();
                }
            }
        });
        bar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onTopBarClicked();
                }
            }
        });
    }

    public boolean isHidden() {
        return hidden;
    }

    public void hide() {
        if (hidden) {
            return;
        }
        animate().translationY(getHeight()).setStartDelay(0).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                hidden = true;
            }
        }).start();
    }

    public void show() {
        if (!hidden) {
            return;
        }
        animate().translationY(0).setDuration(200).setStartDelay(0).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                hidden = false;
            }
        }).start();
    }

    public OnPadClickListener getOnPadClickListener() {
        return listener;
    }

    public void setOnPadClickListener(OnPadClickListener listener) {
        this.listener = listener;
    }

    public interface OnPadClickListener {
        void onPadClicked(String number);

        void onBackspaceClicked();

        void onTopBarClicked();

    }
}
