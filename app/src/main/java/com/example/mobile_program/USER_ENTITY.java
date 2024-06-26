package com.example.mobile_program;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class USER_ENTITY {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ID")
    public String id;

    @ColumnInfo(name = "Password")
    public String password;

    @ColumnInfo(name = "Point")
    public int point = 0; // 기본 값은 0으로 설정

    @ColumnInfo(name = "Nickname")
    public String nickname;

    public USER_ENTITY(String id, String password, String nickname) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.point = 0;
    }
}

