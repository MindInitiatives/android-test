package ng.riby.androidtest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ng.riby.androidtest.interfaces.ConstantInterface;
import ng.riby.androidtest.util.AppUtility;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements ConstantInterface, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {


    private FusedLocationProviderClient client;
    TextView resultText;
    MainData mainData;
    private Button startBtn, stopBtn;
    private GoogleApiClient googleApiClient;
    private AppUtility appUtility;
    private LocationRequest locationRequest;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appUtility = new AppUtility(this);
        requestPermission();
        if (appUtility.checkPlayServices()) {

            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }


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

                checkLocation(googleApiClient, MainActivity.this, 2);
//                getLocation(2);
                showDistance();
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
                    Toast.makeText(MainActivity.this, "mainData" + mainData.getLatitude() + ", " + mainData.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }


    public void checkLocation(GoogleApiClient mGoogleApiClient, final Activity activity, final int uid) {
        // Creating location request object
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
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
                    Toast.makeText(MainActivity.this, "mainData" + mainData.getLatitude() + ", " + mainData.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestPermission();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}