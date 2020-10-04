package com.araboy.natehealthapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId;
    TextView txtEmail, txtName;
    ImageView profilePic;
    Button btnLogout;
    boolean loggedOut = false;

    Toolbar tb;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(loggedOut){
            finish();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtEmail = (TextView) headerView.findViewById(R.id.txtEmailNav);
        txtName = (TextView) headerView.findViewById(R.id.txtNameNav);
        profilePic = headerView.findViewById(R.id.imgProfilePic);
        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if (user != null) {

            userId = user.getUid();
            DocumentReference dName = fStore.collection("Users").document(userId);
            if(dName != null && !loggedOut) {
                dName.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        if(value != null) {
                            if (value.getString("Email") != null) {
                                txtEmail.setText(value.getString("Email"));
                                //   txtEmail.setText("email");
                            }
                            if (value.getString("Full Name") != null) {
                                txtName.setText(value.getString("Full Name"));
                            }
                        }
                    }
                });
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
    //    NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        drawer.addDrawerListener(toggle);
        toggle.syncState();







        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        //Recieving message from dailyMeals Frag
        Intent intent = getIntent();
        String message = intent.getStringExtra("Date");
        Bundle bundle=new Bundle();
        bundle.putString("Date", message);

        //set Fragmentclass Arguments
        DailyMealsFragment dailyMealsFragment =new DailyMealsFragment();
        dailyMealsFragment.setArguments(bundle);


/*
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//Logout
                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                finish();
                return;
            }
        });
        */

    }

    @Override
    protected void onStart() {
        super.onStart();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //Opens Add Food fragment from the meals Rec View
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean isNew = extras.getBoolean("addFood", false);
       //     Toast.makeText(this, "Extra != null && " + isNew, Toast.LENGTH_SHORT).show();
            if (isNew) {
                // Do something
                 switch (getIntent().getStringExtra("EXTRA")) {
                     case "addFood":
                         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddFoodFragment()).commit();
                         navigationView.setCheckedItem(R.id.nav_add_food);
                         break;
                 }
               // if(getIntent().getStringExtra("EXTRA").equals("addFood")) {
                 //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddFoodFragment()).commit();
                   // navigationView.setCheckedItem(R.id.nav_add_food);
               // }
                // getSupportActionBar().setTitle("Fragment Activity B");
                //     break;
                //}
            } else {
                // Do something else
              //  if(getIntent().getStringExtra("EXTRA").equals("addFood")) {
                //    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddFoodFragment()).commit();
                  //  navigationView.setCheckedItem(R.id.nav_add_food);
                //TODO: Fix this MESS of a method
                //The code below shouldn't be running, instead it should be getting run if(isNew) == true
                //But it's always false
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddFoodFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_add_food);
                }
            }
        }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public  void logout(View view){
        loggedOut = true;
        FirebaseAuth.getInstance().signOut();//Logout
        startActivity(new Intent(getApplicationContext(),MainActivity2.class));
        finish();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
            case R.id.nav_add_food:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddFoodFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_goals:
                 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                      new GoalsFragment()).commit();
                //TODO startActivity(new Intent(getApplicationContext(), AddFoodActivity.class));

                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
                //Todo: Delete next case
            case R.id.nav_delete:
                startActivity(new Intent(getApplicationContext(), DeleteLaterActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //MAYBE TAKE OUT
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Handling Action Bar button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if(i == R.id.home){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        } else if(i == R.id.nateChat){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NateChatFragment()).commit();
        }
        return true;
    }

}