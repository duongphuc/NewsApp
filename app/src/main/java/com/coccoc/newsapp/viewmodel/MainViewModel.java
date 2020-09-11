package com.coccoc.newsapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.coccoc.newsapp.SingleLiveEvent;
import com.coccoc.newsapp.data.AppRepository;
import com.coccoc.newsapp.data.model.NewsSchema;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private AppRepository mAppRepository;
    private SingleLiveEvent<String> linkDetail = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> darkModeEnable = new SingleLiveEvent<>();

    public MainViewModel(AppRepository appRepository) {
        this.mAppRepository = appRepository;
    }

    public String getLinkUrl() {
        return linkDetail.getValue();
    }

    public SingleLiveEvent<String> getLinkDetail() {
        return linkDetail;
    }

    public SingleLiveEvent<Boolean> getDarkModeEnable() {
        return darkModeEnable;
    }

    public Single<List<NewsSchema>> fetchNews() {
        return mAppRepository.fetchListNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void onNewsClick(String link) {
        linkDetail.setValue(link);
    }
}
