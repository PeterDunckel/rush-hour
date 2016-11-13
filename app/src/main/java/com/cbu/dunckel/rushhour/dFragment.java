package com.cbu.dunckel.rushhour;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by NickNatali on 11/12/16.
 */
//Dialog Fragment.
public class dFragment extends android.support.v4.app.DialogFragment {

    private static Context context;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_restaurant_list, container, false);
            getDialog().setTitle("Menu");

            return rootView;
        }

}
