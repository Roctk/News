package com.example.dell_pc.news.Interface;

import com.example.dell_pc.news.Common.Common;
import com.example.dell_pc.news.Model.News;
import com.example.dell_pc.news.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService {
    @GET("v2/sources?language=en&apiKey="+ Common.API_KEY)
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);
}
