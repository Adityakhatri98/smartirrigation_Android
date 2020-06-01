package com.example.smartcrop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserBean
{
    @SerializedName("email")
    @Expose
    String userEmail;
    @SerializedName("name")
    @Expose
    String userName;
    @SerializedName("pwd")
    @Expose
    String userPwd;
    @SerializedName("pincode")
    @Expose
    String userPincode;
    @SerializedName("address")
    @Expose
    String userAddress;
    @SerializedName("phone")
    @Expose
    String userPhone;
    @SerializedName("node")
    @Expose
    String userNode;
    @SerializedName("auth")
    @Expose
    String userauth;

    public UserBean(String userEmail, String userName, String userPwd, String userPincode, String userAddress, String userPhone, String userNode, String userauth) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userPincode = userPincode;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
        this.userNode = userNode;
        this.userauth = userauth;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userPincode='" + userPincode + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userNode='" + userNode + '\'' +
                ", userauth='" + userauth + '\'' +
                '}';
    }

    public String getUserauth() {
        return userauth;
    }

    public void setUserauth(String userauth) {
        this.userauth = userauth;
    }

    public String getUserNode() {
        return userNode;
    }

    public void setUserNode(String userNode) {
        this.userNode = userNode;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPincode() {
        return userPincode;
    }

    public void setUserPincode(String userPincode) {
        this.userPincode = userPincode;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
