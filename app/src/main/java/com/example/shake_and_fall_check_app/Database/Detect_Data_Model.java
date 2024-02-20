package com.example.shake_and_fall_check_app.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "detection_data_table")
public class Detect_Data_Model implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String detectTitle;
    @ColumnInfo(name = "acceleration")
    private float acceleration;
    @ColumnInfo(name = "time")
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetectTitle() {
        return detectTitle;
    }

    public void setDetectTitle(String detectTitle) {
        this.detectTitle = detectTitle;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
}
