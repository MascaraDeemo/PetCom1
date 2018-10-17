package petcom.sydney.edu.au.petcom.UserProfiles;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import petcom.sydney.edu.au.petcom.R;


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
         adapter.addFragment(new RegistrationFragment2(),"userProfile");
         viewPager.setAdapter(adapter);
    }


    public void setViewPager(int fragmentNumber){
         viewPager.setCurrentItem(fragmentNumber);
    }









}
