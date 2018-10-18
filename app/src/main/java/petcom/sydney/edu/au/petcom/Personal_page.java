package petcom.sydney.edu.au.petcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import petcom.sydney.edu.au.petcom.UserProfiles.AddToDatabase;

public class Personal_page extends AppCompatActivity {
    Button edit_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page);
        edit_button =(Button)findViewById(R.id.edit_person_btn);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Personal_page.this, AddToDatabase.class);
                startActivity(intent);
            }
        });
      ;
    }
}
