package com.araboy.natehealthapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Survey5WeeklyGoalActivity extends AppCompatActivity {
    RadioGroup rgGoal;
    RadioButton rb5, rb1, rb0;
    Button btnFinish;
    String iden = "";




    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    DocumentReference documentReference, goalsDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey5_weekly_goal);

        rgGoal = findViewById(R.id.rgGoal);
        rb0 = findViewById(R.id.rb0);
        rb1 = findViewById(R.id.rb1);
        rb5 = findViewById(R.id.rb5);
        btnFinish = findViewById(R.id.btnFinish);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = user.getUid();


            documentReference = fStore.collection(userId).document("Survey");


            goalsDocRef = fStore.collection(userId).document("Goals");


            if(documentReference != null) {
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        try{
                            double cw = (Double) value.get("Weight (kg)");
                            double nw = (Double) value.get("Goal Weight (kg)");
                            if (nw > cw) {
                                rb5.setText("Gain 0.5 lbs a week");
                                rb1.setText("Gain 1 lb a week");
                                iden = "gain";
                            } else if (nw < cw) {
                                rb5.setText("Lose 0.5 lbs a week");
                                rb1.setText("Lose 1 lb a week");
                                iden = "lose";
                            } else {
                                switch (value.getString("Goal")) {
                                    case "Gain Weight":
                                        rb5.setText("Gain 0.5 lbs a week");
                                        rb1.setText("Gain 1 lb a week");
                                        iden = "gain";
                                        break;
                                    case "Lose Weight":
                                        rb5.setText("Lose 0.5 lbs a week");
                                        rb1.setText("Lose 1 lb a week");
                                        iden = "lose";
                                        break;
                                    default:
                                        rb5.setText("Lose 0.5 lbs a Week");
                                        rb1.setText("Gain 0.5 lbs a Week");
                                        iden = "main";
                                        break;
                                }
                            }
                        } catch (Exception e){

                        }
                    }
                });
            }
            btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> weeklyGoal = new HashMap<>();
                    Map<String, Object> weeklyGoalGoals = new HashMap<>();
                    String goal ="";
                    if (rgGoal.getCheckedRadioButtonId() != -1) {
                        if (rb0.isChecked()) {
                            weeklyGoal.put("Weekly Goal", 0);
                            goal = "Maintain Weight";

                        } else if (rb5.isChecked()) {
                            if (iden.equals("gain")) {
                                weeklyGoal.put("Weekly Goal", 0.5);
                                goal = "Gain 0.5 lbs a week";
                            } else {
                                weeklyGoal.put("Weekly Goal", -0.5);
                                goal = "Lose 0.5 lbs a week";
                            }
                        } else {
                            if (iden.equals("gain")) {
                                weeklyGoal.put("Weekly Goal", 1);
                                goal = "Gain 1 lb a week";
                            } else if (iden.equals("lose")) {
                                weeklyGoal.put("Weekly Goal", -1);
                                goal = "Lose 1 lb a week";
                            } else {
                                weeklyGoal.put("Weekly Goal", 0.5);
                                goal = "Gain 0.5 lbs a week";
                            }
                        }
                        weeklyGoalGoals.put("Title", goal);
                        weeklyGoalGoals.put("Desc", goal);
                        DocumentReference docGoal = goalsDocRef.collection("Weekly Goals").document(goal);

                        docGoal.set(weeklyGoalGoals, SetOptions.merge());
                        weeklyGoal.put("isComplete", true);
                        documentReference.set(weeklyGoal, SetOptions.merge());
                    } else {
                        Toast.makeText(Survey5WeeklyGoalActivity.this, "Select a Goal", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            });
        }

    }
}