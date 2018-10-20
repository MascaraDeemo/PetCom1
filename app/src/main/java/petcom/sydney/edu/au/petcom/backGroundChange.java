package petcom.sydney.edu.au.petcom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class backGroundChange extends AppCompatActivity {

    TextView takePhoto;
    TextView fromGallery;
    Button btnSave;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    private Uri imageHoldUri = null;
    ProgressDialog mProgress;
    private File file;
    private String photoFileName;
    private Uri file_uri;

    private EditText editUserName;
    private LinearLayout btnToFinish;
    private ImageView userBackgroundView;
    private StorageReference mChildStorage;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_ground_change);
        takePhoto = (TextView) findViewById(R.id.textViewChangeByTakingPhoto);
        fromGallery = (TextView) findViewById(R.id.textViewFromGallery);
        btnSave = (Button) findViewById(R.id.btnSaveBackground);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userBackgroundView = (ImageView) findViewById(R.id.userBackgroundView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        fromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });

        saveUserProfile();

    }


    private void saveUserProfile() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (imageHoldUri != null) {

                        mProgress.setTitle("Saving Background");
                        mProgress.setMessage("Please wait....");
                        mProgress.show();

                        mChildStorage = mStorageRef.child("User_Profile_BackGround/" + mAuth.getCurrentUser().getUid() + ".jpg");

                        UploadTask uploadTask = mChildStorage.putFile(imageHoldUri);
                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return mChildStorage.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri imageUrl = task.getResult();
                                    String uid = mAuth.getCurrentUser().getUid();
                                    databaseReference.child("User").child(uid).child("BackgroundUri").setValue(imageUrl.toString());
                                    Intent gotoMainIntent = new Intent(backGroundChange.this, main_activity.class);
                                    startActivity(gotoMainIntent);

                                }
                            }
                        });

                    } else {

                        Toast.makeText(backGroundChange.this, "Please select the Background pic", Toast.LENGTH_LONG).show();

                    }

                }


        });
    }


    private void cameraIntent() {

        //CHOOSE CAMERA
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        photoFileName = "IMG_" + timeStamp + ".jpg";
        file_uri=getFileUri(photoFileName,0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
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
                Log.d("sophie", "failed to create directory");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setMultiTouchEnabled(true)
                    .start(this);

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA


            if(file_uri !=null) {

                CropImage.activity(file_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMultiTouchEnabled(true)
                        .start(this);
            }

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                userBackgroundView.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


}
