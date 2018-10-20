package petcom.sydney.edu.au.petcom;

import android.app.Person;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import petcom.sydney.edu.au.petcom.UserProfiles.AddToDatabase;

public class Personal_page extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    FirebaseAuth firebaseAuth;
    ImageView user_pic;
    Button edit_button;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private String userName;
    TextView usernameInProfile;
    TextView textViewUserEmail;
    TextView textViewPostCount;
    LinearLayout postLinearLayout;
    private String userProfileUrl;
    ImageView userBackground;
    File file;
    private Uri imageHoldUri = null;
    private String photoFileName;
    private Uri file_uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page);

        user_pic = (ImageView) findViewById(R.id.user_pic);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //userBackground = (ImageView)findViewById(R.id.user_pic_post);
        usernameInProfile = (TextView) findViewById(R.id.usernameInProfile);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewPostCount = (TextView) findViewById(R.id.textViewPostCount);

//        edit_button =(Button)findViewById(R.id.edit_person_btn);
//        edit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Personal_page.this, AddToDatabase.class);
//                startActivity(intent);
//            }
//        });
//
//        postLinearLayout = (LinearLayout) findViewById(R.id.postLinearLayout);
//        postLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Personal_page.this, UsersPostListView.class);
//                startActivity(intent);
//            }
//        });
//        userBackground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Personal_page.this, )
//                profilePicSelection();
//            }
//        });
//
//    }
//
//    private void saveUserProfile() {
//        btnToFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                final String username;
//
//                username = editUserName.getText().toString();
//
//                if (!TextUtils.isEmpty(username)) {
//
//                    if (imageHoldUri != null) {
//
//                        mProgress.setTitle("Saving Profile");
//                        mProgress.setMessage("Please wait....");
//                        mProgress.show();
//
//                        mChildStorage = mStorageRef.child("User_Profile/" + mAuth.getCurrentUser().getUid() + ".jpg");
//
//                        UploadTask uploadTask = mChildStorage.putFile(imageHoldUri);
//                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                            @Override
//                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                if (!task.isSuccessful()) {
//                                    throw task.getException();
//                                }
//                                return mChildStorage.getDownloadUrl();
//                            }
//                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Uri> task) {
//                                if (task.isSuccessful()) {
//                                    Uri imageUrl = task.getResult();
//                                    String uid = mAuth.getCurrentUser().getUid();
//                                    databaseReference.child("User").child(uid).child("UserName").setValue(username);
//                                    databaseReference.child("User").child(uid).child("ProfileUrl").setValue(imageUrl.toString());
//                                    Intent gotoMainIntent = new Intent(AddToDatabase.this, main_activity.class);
//                                    startActivity(gotoMainIntent);
//
//                                }
//                            }
//                        });
//
//                    } else {
//
//                        Toast.makeText(AddToDatabase.this, "Please select the profile pic", Toast.LENGTH_LONG).show();
//
//                    }
//
//                } else {
//
//                    Toast.makeText(AddToDatabase.this, "Please enter username and status", Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//        });
//    }
//
//    private void profilePicSelection() {
//        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY
//
//        final CharSequence[] items = {"Take Photo", "Choose from Library",
//                "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add Photo!");
//
//        //SET ITEMS AND THERE LISTENERS
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (items[item].equals("Take Photo")) {
//                    cameraIntent();
//                } else if (items[item].equals("Choose from Library")) {
//                    galleryIntent();
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//
//    }
//    private void cameraIntent() {
//
//        //CHOOSE CAMERA
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        photoFileName = "IMG_" + timeStamp + ".jpg";
//        file_uri=getFileUri(photoFileName,0);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
//        startActivityForResult(intent, REQUEST_CAMERA);
//    }
//
//    private void galleryIntent() {
//
//        //CHOOSE IMAGE FROM GALLERY
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, SELECT_FILE);
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        final String uid = firebaseAuth.getCurrentUser().getUid();
//        textViewUserEmail.setText("Email Address: " + firebaseAuth.getCurrentUser().getEmail());
//
//        databaseReference.child("User").child(uid).child("UserName").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userName = dataSnapshot.getValue(String.class);
//                usernameInProfile.setText(userName);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        databaseReference.child("User").child(uid).child("ProfileUrl").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userProfileUrl = dataSnapshot.getValue(String.class);
//                Picasso.with(getApplicationContext()).load(userProfileUrl).resize(350, 350).into(user_pic);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        databaseReference.child("User").child(uid).child("postID").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Long count = dataSnapshot.getChildrenCount();
//                textViewPostCount.setText(count.toString());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        //SAVE URI FROM GALLERY
//        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
//        {
//            Uri imageUri = data.getData();
//
//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .setMultiTouchEnabled(true)
//                    .start(this);
//
//        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
//            //SAVE URI FROM CAMERA
//
//
//            if(file_uri !=null) {
//
//                CropImage.activity(file_uri)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1, 1)
//                        .setMultiTouchEnabled(true)
//                        .start(this);
//            }
//
//        }
//
//
//        //image crop library code
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                imageHoldUri = result.getUri();
//
//                userBackground.setImageURI(imageHoldUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//
//    }
//
//
//    public Uri getFileUri(String fileName, int type) {
//        Uri fileUri = null;
//        try {
//            String typestr = "/images/"; //default to images type
//            if (type == 1) {
//                typestr = "/videos/";
//            } else if (type != 0) {
//                typestr = "/audios/";
//            }
//
//            // Get safe storage directory depending on type
//            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
//                    typestr+fileName);
//
//            // Create the storage directory if it does not exist
//            if (!mediaStorageDir.getParentFile().exists() && !mediaStorageDir.getParentFile().mkdirs()) {
//                Log.d("sophie", "failed to create directory");
//            }
//
//            // Create the file target for the media based on filename
//            file = new File(mediaStorageDir.getParentFile().getPath() + File.separator + fileName);
//
//            // Wrap File object into a content provider, required for API >= 24
//            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//            if (Build.VERSION.SDK_INT >= 24) {
//                fileUri = FileProvider.getUriForFile(
//                        this.getApplicationContext(),
//                        "petcom.sydney.edu.au.petcom.fileProvider", file);
//            } else {
//                fileUri = Uri.fromFile(mediaStorageDir);
//            }
//        } catch (Exception ex) {
//            Log.d("getFileUri", ex.getStackTrace().toString());
//        }
//        return fileUri;
    }

}
