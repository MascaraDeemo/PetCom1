package petcom.sydney.edu.au.petcom.UserProfiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import petcom.sydney.edu.au.petcom.R;

public class userProfile extends AppCompatActivity {

    EditText editText;
    Button btnAdd;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser u;
    String uid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment2_profile);

        editText = (EditText) findViewById(R.id.editUserName);
       // btnAdd = (LinearLayout) findViewById(R.id.saveProfile);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dbRef = db.getReference();
        u = auth.getCurrentUser();
        uid = u.getUid();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editText.getText().toString();
                if(!userName.equals("")) {
                   // User user = new User(userName);
                  //  dbRef.child("User").child(uid).setValue(user);
                }

            }
        });

    }


}
