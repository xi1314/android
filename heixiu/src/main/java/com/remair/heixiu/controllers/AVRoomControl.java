package com.remair.heixiu.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.remair.heixiu.HXApp;
import com.remair.heixiu.bean.MemberInfo;
import com.remair.heixiu.utils.Utils;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVEndpoint;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sdk.AVRoom;
import com.tencent.av.sdk.AVRoomMulti;
import java.util.ArrayList;
import studio.archangel.toolkitv2.util.Logger;

class AVRoomControl {

    private static final int TYPE_MEMBER_CHANGE_IN = 0;
    private static final int TYPE_MEMBER_CHANGE_OUT = TYPE_MEMBER_CHANGE_IN + 1;
    private static final int TYPE_MEMBER_CHANGE_UPDATE = TYPE_MEMBER_CHANGE_OUT + 1;
    private static final String TAG = "AVRoomControl";
    private boolean mIsInCreateRoom = false;
    private boolean mIsInCloseRoom = false;
    private Context mContext;
    private ArrayList<MemberInfo> mMemberList = new ArrayList<MemberInfo>();
    private int audioCat = Utils.AUDIO_VOICE_CHAT_MODE;
    private int mVideoRecvMode = AVRoom.VIDEO_RECV_MODE_MANUAL;
    private AVRoomMulti.Delegate mRoomDelegate = new AVRoomMulti.Delegate() {
        // 创建房间成功回调
        public void onEnterRoomComplete(int result) {
            Logger.out("WL_DEBUG mRoomDelegate.onEnterRoomComplete result = " + result);
            if (result != AVError.AV_OK) {
                Logger.out("result:" + result);
                mIsInCreateRoom = false;
//                HomeActivity.i = 0;
            }
            mIsInCreateRoom = false;
            mContext.sendBroadcast(new Intent(Utils.ACTION_ROOM_CREATE_COMPLETE).putExtra(Utils.EXTRA_AV_ERROR_RESULT, result));

        }

        // 离开房间成功回调
        public void onExitRoomComplete(int result) {
            Logger.out("WL_DEBUG mRoomDelegate.onExitRoomComplete result = " + result);
            mIsInCloseRoom = false;
            mMemberList.clear();
            mContext.sendBroadcast(new Intent(Utils.ACTION_CLOSE_ROOM_COMPLETE));
        }

        @Override
        public void onEndpointsUpdateInfo(int i, String[] strings) {
            Logger.out("WL_DEBUG:onEnterRoomComplete onEndpointsUpdateInfo");
            mContext.sendBroadcast(new Intent(Utils.ACTION_MEMBER_CHANGE));
        }

        @Override
        public void OnPrivilegeDiffNotify(int i) {

        }

        @Override
        public void OnSemiAutoRecvCameraVideo(String[] strings) {
            Logger.out("WL_DEBUG:onEnterRoomComplete OnSemiAutoRecvCameraVideo" + TAG + strings.length);
//            mContext.sendBroadcast(new Intent(Utils.ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO).putExtra(Utils.ACTION_SEMI_AUTO_RECV_CAMERA_VIDEO, strings).putExtra("type", TAG));
        }

    };


    AVRoomControl(Context context) {
        mContext = context;
    }


//	private AVRoomMulti.Delegate mRoomDelegate = new AVRoomMulti.Delegate() {
//		// 创建房间成功回调
//		protected void onEnterRoomComplete(int result) {
//			Logger.out( "WL_DEBUG mRoomDelegate.onEnterRoomComplete result = " + result);
//			mIsInCreateRoom = false;
//			mContext.sendBroadcast(new Intent(Utils.ACTION_ROOM_CREATE_COMPLETE).putExtra(Utils.EXTRA_AV_ERROR_RESULT, result));
//		}
//
//		// 离开房间成功回调
//		protected void onExitRoomComplete(int result) {
//			Logger.out( "WL_DEBUG mRoomDelegate.onExitRoomComplete result = " + result);
//			mIsInCloseRoom = false;
//			mMemberList.clear();
//			mContext.sendBroadcast(new Intent(Utils.ACTION_CLOSE_ROOM_COMPLETE));
//		}
//		/*
//                protected void onEndpointsEnterRoom(int endpointCount, AVEndpoint endpointList[]) {
//                    Logger.out( "WL_DEBUG onEndpointsEnterRoom. endpointCount = " + endpointCount);
//                    onMemberChange(TYPE_MEMBER_CHANGE_IN, endpointList, endpointCount);
//                }
//
//                protected void onEndpointsExitRoom(int endpointCount, AVEndpoint endpointList[]) {
//                    Logger.out( "WL_DEBUG onEndpointsExitRoom. endpointCount = " + endpointCount);
//                    onMemberChange(TYPE_MEMBER_CHANGE_OUT, endpointList, endpointCount);
//                }
//        */
//		protected void onEndpointsUpdateInfo(int eventid, String[] updateList) {
//			Logger.out( "WL_DEBUG onEndpointsUpdateInfo. eventid = " + eventid);
//			onMemberChange(eventid, updateList);
//		}
//
//		protected void OnPrivilegeDiffNotify(int privilege) {
//			Logger.out( "OnPrivilegeDiffNotify. privilege = " + privilege);
//		}
//	};

