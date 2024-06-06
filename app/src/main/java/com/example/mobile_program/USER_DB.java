package com.example.mobile_program;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {USER_ENTITY.class, WalkingRecord.class}, version = 1)
public abstract class USER_DB extends RoomDatabase {
    public abstract USER_DAO userDao();
    public abstract WalkingRecordDao walkingRecordDao();

    private static volatile USER_DB INSTANCE;

    public static USER_DB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (USER_DB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    USER_DB.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
