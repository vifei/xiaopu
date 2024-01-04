package com.example.xiaopu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Index#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Index extends Fragment {


    private View rootView;
    private ImageButton imageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_index, container, false);
        }

        imageButton = rootView.findViewById(R.id.mbti);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = getActivity();
                Intent intent = new Intent(currentActivity, Exam.class);
                currentActivity.startActivity(intent);
            }
        });

        return rootView;

    }

}