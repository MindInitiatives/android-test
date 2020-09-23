package ng.riby.androidtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {


    private FusedLocationProviderClient client;
    TextView resultText;
    MainData mainData;
    private Button startBtn, stopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        startBtn = findViewById(R.id.start);
        stopBtn = findViewById(R.id.stop);
        resultText = findViewById(R.id.result);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultText.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.GONE);
                stopBtn.setVisibility(View.VISIBLE);
                getLocation(1);


//                float[] results = new float[1];
//                Location.distanceBetween(latLongA.latitude, latLongA.longitude,
//                        latLongB.latitude, latLongB.longitude,
//                        results);

            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBtn.setVisibility(View.GONE);
                startBtn.setVisibility(View.VISIBLE);
                getLocation(2);
                showDistance();
            }
        });
    }

    private void getLocation(final int uid) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    double latitude = (double) location.getLatitude();

                    double longitude = (double) location.getLongitude();

                    Log.i("Values", String.valueOf(latitude));

//                    uid = 1;
                    //saving the location to DB
                    //Initialize main data
                    final MainData mainData = new MainData();
                    mainData.setID(uid);
                    mainData.setLatitude(latitude);
                    mainData.setLongitude(longitude);
                    RoomDB.getInstance(MainActivity.this).mainDao().insert(mainData);
                    Toast.makeText(MainActivity.this, "mainData" + mainData.getLatitude(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDistance() {

//        RoomDB.getInstance(MainActivity.this).mainDao().getAll().getLatitude();

        Location locationA = new Location("Location A");

        locationA.setLatitude(RoomDB.getInstance(MainActivity.this).mainDao().getPointA(1).getLatitude());

        locationA.setLongitude(RoomDB.getInstance(MainActivity.this).mainDao().getPointB(1).getLongitude());

        Location locationB = new Location("Location B");

        locationB.setLatitude(RoomDB.getInstance(MainActivity.this).mainDao().getPointA(2).getLatitude());

        locationB.setLongitude(RoomDB.getInstance(MainActivity.this).mainDao().getPointA(2).getLatitude());

        resultText = findViewById(R.id.result);

        resultText.setText("Distance : " + new DecimalFormat("##.##").format(locationA.distanceTo(locationB)) + "m");
//        RoomDB.getInstance(MainActivity.this).mainDao().reset(mainData);

    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

}