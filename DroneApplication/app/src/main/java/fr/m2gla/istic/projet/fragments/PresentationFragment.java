package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.m2gla.istic.projet.activity.R;


public class PresentationFragment extends Fragment {

    private View                                view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_intervention_list, container, false);

        return this.view;

    }

}