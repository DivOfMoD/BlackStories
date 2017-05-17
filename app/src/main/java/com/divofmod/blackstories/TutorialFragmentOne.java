package com.divofmod.blackstories;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialFragmentOne extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);

        ImageView image = (ImageView)rootView.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_launcher_web);

        TextView tutorial = (TextView)rootView.findViewById(R.id.tutorial);
        tutorial.setText(R.string.tutorial_text_1);

        return rootView;
    }
}
