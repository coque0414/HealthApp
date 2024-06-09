package com.example.mobile_program;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RewardDao {
    @Insert
    void insertReward(Reward reward);

    @Query("SELECT * FROM Reward WHERE id = :id AND date = :date")
    Reward getRewardByDate(int uid, String date);

    @Query("UPDATE Reward SET boxCount = :boxCount WHERE id = :id AND date = :date")
    void updateBoxCount(int uid, String date, int boxCount);

    @Query("DELETE FROM Reward WHERE date < :date")
    void deleteOldRewards(String date); // 하루마다 초기화
}