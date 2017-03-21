package com.example.dell.usermanagementsystem.service;

import com.example.dell.usermanagementsystem.model.User;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by DELL on 21-Mar-17.
 */

public interface APIService {

    @GET("api/books")
    Call<List<User>> getUserDetails();
}
