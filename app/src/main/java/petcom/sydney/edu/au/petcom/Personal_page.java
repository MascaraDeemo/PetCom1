package petcom.sydney.edu.au.petcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import petcom.sydney.edu.au.petcom.UserProfiles.AddToDatabase;

public class Personal_page extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ImageView user_pic;
    Button edit_button;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private String userName;
    TextView usernameInProfile;
    TextView textViewUserEmail;
    TextView textViewPostCount;
    LinearLayout postLinearLayout;
    private String userProfileUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page);

        user_pic = (ImageView) findViewById(R.id.user_pic);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();

        usernameInProfile = (TextView) findViewById(R.id.usernameInProfile);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewPostCount = (TextView) findViewById(R.id.textViewPostCount);

        edit_button =(Button)findViewById(R.id.edit_person_btn);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Personal_page.this, AddToDatabase.class);
                startActivity(intent);
            }
        });

        postLinearLayout = (LinearLayout) findViewById(R.id.postLinearLayout);
        postLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Personal_page.this, UsersPostListView.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final String uid = firebaseAuth.getCurrentUser().getUid();
        textViewUserEmail.setText("Email Address: " + firebaseAuth.getCurrentUser().getEmail());

        databaseReference.child("User").child(uid).child("UserName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
                usernameInProfile.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReference.child("User").child(uid).child("ProfileUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileUrl = dataSnapshot.getValue(String.class);
                Picasso.with(getApplicationContext()).load(userProfileUrl).resize(350, 350).into(user_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("User").child(uid).child("postID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long count = dataSnapshot.getChildrenCount();
                textViewPostCount.setText(count.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
