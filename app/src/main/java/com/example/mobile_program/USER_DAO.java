package com.example.mobile_program;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface USER_DAO {
    @Insert
    void insertUser(USER_ENTITY PutInUser);

    @Query("SELECT * FROM User WHERE id = :id")
    USER_ENTITY getUser(int id);

    // 중복되는 아이디 체크
    @Query("SELECT COUNT(*) FROM User WHERE id = :id")
    int checkUserExists(String id);

    // 비밀번호 체크
    @Query("SELECT password FROM User WHERE id = :id")
    String getPassword(String id);

    @Update
    void updateUser(USER_ENTITY user);

    @Query("SELECT * FROM User WHERE id = :id")
    USER_ENTITY getUserByID(String id);
}

