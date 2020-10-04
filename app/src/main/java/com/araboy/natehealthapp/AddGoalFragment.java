package com.araboy.natehealthapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGoalFragment extends Fragment {

    Spinner spinnerGoalType;
    EditText edtGoalTitle, edtGoalDesc;
    Button btnSubmitGoal;

    String goalType, goalTitle, goalDesc;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId, dateS;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddGoalFragment() {
        // Required empty public constructor
    }

    public static AddGoalFragment newInstance(String param1, String param2) {
        AddGoalFragment fragment = new AddGoalFragment();
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
        View view =  inflater.inflate(R.layout.fragment_add_goal, container, false);
        instantiate(view);
        spinnerGoalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                goalType = spinnerGoalType.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmitGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emptyField = false;
                try {
                    goalTitle = edtGoalTitle.getText().toString();
                    goalDesc = edtGoalDesc.getText().toString();
                } catch(Exception e){
                    emptyField = true;
                }

                if(TextUtils.isEmpty(goalTitle)){
                    edtGoalTitle.setError("Title is Required");
                    return;
                }

                if(TextUtils.isEmpty(goalDesc)){
                    edtGoalDesc.setError("Description is Required");
                    return;
                }

                if(!emptyField) {
                    Map<String, Object> goal = new HashMap<>();
                    goal.put("Title", goalTitle);
                    goal.put("Desc", goalDesc);
                    if (user != null) {
                        DocumentReference docGoals = fStore.collection(userId).document("Goals");
                        DocumentReference docGoal = docGoals.collection(goalType).document(goalTitle);
                        docGoal.set(goal, SetOptions.merge());
                    }
                }
                Toast.makeText(getActivity(), "Goal Stored Successfully!", Toast.LENGTH_SHORT).show();
                edtGoalDesc.setText("");
                edtGoalTitle.setText("");
                spinnerGoalType.setSelection(0);
            }
        });


        return view;
    }

    public void instantiate(View view) {
        btnSubmitGoal = view.findViewById(R.id.btnSubmitGoal);
        spinnerGoalType = view.findViewById(R.id.spinnerGoal);
        edtGoalDesc = view.findViewById(R.id.edtGoalDesc);
        edtGoalTitle = view.findViewById(R.id.edtGoalTitle);

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