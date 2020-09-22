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
    @ColumnInfo(name = "text")
    private String text;

    //Generating getter and setter


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
