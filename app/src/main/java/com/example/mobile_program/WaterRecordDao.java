package com.example.mobile_program;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WaterRecordDao {
    @Insert
    void insertWaterRecord(WaterRecord waterRecord);

    @Query("SELECT * FROM WaterRecord WHERE id = :id AND date = :date")
    WaterRecord getWaterRecordByDate(int uid, String date);

    @Query("UPDATE WaterRecord SET waterCount = :waterCount WHERE id = :id AND date = :date")
    void updateWaterCount(int uid, String date, int waterCount);

    @Query("DELETE FROM WaterRecord WHERE date < :date")
    void deleteOldRecords(String date); // 하루마다 초기화
}