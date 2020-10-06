package com.araboy.natehealthapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyMealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyMealsFragment extends Fragment {

    //Variables
    RecyclerView recyclerView;
    TextView txtDate;
    Button btnAddFood;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    String dateS = getDate(new Date());
    int count;

    String dateFromCal = getDate(new Date());

    public ArrayList<Meal> meals = new ArrayList<Meal>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DailyMealsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyMealsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyMealsFragment newInstance(String param1, String param2) {
        DailyMealsFragment fragment = new DailyMealsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_daily_meals, container, false);
        instantiate(view);

        Intent intent = getActivity().getIntent();
        if (intent.getExtras() != null) {
            dateFromCal =intent.getStringExtra("Date");
        }
        txtDate.setText(dateFromCal);

        if(user != null){

            if(dateFromCal == null){
                dateFromCal = getDate(new Date());
            }
            fStore.collection(userId).document("Daily Food").collection(dateFromCal)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> foods = task.getResult().getDocuments();
                        count = foods.size();
                        for (DocumentSnapshot ds : foods) {
                            meals.add(new Meal(ds.getString("Name"), (double) ds.get("Calories"),
                                    (double) ds.get("Carbs"), (double) ds.get("Protein"), (double) ds.get("Fat")));
                        }


                        //   MealAdapter mealAdapter = new MealAdapter(this, "idk", 1, 2, 3, 4);
                        MealAdapter mealAdapter = new MealAdapter(getActivity().getApplicationContext(), meals);
                        recyclerView.setAdapter(mealAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        //  meals = mealsTemp;
                    } else {

                    }
                }
            });
        }

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity().getApplicationContext(), HomeActivity.class);
                intent.putExtra("EXTRA", "addFood");
                startActivity(intent);
            }
        });

        return view;
    }

    public void instantiate(View view){
        txtDate = view.findViewById(R.id.txtDateMeal);
        btnAddFood = view.findViewById(R.id.btnAdd);
        recyclerView = view.findViewById(R.id.recyclerView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(user != null){
            userId =  user.getUid();
        }

        dateS = getDate(new Date());

        // txtDate.setText(dateS);

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