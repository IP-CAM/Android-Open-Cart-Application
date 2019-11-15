package com.oshop.hm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oshop.hm.R;
import com.oshop.hm.app.MyAppPrefsManager;

public class currency extends Fragment {
    View rootView;
    MyAppPrefsManager myAppPrefsManager ;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_currency, container, false);

        // set the pref manager
        myAppPrefsManager = new MyAppPrefsManager(getContext());


        return rootView;
    }

    public void changeCurrency(String currency){

    }

    public static boolean getCurrency(){
        return false;
    }
}
