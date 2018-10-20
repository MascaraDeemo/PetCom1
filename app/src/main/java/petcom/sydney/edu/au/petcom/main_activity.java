package petcom.sydney.edu.au.petcom;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.shape.RoundedCornerTreatment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Transformation;

//import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import petcom.sydney.edu.au.petcom.UserProfiles.LoginActivity;
import petcom.sydney.edu.au.petcom.UserProfiles.MainActivity;
import petcom.sydney.edu.au.petcom.UserProfiles.User;

public class main_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseAuth dbAuth;

    private StorageReference mStorageRef;

    private Post p;
    private ListView listView;
    private PostAdapter postAdapter;
    private TextView textViewUserHead;
    private ValueEventListener postListener;

    private String userProfileUrl;
    private ImageView imageViewforUserPic;

    private String userName;

    private ArrayList<Post> pList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        dbAuth = FirebaseAuth.getInstance();
        listView = (ListView)findViewById(R.id.list_view_main);
        pList=new ArrayList<Post>();
        db=FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        updateListView();
        dbRef.orderByKey().addListenerForSingleValueEvent(postListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String uid = dbAuth.getCurrentUser().getUid();
        dbRef.child("User").child(uid).child("UserName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
                textViewUserHead = (TextView) findViewById(R.id.username_head);
                textViewUserHead.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        dbRef.child("User").child(uid).child("ProfileUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileUrl = dataSnapshot.getValue(String.class);
                imageViewforUserPic = (ImageView) findViewById(R.id.imageViewForUserProfilePic);
                Picasso.with(getApplicationContext()).load(userProfileUrl).resize(250,250).transform(new CircleTransform()).into(imageViewforUserPic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.calendar) {
            Intent intent = new Intent(main_activity.this, post_new.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_moment) {
            // Handle the camera action
        } else if (id == R.id.nav_personal) {


        } else if (id == R.id.nav_post) {

        } else if (id == R.id.nav_favorites) {
            Intent infoIntent = new Intent(main_activity.this, login_test.class);
            startActivity(infoIntent);

        } else if (id == R.id.nav_outdoor) {

        }else if (id== R.id.nav_editinfo){


            Intent infoIntent = new Intent(main_activity.this, Personal_page.class);


            startActivity(infoIntent);

        }else if (id== R.id.nav_signout){
            FirebaseAuth.getInstance().signOut();
            Intent singinIntent = new Intent(main_activity.this, LoginActivity.class);
            startActivity(singinIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void updateListView(){
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pList.clear();
                for (DataSnapshot shot : dataSnapshot.child("Post").getChildren()) {
                    p = new Post();
                    String postID = shot.getKey();
                    p.setPostID(postID);
                    p.setLocationByString(shot.child("location").getValue(String.class));
                    p.setUser(shot.getValue(Post.class).getUser());
                    p.setTitle(shot.getValue(Post.class).getTitle());
                    p.setInput(shot.getValue(Post.class).getInput());
                    p.setPicture(shot.getValue(Post.class).getPicture());
                    p.setHasPicture(shot.getValue(Post.class).getHasPicture());
                    p.setDuration(shot.getValue(Post.class).getDuration());
                    pList.add(0,p);
                }
                postAdapter = new PostAdapter(main_activity.this, R.layout.post_layout, pList);
                listView.setAdapter(postAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("yao","didnotwork"+databaseError.toException());
            }
        };
    }



}
