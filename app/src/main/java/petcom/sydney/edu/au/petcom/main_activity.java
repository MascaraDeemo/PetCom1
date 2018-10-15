package petcom.sydney.edu.au.petcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

//import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class main_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //FirebaseDatabase FBDB;
    ListView list;
    PostAdapter postAdapter;
    Post testObj;
    ArrayList<Post> p;

    private static final int REQUEST_NEW_POST = 101;

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

       // FBDB = FirebaseDatabase.getInstance();
        //
//        testObj = new Post();
//        testObj.setUserName("董天志");
//        testObj.setTitle("我是傻逼吗？");
//        testObj.setInput("of course!");
//        testObj.setComment(0);
//        testObj.setLike(3);
        //
        p=new ArrayList<Post>();
        p.add(testObj);
        list = (ListView)findViewById(R.id.list_view);
        postAdapter=new PostAdapter(this,R.layout.post_layout,p);
//        postAdapter.add(testObj);

        list.setAdapter(postAdapter);
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

        } else if (id == R.id.nav_film) {

        } else if (id == R.id.nav_post) {

        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_outdoor) {

        }else if (id== R.id.nav_editinfo){

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
                startActivityForResult(intent,REQUEST_NEW_POST);
            }
        });
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data){
        if(request_code == REQUEST_NEW_POST){
            if(result_code == post_new.POST_NEW){
                //操作传回来的新加的post title和item，存到数据库等等
            }
        }
    }

}
