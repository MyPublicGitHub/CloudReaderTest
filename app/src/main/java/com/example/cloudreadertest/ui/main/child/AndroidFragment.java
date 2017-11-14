package com.example.cloudreadertest.ui.main.child;


import android.support.v4.app.Fragment;

import com.example.cloudreadertest.R;
import com.example.cloudreadertest.base.BaseFragment;
import com.example.cloudreadertest.databinding.FragmentAndroidBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AndroidFragment extends BaseFragment<FragmentAndroidBinding> {

    @Override
    public int setContentView() {
        return R.layout.fragment_android;
    }

}
