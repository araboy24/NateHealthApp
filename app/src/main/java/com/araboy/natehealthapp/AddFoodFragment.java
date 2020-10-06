package com.araboy.natehealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddFoodFragment extends Fragment {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;

    Double calories, carbs, fat, protein;
    String name;

    Date date;
    String sDate;

    Button btnDone, btnClear;
    EditText edtCalories, edtProtein, edtFat, edtCarbs, edtName;

    boolean isFood;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_food, container, false);
        instantiate(view);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emptyField = false;
                try {
                    name = edtName.getText().toString();
                    calories = Double.parseDouble(edtCalories.getText().toString());
                    carbs = Double.parseDouble(edtCarbs.getText().toString());
                    protein = Double.parseDouble(edtProtein.getText().toString());
                    fat = Double.parseDouble(edtFat.getText().toString());
                } catch(Exception e){
                    emptyField = true;
                }
                if(emptyField == false) {
                    Map<String, Object> food = new HashMap<>();
                    food.put("Name", name);
                    food.put("Calories", calories);
                    food.put("Carbs", carbs);
                    food.put("Protein", protein);
                    food.put("Fat", fat);

                    if (user != null) {
                        DocumentReference docFood = fStore.collection(userId).document("Daily Food");
                        DocumentReference docDay = docFood.collection(sDate).document(name);
                        docDay.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        isFood = true;
                                    } else {
                                        isFood = false;
                                    }
                                }
                            }
                        });

                        if(isFood){
                            docDay = docFood.collection(sDate).document(name+"!");
                        }
                        docDay.set(food, SetOptions.merge());
                    }
                }
                Toast.makeText(getActivity(), "Food Stored Successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtCalories.setText("");
                edtCarbs.setText("");
                edtFat.setText("");
                edtName.setText("");
                edtProtein.setText("");
            }
        });


        return view;
    }

    public void instantiate(View view) {
        btnDone = view.findViewById(R.id.btnDone);
        btnClear = view.findViewById(R.id.btnClear);
        edtCalories = view.findViewById(R.id.edtCalories);
        edtCarbs = view.findViewById(R.id.edtCarbs);
        edtFat = view.findViewById(R.id.edtFat);
        edtProtein = view.findViewById(R.id.edtProtein);
        edtName = view.findViewById(R.id.edtFoodName);
        date = new Date();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        sDate = getDate(date);

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
