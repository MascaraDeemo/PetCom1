package petcom.sydney.edu.au.petcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;

//import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import petcom.sydney.edu.au.petcom.UserProfiles.LoginActivity;

public class main_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseAuth dbAuth;
    FirebaseAuth.AuthStateListener dbListener;

    ListView listView;
    PostAdapter postAdapter;

    ArrayList<Post> pList;
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

        listView = (ListView)findViewById(R.id.list_view_main);
        pList=new ArrayList<Post>();
        db=FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pList.clear();
                for(DataSnapshot shot : dataSnapshot.child("Post").getChildren()){
                    Post p = new Post();
                    p.setUserName(shot.getValue(Post.class).getUserName());
                    p.setTitle(shot.getValue(Post.class).getTitle());
                    p.setInput(shot.getValue(Post.class).getInput());
                    pList.add(p);
                    Log.i("yaoxy",pList.size()+"");
                }
                Collections.reverse(pList);
                postAdapter=new PostAdapter(main_activity.this,R.layout.post_layout_old,pList);
                listView.setAdapter(postAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("yao","didnotwork"+databaseError.toException());
            }
        };
        dbRef.orderByKey().addListenerForSingleValueEvent(postListener);

//        Post testObj1 = new Post("a","b","c");
//        Post testObj2 = new Post("a","b","c");
//        pList.add(testObj1);
//        pList.add(testObj2);
        post_new();
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
        if (id == R.id.action_settings) {
            return true;
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

    private void post_new(){
        FloatingActionButton postNew = (FloatingActionButton)findViewById(R.id.fab);
        postNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_activity.this, post_new.class);
                startActivity(intent);
            }
        });
    }

}
