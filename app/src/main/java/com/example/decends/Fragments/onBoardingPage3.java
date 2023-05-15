package com.example.decends.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.decends.PeerId;
import com.example.decends.R;
import com.example.decends.SeekBar;
import com.example.decends.onBoarding;

import java.util.zip.Inflater;


public class onBoardingPage3 extends Fragment {

    Button yes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_boarding_page3, container, false);
        yes = view.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Hey", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), PeerId.class);
                startActivity(intent);

            }
        });
        return view;
    }

}