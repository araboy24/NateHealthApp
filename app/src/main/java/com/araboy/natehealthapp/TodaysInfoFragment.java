package com.araboy.natehealthapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodaysInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysInfoFragment extends Fragment {

    TextView txtCalories, txtCarbs, txtFat, txtProtein, txtDate;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId;
    Button btnHome;
    String dateS;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodaysInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodaysInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodaysInfoFragment newInstance(String param1, String param2) {
        TodaysInfoFragment fragment = new TodaysInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_todays_info, container, false);
        instantiate(view);

        if(user != null){
            fStore.collection(userId).document("Daily Food").collection(dateS)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> foods = task.getResult().getDocuments();
                        double sumCal = 0;
                        double sumCarb = 0;
                        double sumProtein = 0;
                        double sumFat = 0;
                        for (DocumentSnapshot ds : foods) {
                            sumCal += (double) ds.get("Calories");
                            sumCarb += (double) ds.get("Carbs");
                            sumFat += (double) ds.get("Fat");
                            sumProtein += (double) ds.get("Protein");
                        }
                        txtCalories.setText("Calories: " + sumCal);
                        txtCarbs.setText("Carbohydrates: " + sumCarb);
                        txtFat.setText("Fat: " + sumFat);
                        txtProtein.setText("Protein: " + sumProtein);
                    } else {

                    }
                }
            });
        }
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, homeFragment, homeFragment.getTag()).commit();
            }
        });

        return view;
    }

    public  void instantiate(View view) {

        txtDate = view.findViewById(R.id.txtDateTitle);
        txtCalories = view.findViewById(R.id.txtCal);
        txtCarbs = view.findViewById(R.id.txtCarb);
        txtFat = view.findViewById(R.id.txtFat);
        txtProtein = view.findViewById(R.id.txtProtein);
        btnHome = view.findViewById(R.id.btnHome);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        dateS = getDate(new Date());
        txtDate.setText(dateS);
    } //End instantiate

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