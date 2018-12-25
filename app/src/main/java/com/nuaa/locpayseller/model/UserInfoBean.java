package com.nuaa.locpayseller.model;

public class UserInfoBean {

    private String aid;

    private String loc;

    private long timeStamp;

    private String sign;

    public UserInfoBean(String aid, String loc, long timeStamp, String sign) {
        this.aid = aid;
        this.loc = loc;
        this.timeStamp = timeStamp;
        this.sign = sign;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
