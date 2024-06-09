package com.example.mobile_program;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WaterRecord")
public class WaterRecord {
    @PrimaryKey(autoGenerate = true)
    public int No;

    @ColumnInfo(name = "ID")
    public int id;

    @ColumnInfo(name = "Date")
    public String date;

    @ColumnInfo(name = "WaterCount")
    public int waterCount = 0; // 하루에 마신 물의 잔 수
}
