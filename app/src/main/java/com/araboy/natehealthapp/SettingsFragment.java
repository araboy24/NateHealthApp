package com.araboy.natehealthapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {
    SwitchMaterial switchNotif;
    Button btnLogout, btnSave;
    RadioGroup rgUnits;
    RadioButton rbMetric, rbImper;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId, dateS, units;
    boolean isNotifOn;
    TextView txtUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        instantiate(view);

        //Initializing values on open
        final DocumentReference docSettings = fStore.collection(userId).document("Settings");
        if(docSettings!= null){
            docSettings.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value != null){
                        if (value.getString("Units") != null) {
                            if(value.getString("Units").equals("Metric")){
                                rbImper.setChecked(false);
                                rbMetric.setChecked(true);
                                units = "Metric";
                            } else {
                                rbImper.setChecked(true);
                                rbMetric.setChecked(false);
                                units = "Imperial";
                            }
                        }

                        if(value.get("Notifications") != null){
                            if((boolean)value.get("Notifications") == true){
                                switchNotif.setChecked(true);
                                isNotifOn = true;
                            } else {
                                switchNotif.setChecked(false);
                                isNotifOn = false;
                            }
                        }
                    }
                }
            });
        }

        switchNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switchNotif.isChecked()){
                    switchNotif.setText("On");
                    isNotifOn = true;
                } else{
                    switchNotif.setText("Off");
                    isNotifOn = false;
                }
            }
        });
        rgUnits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(rgUnits.getCheckedRadioButtonId()){
                    case R.id.rbMetric:
                        units = "Metric";
                        break;
                    case R.id.rbImperial:
                        units = "Imperial";
                        break;
                }
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).logout(view);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> settings = new HashMap<>();
                settings.put("Units", units);
                settings.put("Notifications", isNotifOn);
                docSettings.set(settings, SetOptions.merge());
                Toast.makeText(getActivity().getApplicationContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void instantiate(View view) {
        switchNotif = view.findViewById(R.id.switchNotif);
        btnLogout = view.findViewById(R.id.btnLogOutSettings);
        btnSave = view.findViewById(R.id.btnSave);
        rgUnits = view.findViewById(R.id.rgUnits);
        rbImper = view.findViewById(R.id.rbImperial);
        rbMetric = view.findViewById(R.id.rbMetric);

        fStore = FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        //TODO take out before launch
        txtUserId = view.findViewById(R.id.txtUserId);
        if(user!= null) {
            userId = user.getUid();
            txtUserId.setText("User ID: " + userId);
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
