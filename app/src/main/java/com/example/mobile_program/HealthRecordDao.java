package com.example.mobile_program;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HealthRecordDao {
    @Insert
    void insertWalkingRecord(HealthRecord record);

    @Query("SELECT * FROM HealthRecord WHERE id = :id AND datetime = :date")
    HealthRecord getHealthRecordByDate(String id, String date);

    @Update
    void updateHealthRecord(HealthRecord healthRecord);

    @Query("UPDATE HealthRecord SET walking = :walking, boxCount = :boxCount WHERE id = :id AND datetime = :date")
    void updateHealthRecord(String id, String date, int walking, int boxCount);

    @Insert
    void insertHealthRecord(HealthRecord healthRecord);

    @Update
    void updateHealthRecord_water(HealthRecord healthRecord);

//    @Query("DELETE FROM Walking_Record WHERE datetime < :date")
//    void deleteOldRecords(String date); // 하루마다 초기화
}
