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
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
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

import java.util.ArrayList;

public class u_map extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapClickListener {

    private GoogleMap mMap,dMap;

    private Marker desmark;
    FusedLocationProviderClient fusedLocationProviderClient;
    double x, y;
    double a, b;
    double mx=0;
    private int radious =1;
    private  Boolean dfound=false;
    private String Did;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private DatabaseReference reference3;
    double rating;
    int flg=1;
    LatLng dloc;
    LatLng myloc;
    LatLng desloc;
    private Marker strt,des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_map);
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
        dMap = googleMap;
        rootNode = FirebaseDatabase.getInstance();
        reference=rootNode.getReference("activedriver");


        // Add a marker in Sydney and move the camera
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
                x= (double)location.getLatitude();
                y= (double)location.getLongitude();
                myloc = new LatLng(x, y);
                mMap.addMarker(new MarkerOptions().position(myloc).title("I am here !").icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_face_24)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myloc,14),3000,null);


                GetDriver();
                // show all driver

                mMap.setOnMapClickListener(this::onMapClick);

            }

            private void onMapClick(LatLng latLng) {
                if(desmark!=null){
                    mMap.clear();
                }
                desloc=latLng;
                desmark = mMap.addMarker(new MarkerOptions().position(latLng));
               // Toast.makeText(u_map.this, "lati!"+latLng.latitude, Toast.LENGTH_LONG).show();
            }
        });
        Button d_searchbtn;

        d_searchbtn=findViewById(R.id.d_search);

        d_searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dfound==false){
                    Toast.makeText(u_map.this, "No Driver Available!", Toast.LENGTH_LONG).show();
                    return;

                }

                double des =GetDistance();
                double pr= des*20;
                String price = Double.toString(pr);
                //   String str =  new Double(des).toString();
              //Toast.makeText(u_map.this, str, Toast.LENGTH_LONG).show();
                Intent i = getIntent();
                String Cnum =i.getStringExtra("num");
                String drnum= "33333333333";

              //  Toast.makeText(u_map.this, drnum, Toast.LENGTH_LONG).show();
                dloc= new LatLng(a,b);


                request_handle sender= new request_handle(Cnum,price,desloc, dloc, drnum,myloc);
               // Toast.makeText(u_map.this, drnum, Toast.LENGTH_LONG).show();
                //sender.setDnum(drnum);
                //sender.setDloc(dloc);
                reference2 =FirebaseDatabase.getInstance().getReference("Request");
                reference2.child(Cnum).setValue(sender);
                //Intent intent = new Intent(u_map.this, set_map.class);
                //intent.putExtra("unum",Cnum);
                //startActivity(intent);
               strt=mMap.addMarker(new MarkerOptions().position(dloc).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_local_taxi_24)));


            }
        });



    }

    private double GetDistance() {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(myloc.latitude-desloc.latitude);
        double dLng = Math.toRadians(myloc.longitude-desloc.longitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(desloc.latitude)) * Math.cos(Math.toRadians(myloc.latitude)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return  dist;
    }

    private void GetDriver() {
        LatLng mlc = new LatLng(x, y);
        GeoFire geoFire = new GeoFire(reference);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mlc.latitude,mlc.longitude),radious);

       geoQuery.removeAllListeners();

        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                if(!dfound){
                    dfound=true;
                 //  Did= dataSnapshot.getKey().toString();
                    dloc= new LatLng(location.latitude,location.longitude);
                    a=location.latitude;
                    b=location.longitude;
                //    Toast.makeText(u_map.this,"Dist"+ Did, Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!dfound){

                    radious++;
                    if(radious>5){
                        flg=0;
                    }
                    GetDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }

    private void getLastLocation() {
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
                if(location!=null){
                    x=(double) location.getLatitude();
                    y=(double) location.getLongitude();
                   // Toast.makeText(login.this, "lati!"+location.getLatitude(), Toast.LENGTH_LONG).show();
                  //  Toast.makeText(login.this, "long !"+location.getLongitude(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(desmark!=null){
            mMap.clear();
        }
        desmark = mMap.addMarker(new MarkerOptions().position(latLng));
      //  Toast.makeText(u_map.this, "lati!"+latLng.latitude, Toast.LENGTH_LONG).show();
    }
}