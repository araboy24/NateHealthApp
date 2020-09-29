package com.araboy.natehealthapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    ImageView imgProfilePic;
    Button btnChange;
    Button btnCalendar;
    TextView txtGoal, txtCurrent, txtName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        instantiate(view);
        return view;
    }

    public void instantiate(View view) {
        btnCalendar = view.findViewById(R.id.btnCalendar);
        btnChange = view.findViewById(R.id.btnResetPass);
        txtCurrent = view.findViewById(R.id.txtCurrentWeight);
        txtGoal = view.findViewById(R.id.txtGoalWeight);
        txtName = view.findViewById(R.id.txtFullName);
        imgProfilePic = view.findViewById(R.id.imgProfilePic);

    }
}
