package com.araboy.natehealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity {
    Button btnNext;
    RadioGroup radioGroup;
    RadioButton rbLose, rbGain, rbMaintain;

    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        btnNext = findViewById(R.id.btnNext);
        radioGroup = findViewById(R.id.radioGroup2);
        rbLose = findViewById(R.id.rbLose);
        rbGain = findViewById(R.id.rbGain);
        rbMaintain = findViewById(R.id.rbMaintain);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = user.getUid();


            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String goal;
                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        if (rbGain.isChecked()) {
                            goal = "Gain Weight";
                        } else if (rbLose.isChecked()) {
                            goal = "Lose Weight";
                        } else {
                            goal = "Maintain Weight";
                        }
                        documentReference = fStore.collection(userId).document("Survey");
                        Map<String, Object> mGoal = new HashMap<String, Object>();
                        mGoal.put("Goal", goal);
                        documentReference.set(mGoal, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: User Goal added successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });

                        startActivity(new Intent(getApplicationContext(), Survey2ActiveActivity.class));
                    }

                }
            });
        }

    }
}