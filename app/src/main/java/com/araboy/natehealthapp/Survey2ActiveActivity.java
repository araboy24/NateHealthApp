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

public class Survey2ActiveActivity extends AppCompatActivity {

    Button btnNext2;
    RadioGroup rgActivity;
    RadioButton rbNotActive, rbLightly, rbActive, rbVery;

    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey2_active);
        btnNext2 = findViewById(R.id.btnNext2);
        rgActivity = findViewById(R.id.rgActive);
        rbNotActive = findViewById(R.id.rbNotActive);
        rbActive = findViewById(R.id.rbActive);
        rbLightly = findViewById(R.id.rbLightly);
        rbVery = findViewById(R.id.rbVery);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = fAuth.getCurrentUser();
            userId = user.getUid();


            btnNext2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String activityLevel;
                    if (rgActivity.getCheckedRadioButtonId() != -1) {
                        if (rbActive.isChecked()) {
                            activityLevel = "Active";
                        } else if (rbLightly.isChecked()) {
                            activityLevel = "Lightly Active";
                        } else if (rbNotActive.isChecked()) {
                            activityLevel = "Not Active";
                        } else {
                            activityLevel = "Very Active";
                        }
                        documentReference = fStore.collection(userId).document("Survey");
                        Map<String, Object> mActivity = new HashMap<String, Object>();
                        mActivity.put("Activity Level", activityLevel);
                        documentReference.set(mActivity, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: User activity level added successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });

                        startActivity(new Intent(getApplicationContext(), Survey3UserInfoActivity.class));
                    }
                }
            });
        }

    }
}
