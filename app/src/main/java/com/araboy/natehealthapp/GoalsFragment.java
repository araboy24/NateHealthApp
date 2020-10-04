package com.araboy.natehealthapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoalsFragment extends Fragment {

    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userId, dateS;
    FloatingActionButton btnAddGoal;
    Spinner spinnerGoalType;
    int count;
    RecyclerView recyclerView;

    public ArrayList<Goal> goals= new ArrayList<Goal>();
    public ArrayList<Goal> goalsAll= new ArrayList<Goal>();
    TextView txtGoal;
    String goalSelected;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        instantiate(view);

/*
        DocumentReference dStats = fStore.collection(userId).document("Survey");
        if (dStats != null) {
            try {
                dStats.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        if(value != null) {
                            if (value.get("Goal") != null) {

                               txtGoal.setText("Weight Goal: " + value.get("Goal"));
                            } else {
                          //      txtCurrent.setText("DIDn't work");
                            }

                        }
                    }
                });
            } catch(Exception e){}
        }


 */


        if(user != null){

            fStore.collection(userId).document("Goals").collection("Daily Goals")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> goalsDs = task.getResult().getDocuments();
                        count = goalsDs.size();
                        for (DocumentSnapshot ds : goalsDs) {
                            goalsAll.add(new Goal(ds.getString("Title"), ds.getString("Desc")));
                        }
                    }
                }
            });

            fStore.collection(userId).document("Goals").collection("Weekly Goals")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> goalsDs = task.getResult().getDocuments();
                        count = goalsDs.size();
                        for (DocumentSnapshot ds : goalsDs) {
                            goalsAll.add(new Goal(ds.getString("Title"), ds.getString("Desc")));
                        }
                    }
                }
            });

            fStore.collection(userId).document("Goals").collection("Nutritional Goals")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> goalsDs = task.getResult().getDocuments();
                        count = goalsDs.size();
                        for (DocumentSnapshot ds : goalsDs) {
                            goalsAll.add(new Goal(ds.getString("Title"), ds.getString("Desc")));
                        }
                    }
                }
            });

            fStore.collection(userId).document("Goals").collection("Weight Goals")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> goalsDs = task.getResult().getDocuments();
                        count = goalsDs.size();
                        for (DocumentSnapshot ds : goalsDs) {
                            goalsAll.add(new Goal(ds.getString("Title"), ds.getString("Desc")));
                        }
                    }
                }
            });

            fStore.collection(userId).document("Goals").collection("Monthly Goals")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> goalsDs = task.getResult().getDocuments();
                        count = goalsDs.size();
                        for (DocumentSnapshot ds : goalsDs) {
                            goalsAll.add(new Goal(ds.getString("Title"), ds.getString("Desc")));
                        }
                    }
                }
            });

            GoalAdapter goalAdapter = new GoalAdapter(getActivity().getApplicationContext(), goalsAll);
            if (goalAdapter != null) {
                recyclerView.setAdapter(goalAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            }
        }



        spinnerGoalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {

                  ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                  goalSelected = spinnerGoalType.getSelectedItem().toString();
                  if(user != null) {

                      if (goalSelected.equals("All Goals")) {
                          GoalAdapter goalAdapter = new GoalAdapter(getActivity().getApplicationContext(), goalsAll);
                          if (goalAdapter != null) {
                              recyclerView.setAdapter(goalAdapter);
                              recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                          }
                      } else {

                          fStore.collection(userId).document("Goals").collection(goalSelected)
                                  .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                              @Override
                              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                  if (task.isSuccessful()) {
                                      List<DocumentSnapshot> goalsDs = task.getResult().getDocuments();
                                      count = goalsDs.size();
                                      goals.clear();
                                      for (DocumentSnapshot ds : goalsDs) {
                                          goals.add(new Goal(ds.getString("Title"), ds.getString("Desc")));
                                      }


                                      //    recyclerView = view.findViewById(R.id.recGoals);

                                      //   MealAdapter mealAdapter = new MealAdapter(this, "idk", 1, 2, 3, 4);
                                      GoalAdapter goalAdapter = new GoalAdapter(getActivity().getApplicationContext(), goals);
                                      if (goalAdapter != null) {
                                          recyclerView.setAdapter(goalAdapter);
                                          recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                      }
                                      //  meals = mealsTemp;
                                  } else {

                                  }
                              }
                          });
                      }
                  }

              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {
               // goalSelected = "All Goals";
              }
          });

                btnAddGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddGoalFragment addGoalFragment = new AddGoalFragment();
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction().replace(R.id.fragment_container, addGoalFragment, addGoalFragment.getTag()).commit();
                    }
                });

        return view;
    }

    public void instantiate(View view){
        btnAddGoal = view.findViewById(R.id.btnAddGoal);
        spinnerGoalType = view.findViewById(R.id.spinnerGoalTypeGoals);
        recyclerView = view.findViewById(R.id.recGoals);

        txtGoal = view.findViewById(R.id.txtWeightGoal);
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
