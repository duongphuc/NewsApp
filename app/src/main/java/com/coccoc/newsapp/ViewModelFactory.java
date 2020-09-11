/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.coccoc.newsapp;

import android.annotation.SuppressLint;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.coccoc.newsapp.data.AppRepository;
import com.coccoc.newsapp.viewmodel.MainViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static ViewModelFactory instance;

    private final AppRepository mRepository;

    public static synchronized ViewModelFactory getInstance() {
        if (instance == null) {
            instance = new ViewModelFactory(Injection.provideTasksRepository(Injection.getApiInterface()));
        }
        return instance;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        instance = null;
    }

    private ViewModelFactory(AppRepository repository) {
        mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
