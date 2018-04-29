package com.example.dell_pc.news.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IconClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://besticon-demo.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
