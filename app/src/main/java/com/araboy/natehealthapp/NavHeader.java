package com.araboy.natehealthapp;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NavHeader extends NavigationView {
    //TODO Probably delete this garbage class
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId;
    String name, email;
    public static TextView txtEmail, txtName;

    public NavHeader(@NonNull Context context) {
        super(context);
        txtEmail = findViewById(R.id.txtEmailNav);
        txtName = findViewById(R.id.txtNameNav);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();

            DocumentReference dName = fStore.collection("Users").document(userId);
            dName.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    assert value != null;
                    if(value.getString("Email") != null) {
                        txtEmail.setText(value.getString("Email"));
                    }
                    if(value.getString("Full Name") != null) {
                        txtName.setText(value.getString("Full Name"));
                    }
                }
            });
        }
    }

    public static  TextView getTxtEmailNav(){
        return txtEmail;
    }

    public static  TextView getTxtNameNav(){
        return txtName;
    }
}


