package com.example.shake_and_fall_check_app.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
@Database(entities = {Detect_Data_Model.class}, version = 1,exportSchema = false)
public abstract class Detect_Database extends RoomDatabase {

        private static Detect_Database database;
        public static String DATABASE_NAME = "Fall_Shake_Detection_Database";

        public synchronized static Detect_Database getInstance(Context context){
            if (database == null){
                database = Room.databaseBuilder(context.getApplicationContext(),
                                Detect_Database.class, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return database;
        }

        public abstract MainDAO mainDao();
    }
