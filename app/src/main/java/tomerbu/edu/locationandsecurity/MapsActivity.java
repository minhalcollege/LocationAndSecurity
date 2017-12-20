package tomerbu.edu.locationandsecurity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Declare a field for the api client:
    //fine and coarse (*fused)
    private FusedLocationProviderClient mClient;
    private static final int RC_ACCESS_FINE_LOCATION = 2;
    private RadioGroup rgMapType;
    private TextView tvLocation;
    private GoogleMap map;

    //method that requires permission:
    private void showLocation() {
        //if no permission-> request it and return
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request the permission:
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RC_ACCESS_FINE_LOCATION);
            return;
        }
        //TODO: Use the Permission
        mClient = LocationServices.getFusedLocationProviderClient(this);

        //1) get the last known location:
        Task<Location> lastLocation = mClient.getLastLocation();


        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //ALWAYS Consider the nullability:
                if (location != null)
                    tvLocation.setText(String.format("(%s , %s)", location.getLatitude(), location.getLongitude()));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //we have permission: call the showUserLocation method again
            showLocation();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //findViews:
        tvLocation = findViewById(R.id.tvLocation);
        rgMapType = findViewById(R.id.rgMapType);

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocation();
            }
        });
        SupportMapFragment mapFragment = new SupportMapFragment();//(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1, mapFragment).commit();
        mapFragment.getMapAsync(this);
        rgMapType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (map == null) {
                    Toast.makeText(MapsActivity.this, "Map Not Ready, Try Again when the map is ready", Toast.LENGTH_SHORT).show();
                    return;
                }//Map Not Ready
                switch (checkedId) {
                    case R.id.radioSatalite:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.radioHybrid:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case R.id.radioNormal:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng minhal = new LatLng(31.2515321, 34.7992134);

        map.addMarker(new MarkerOptions().position(minhal));

        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //Animate camera:
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(minhal, 13 /*zoom*/));
    }
}
