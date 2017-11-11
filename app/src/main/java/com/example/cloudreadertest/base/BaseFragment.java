package com.example.cloudreadertest.base;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.cloudreadertest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment<SV extends ViewDataBinding> extends Fragment {

    public SV bindingView;
    RelativeLayout mContainer;
    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_base,null);
        bindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(),setContent(),null,false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        mContainer = inflate.findViewById(R.id.rl_container);
        mContainer.addView(bindingView.getRoot());
        return inflate;
    }
    public abstract int setContent();
}
