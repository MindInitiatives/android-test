package ng.riby.androidtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class
MainActivity extends AppCompatActivity {

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    private FusedLocationProviderClient client;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        btn = findViewById(R.id.capture);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn.setText("Stop");
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
                            int uid = 001;
                            //saving the location to DB
                            //Initialize main data
                            final MainData mainData = new MainData();
                            mainData.setID(uid);
                            mainData.setText(location.toString());
                            RoomDB.getInstance(MainActivity.this).mainDao().insert(mainData);
                            Toast.makeText(MainActivity.this, "mainData" + mainData, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

}
