package com.newsreader.android.newsreader.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newsreader.android.newsreader.R;
import com.newsreader.android.newsreader.WebActivity;
import com.newsreader.android.newsreader.model.newsapi.Article;
import com.squareup.picasso.Picasso;

import java.util.List;



public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {
    private List<Article> dataList;

    public ArticleListAdapter(List<Article> dataList){
        this.dataList = dataList;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle, txtSource, txtDescription;
        private ImageView imgArticle;
        private CardView cardView;
        public ArticleViewHolder(View itemView) {
            super(itemView);
            imgArticle = (ImageView) itemView.findViewById(R.id.articleImage);
            txtTitle = (TextView)itemView.findViewById(R.id.articleTitle);
            txtSource = (TextView)itemView.findViewById(R.id.articleSource);
            txtDescription = (TextView)itemView.findViewById(R.id.articleDescription);
            cardView = (CardView)itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), WebActivity.class);
//                    intent.putExtra("url", dataList.get(getAdapterPosition()).getUrl());
//                    v.getContext().startActivity(intent);
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(4209981);
                    builder.setSecondaryToolbarColor(4209981);
                    builder.enableUrlBarHiding();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(v.getContext(), Uri.parse(dataList.get(getAdapterPosition()).getUrl()));

                }
            });
        }
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getTitle());
        holder.txtSource.setText(dataList.get(position).getSource().getName());
        holder.txtDescription.setText(dataList.get(position).getDescription());
        if (dataList.get(position).getUrlToImage() != null) {
            Picasso.get().load(dataList.get(position).getUrlToImage().replace("=s90", "=s360")).into(holder.imgArticle);
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }



}
