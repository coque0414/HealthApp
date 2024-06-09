package com.example.mobile_program;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WalkingRecordDao {
    @Insert
    void insertWalkingRecord(WalkingRecord walkingRecord);

    @Query("SELECT * FROM Walking_Record WHERE id = :id AND datetime = :date")
    WalkingRecord getWalkingRecordByDate(int id, String date);

    @Update
    void updateWalkingRecord(WalkingRecord walkingRecord);

    @Query("UPDATE Walking_Record SET walking = :walking, boxCount = :boxCount WHERE id = :id AND datetime = :date")
    void updateWalkingRecord(int id, String date, int walking, int boxCount);

//    @Query("DELETE FROM Walking_Record WHERE datetime < :date")
//    void deleteOldRecords(String date); // 하루마다 초기화
}
