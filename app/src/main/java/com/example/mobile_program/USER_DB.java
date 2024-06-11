package com.example.mobile_program;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {USER_ENTITY.class, HealthRecord.class}, version = 9)
public abstract class USER_DB extends RoomDatabase {
    public abstract USER_DAO userDao();
    public abstract HealthRecordDao HealthRecordDao();

    private static volatile USER_DB INSTANCE;

    public static USER_DB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (USER_DB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    USER_DB.class, "USER_DB")
                            .fallbackToDestructiveMigration() // 파괴적 마이그레이션 허용
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