    public void setAudioCat(int audioCat) {
        this.audioCat = audioCat;
    }

    private void onMemberChange(int type, AVEndpoint endpointList[], int endpointCount) {

        mContext.sendBroadcast(new Intent(Utils.ACTION_MEMBER_CHANGE));
    }


//	private void onMemberChange(int eventid, String[] updateList) {
//		Logger.out( "WL_DEBUG onMemberChange type = " + eventid);
//		Logger.out( "WL_DEBUG onMemberChange endpointCount = " + updateList.length);
//		int endpointCount = updateList.length;
//		QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
//		AVRoomMulti avRoomMulti = ((AVRoomMulti) qavsdk.getRoom());
///*
//		for (int i = 0; i < endpointCount; i++) {
//			Logger.out( "WL_DEBUG onMemberChange endpointList[" + i + "].getInfo().openId = " + endpointList[i].getInfo().openId);
//			Logger.out( "WL_DEBUG onMemberChange endpointList[" + i + "].hasVideo() = " + endpointList[i].hasVideo());
//		}
//		*/
////		String selfIdentifier = ((HXApp) mContext).getQavsdkControl().getSelfIdentifier();
//		if (TYPE_MEMBER_CHANGE_IN == eventid) {/*1.3测试进度风险，先和老版本保持一直，不展示非语音视频成员信息
//			for (int i = 0; i < endpointCount; i++) {
//				AVEndpoint endpoint = avRoomMulti.getEndpointById(updateList[i]);
//				AVEndpoint.Info userInfo = endpoint.getInfo();
//				String identifier = userInfo.openId;
//				boolean bExist = false;
//				for(int j = 0; j < mMemberList.size(); j++) {
//					if (mMemberList.get(j).identifier.equals(identifier)) {
//						bExist = true;
//						break;
//					}
//				}
//
//				if (!bExist) {
//					MemberInfo info = new MemberInfo();
//					info.identifier = userInfo.openId;
//					info.name = userInfo.openId;
//					info.isVideoIn = endpoint.hasVideo();
//					info.isSpeaking = endpoint.hasAudio();
//					mMemberList.add(info);
//				}
//			}*/
//		} else if (TYPE_MEMBER_CHANGE_OUT == eventid) {
//			for (int i = 0; i < endpointCount; i++) {
//				for (int j = 0; j < mMemberList.size(); j++) {
//					if (mMemberList.get(j).identifier.equals(updateList[i])) {
//						mMemberList.remove(j);
//						break;
//					}
//				}
//			}
//		} else{
//			for (int i = 0; i < endpointCount; i++) {
//				AVEndpoint endpoint = avRoomMulti.getEndpointById(updateList[i]);
//				if(endpoint == null)//endpoint is not exist at all
//				{
//					for (int k=0; k<mMemberList.size(); k++) {
//						if (mMemberList.get(k).identifier.equals(updateList[i])){
//							mMemberList.remove(k);
//							break;
//						}
//					}
//
//					continue;
//				}
//
//				AVEndpoint.Info userInfo = endpoint.getInfo();
//				String identifier = userInfo.openId;
//				boolean identifierExist = false;
//				for (int j = 0; j < mMemberList.size(); j++) {
//					if (mMemberList.get(j).identifier.equals(identifier)) {
//						mMemberList.remove(j);
//						MemberInfo info = new MemberInfo();
//						info.identifier = userInfo.openId;
//						info.name = userInfo.openId;
//						info.isVideoIn = endpoint.hasVideo();
//						info.isSpeaking = endpoint.hasAudio();
//						mMemberList.add(j, info);
//						identifierExist = true;
//						break;
//					}
//				}
//
//				if (!identifierExist) {
//					MemberInfo info = new MemberInfo();
//					info.identifier = userInfo.openId;
//					info.name = userInfo.openId;
//					info.isVideoIn = endpoint.hasVideo();
//					info.isSpeaking = endpoint.hasAudio();
//					mMemberList.add(info);
//				}
//			}
//
//			for (int i=0; i<mMemberList.size(); i++) {
//				MemberInfo info = mMemberList.get(i);
//				if (info != null) {
//					if (!info.isSpeaking && !info.isVideoIn) {
//						mMemberList.remove(i);
//					}
//				}
//			}
//		}
//
//		for (int i = 0; i < mMemberList.size(); i++) {
//			Logger.out( "WL_DEBUG onMemberChange mMemberList.get(" + i + ") = " + mMemberList.get(i));
//		}
//
//		mContext.sendBroadcast(new Intent(Utils.ACTION_MEMBER_CHANGE));
//	}

