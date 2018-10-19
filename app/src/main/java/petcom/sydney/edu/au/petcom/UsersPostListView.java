package petcom.sydney.edu.au.petcom;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import petcom.sydney.edu.au.petcom.UserProfiles.userInfoPage;

public class UsersPostListView extends AppCompatActivity {

    ListView listViewUserPosts;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    ArrayList<userInfoPage> userList;
    userInfoPage userPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_post_list_view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        listViewUserPosts = (ListView) findViewById(R.id.listViewUserPosts);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final ArrayList<String> postID = new ArrayList<String>();
        final String uid = firebaseAuth.getCurrentUser().getUid();
        userList = new ArrayList<userInfoPage>();

        databaseReference.child("User").child(uid).child("postID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot shot : dataSnapshot.getChildren()){

                    databaseReference.child("Post").child(shot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot s) {

                            userPost = new userInfoPage();
                            userPost.setTitle(s.child("title").getValue(String.class));
                            Log.i("Edward",s.child("title").getValue(String.class));
                            userList.add(0, userPost);

                            ArrayList<String> myList = new ArrayList<>();
                            for(userInfoPage i: userList){
                                myList.add(0,i.getTitle());
                                Log.i("Edward",i.getTitle()+"");
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,myList);
                            listViewUserPosts.setAdapter(adapter);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

