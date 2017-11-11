package com.example.cloudreadertest.ui.main.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudreadertest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EverydayFragment extends Fragment {


    public EverydayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_everyday, container, false);
    }

}