    AVContext.Config mConfig = null;

    /**
     * 创建房间
     *
     * @param relationId 讨论组号
     */
    void enterRoomCreater(int relationId, String roomRole, boolean isAutoCreateSDKRoom, int videoRecvMode) {
        Logger.out(" WL_DEBUGenterRoom relationId = " + relationId);
//		int roomType = AVRoom.AV_ROOM_MULTI;
//		int roomId = 0;
//		int authBufferSize = 0;//权限位加密串长度；
//		String controlRole = "";//角色名；多人房间专用。该角色名就是web端音视频参数配置工具所设置的角色名。TODO：请业务侧填根据自己的情况填上自己的角色名。
//		int audioCategory = audioCat;

        mVideoRecvMode = videoRecvMode;
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVContext avContext = qavsdk.getAVContext();
        long authBits = AVRoom.AUTH_BITS_DEFAULT;//权限位；默认值是拥有所有权限。TODO：请业务侧填根据自己的情况填上权限位。观众没有上行

//        AVRoom.AUTH_BITS_CREATE_ROOM
        byte[] authBuffer = null;//权限位加密串；TODO：请业务侧填上自己的加密串。
        //AVRoom.EnterRoomParam enterRoomParam = new AVRoomMulti.EnterRoomParam(relationId, authBits, authBuffer, "", audioCat);
        AVRoomMulti.EnterRoomParam enterRoomParam = new AVRoomMulti.EnterRoomParam();
        enterRoomParam.appRoomId = relationId;
        enterRoomParam.authBits = authBits;
        enterRoomParam.authBuffer = authBuffer;
        enterRoomParam.avControlRole = roomRole;
        enterRoomParam.audioCategory = audioCat;
        enterRoomParam.autoCreateRoom = isAutoCreateSDKRoom;
        enterRoomParam.videoRecvMode = videoRecvMode;
        if (avContext == null) {
//            Toast.makeText(mContext, "avContext is null", Toast.LENGTH_SHORT).show();
            Logger.out("enterRoom avContext is null");
//            avContext =new AVContext ();
            mContext.sendBroadcast(new Intent(Utils.ACTION_CLOSE_ROOM_COMPLETE));
            return;

        }
        int i = avContext.enterRoom(AVRoom.AV_ROOM_MULTI, mRoomDelegate, enterRoomParam);
        if (i != AVError.AV_OK) {
            Logger.out("mRoomDelegate:" + i);
        }
        Logger.out("enterRoom done !!!!");
        mIsInCreateRoom = true;
    }

    /**
     * 创建房间
     *
     * @param relationId 讨论组号
     */
    void enterRoomvViewer(int relationId, String roomRole, boolean isAutoCreateSDKRoom, int videoRecvMode) {
        Logger.out(" WL_DEBUGenterRoom relationId = " + relationId);
//		int roomType = AVRoom.AV_ROOM_MULTI;
//		int roomId = 0;
//		int authBufferSize = 0;//权限位加密串长度；
//		String controlRole = "";//角色名；多人房间专用。该角色名就是web端音视频参数配置工具所设置的角色名。
//		int audioCategory = audioCat;

        mVideoRecvMode = videoRecvMode;
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVContext avContext = qavsdk.getAVContext();
        //权限位；默认值是拥有所有权限。
        long authBits = AVRoom.AUTH_BITS_CREATE_ROOM | AVRoom.AUTH_BITS_JOIN_ROOM | AVRoom.AUTH_BITS_RECV_AUDIO | AVRoom.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoom.AUTH_BITS_RECV_SCREEN_VIDEO;

//        AVRoom.AUTH_BITS_CREATE_ROOM
        byte[] authBuffer = null;//权限位加密串；
        //AVRoom.EnterRoomParam enterRoomParam = new AVRoomMulti.EnterRoomParam(relationId, authBits, authBuffer, "", audioCat);
        AVRoomMulti.EnterRoomParam enterRoomParam = new AVRoomMulti.EnterRoomParam();
        enterRoomParam.appRoomId = relationId;
        enterRoomParam.authBits = authBits;
        enterRoomParam.authBuffer = authBuffer;
        enterRoomParam.avControlRole = roomRole;
        enterRoomParam.audioCategory = audioCat;
        enterRoomParam.autoCreateRoom = isAutoCreateSDKRoom;
        enterRoomParam.videoRecvMode = videoRecvMode;
        if (avContext == null) {
//            Toast.makeText(mContext, "avContext is null", Toast.LENGTH_SHORT).show();
            Logger.out("enterRoom avContext is null");
//            avContext =new AVContext ();
            mContext.sendBroadcast(new Intent(Utils.ACTION_CLOSE_ROOM_COMPLETE));
            return;

        }
        int i = avContext.enterRoom(AVRoom.AV_ROOM_MULTI, mRoomDelegate, enterRoomParam);
        if (i != AVError.AV_OK) {
            Logger.out("mRoomDelegate:" + i);
        }
        Logger.out("enterRoom done !!!!");
        mIsInCreateRoom = true;
    }

