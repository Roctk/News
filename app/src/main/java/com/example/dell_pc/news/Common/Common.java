package com.example.dell_pc.news.Common;

import com.example.dell_pc.news.Interface.IconService;
import com.example.dell_pc.news.Interface.NewsService;
import com.example.dell_pc.news.Remote.IconClient;
import com.example.dell_pc.news.Remote.RetrofitClient;

public class Common {
    private static final String BASE_URL="https://newsapi.org/";
    public static final String API_KEY = "072b146bdf06414faabacb5ff4dd529f";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconService getIconService(){
        return IconClient.getClient().create(IconService.class);
    }

    public static String getAPIUrl(String source,String apiKEY){
        StringBuilder apiURL = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiURL.append(source)
                .append("&apiKey=")
                .append(apiKEY)
                .toString();
    }
}
