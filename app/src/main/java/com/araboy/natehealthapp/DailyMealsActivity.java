package com.araboy.natehealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyMealsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtDate;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    String dateS;
    int count;
    public ArrayList<Meal> meals = new ArrayList<Meal>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_meals);

        instantiate();

        if(user != null){

            fStore.collection(userId).document("Daily Food").collection(dateS)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> foods = task.getResult().getDocuments();
                        count = foods.size();
                        ArrayList<Meal> mealsTemp = new ArrayList<Meal>();
                        for (DocumentSnapshot ds : foods) {
                            mealsTemp.add(new Meal(ds.getString("Name"), (double) ds.get("Calories"),
                                    (double) ds.get("Carbs"), (double) ds.get("Protein"), (double) ds.get("Fat")));
                          //  txtDate.setText(""+mealsTemp.size());

                            meals.add(new Meal(ds.getString("Name"), (double) ds.get("Calories"),
                                    (double) ds.get("Carbs"), (double) ds.get("Protein"), (double) ds.get("Fat")));
                         //   txtDate.setText(""+meals.size());
                        }
                        int x = mealsTemp.size();
                        int y = meals.size();

                        recyclerView = findViewById(R.id.recyclerView);

                        //   MealAdapter mealAdapter = new MealAdapter(this, "idk", 1, 2, 3, 4);
                        MealAdapter mealAdapter = new MealAdapter(getApplicationContext(), meals);
                        recyclerView.setAdapter(mealAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                      //  meals = mealsTemp;
                    } else {

                    }
                }
            });
        }






    }
    public void instantiate(){
        txtDate = findViewById(R.id.txtDateMeal);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(user != null){
            userId =  user.getUid();
        }

        dateS = getDate(new Date());
        txtDate.setText(getDate(new Date()));

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