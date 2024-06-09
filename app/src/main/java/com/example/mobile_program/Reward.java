package com.example.mobile_program;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Reward")
public class Reward {
    @PrimaryKey(autoGenerate = true)
    public int No;

    @ColumnInfo(name = "ID")
    public int id;

    @ColumnInfo(name = "Date")
    public String date;

    @ColumnInfo(name = "BoxCount")
    public int boxCount = 0; // 얻은 상자 수
}
