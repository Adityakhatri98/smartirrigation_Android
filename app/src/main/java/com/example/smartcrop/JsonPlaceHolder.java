package com.example.smartcrop;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonPlaceHolder
{
    @POST("user")
    @FormUrlEncoded
    Call<UserBean> createPost(@Field("email") String email,@Field("name") String name,
    @Field("pwd") String pwd,@Field("pincode") String pincode,
    @Field("address") String address,@Field("phone") String phone);

    @POST("email")
    @FormUrlEncoded
    Call<ApiError> checkemail(@Field("email") String email);

    @POST("1")
    @FormUrlEncoded
    Call<UserBean> createUser(@Field("email") String email,@Field("pwd") String pwd);

}
