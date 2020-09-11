package com.coccoc.newsapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coccoc.newsapp.BR;
import com.coccoc.newsapp.R;
import com.coccoc.newsapp.ViewModelFactory;
import com.coccoc.newsapp.databinding.MainActivityBinding;
import com.coccoc.newsapp.databinding.MainFragmentBinding;
import com.coccoc.newsapp.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commit();
        }
        obtainViewModel(this).getLinkDetail().observe(this, s -> addDetailFragment());
        obtainViewModel(this).getDarkModeEnable().observe(this, darkModeEnable -> {
            if (Boolean.TRUE.equals(darkModeEnable)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void performDataBinding() {
        MainActivityBinding mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mViewDataBinding.setVariable(BR.viewModel, obtainViewModel(this));
        mViewDataBinding.executePendingBindings();
    }


    public static MainViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance();
        return new ViewModelProvider(activity, factory).get(MainViewModel.class);
    }

    private void addDetailFragment() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.container, DetailsFragment.newInstance())
                .commit();
    }
}
