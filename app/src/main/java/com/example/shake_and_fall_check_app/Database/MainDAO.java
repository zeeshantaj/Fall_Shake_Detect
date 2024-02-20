package com.example.shake_and_fall_check_app.Database;

import static androidx.room.OnConflictStrategy.REPLACE;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insert(Detect_Data_Model model);

    @Query("SELECT * FROM detection_data_table ORDER BY id DESC")
    List<Detect_Data_Model> getALL();
//    @Query("UPDATE alarm_data SET enabled = :enabled WHERE ID = :id")
//    void isEnabled(int id,boolean enabled);


    @Delete
    void delete(Detect_Data_Model model);
}
