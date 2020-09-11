package com.coccoc.newsapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coccoc.newsapp.R;
import com.coccoc.newsapp.adapter.NewsListAdapter;
import com.coccoc.newsapp.data.model.NewsSchema;
import com.coccoc.newsapp.databinding.MainFragmentBinding;
import com.coccoc.newsapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainFragment extends Fragment {
    private CompositeDisposable disposable = new CompositeDisposable();
    private MainViewModel mViewModel;
    private NewsListAdapter adapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        MainFragmentBinding mBinding = MainFragmentBinding.bind(view);
        mViewModel = MainActivity.obtainViewModel(getActivity());
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(getActivity());
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view.findViewById(R.id.recyclerViewList));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fetchData();
        super.onActivityCreated(savedInstanceState);
    }

    private void fetchData() {
        disposable.add(mViewModel.fetchNews()
                .subscribe(this::bindView, throwable -> {
                }));
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        adapter = new NewsListAdapter(new ArrayList<>(), mViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void bindView(List<NewsSchema> listData) {
        adapter.clear();
        adapter.addAll(listData);
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
