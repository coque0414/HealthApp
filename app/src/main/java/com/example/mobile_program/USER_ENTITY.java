package com.example.mobile_program;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(tableName = "User")
public class USER_ENTITY {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "ID")
    public String id;

    @ColumnInfo(name = "Password")
    public String password;

    @ColumnInfo(name = "Point")
    public int point = 0; // 기본 값은 0으로 설정
}

