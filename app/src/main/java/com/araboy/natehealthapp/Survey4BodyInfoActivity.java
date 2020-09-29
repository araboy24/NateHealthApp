package com.araboy.natehealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Survey4BodyInfoActivity extends AppCompatActivity {

    Button btnNext;
    EditText edtHeight, edtWeight, edtGoalWeight;

    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    DocumentReference documentReference, metric, imp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey4_body_info);
        btnNext = findViewById(R.id.btnNext4);
        edtGoalWeight = findViewById(R.id.edtGoalWeight);
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = user.getUid();


            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String weight, height, goalWeight;
                    weight = edtWeight.getText().toString();
                    goalWeight = edtGoalWeight.getText().toString();
                    height = edtHeight.getText().toString();
                    String[] heightA = height.split("'");
                    if (heightA.length != 2) {
                        Toast.makeText(Survey4BodyInfoActivity.this, "Enter Height Correctly", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            double weightD = Double.parseDouble(weight);
                            double gWeightD = Double.parseDouble(goalWeight);
                            Integer h1 = Integer.parseInt(heightA[0]);
                            double h2 = Double.parseDouble(heightA[1]);

                            documentReference = fStore.collection(userId).document("Survey");
                            metric = fStore.collection(userId).document("Survey").collection("HeightWeight").document("Metric");
                            imp = fStore.collection(userId).document("Survey").collection("HeightWeight").document("Imperial");

                            Map<String, Object> bodyInfo = new HashMap<>();
                            bodyInfo.put("Weight (lbs)", weightD);
                            bodyInfo.put("Goal Weight (lbs)", gWeightD);
                            bodyInfo.put("Height (in)", (h1 * 12) + h2);
                            bodyInfo.put("Height (ft & in)", h1 + "'" + h2 + "''");
                            bodyInfo.put("Weight (kg)", weightD / 2.2);
                            bodyInfo.put("Goal Weight (kg)", gWeightD / 2.2);
                            bodyInfo.put("Height (cm)", ((h1 * 12) + h2) * 2.54);
                            documentReference.set(bodyInfo, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: body info added");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure" + e.toString());
                                }
                            });


                            Map<String, Object> bodyInfoI = new HashMap<>();
                            bodyInfoI.put("Weight (lbs)", weightD);
                            bodyInfoI.put("Goal Weight (lbs)", gWeightD);
                            bodyInfoI.put("Height (in)", (h1 * 12) + h2);
                            bodyInfoI.put("Height (ft & in)", h1 + "'" + h2 + "''");
                            imp.set(bodyInfoI, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: body info added");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure" + e.toString());
                                }
                            });

                            Map<String, Object> bodyInfoM = new HashMap<>();
                            bodyInfoM.put("Weight (kg)", weightD / 2.2);
                            bodyInfoM.put("Goal Weight (kg)", gWeightD / 2.2);
                            bodyInfoM.put("Height (cm)", ((h1 * 12) + h2) * 2.54);
                            metric.set(bodyInfoM, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: body info added");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure" + e.toString());
                                }
                            });


                            startActivity(new Intent(getApplicationContext(), Survey5WeeklyGoalActivity.class));
                        } catch (NumberFormatException e) {
                            Toast.makeText(Survey4BodyInfoActivity.this, "Enter Weights Correctly", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(Survey4BodyInfoActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }


                    }
                }
            });
        }
    }
}