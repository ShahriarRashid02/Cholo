package com.example.cholov2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class d_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private Marker strt,des;
    LatLng strm,desm;
    FusedLocationProviderClient fusedLocationProviderClient;
    double xx1, yy1;
    double xx2, yy2;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                ///take number from extar

                Intent i = getIntent();
                String pho_num =i.getStringExtra("num");
                double x=location.getLatitude();
                double y=location.getLongitude();
                Toast.makeText(d_map.this, pho_num, Toast.LENGTH_LONG).show();


                LatLng loc = new LatLng(x, y);
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("activedriver");

                //Toast.makeText(d_map.this, reference.toString(), Toast.LENGTH_LONG).show();

                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in Sydney").icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_local_taxi_24)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,14),3000,null);
                GeoFire geoFire = new GeoFire(reference);
                geoFire.setLocation(pho_num,new GeoLocation(location.getLatitude(),location.getLongitude()));
                reference=rootNode.getReference("Request");
               reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*        Iterator<DataSnapshot> itm= snapshot.getChildren().iterator();
                while (itm.hasNext()){
                    DataSnapshot data=itm.next();
                    if(data.child("dum").toString()==pho_num){
                        xx1=Double.parseDouble(data.child("strt").child("latitude").toString());
                        yy1=Double.parseDouble(data.child("strt").child("longitude").toString());
                        xx2=Double.parseDouble(data.child("des").child("latitude").toString());
                        yy2=Double.parseDouble(data.child("des").child("longitude").toString());
Toast.makeText(d_map.this, "Xx1", Toast.LENGTH_LONG).show();

                    }
                }*/
                           // request_handle data = snapshot.getValue(request_handle.class);
                            //Toast.makeText(d_map.this, data.getCnum().toString(), Toast.LENGTH_LONG).show();


                       // Toast.makeText(d_map.this, data.getCnum().toString(), Toast.LENGTH_LONG).show();
                       // Toast.makeText(d_map.this, data.getDloc().toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
              // txt= findViewById(R.id.Price);
             //  txt.setText("Price : 120 tk");

                LatLng start = new LatLng(22.394264, 91.868446);
                LatLng dest = new LatLng(22.36792, 91.843530);
             strt = mMap.addMarker(new MarkerOptions().position(start).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_face_24)));
                des = mMap.addMarker(new MarkerOptions().position(dest).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_push_pin_24)));

            }
        });

    }

    @Override
    protected void onStop() {

        super.onStop();
      // remove if log out
       // Location location = null;
        //Intent i = getIntent();
        //String pho_num =i.getStringExtra("num");
        //rootNode=FirebaseDatabase.getInstance();
        //reference=rootNode.getReference("activedriver");
        //GeoFire geoFire = new GeoFire(reference);
       //geoFire.removeLocation(pho_num);

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}