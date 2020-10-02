package com.araboy.natehealthapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class SettingsFragment extends Fragment {
    SwitchMaterial switchNotif;
    Button btnLogout;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId, dateS;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        instantiate(view);

        switchNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switchNotif.isChecked()){
                    switchNotif.setText("On");
                } else{
                    switchNotif.setText("Off");
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).logout(view);
            }
        });

        return view;
    }

    public void instantiate(View view) {
        switchNotif = view.findViewById(R.id.switchNotif);
        btnLogout = view.findViewById(R.id.btnLogOutSettings);

        fStore = FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        if(user!= null) {
            userId = user.getUid();
        }
        dateS = getDate(new Date());

    }

    public static String getDate(Date date) {
        String month, day, year, dateWTime;
        dateWTime = date.toString();
        switch(dateWTime.substring(4, 7)) {
            case "Jan":
                month = "1";
                break;
            case "Feb":
                month = "2";
                break;
            case "Mar":
                month = "3";
                break;
            case "Apr":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "Jun":
                month = "6";
                break;
            case "Jul":
                month = "7";
                break;
            case "Aug":
                month = "8";
                break;
            case "Sep":
                month = "9";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                month = "else";
                break;
        }

        day = dateWTime.substring(8, 10);

        year = dateWTime.substring(dateWTime.length()-4, dateWTime.length());

        return month+"-"+day+"-"+year;

    }


}
