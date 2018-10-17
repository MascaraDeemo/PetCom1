package petcom.sydney.edu.au.petcom.UserProfile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import petcom.sydney.edu.au.petcom.R;
import petcom.sydney.edu.au.petcom.main_activity;

public class RegistrationFragment2 extends Fragment {

    private static final String TAG = "Registration2";


    private EditText editUserName;

    private Button btnToFinish;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_profile, container, false);


        editUserName = (EditText) view.findViewById(R.id.editUserName);
        btnToFinish = (Button) view.findViewById(R.id.btnFinishReg);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        btnToFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserDetailsInDatabase(editUserName.getText().toString());
                Intent gotoMainIntent = new Intent((Activity)getContext(), main_activity.class);
                startActivity(gotoMainIntent);
            }
        });



        return view;
    }

    private void setUserDetailsInDatabase(String userName){
        if (!validateForm()) {
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        databaseReference.child("User").child(uid).child("UserName").setValue(editUserName.getText().toString());
    }

    private boolean validateForm() {
        boolean valid = true;

        String uname = editUserName.getText().toString();
        if (uname.equals("")) {
            editUserName.setError("Required.");
            valid = false;
        } else {
            editUserName.setError(null);
        }

        return valid;
    }
}
