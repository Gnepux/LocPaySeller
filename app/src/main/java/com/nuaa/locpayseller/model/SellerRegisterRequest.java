package com.nuaa.locpayseller.model;

public class SellerRegisterRequest {

    private String sid;

    private String account;

    private String loc;

    private String publicKey;

    public SellerRegisterRequest(String sid, String account, String loc, String publicKey) {
        this.sid = sid;
        this.account = account;
        this.loc = loc;
        this.publicKey = publicKey;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
