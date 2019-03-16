package com.example.dwl.menuapplication.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dwl.menuapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteFragment extends Fragment {


    public WriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_write, container, false);
//
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        Toolbar mToolBar = (Toolbar) activity.findViewById(R.id.toolbar_main);
//        mToolBar.setVisibility(View.GONE);
        return view;
    }

}
