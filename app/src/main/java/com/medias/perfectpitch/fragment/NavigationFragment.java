package com.medias.perfectpitch.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medias.perfectpitch.R;
import com.medias.perfectpitch.activity.InstrumentActivity;
import com.medias.perfectpitch.activity.MainActivity;
import com.medias.perfectpitch.activity.SettingsActivity;
import com.medias.perfectpitch.activity.PlayActivity;
import com.medias.perfectpitch.databinding.FragmentActionBinding;
import com.medias.perfectpitch.utils.Typefaces;


public class NavigationFragment extends Fragment {
    public static final String INTENT_SENDER = "INTENT_SENDER";
    private FragmentActionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_action, container, false);
        binding.textPitch.setTypeface(Typefaces.ptSans(getActivity()));
        binding.textPerfect.setTypeface(Typefaces.ptSans(getActivity()));
        binding.setHandler(this);
        return binding.getRoot();
    }

    public void onBackPressed(View view) {
        getActivity().onBackPressed();
    }

    public void onOptionsPressed(View view) {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.putExtra(INTENT_SENDER, getActivity().getClass());
        startActivity(intent);
    }

    public boolean isBackVisible() {
        return !(getActivity() instanceof MainActivity);
    }

    public boolean isOptionsVisible() {
        return (getActivity() instanceof InstrumentActivity || getActivity() instanceof PlayActivity);
    }

    public boolean isCloseVisible() {
        return (getActivity() instanceof MainActivity);
    }
}
