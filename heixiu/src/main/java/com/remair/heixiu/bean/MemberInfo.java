package com.remair.heixiu.bean;

public class MemberInfo {
    public String identifier = "";
    public String name;
    public boolean isSpeaking = false;
    public boolean isVideoIn = false;
    public boolean isShareSrc = false;
    public boolean isShareMovie = false;
    public boolean hasGetInfo = false;
    public String userPhone = "";
    public String userName;
    public String headImagePath = "";


    public MemberInfo() {

    }


    public MemberInfo(String phone, String name, String path) {
        userPhone = phone;
        userName = name;
        headImagePath = path;
    }
}