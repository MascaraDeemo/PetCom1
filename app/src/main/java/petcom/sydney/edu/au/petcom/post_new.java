package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class post_new extends AppCompatActivity {
    protected static final int POST_NEW = 201;
    protected  static final int MY_PERMISSIONS_REQUEST_READ_PHOTOS=202;

    EditText editTitle;
    EditText editItem;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_new);
        editTitle = (EditText)findViewById(R.id.post_set_title);
        editItem = (EditText)findViewById(R.id.post_set_item);
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();

        Button publishBtn = (Button)findViewById(R.id.publish_btn);

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewPost(editTitle.getText().toString(), editItem.getText().toString(), u.getUid());
                Log.i("sophie",dbRef.child("User").child(u.getUid()).child("userName").toString());
                Intent intent = new Intent(post_new.this, main_activity.class);
                startActivity(intent);
            }
        });

        Button addPic = (Button)findViewById(R.id.add_pic);
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(post_new.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    ==PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_READ_PHOTOS);
                }else{
                    ActivityCompat.requestPermissions(post_new.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},999);
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        ImageView img1=(ImageView)findViewById(R.id.imageView2);

        if(requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTOS){
            if(resultCode == RESULT_OK){
                Uri pic = data.getData();
                Bitmap selectedPic;
                try{
                    selectedPic = MediaStore.Images.Media.getBitmap(this.getContentResolver(),pic);
                    img1.setImageBitmap(selectedPic);
                    img1.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeNewPost(String title, String item, String userName){
        String key = dbRef.child("Post").push().getKey();

        Post p = new Post(title,item,userName);
        Map<String,Object> postValue = p.toMap();

        Map<String,Object> childUpdate = new HashMap<>();
        childUpdate.put("/Post/"+key,postValue);
        dbRef.updateChildren(childUpdate);
    }
}
