package com.lifed.testapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lifed.testapp.R;
import com.lifed.testapp.interfaces.DataFragmentListener;
import com.lifed.testapp.models.Data;

public class DataFragment extends Fragment {

    private TextView tvCity, tvDate, tvTemperature;
    private DataFragmentListener dataFragmentListener;

    public void setDataFragmentListener(DataFragmentListener dataFragmentListener) {
        this.dataFragmentListener = dataFragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        tvCity = view.findViewById(R.id.tv_city);
        tvDate = view.findViewById(R.id.tv_date);
        tvTemperature = view.findViewById(R.id.tv_temperature);

        setupWithData();

        return view;
    }

    public void setupWithData() {
        try {
            if (getArguments() == null) {
                throw new NullPointerException("No data in bundle");
            }

            Data data = getArguments().getParcelable("data");

            if (data == null) {
                throw new NullPointerException("Parcelable data is null");
            }

            tvCity.setText(data.getCity());
            tvDate.setText(data.getDate());
            tvTemperature.setText(getString(R.string.temperature_placeholder, data.getTemperature()));
        } catch (Exception e) {
            if (dataFragmentListener != null) {
                dataFragmentListener.error();
            } else {
                Toast.makeText(getContext(), "Error while fetching data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
