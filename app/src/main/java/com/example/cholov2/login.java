package com.example.cholov2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;

public class login extends AppCompatActivity {
    private Button reg, lg, rlg;
    TextView perStat;
    EditText ephonenum, epassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    //private ListView listView;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reg = findViewById(R.id.btn_reg);
        lg = findViewById(R.id.btn_userlog);
        rlg = findViewById(R.id.btn_riderlog);
        ephonenum = findViewById(R.id.Number);
        epassword = findViewById(R.id.pass);
        rootNode = FirebaseDatabase.getInstance();
        //perStat = findViewById(R.id.perm_stat);
        //listView= findViewById(R.id.list);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        /* ArrayList<String> slist =new ArrayList<>();
        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.list_view,slist);
        listView.setAdapter(adapter);
        */

        rlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_pnum, s_pass;
                s_pnum = ephonenum.getText().toString();
                s_pass = epassword.getText().toString();
                reference = rootNode.getReference("driver").child(s_pnum);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                     /*   slist.clear();
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            regHelper reg =snapshot.getValue(regHelper.class);
                            String txt = reg.getPhonum()+" : "+reg.getName() +" : "+ reg.getPassword();
                            slist.add(txt);
                        }
                        adapter.notifyDataSetChanged();
                    */
                        regHelper data = snapshot.getValue(regHelper.class);

                        assert data != null;
                        if (s_pass.equals(data.getPassword().toString())) {
                            Intent intent = new Intent(login.this, d_map.class);
                            intent.putExtra("num",s_pnum);
                            startActivity(intent);
                            Toast.makeText(login.this, "Login Sucess!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(login.this, "Inncorrect Number/Password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, reg_page.class);

                startActivity(intent);
            }
        });

        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_pnum, s_pass;
                s_pnum = ephonenum.getText().toString();
                s_pass = epassword.getText().toString();
                reference = rootNode.getReference("user").child(s_pnum);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                     /*   slist.clear();
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            regHelper reg =snapshot.getValue(regHelper.class);
                            String txt = reg.getPhonum()+" : "+reg.getName() +" : "+ reg.getPassword();
                            slist.add(txt);
                        }
                        adapter.notifyDataSetChanged();
                    */
                        regHelper data = snapshot.getValue(regHelper.class);

                        assert data != null;
                        if (s_pass.equals(data.getPassword().toString())) {
                            Intent intent = new Intent(login.this, u_map.class);
                            intent.putExtra("num",s_pnum);
                            startActivity(intent);
                            Toast.makeText(login.this, "Sucess!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(login.this, "Inncorrect Number/Password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });

    }

    @Override
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
                   // Toast.makeText(login.this, "lati!"+location.getLatitude(), Toast.LENGTH_LONG).show();
                   // Toast.makeText(login.this, "long !"+location.getLongitude(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}