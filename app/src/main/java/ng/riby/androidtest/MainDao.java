package ng.riby.androidtest;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    //Delete query
    @Delete
    void delete(MainData mainData);

    //Delete all query
    @Delete
    void reset(MainData mainData);
//
//    //update query
//    @Query("UPDATE table_name SET longitude = :longitude  AND Latitude = :latitude  WHERE ID = :sID")
//    void update(int sID, float longitude, float latitude);
//
//    //Get latitude data query
//    @Query("SELECT Latitude = :latitude FROM table_name WHERE ID = :sID")
//    void getLatitude(Float latitude, int sID);
//
//    //Get longitude data query
//    @Query("SELECT longitude = :longitude FROM table_name WHERE ID = :sID")
//    void getLongitude(Float longitude, int sID);

    //Get Point A
    @Query("SELECT * FROM table_name WHERE ID = :sID")
    MainData getPointA(int sID);

    //Get Point B
    @Query("SELECT * FROM table_name WHERE ID = :sID")
    MainData getPointB(int sID);

    //Get all data query
    @Query("SELECT * FROM table_name")
    MainData getAll();
}
