package com.araboy.natehealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Survey3UserInfoActivity extends AppCompatActivity {
    Button btnNext;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    EditText edtDob;

    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey3_user_info);
        edtDob = findViewById(R.id.edtDob);
        btnNext = findViewById(R.id.btnNext3);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rgGender = findViewById(R.id.rgGender);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = user.getUid();


            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String gender;
                    String sDob;
                    Date dDob = null;
                    if (rgGender.getCheckedRadioButtonId() != -1) {
                        if (rbFemale.isChecked()) {
                            gender = "Female";
                        } else {
                            gender = "Male";
                        }

                        sDob = edtDob.getText().toString();
                        try {
                            dDob = new SimpleDateFormat("MM/dd/yyyy").parse(sDob);
                        } catch (ParseException e) {
                            Toast.makeText(Survey3UserInfoActivity.this, "Format the Date Correctly", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        if (dDob != null) {
                            documentReference = fStore.collection(userId).document("Survey");
                            Map<String, Object> personalInfo = new HashMap<>();
                            personalInfo.put("Gender", gender);
                            personalInfo.put("DOB", dDob);
                            personalInfo.put("SDOB", sDob);
                            documentReference.set(personalInfo, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: Personal Info Added");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), Survey4BodyInfoActivity.class));
                        }
                    } else {
                        Toast.makeText(Survey3UserInfoActivity.this, "Please choose a gender", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}