    void enterRoom(int relationId, String roomRole) {
        Logger.out("WL_DEBUG enterRoom relationId = " + relationId);
//		int roomType = AVRoom.AV_ROOM_MULTI;f
//		int roomId = 0;
//		int authBufferSize = 0;//权限位加密串长度；TODO：请业务侧填上自己的加密串长度。
//		String controlRole = "";//角色名；多人房间专用。该角色名就是web端音视频参数配置工具所设置的角色名。TODO：请业务侧填根据自己的情况填上自己的角色名。
//		int audioCategory = audioCat;

        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVContext avContext = qavsdk.getAVContext();
        long authBits = AVRoom.AUTH_BITS_DEFAULT;//权限位；默认值是拥有所有权限。TODO：请业务侧填根据自己的情况填上权限位。
        byte[] authBuffer = null;//权限位加密串；TODO：请业务侧填上自己的加密串。
        // AVRoom.EnterRoomParam enterRoomParam = new AVRoomMulti.EnterRoomParam(relationId, authBits, authBuffer, roomRole, audioCat);
        AVRoom.EnterRoomParam enterRoomParam = new AVRoomMulti.EnterRoomParam();
        if (avContext == null) {
            Toast.makeText(mContext, "avContext is null", Toast.LENGTH_SHORT);
            Logger.out("enterRoom avContext is null");
//            avContext =new AVContext ();
            mContext.sendBroadcast(new Intent(Utils.ACTION_CLOSE_ROOM_COMPLETE));
            return;
        }
        avContext.enterRoom(AVRoom.AV_ROOM_MULTI, mRoomDelegate, enterRoomParam);
        Logger.out("enterRoom done !!!!");
//		AVRoom.Info roomInfo = new AVRoom.Info(roomType, roomId, relationId, AVRoom.AV_MODE_AUDIO, "", authBits, authBuffer, authBufferSize, audioCategory, controlRole);
//		// create room
//		avContext.enterRoom(mRoomDelegate, roomInfo);
        mIsInCreateRoom = true;
    }


    /**
     * 关闭房间
     */


    int exitRoom() {
        Logger.out("WL_DEBUG exitRoom");
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVContext avContext = qavsdk.getAVContext();
        int result = -1;
        if (avContext != null)
            result = avContext.exitRoom();
        mIsInCloseRoom = true;
        return result;
    }


    /**
     * 获取成员列表
     *
     * @return 成员列表
     */
    ArrayList<MemberInfo> getMemberList() {
        return mMemberList;
    }

    boolean getIsInEnterRoom() {
        return mIsInCreateRoom;
    }

    boolean getIsInCloseRoom() {
        return mIsInCloseRoom;
    }

    public void setCreateRoomStatus(boolean status) {
        mIsInCreateRoom = status;
    }

    public void setCloseRoomStatus(boolean status) {
        mIsInCloseRoom = status;
    }

    public void setNetType(int netType) {
        QavsdkControl qavsdk = ((HXApp) mContext).getQavsdkControl();
        AVContext avContext = qavsdk.getAVContext();
        if (avContext == null)
            return;
        AVRoomMulti room = (AVRoomMulti) avContext.getRoom();

        if (null != room) {
            room.setNetType(netType);
        }
    }

    public int getVideoRecvMode() {
        return mVideoRecvMode;
    }
}