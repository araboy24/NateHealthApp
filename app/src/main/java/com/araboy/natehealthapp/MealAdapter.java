package com.araboy.natehealthapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealHolder> {
    double cals, carbs, protein, fat;
    int count;
    String name;
    Context context;
    ArrayList<Meal> meals;

    public MealAdapter(Context ct, String name, double cals, double carbs, double protein, double fat){
        this.cals = cals;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.name = name;
        context = ct;
    }

    public MealAdapter(Context ct, ArrayList<Meal> mealss){
        context = ct;
        this.meals = mealss;
    }

    public MealAdapter(){}

    @NonNull
    @Override
    public MealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal, parent, false);
        return new MealHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MealHolder holder, int position) {
        /*
        holder.txtProtein.setText(protein+"g Protein");
        holder.txtFat.setText(fat+"g Fat");
        holder.txtCarb.setText(carbs+"g Carbs");
        holder.txtCal.setText(cals+" Calories");
        holder.txtName.setText(name);
        */
        holder.txtProtein.setText(meals.get(position).getProtein()+"g Protein");
        holder.txtFat.setText(meals.get(position).getFat()+"g Fat");
        holder.txtCarb.setText(meals.get(position).getCarbs()+"g Carbs");
        holder.txtCal.setText(meals.get(position).getName()  + ": " + meals.get(position).getCalories()+" Calories");
        holder.txtName.setText(meals.get(position).getName());


        //Add on click listener here if necessary
        /*
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
         */

    }

    @Override
    public int getItemCount() {
       // return 1;  TODO CHANGE LATER Maybe good now test
        return  meals.size();
    }

    public class MealHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtCal, txtCarb, txtFat, txtProtein;
        ConstraintLayout mainLayout;

        public MealHolder(@NonNull View itemView) {
            super(itemView);
            txtCal = itemView.findViewById(R.id.txtCaloriesMeal);
            txtName = itemView.findViewById(R.id.txtNameMeal);
            txtCarb = itemView.findViewById(R.id.txtCarbsMeal);
            txtFat = itemView.findViewById(R.id.txtFatMeal);
            txtProtein = itemView.findViewById(R.id.txtProteinMeal);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
