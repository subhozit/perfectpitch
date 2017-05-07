package com.medias.perfectpitch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.medias.perfectpitch.R;


public class PromoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner_ad, container, false);
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("94CD06DD9E2B819366D02B26D1B3606B").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }
}
