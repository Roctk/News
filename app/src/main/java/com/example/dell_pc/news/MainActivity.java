package com.example.dell_pc.news;

import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dell_pc.news.Adapter.ListSourceAdapter;
import com.example.dell_pc.news.Common.Common;
import com.example.dell_pc.news.Interface.NewsService;
import com.example.dell_pc.news.Model.WebSite;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView listWebSite;
    RecyclerView.LayoutManager layoutManager;
    NewsService newsService;
    ListSourceAdapter sourceAdapter;
    AlertDialog dialog;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        newsService = Common.getNewsService();

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebSiteSource(true);
            }
        });

        listWebSite = (RecyclerView)findViewById(R.id.list_source);
        listWebSite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebSite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);
        loadWebSiteSource(false);
    }

    private void loadWebSiteSource(boolean isRefreshed){

        if(!isRefreshed)
        {
            String cache = Paper.book().read("cache");
            if(cache!=null && !cache.isEmpty()&&cache.equals("null"))
            {
                WebSite webSite = new Gson().fromJson(cache,WebSite.class);
                sourceAdapter = new ListSourceAdapter(getBaseContext(),webSite);
                sourceAdapter.notifyDataSetChanged();
                listWebSite.setAdapter(sourceAdapter);
            }
            else
                {dialog.show();
                newsService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        sourceAdapter  = new ListSourceAdapter(getBaseContext(),response.body());
                        sourceAdapter.notifyDataSetChanged();
                        listWebSite.setAdapter(sourceAdapter);

                        Paper.book().write("cache",new Gson().toJson(response.body()));
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        }
        else {
            swipeLayout.setRefreshing(true);
            newsService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    sourceAdapter  = new ListSourceAdapter(getBaseContext(),response.body());
                    sourceAdapter.notifyDataSetChanged();
                    listWebSite.setAdapter(sourceAdapter);

                    Paper.book().write("cache",new Gson().toJson(response.body()));

                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }

    }

}
