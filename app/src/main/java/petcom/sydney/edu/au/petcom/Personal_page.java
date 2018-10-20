package petcom.sydney.edu.au.petcom;

import android.app.Person;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import petcom.sydney.edu.au.petcom.UserProfiles.AddToDatabase;

public class Personal_page extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
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
    private String userBackgroundUrl;
    ImageView userBackground;
    File file;
    private Uri imageHoldUri = null;
    private String photoFileName;
    private Uri file_uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page);

        user_pic = (ImageView) findViewById(R.id.user_pic);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();

        userBackground = (ImageView)findViewById(R.id.imageViewBackGround);
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
        userBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Personal_page.this);
                builder.setTitle("Change Background").setMessage("Do you wish to change you background picture?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Personal_page.this, backGroundChange.class );
                        startActivity(intent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
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
        databaseReference.child("User").child(uid).child("BackgroundUri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userBackgroundUrl = dataSnapshot.getValue(String.class);
                Picasso.with(getApplicationContext()).load(userBackgroundUrl).resize(350, 325).into(userBackground);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
