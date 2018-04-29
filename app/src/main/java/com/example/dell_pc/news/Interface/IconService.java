package com.example.dell_pc.news.Interface;

import com.example.dell_pc.news.Model.IconNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IconService {
    @GET
    Call<IconNews> getIconUrl(@Url String url);
}
