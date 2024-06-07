package com.example.mobile_program;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WalkingRecordDao {
    @Insert
    void insertWalkingRecord(WalkingRecord walkingRecord);

    @Query("SELECT * FROM Walking_Record WHERE id = :id AND datetime = :date")
    WalkingRecord getWalkingRecordByDate(int id, String date);
}
