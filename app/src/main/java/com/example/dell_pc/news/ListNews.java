package com.example.dell_pc.news;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dell_pc.news.Adapter.ListNewsAdapter;
import com.example.dell_pc.news.Common.Common;
import com.example.dell_pc.news.Interface.NewsService;
import com.example.dell_pc.news.Model.Article;
import com.example.dell_pc.news.Model.News;
import com.example.dell_pc.news.Model.Source;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {

    KenBurnsView kenBurnsView;
    DiagonalLayout diagonalLayout;
    AlertDialog dialog;
    NewsService newsService;
    TextView top_author, top_title;
    SwipeRefreshLayout swipeRefreshLayout;

    String source = "",webHotURL = "";

    ListNewsAdapter newsAdapter;
    RecyclerView listNews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        newsService = Common.getNewsService();
        dialog = new SpotsDialog(this);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source,true);
            }
        });

        diagonalLayout = (DiagonalLayout)findViewById(R.id.diagonal_layout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(getBaseContext(),DetailArticle.class);
                detail.putExtra("webURL",webHotURL);
                startActivity(detail);
            }
        });
        kenBurnsView = (KenBurnsView)findViewById(R.id.top_image);
        top_author = (TextView)findViewById(R.id.top_author);
        top_title = (TextView)findViewById(R.id.top_title);

        listNews = (RecyclerView)findViewById(R.id.list_news);
        listNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listNews.setLayoutManager(layoutManager);

        if(getIntent()!=null)
        {
            source = getIntent().getStringExtra("source");
            if (!source.isEmpty())
                loadNews(source,false);
        }

    }
    private void loadNews(String source, boolean isRefreshed){
        if(!isRefreshed)
        {
            dialog.show();
            newsService.getNewestArticles(Common.getAPIUrl(source,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();
                            Picasso.with(getBaseContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kenBurnsView);
                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);
                            newsAdapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            newsAdapter.notifyDataSetChanged();
                            listNews.setAdapter(newsAdapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }
        else
        {
            dialog.show();
            newsService.getNewestArticles(Common.getAPIUrl(source,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();
                            Picasso.with(getBaseContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kenBurnsView);
                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);
                            newsAdapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            newsAdapter.notifyDataSetChanged();
                            listNews.setAdapter(newsAdapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
