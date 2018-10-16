package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class post_new extends AppCompatActivity {
    protected static final int POST_NEW = 201;
    protected  static final int MY_PERMISSIONS_REQUEST_READ_PHOTOS=202;
    protected  static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTOS=203;

    EditText editTitle;
    MultiAutoCompleteTextView editItem;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser u;
    private File file;
    public String photoFileName = "";
    String userName;
    ImageView img1;
    Bitmap selectedPic;
    //request codes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_new);
        editTitle = (EditText)findViewById(R.id.post_set_title);
        editItem = (MultiAutoCompleteTextView)findViewById(R.id.post_set_item);
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();


        Button publishBtn = (Button)findViewById(R.id.publish_btn);

        dbRef.child("User").child(u.getUid()).child("UserName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               userName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewPost(editTitle.getText().toString(), editItem.getText().toString(), userName);
                Log.i("sophie",dbRef.child("User").child(u.getUid()).child("userName").toString());
                Intent intent = new Intent(post_new.this, main_activity.class);
                startActivity(intent);
            }
        });

        Button addPic = (Button)findViewById(R.id.add_pic);
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(post_new.this);
                builder.setTitle("Add picture" )
                        .setMessage("Please select one way" )
                        .setPositiveButton("From storage" ,   new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(ContextCompat.checkSelfPermission(post_new.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                                        ==PackageManager.PERMISSION_GRANTED){
                                    Intent intent = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_READ_PHOTOS);
                                }else{
                                    ActivityCompat.requestPermissions(post_new.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},999);
                                }
                            }
                        } )
                        .setNegativeButton("From camera" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(ContextCompat.checkSelfPermission(post_new.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED ||
                                        ContextCompat.checkSelfPermission(post_new.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(post_new.this,
                                            new String[]{Manifest.permission.CAMERA},998);
                                    ActivityCompat.requestPermissions(post_new.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},997);

                                }else{
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                                    Uri file_uri = Uri.parse(file.getAbsolutePath());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);

                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_TAKE_PHOTOS);
                                    }
                                }
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        img1=(ImageView)findViewById(R.id.imageView2);

        if(requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTOS){
            if(resultCode == RESULT_OK){
                Uri pic = data.getData();
                try{
                    selectedPic = MediaStore.Images.Media.getBitmap(this.getContentResolver(),pic);
                    Log.i("sophie",BitmapFactory.decodeFile(file.getAbsolutePath())+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode ==MY_PERMISSIONS_REQUEST_TAKE_PHOTOS) {
            if(resultCode == RESULT_OK){
                selectedPic = BitmapFactory.decodeFile(file.getAbsolutePath());
                Log.i("sophie",BitmapFactory.decodeFile(file.getAbsolutePath())+"");
            }
        }
        img1.setImageBitmap(selectedPic);
        img1.setVisibility(View.VISIBLE);
    }

    private void writeNewPost(String title, String item, String userName){
        String key = dbRef.child("Post").push().getKey();

        Post p = new Post(title,item,userName);
        Map<String,Object> postValue = p.toMap();

        Map<String,Object> childUpdate = new HashMap<>();
        childUpdate.put("/Post/"+key,postValue);
        dbRef.updateChildren(childUpdate);
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Images");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Images", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

}
