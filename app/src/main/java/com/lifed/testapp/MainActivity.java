package com.lifed.testapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lifed.testapp.fragments.DataFragment;
import com.lifed.testapp.fragments.FailedLoadData;
import com.lifed.testapp.fragments.LoadingFragment;
import com.lifed.testapp.fragments.NoDataFragment;
import com.lifed.testapp.interfaces.DataFragmentListener;
import com.lifed.testapp.interfaces.ServiceAPI;
import com.lifed.testapp.models.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DataFragmentListener {

    private DrawerLayout drawerLayout;
    private ViewGroup fragmentHolder;
    private FragmentManager fragmentManager;

    private DataFragment dataFragment;

    // Initialize Retrofit API class
    private ServiceAPI serviceApi = ServiceAPI.retrofit.create(ServiceAPI.class);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_white); // Although I prefer to use XML icons instead PNG
        }

        fragmentHolder = findViewById(R.id.content_frame);
        fragmentManager = getSupportFragmentManager();

        // setup NoDataFragment
        setFragment(NoDataFragment.getInstance());

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (actionBar != null) {
                    // Set close icon
                    actionBar.setHomeAsUpIndicator(R.drawable.baseline_close_white);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (actionBar != null) {
                    // Set menu icon
                    actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_white);
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.mi_download:
                        getData();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup loading fragment and try get data from server
     */
    private void getData() {
        setFragment(LoadingFragment.getInstance());

        // Getting data from the server asynchronously
        serviceApi.getData().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.isSuccessful()) {
                    success(response.body());
                } else {
                    error();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {
                error();
            }
        });
    }

    /**
     * Set passed fragment to fragmentHolder
     *
     * @param fragment fragment object that need to be set
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentHolder.getId(), fragment);
        fragmentTransaction.commit();
    }

    /**
     * Pass data to DataFragment and open it
     *
     * @param data Data from the server
     */
    private void success(Data data) {
        // We don't need to create data fragment every time, just use lazy initialize
        dataFragment = getDataFragment();

        // Put parcelable data into bundle
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);

        // Set bundle to fragment as argument
        dataFragment.setArguments(bundle);

        setFragment(dataFragment);
    }

    /**
     * Calls when data can not be loaded from server
     * or fails when put on fragment.
     */
    public void error() {
        setFragment(FailedLoadData.getInstance());
    }

    /**
     * Returns an object of DataFragment using lazy initialize (like Singleton)
     *
     * @return DataFragment object
     */
    private DataFragment getDataFragment() {
        if (dataFragment == null) {
            dataFragment = new DataFragment();
            // add listener to DataFragment to handle some cases
            dataFragment.setDataFragmentListener(this);
        }

        return dataFragment;
    }
}
