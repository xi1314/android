package com.remair.heixiu.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.R;
import com.remair.heixiu.activity.FriendMessageActivity;
import com.remair.heixiu.adapters.PersonMessageAdapter;
import com.remair.heixiu.bean.FriendInfo;
import com.remair.heixiu.bean.UserInfo;
import com.remair.heixiu.fragment.base.HXBaseFragment;
import com.remair.heixiu.net.HXHttpUtil;
import com.remair.heixiu.net.HttpSubscriber;
import com.remair.heixiu.view.MyLinearLayoutManager;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import studio.archangel.toolkitv2.adapters.CommonRecyclerAdapter;
import studio.archangel.toolkitv2.util.Util;

/**
 * 项目名称：Android
 * 类描述：
 * 创建人：JXH
 * 创建时间：2016/5/26 12:21
 * 修改人：JXH
 * 修改时间：2016/5/26 12:21
 * 修改备注：
 */
public class RecomFragment extends HXBaseFragment implements PlatformActionListener {
    protected int type = 0;
    @BindView(R.id.tv_needadd) TextView tv_needadd;
    @BindView(R.id.tv_haveadd) TextView tv_haveadd;
    @BindView(R.id.rv_haveadd) RecyclerView rv_haveadd;
    @BindView(R.id.rv_needadd) RecyclerView rv_needadd;
    protected Context mContext;
    protected List<FriendInfo> friendlist_need, friendlist_had;
    protected PersonMessageAdapter personMessageAdapter_need, personMessageAdapter_had;
    protected String str = "";
    protected HashMap<String, String> numberList = new HashMap<>();
    protected UserInfo mUserInfo;
    protected ViewGroup mView;
    HashMap<String, String> con = new HashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mContext = getContext().getApplicationContext();
        mUserInfo = HXApp.getInstance().getUserInfo();
        if (arguments != null) {
            type = (int) arguments.get("type");
        }
        friendlist_need = new ArrayList<>();
        friendlist_had = new ArrayList<>();
    }


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container) {
        mView = (ViewGroup) inflater.inflate(R.layout.fragment_phone, null);
        ButterKnife.bind(this, mView);
        return mView;
    }


    @Override
    public void initData() {
        tv_needadd.setVisibility(View.GONE);
        tv_haveadd.setVisibility(View.GONE);
        rv_haveadd.setVisibility(View.GONE);
        rv_needadd.setVisibility(View.GONE);
        rv_needadd.setLayoutManager(new MyLinearLayoutManager(mContext));
        rv_needadd.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = Util.getPX((float) 0.6);
            }
        });
        rv_haveadd.setLayoutManager(new MyLinearLayoutManager(mContext));
        rv_haveadd.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = Util.getPX((float) 0.6);
            }
        });

        final View inflate = View
                .inflate(getContext(), R.layout.poup_recom, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util
                .getPX(50));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mView.addView(inflate, layoutParams);
        TextView tv_toast = (TextView) inflate.findViewById(R.id.tv_toast);
        if (type == 0) {
            tv_toast.setText(getString(R.string.phone_fr));
        } else if (type == 1) {
            tv_toast.setText(getString(R.string.weibo_fr));
        }

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflate.setVisibility(View.GONE);
                mView.removeView(inflate);
                try {
                    if (type == 0) {
                        ContentResolver res = mContext.getContentResolver();
                        Cursor cursor = res
                                .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                        while (cursor != null && cursor.moveToNext()) {
                            //取得联系人的名字索引
                            int nameIndex = cursor
                                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                            String contact = cursor.getString(nameIndex);
                            //取得联系人的ID索引值
                            String contactId = cursor.getString(cursor
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                            Cursor phone = res
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                                                    "=" +
                                                    contactId, null, null);
                            while (phone != null && phone.moveToNext()) {
                                String strPhoneNumber = phone.getString(phone
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                str = str.replace("-", "");
                                str += (strPhoneNumber + ",");
                                numberList.put(strPhoneNumber, contact);
                            }
                            if (phone != null) {
                                phone.close();
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        doSomeThing(0);
                    } else if (type == 1) {
                        ShareSDK.initSDK(mContext);
                        Platform sinaWeibo = ShareSDK
                                .getPlatform(SinaWeibo.NAME);
                        sinaWeibo.SSOSetting(false);
                        sinaWeibo.setPlatformActionListener(RecomFragment.this);
                        sinaWeibo.showUser(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String token = platform.getDb().getToken();
        str = requestToWeibo(token, 0);
        doSomeThing(1);
    }


    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
    }


    @Override
    public void onCancel(Platform platform, int i) {

    }


    public void doSomeThing(final int type) {
        if (str == null || str.equals("")) {
            return;
        }
        str = str.substring(0, str.length() - 1);
        if (mUserInfo == null) {
            return;
        }
        HXHttpUtil.getInstance().friendByPhone(mUserInfo.user_id, str)
                  .subscribe(new HttpSubscriber<List<FriendInfo>>() {
                      @Override
                      public void onNext(List<FriendInfo> friendInfos) {
                          for (int i = 0; i < friendInfos.size(); i++) {
                              FriendInfo friendInfo = friendInfos.get(i);
                              if (type == 0) {
                                  friendInfo.phonename = numberList
                                          .get(friendInfo.phone_num);
                              } else {
                                  friendInfo.phonename = con
                                          .get(friendInfo.sina_weibo_openid);
                              }
                              int relation_type = friendInfo.relation_type;
                              if (relation_type == 0) {
                                  //未关注
                                  friendlist_need.add(friendInfo);
                              } else {
                                  //已关注
                                  friendlist_had.add(friendInfo);
                              }
                          }
                          rv_needadd.setVisibility(View.VISIBLE);
                          tv_needadd.setVisibility(View.VISIBLE);
                          tv_haveadd.setVisibility(View.VISIBLE);
                          rv_haveadd.setVisibility(View.VISIBLE);

                          tv_needadd.setText(String.valueOf(
                                  friendlist_need.size() +
                                          getString(R.string.fr_ready)));
                          tv_haveadd.setText(String.valueOf(
                                  friendlist_had.size() +
                                          getString(R.string.fr_added)));
                          personMessageAdapter_need = new PersonMessageAdapter(mContext, friendlist_need, new PersonMessageAdapter.AttentionListener() {
                              @Override
                              public void attention(int postion) {
                                  friendlist_had
                                          .add(0, friendlist_need.get(postion));
                                  friendlist_need.remove(postion);
                                  personMessageAdapter_need
                                          .notifyDataSetChanged();
                                  personMessageAdapter_had
                                          .notifyDataSetChanged();
                                  tv_needadd.setText(String.valueOf(
                                          friendlist_need.size() +
                                                  getString(R.string.fr_ready)));
                                  tv_haveadd.setText(String.valueOf(
                                          friendlist_had.size() +
                                                  getString(R.string.fr_added)));
                              }


                              @Override
                              public void unAttention(int postion) {

                              }
                          }, true);
                          personMessageAdapter_had = new PersonMessageAdapter(mContext, friendlist_had, new PersonMessageAdapter.AttentionListener() {
                              @Override
                              public void attention(int postion) {

                              }


                              @Override
                              public void unAttention(int postion) {
                                  friendlist_need
                                          .add(0, friendlist_had.get(postion));
                                  friendlist_had.remove(postion);
                                  personMessageAdapter_need
                                          .notifyDataSetChanged();
                                  personMessageAdapter_had
                                          .notifyDataSetChanged();
                                  tv_needadd.setText(String.valueOf(
                                          friendlist_need.size() +
                                                  getString(R.string.fr_ready)));
                                  tv_haveadd.setText(String.valueOf(
                                          friendlist_had.size() +
                                                  getString(R.string.fr_added)));
                              }
                          }, true);
                          personMessageAdapter_need
                                  .setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
                                      @Override
                                      public void onItemClick(View view, Object data) {
                                          FriendInfo friendInfo = (FriendInfo) data;
                                          startActivity(new Intent(mContext, FriendMessageActivity.class)
                                                  .putExtra("viewed_user_id", friendInfo.user_id));
                                      }
                                  });
                          personMessageAdapter_had
                                  .setOnItemToClickListener(new CommonRecyclerAdapter.OnItemToClickListener() {
                                      @Override
                                      public void onItemClick(View view, Object data) {
                                          FriendInfo friendInfo = (FriendInfo) data;
                                          startActivity(new Intent(mContext, FriendMessageActivity.class)
                                                  .putExtra("viewed_user_id", friendInfo.user_id));
                                      }
                                  });
                          personMessageAdapter_need.setRecyclerView(rv_needadd);
                          personMessageAdapter_had.setRecyclerView(rv_haveadd);
                          rv_needadd.setAdapter(personMessageAdapter_need);
                          rv_haveadd.setAdapter(personMessageAdapter_had);
                          personMessageAdapter_had.notifyDataSetChanged();
                          personMessageAdapter_need.notifyDataSetChanged();
                      }
                  });
    }


    public String requestToWeibo(String token, int cursor) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("access_token", token);
        params.put("cursor", cursor);
        String s = null;
        try {
            s = sendGETRequest("https://api.weibo.com/2/friendships/friends/ids.json", params, "UTF-8");
            JSONObject jsonObject = new JSONObject(s);
            JSONArray ids = jsonObject.optJSONArray("ids");
            int next_cursor = jsonObject.optInt("next_cursor");
            s = "";
            for (int b = 0; b < ids.length(); b++) {

                s += ids.get(b) + ",";
            }
            if (next_cursor != 0) {
                s += requestToWeibo(token, next_cursor);
            }
        } catch (Exception e) {

        }
        return s;
    }


    private String sendGETRequest(String path, HashMap<String, Object> parms, String encoding) throws Exception {
        StringBuilder sb = new StringBuilder();
        String json = null;
        sb.append(path);
        if (parms != null && !parms.isEmpty()) {
            sb.append("?");
            for (Map.Entry entry : parms.entrySet()) {
                sb.append(entry.getKey()).append('=')
                  .append(URLEncoder.encode(entry.getValue() + "", encoding))
                  .append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        URL url = new URL(sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(1000 * 5);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            inputStream.close();
            json = baos.toString();
            baos.close();
        }
        return json;
    }
}