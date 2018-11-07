package com.newsreader.android.newsreader.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newsreader.android.newsreader.R;
import com.newsreader.android.newsreader.adapter.ArticleListAdapter;
import com.newsreader.android.newsreader.model.newsapi.Article;
import com.newsreader.android.newsreader.model.newsapi.Articles;
import com.newsreader.android.newsreader.network.GetHackerNewsDataService;
import com.newsreader.android.newsreader.network.RetrofitInstance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomFragment extends Fragment {

    //list to hold the info
    List<Article> listTopArticles = new ArrayList<>();

    private ArticleListAdapter adapter;
    private RecyclerView recyclerView;
    private int resultPagination = 1;

    private Date date;
    private String topic;
    private String today;

    private final String APP_KEY = "4883e3dd1bb14d618525c3f08e04dd64";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newslist,container,false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        topic = getArguments().getString("Topic");

        GetHackerNewsDataService service = RetrofitInstance.getRetrofitInstance().create(GetHackerNewsDataService.class);


        date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(date);


        Call<Articles> call = service.getCustom(topic,today,"popularity","en",resultPagination,APP_KEY);
        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");
        call.enqueue(new Callback<Articles>() {
            @Override
            public void onResponse(Call<Articles> call, Response<Articles> response) {
                listTopArticles = response.body().getArticles();
                adapter = new ArticleListAdapter(listTopArticles);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Articles> call, Throwable t) {
                Log.i("BAD","BAD");
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy != 0) {

                    Log.i("SCROLLED","SCROLLING");
                    resultPagination += 1;
                    GetHackerNewsDataService service = RetrofitInstance.getRetrofitInstance().create(GetHackerNewsDataService.class);
                    Call<Articles> call = service.getCustom(topic,today,"popularity","en",resultPagination,APP_KEY);
                    call.enqueue(new Callback<Articles>() {
                        @Override
                        public void onResponse(Call<Articles> call, Response<Articles> response) {
                            int curSize = adapter.getItemCount();
                            listTopArticles.addAll(response.body().getArticles());
                            adapter.notifyItemRangeInserted(curSize, listTopArticles.size() - 1);
                        }

                        @Override
                        public void onFailure(Call<Articles> call, Throwable t) {
                            Log.i("BAD","BAD");
                        }
                    });
                }

            }
            }
        );
        return v;
    }
}
