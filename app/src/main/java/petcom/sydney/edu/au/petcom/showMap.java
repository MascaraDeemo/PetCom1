package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class showMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseAuth dbAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        String id = getIntent().getStringExtra("postID");
        db = FirebaseDatabase.getInstance();
        dbRef=db.getReference();
        dbAuth = FirebaseAuth.getInstance();
        String userid = dbAuth.getUid();
        dbRef.child("Post").child(id).child("responder").child(userid).setValue(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            // Add a marker in Sydney and move the camera
            String locString = getIntent().getStringExtra("location");
            String[] latlngTemp = locString.split(",");
            LatLng posterLocation = new LatLng(Double.parseDouble(latlngTemp[0]), Double.parseDouble(latlngTemp[1]));

            mMap.addMarker(new MarkerOptions().position(posterLocation).title("Poster is here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(posterLocation));

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posterLocation, 16));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            mMap.setMyLocationEnabled(true);

        }else{
            Toast.makeText(this,"Error-Map was null!", Toast.LENGTH_SHORT).show();
        }
    }

}
