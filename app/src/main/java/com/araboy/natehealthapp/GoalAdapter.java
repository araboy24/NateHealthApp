package com.araboy.natehealthapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import java.util.ArrayList;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalHolder>  {

    Context context;
    ArrayList<Goal> goals;

    public GoalAdapter(Context ct, ArrayList<Goal> goals){
        context = ct;
        this.goals = goals;
    }

    @NonNull
    @Override
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.goal, parent, false);
        return new GoalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalAdapter.GoalHolder holder, int position) {
        holder.txtDesc.setText(goals.get(position).getDesc());
        holder.txtTitle.setText(goals.get(position).getTitle());

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
        return goals.size();
    }

    public class GoalHolder extends RecyclerView.ViewHolder{
        TextView txtDesc, txtTitle;
        ConstraintLayout mainLayout;

        public GoalHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayoutGoal);
            txtDesc = itemView.findViewById(R.id.txtGoalDescription);
            txtTitle = itemView.findViewById(R.id.txtGoalTitle);
        }
    }
}
