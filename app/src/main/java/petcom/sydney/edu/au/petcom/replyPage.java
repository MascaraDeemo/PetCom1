
//package petcom.sydney.edu.au.petcom;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//public class replyPage extends AppCompatActivity {
//    EditText replyMsg;
//    Button cancelBtn;
//    Button replyBtn;
//    private FirebaseAuth uAuth;
//    private
//    @Override
//    protected void onCreate(Bundle savedInstanceBundle){
//        super.onCreate(savedInstanceBundle);
//        setContentView(R.layout.replay_page);
//
//        replyMsg= (EditText)findViewById(R.id.edit_msg);
//        cancelBtn = (Button)findViewById(R.id.reply_cancel_btn);
//        replyBtn = (Button)findViewById(R.id.reply_btn);
//
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        replyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }
//
//}

package petcom.sydney.edu.au.petcom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import petcom.sydney.edu.au.petcom.UserProfiles.User;


public class replyPage extends AppCompatActivity {
    EditText replyMsg;
    Button cancelBtn;
    Button replyBtn;
    private FirebaseAuth uAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private StorageReference storageReference;

    private String uid;
    private User user;
    private String postKey;
    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.replay_page);

        replyMsg= (EditText)findViewById(R.id.edit_msg);
        cancelBtn = (Button)findViewById(R.id.reply_cancel_btn);
        replyBtn = (Button)findViewById(R.id.reply_btn);
        db=FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        uAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        uid = uAuth.getCurrentUser().getUid();
        user = new User();
        dbRef.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.setUid(uid);
                user.setProfileUrl(dataSnapshot.child("ProfileUrl").getValue(String.class));
                user.setUserName(dataSnapshot.child("UserName").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        postKey = getIntent().getStringExtra("postID");
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comment = new Comment();
                comment.setReply(replyMsg.getText().toString());
                comment.setUser(user);

                Map<String,Object> commentValue = comment.toMap();
                String commentkey = dbRef.child("Post").child("postKey").child("comment").push().getKey();
                Map<String,Object> commentUpdate = new HashMap<>();
                commentUpdate.put("/Post/"+postKey+"/comment/"+commentkey,commentValue);
                dbRef.updateChildren(commentUpdate);

            }
        });
    }

}

