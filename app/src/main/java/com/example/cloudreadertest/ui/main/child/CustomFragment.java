package com.example.cloudreadertest.ui.main.child;


import android.support.v4.app.Fragment;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.databinding.FragmentCustomBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomFragment extends BaseFragment<FragmentCustomBinding> {


    @Override
    public int setContentView() {
        return R.layout.fragment_custom;
    }

}
