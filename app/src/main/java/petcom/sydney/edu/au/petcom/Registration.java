package petcom.sydney.edu.au.petcom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Registration extends AppCompatActivity {
    private static final String TAG = "Registration";

    private SectionsStatePagerAdapter sectionsStatePagerAdapter;
    private ViewPager viewPager;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);



        setupViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();

    }

    private void setupViewPager(ViewPager viewPager){
         SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
         adapter.addFragment(new RegistrationFragment1(),"EmailPassword");
         adapter.addFragment(new RegistrationFragment2(),"UserProfile");
         viewPager.setAdapter(adapter);
    }


    public void setViewPager(int fragmentNumber){
         viewPager.setCurrentItem(fragmentNumber);
    }









}
