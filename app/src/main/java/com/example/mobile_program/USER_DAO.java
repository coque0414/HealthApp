package com.example.mobile_program;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface USER_DAO {
    @Insert
    void insertUser(USER_ENTITY user);

    @Query("SELECT * FROM User WHERE uid = :uid")
    USER_ENTITY getUser(int uid);
}

