package petcom.sydney.edu.au.petcom;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class showMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            // Add a marker in Sydney and move the camera
            String locString = getIntent().getStringExtra("location");
            String[] latlngTemp = locString.split(",");
            LatLng posterLocation = new LatLng(Double.parseDouble(latlngTemp[0]),Double.parseDouble(latlngTemp[1]));

            mMap.addMarker(new MarkerOptions().position(posterLocation).title("Poster is here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(posterLocation));

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posterLocation, 16));
        }else{
            Toast.makeText(this,"Error-Map was null!", Toast.LENGTH_SHORT).show();
        }
    }

}
