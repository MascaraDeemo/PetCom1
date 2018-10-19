package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import petcom.sydney.edu.au.petcom.UserProfiles.User;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class post_new extends AppCompatActivity {
    protected  static final int MY_PERMISSIONS_REQUEST_READ_PHOTOS=202;
    protected  static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTOS=203;
    private MarshmallowPermission permission;

    EditText editTitle;
    MultiAutoCompleteTextView editItem;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser u;
    private StorageReference mStorageRef;
    private StorageReference picRef;
    private File file;
    Uri file_uri;
    String userName;
    ImageView img1;
    Bitmap selectedPic;
    String photoFileName;
    String key;
    User user;
    Post p;
    StopWatch stopWatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_new);
        editTitle = (EditText) findViewById(R.id.post_set_title);
        editItem = (MultiAutoCompleteTextView) findViewById(R.id.post_set_item);
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();
        permission = new MarshmallowPermission(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        p = new Post();

        Button publishBtn = (Button) findViewById(R.id.publish_btn);
        dbRef.child("User").child(u.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               user=new User();
               user.setUserName(dataSnapshot.child("UserName").getValue(String.class));
               user.setProfileUrl(dataSnapshot.child("ProfileUrl").getValue(String.class));
               user.setUid(u.getUid());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        RadioGroup btngroup = (RadioGroup)findViewById(R.id.select_time);
        btngroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            RadioButton btn5 = (RadioButton)findViewById(R.id.five);
            RadioButton btn10 = (RadioButton)findViewById(R.id.ten);
            RadioButton btn30 = (RadioButton)findViewById(R.id.thirty);
            RadioButton btn1 = (RadioButton)findViewById(R.id.one);
            RadioButton btn2 = (RadioButton)findViewById(R.id.two);
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                long addedTime = 0;
                if(btn5.getId() == checkedId){
                    addedTime = 5;
                }else if(btn10.getId() == checkedId){
                    addedTime = 10;
                }else if(btn30.getId() == checkedId){
                    addedTime = 30;
                }else if (btn1.getId() == checkedId){
                    addedTime = 60;
                }else if(btn2.getId() == checkedId){
                    addedTime = 120;
                }
                try {
                    p.setEnddate(addedTime*1000*60);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewPost();
            }
        });

        Button addPic = (Button) findViewById(R.id.add_pic);
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(post_new.this);
                builder.setTitle("Add picture")
                        .setMessage("Please select")
                        .setPositiveButton("From storage", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!permission.checkPermissionForReadfiles()) {
                                    permission.requestPermissionForReadfiles();
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_READ_PHOTOS);
                                }
                            }
                        })
                        .setNegativeButton("From camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!permission.checkPermissionForCamera() || !permission.checkPermissionForExternalStorage()) {
                                    permission.requestPermissionForCamera();
                                } else {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                            Locale.getDefault()).format(new Date());
                                    photoFileName = "IMG_" + timeStamp + ".jpg";
                                    file_uri=getFileUri(photoFileName,0);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
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
        img1=(ImageView)findViewById(R.id.post_new_add_img);

        if(requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTOS){
            if(resultCode == RESULT_OK){
                file_uri = data.getData();
                try{
                    selectedPic = MediaStore.Images.Media.getBitmap(this.getContentResolver(),file_uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == MY_PERMISSIONS_REQUEST_TAKE_PHOTOS) {
            if(resultCode == RESULT_OK){
                selectedPic = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }
        img1.setDrawingCacheEnabled(true);
        img1.buildDrawingCache();
        img1.setImageBitmap(selectedPic);
        img1.setVisibility(View.VISIBLE);
    }

    private void writeNewPost(){
        key = dbRef.child("Post").push().getKey();

        if(file_uri!=null) {

            picRef = mStorageRef.child("image/"+key +".jpg");
            UploadTask uploadTask = picRef.putFile(file_uri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return picRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri pUri = task.getResult();
                        p.setTitle(editTitle.getText().toString());
                        p.setInput(editItem.getText().toString());
                        p.setPicture(pUri.toString());
                        p.setUser(user);
                        p.setHasPicture(true);
                        p.setPostID(key);
                        Map<String,Object> postValue = p.toMap();
                        Map<String,Object> childUpdate = new HashMap<>();
                        childUpdate.put("/Post/"+key,postValue);

                        Map<String,Object> userValue = user.toMap();
                        Map<String,Object> userUpdate = new HashMap<>();
                        userUpdate.put("/Post/"+key+"/"+u.getUid()+"/",userValue);

                        dbRef.updateChildren(userUpdate);

                        dbRef.child("User").child(u.getUid()).child("postID").child(key).setValue(true);
                        dbRef.updateChildren(childUpdate);
                        Intent intent = new Intent(post_new.this, main_activity.class);
                        startActivity(intent);
                    } else {
                    }
                }
            });
        }else{
            p.setTitle(editTitle.getText().toString());
            p.setInput(editItem.getText().toString());
            p.setUser(user);
            p.setHasPicture(false);
            p.setPostID(key);
            Map<String,Object> postValue = p.toMap();
            Map<String,Object> userValue = user.toMap();

            Log.i("yaoxy007",user.getUserName()+"  "+user.getProfileUrl());
            Map<String,Object> userUpdate = new HashMap<>();
            Map<String,Object> childUpdate = new HashMap<>();

            childUpdate.put("/Post/"+key,postValue);
            userUpdate.put("/Post/"+key+"/user/",userValue);
            dbRef.updateChildren(childUpdate);
            dbRef.updateChildren(userUpdate);
            dbRef.child("User").child(u.getUid()).child("postID").child(key).setValue(true);
            Intent intent = new Intent(post_new.this, main_activity.class);
            startActivity(intent);
        }

    }

    public Uri getFileUri(String fileName, int type) {
        Uri fileUri = null;
        try {
            String typestr = "/images/"; //default to images type
            if (type == 1) {
                typestr = "/videos/";
            } else if (type != 0) {
                typestr = "/audios/";
            }

            // Get safe storage directory depending on type
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    typestr+fileName);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.getParentFile().exists() && !mediaStorageDir.getParentFile().mkdirs()) {

            }

            // Create the file target for the media based on filename
            file = new File(mediaStorageDir.getParentFile().getPath() + File.separator + fileName);

            // Wrap File object into a content provider, required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(
                        this.getApplicationContext(),
                        "petcom.sydney.edu.au.petcom.fileProvider", file);
            } else {
                fileUri = Uri.fromFile(mediaStorageDir);
            }
        } catch (Exception ex) {
            Log.d("getFileUri", ex.getStackTrace().toString());
        }
        return fileUri;
    }
}
