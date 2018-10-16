package petcom.sydney.edu.au.petcom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

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
        setContentView(R.layout.add_to_database_layout);

        editText = (EditText) findViewById(R.id.editUserName);
        btnAdd = (Button) findViewById(R.id.btnEditUsername);

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
                    User user = new User(userName);
                    dbRef.child("User").child(uid).setValue(user);
                }

            }
        });

    }


}