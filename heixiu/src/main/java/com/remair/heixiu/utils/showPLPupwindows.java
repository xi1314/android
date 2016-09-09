package com.remair.heixiu.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.badoo.mobile.util.WeakHandler;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.HXJavaNet;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.AvActivity;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMCustomElem;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import java.util.HashMap;
import studio.archangel.toolkitv2.util.networking.AngelNetCallBack;
import studio.archangel.toolkitv2.util.text.Notifier;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/5/10 17:39
 * 修改人：JXH
 * 修改时间：2016/5/10 17:39
 * 修改备注：
 */
public class showPLPupwindows {

    TextView tv_blacklist;
    TextView tv_report;
    TextView tv_dimiss;
    private PopupWindow pw;
    private Context context;


    public showPLPupwindows(Context context, View v) {
        this.context=context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_popu, null);
        view.measure(0, 0);
        tv_blacklist = (TextView) view.findViewById(R.id.tv_blacklist);
        tv_report = (TextView) view.findViewById(R.id.tv_report);
        tv_dimiss = (TextView) view.findViewById(R.id.tv_dimiss);

        pw = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, view
                .getMeasuredHeight());
        // 点击外部可以被关闭
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    pw.dismiss();
                }
                return false;
            }
        });
        pw.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


    public void showPopupWindow1(final String viewed_user_id, final String name, final String groupId, final TIMConversation con, final WeakHandler handler) {
        tv_blacklist.setText("禁言");
        tv_report.setText("房管");
        tv_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TIMGroupManager.getInstance().modifyGroupMemberInfoSetSilence(
                        groupId + "",
                        viewed_user_id + "", 200000, new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {
                                pw.dismiss();
                            }


                            @Override
                            public void onSuccess() {
                                TIMMessage Nmsg = new TIMMessage();
                                TIMCustomElem ce = new TIMCustomElem();
                                ce.setData((HXUtil.createLiveChatsilenceMessage(
                                        name + "已被管理员禁言", "0", viewed_user_id)
                                                  .toString().getBytes()));
                                if (Nmsg.addElement(ce) != 0) {
                                    return;
                                }
                                con.sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                                    @Override
                                    public void onError(int i, String s) {
                                        if (i == 85) {
                                            handler.sendEmptyMessage(AvActivity.ERROR_MESSAGE_TOO_LONG);
                                        } else if (i == 6011) {
                                            handler.sendEmptyMessage(AvActivity.ERROR_ACCOUNT_NOT_EXIT);
                                        }
                                    }


                                    @Override
                                    public void onSuccess(TIMMessage timMessage) {

                                        Toast.makeText(context, "已禁言", Toast.LENGTH_SHORT)
                                             .show();
                                    }
                                });

                                pw.dismiss();
                            }
                        });
            }
        });
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMGroupManager.getInstance().modifyGroupMemberInfoSetRole(
                        groupId + "", viewed_user_id +
                                "", TIMGroupMemberRoleType.Admin, new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {
                                pw.dismiss();
                            }


                            @Override
                            public void onSuccess() {

                                TIMMessage Nmsg = new TIMMessage();
                                TIMCustomElem ce = new TIMCustomElem();
                                ce.setData((HXUtil.createadminChatsilenceMessage(
                                        name + "已被提升为管理员", viewed_user_id, "0")
                                                  .toString().getBytes()));
                                if (Nmsg.addElement(ce) != 0) {
                                    return;
                                }
                                con.sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                                    @Override
                                    public void onError(int i, String s) {
                                        if (i == 85) {
                                            handler.sendEmptyMessage(AvActivity.ERROR_MESSAGE_TOO_LONG);
                                        } else if (i == 6011) {
                                            handler.sendEmptyMessage(AvActivity.ERROR_ACCOUNT_NOT_EXIT);
                                        }
                                        //         Logger.out("send message failed. code: " + i + " errmsg: " + s);
                                        //                getMessage();
                                    }


                                    @Override
                                    public void onSuccess(TIMMessage timMessage) {

                                        Toast.makeText(context, "已添加管理", Toast.LENGTH_SHORT)
                                             .show();
                                    }
                                });

                                pw.dismiss();
                            }
                        });
            }
        });
        tv_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }


    public void showPopupWindow1(final String viewed_user_id, final String name, final TIMConversation con, final WeakHandler handler) {

        tv_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> parater = new HashMap<>();
                parater.put("user_id", HXApp.getInstance()
                                            .getUserInfo().user_id);
                parater.put("black_user_id", viewed_user_id);
                HXJavaNet
                        .post(HXJavaNet.url_pull_black, parater, new AngelNetCallBack() {
                            @Override
                            public void onSuccess(int ret_code, String ret_data, String msg) {
                                if (ret_code == 200) {

                                    Notifier.showShortMsg(context,
                                            "您已拉黑了" + name);

                                    if (pw != null && pw.isShowing()) {
                                        pw.dismiss();
                                    }
                                }
                            }


                            @Override
                            public void onFailure(String msg) {
                                Notifier.showShortMsg(context, "操作失败");
                                if (pw != null && pw.isShowing()) {
                                    pw.dismiss();
                                }
                            }
                        });
                Notifier.showShortMsg(context, "拉黑");
            }
        });
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifier.showShortMsg(context, "您已举报了" + name);
                pw.dismiss();
            }
        });
        tv_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null && pw.isShowing()) {
                    pw.dismiss();
                }
            }
        });
    }
}
