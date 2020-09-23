package ng.riby.androidtest;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;


//Defining table name
@Entity(tableName = "table_name")
public class MainData implements Serializable {

    //Creating id column
    @PrimaryKey(autoGenerate = true)
    private int ID;

    //Creating text column
    @ColumnInfo(name = "longitude")
    private double longitude;

    //Creating text column
    @ColumnInfo(name = "latitude")
    private double latitude;

    //Generating getter and setter


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
