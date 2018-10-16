package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class post_new extends AppCompatActivity {
    protected static final int POST_NEW = 201;
    protected  static final int MY_PERMISSIONS_REQUEST_READ_PHOTOS=202;
    protected  static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTOS=203;

    EditText editTitle;
    EditText editItem;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser u;
    private File file;
    public String photoFileName = "";

    //request codes
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
                                if((ContextCompat.checkSelfPermission(post_new.this,Manifest.permission.CAMERA)
                                        ==PackageManager.PERMISSION_GRANTED) &&(ContextCompat.checkSelfPermission(post_new.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        ==PackageManager.PERMISSION_GRANTED)){
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                    // set file name
                                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                            Locale.getDefault()).format(new Date());
                                    photoFileName = "IMG_" + timeStamp + ".jpg";

                                    // Create a photo file reference
                                    Uri file_uri = getFileUri(photoFileName,0);

                                    // Add extended data to the intent
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);

                                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                                    // So as long as the result is not null, it's safe to use the intent.
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        // Start the image capture intent to take photo
                                        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_TAKE_PHOTOS);
                                    }
                                }else{
                                    ActivityCompat.requestPermissions(post_new.this,new String[]{Manifest.permission.CAMERA},999);
                                    ActivityCompat.requestPermissions(post_new.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},999);

                                }
                            }
                        });
                //show the button
                builder.create().show();

            }
        });


    }
    public Uri getFileUri(String fileName, int type) {
        Uri fileUri = null;
        try {
            String typestr = "/images/"; //default to images type
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
                        "au.edu.sydney.comp5216.mediaaccess.fileProvider", file);
            } else {
                fileUri = Uri.fromFile(mediaStorageDir);
            }
        } catch (Exception ex) {
            Log.d("getFileUri", ex.getStackTrace().toString());
        }
        return fileUri;
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
        else if (requestCode ==MY_PERMISSIONS_REQUEST_TAKE_PHOTOS)
        {
            if(requestCode == RESULT_OK){
                Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());

                // Load the taken image into a preview
                img1.setImageBitmap(takenImage);
                img1.setVisibility(View.VISIBLE);
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
