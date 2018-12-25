package com.nuaa.locpayseller.model;

public class PayRequest {

    private String sid;

    private long timeStamp;

    private String loc;

    private String amount;

    private String sign;

    private int distance;

    private UserInfoBean userInfo;

    public PayRequest(String sid, long timeStamp, String loc, String amount, String sign, int distance, UserInfoBean userInfo) {
        this.sid = sid;
        this.timeStamp = timeStamp;
        this.loc = loc;
        this.amount = amount;
        this.sign = sign;
        this.distance = distance;
        this.userInfo = userInfo;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
