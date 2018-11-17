package com.lifed.testapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifed.testapp.R;

public class NoDataFragment extends Fragment {

    private static NoDataFragment instance;

    public static NoDataFragment getInstance() {
        if (instance == null) {
            instance = new NoDataFragment();
        }

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_data, container, false);
    }
}
