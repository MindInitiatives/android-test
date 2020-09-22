package ng.riby.androidtest;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

//Adding database entities
@Database(entities = {MainData.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    //create database instance
    private static RoomDB database;

    //define database name
    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context) {

        //Check condition
        if (database == null) {

            //When database is null
            //Initialize database
            database = Room.databaseBuilder(context.getApplicationContext()
                    , RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        //Return database
        return database;
    }

    //Create Dao
    public abstract MainDao mainDao();
}
