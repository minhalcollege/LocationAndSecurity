package tomerbu.edu.locationandsecurity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    RadioGroup rgMapType;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =  new SupportMapFragment();//(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1, mapFragment).commit();
        mapFragment.getMapAsync(this);

        rgMapType = findViewById(R.id.rgMapType);
        rgMapType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (map == null){
                    Toast.makeText(MapsActivity.this, "Map Not Ready, Try Again when the map is ready", Toast.LENGTH_SHORT).show();
                    return;
                }//Map Not Ready
                switch (checkedId){
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
        LatLng minhal = new LatLng(31.2515321,34.7992134);

        map.addMarker(new MarkerOptions().position(minhal));

        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //Animate camera:
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(minhal, 13 /*zoom*/));
    }

}
