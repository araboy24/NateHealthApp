package com.araboy.natehealthapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;

public class ProfileFragment extends Fragment {
    ImageView imgProfilePic;
    Button btnChange;
    Button btnCalendar;
    TextView txtGoal, txtCurrent, txtName;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId, dateS;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        instantiate(view);

        if (user != null) {
            userId = user.getUid();
            DocumentReference dName = fStore.collection("Users").document(userId);
            if(dName != null) {
                dName.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        if (value.getString("Full Name") != null) {
                            txtName.setText("Name: " + value.getString("Full Name"));
                        }
                    }
                });


                DocumentReference dStats = fStore.collection(userId).document("Survey");
                if (dStats != null) {
                    try {
                        dStats.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                assert value != null;
                                if (value.get("Weight (kg)") != null) {
                                    double w = (double) value.get("Weight (kg)");
                                    double a = Math.round(w);

                                    txtCurrent.setText("Current Weight: " + a + " kg");
                                } else {
                                    txtCurrent.setText("DIDn't work");
                                }
                                if (value.get("Goal Weight (kg)") != null) {
                                    double w = (Double) value.get("Goal Weight (kg)");
                                    double a = (double) Math.round(w);
                                    txtGoal.setText("Goal Weight: " + a + " kg");
                                } else {
                                    txtGoal.setText("DIDn't work");
                                }
                            }
                        });
                    } catch(Exception e){}
                }
            }

        }

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Extract email and send the link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Reset Link Sent to your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error ! Reset Link Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });


        return view;
    }

    public void instantiate(View view) {
        btnCalendar = view.findViewById(R.id.btnCalendar);
        btnChange = view.findViewById(R.id.btnResetPass);
        txtCurrent = view.findViewById(R.id.txtCurrentWeight);
        txtGoal = view.findViewById(R.id.txtGoalWeight);
        txtName = view.findViewById(R.id.txtFullName);
        imgProfilePic = view.findViewById(R.id.imgProfilePic);
        fStore = FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        if(user!= null) {
            userId = user.getUid();
        }
        dateS = getDate(new Date());

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
