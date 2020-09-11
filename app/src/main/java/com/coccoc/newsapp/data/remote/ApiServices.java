package com.coccoc.newsapp.data.remote;

import com.coccoc.newsapp.data.model.NewsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiServices {
    @GET("/rss/tin-moi-nhat.rss")
    Single<NewsResponse> fetchListNews();
}
