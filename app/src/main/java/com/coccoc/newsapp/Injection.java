/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coccoc.newsapp;

import com.coccoc.newsapp.data.AppRepository;
import com.coccoc.newsapp.data.remote.ApiServices;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Injection {
    static AppRepository provideTasksRepository(ApiServices apiServices) {
        return AppRepository.getInstance(apiServices);
    }

    static ApiServices getApiInterface() {
        Retrofit retrofit = getRetrofit(getOkHttpClient(getHttpLoggingInterceptor()));
        return retrofit.create(ApiServices.class);
    }

    private static Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static OkHttpClient getOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS);

        httpClient.addInterceptor(httpLoggingInterceptor);
        return httpClient.build();
    }


    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
