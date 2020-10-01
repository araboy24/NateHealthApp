package com.araboy.natehealthapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SplashActivity extends AppCompatActivity {
    //How long splash screen stays
    private static int SPLASH_TIME_OUT = 3000;

    ImageView imgLogo, imgName;

    //Animations
    Animation topAnimation, bottomAnimation, middleAnimation;


    //Background Check
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user = null;
    String userId;
    boolean surveyComplete, loggedIn;
    Activity nextActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Takes out top bar for time, Must be before setcontentView
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        imgLogo = findViewById(R.id.imgLogo);
        imgName = findViewById(R.id.imgText);

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this,R.anim.middle_animation);

        imgName.setAnimation(bottomAnimation);
        imgLogo.setAnimation(topAnimation);

        //Background Check: Logged in or not
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        try {
            if (fAuth.getCurrentUser() != null) {
                loggedIn = true;
                user = fAuth.getCurrentUser();
                userId = user.getUid();
                DocumentReference db = fStore.collection(userId).document("Survey");
                if (db != null){
                    db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null){
                                if (value.getBoolean("isComplete") != null) {
                                    surveyComplete = value.getBoolean("isComplete");
                                    if (value.getBoolean("isComplete")) {
                                      //  nextActivity = new HomeActivity();
                                        //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                      //  finish();
                                    }
                                } else {

                                   // nextActivity = new SurveyActivity();
                               //USE startActivity(new Intent(getApplicationContext(), SurveyActivity.class));
                               //     startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                   // finish();
                                }
                            }

                        }
                    });
                }
            }
        } catch(Exception e){
           // Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        //Splash Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeAct = new Intent(SplashActivity.this, HomeActivity.class);
                Intent surveyAct = new Intent(SplashActivity.this, SurveyActivity.class);
                Intent mainAct = new Intent(SplashActivity.this, MainActivity.class);
                if(loggedIn){
                    if(surveyComplete){
                        startActivity(homeAct);
                    } else {
                       //TODO FIX THE CHECK ON WHETHER SURVEY IS COMPLETE OR NOT
                        // startActivity(surveyAct);
                        startActivity(homeAct);
                    }
                } else {
                    startActivity(mainAct);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);





    }
}