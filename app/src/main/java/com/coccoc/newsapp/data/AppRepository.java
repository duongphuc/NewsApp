package com.coccoc.newsapp.data;

import com.coccoc.newsapp.Constants;
import com.coccoc.newsapp.data.model.NewsResponse;
import com.coccoc.newsapp.data.model.NewsSchema;
import com.coccoc.newsapp.data.remote.ApiServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Single;

public class AppRepository {

    private static AppRepository instance = null;
    private ApiServices mApiServices;

    private AppRepository(ApiServices apiServices) {
        this.mApiServices = apiServices;
    }

    public static synchronized AppRepository getInstance(ApiServices apiServices) {
        if (instance == null) {
            instance = new AppRepository(apiServices);
        }
        return instance;
    }

    public Single<List<NewsSchema>> fetchListNews() {
        return mApiServices.fetchListNews().map(newsResponse -> {
            List<NewsSchema> newsSchemaList = new ArrayList<>();
            List<NewsResponse.Item> itemList = newsResponse.channel.getItems();
            for (NewsResponse.Item item : itemList) {
                NewsSchema news = new NewsSchema(item.getTitle(), formatDate(item.getPubDate()),
                        getImgUrlFromDescription(item.getDescription()), getShortDescription(item.getDescription()), item.getLink());
                newsSchemaList.add(news);
            }
            return newsSchemaList;
        });
    }

    private String getImgUrlFromDescription(String description) {
        Pattern p = Pattern.compile(Constants.REGEX_IMG_SRC);
        Matcher m = p.matcher(description);
        if (m.find())
            return m.group(1);
        return Constants.PLACE_HOLDER_IMG_URL;
    }

    private String getShortDescription(String description) {
        Pattern p = Pattern.compile(Constants.REGEX_DESC_SRC);
        Matcher m = p.matcher(description);
        if (m.find())
            return m.group(1);
        return Constants.DUMMY_TEXT_DESC;
    }

    private String formatDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(date);
            sdf = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.getDefault());
            return sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
