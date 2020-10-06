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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Survey5WeeklyGoalActivity extends AppCompatActivity {
    RadioGroup rgGoal;
    RadioButton rb5, rb1, rb0;
    Button btnFinish;
    String iden = "";

    //Daily intake
    double caloriesADay, caloriesMaintain, caloriesChange;

    String weeklyGoalS = "";



    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    DocumentReference documentReference, goalsDocRef;
    DocumentReference dailyReqDocRef, getBodyStatsDoc;

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

                        weeklyGoalS = goal;
                        weeklyGoalGoals.put("Title", goal);
                        weeklyGoalGoals.put("Desc", goal);
                        DocumentReference docGoal = goalsDocRef.collection("Weekly Goals").document(goal);

                        docGoal.set(weeklyGoalGoals, SetOptions.merge());
                        weeklyGoal.put("isComplete", true);
                        documentReference.set(weeklyGoal, SetOptions.merge());
                    } else {
                        Toast.makeText(Survey5WeeklyGoalActivity.this, "Select a Goal", Toast.LENGTH_SHORT).show();
                    }

                    saveDailyMacros();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            });
        }

    }

    public void saveDailyMacros(){
        dailyReqDocRef = fStore.collection(userId).document("Daily Requirements");
        getBodyStatsDoc = fStore.collection(userId).document("Survey");
        //Getting the body info
        getBodyStatsDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                double weight, height, age, activityLevel, weeklyGoal;
                double caloriesBefore;
            //    Date dob = (Date)value.get("DOB");
                String dobS = value.getString("SDOB");
                weight = (Double) value.get("Weight (kg)");
                height = (Double) value.get("Height (cm)");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date birthDate = null;
                try {
                    birthDate = sdf.parse(dobS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Age ageA = calculateAge(birthDate);
                age = ageA.getYears();
                switch(value.getString("Activity Level")){
                    case "Not Active":
                        activityLevel = 1;
                        break;
                    case "Lightly Active":
                        activityLevel = 1.11;
                        break;
                    case "Active":
                        activityLevel = 1.25;
                        break;
                    case "Very Active":
                        activityLevel = 1.48;
                        break;
                    default:
                        activityLevel = 1;
                }
         //EER = 661.8 - 9.53*age (years) + PAL *(15.91* weight (kg) + 539.6* height (m)),
         // where PAL = 1 if sedentary, 1.11 if low active, 1.25 if active, and 1.48 if very active.
                caloriesBefore = 661.8 - 9.53*age + activityLevel * (15.91 * weight + 539.6 * (height/100));
                double calDif = 0;
                switch(weeklyGoalS){
                    case "Maintain Weight":
                        calDif = 0;
                        break;
                    case "Gain 0.5 lbs a week":
                        calDif = 250;
                        break;
                    case "Lose 0.5 lbs a week":
                        calDif = -250;
                        break;
                    case "Gain 1 lb a week":
                        calDif = 500;
                        break;
                    case "Lose 1 lb a week":
                        calDif = -500;
                        break;
                    default:
                        calDif = 0;
                        break;
                }

                caloriesADay = caloriesBefore + calDif;
                caloriesMaintain = caloriesBefore;
                caloriesChange = calDif;

                Map<String, Object> dailyMacs = new HashMap<>();
                dailyMacs.put("Calories Maintain", (int)caloriesMaintain);
                dailyMacs.put("Calories Difference", caloriesChange);
                dailyMacs.put("Calories Adjusted", (int)caloriesADay);
                dailyReqDocRef.set(dailyMacs, SetOptions.merge());


                //TODO Calculate other macros later

            }
        });




    }

    private static Age calculateAge(Date birthDate)
    {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0)
        {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        }
        else
        {
            days = 0;
            if (months == 12)
            {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new Age(days, months, years);
    }



}