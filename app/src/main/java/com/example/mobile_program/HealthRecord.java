package com.example.mobile_program;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "HealthRecord",
        foreignKeys = @ForeignKey(
                entity = USER_ENTITY.class,
                parentColumns = "ID",
                childColumns = "id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = {"id", "datetime"}, unique = true)}
)
public class HealthRecord {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String id;
    public String datetime;

    @ColumnInfo(name = "Walking")
    public int walking;
    @ColumnInfo(name = "BoxCount")
    public int boxCount; // 얻은 상자 수

    @ColumnInfo(name = "WaterCount")
    public int waterCount; // 마신 물잔의 수

    // 기본 생성자 (Room에서 요구)
    public HealthRecord() {
    }

    // 매개변수가 있는 생성자 추가
    public HealthRecord(String id, String datetime, int walking, int boxCount, int watercount) {
        this.id = id;
        this.datetime = datetime;
        this.walking = walking;
        this.boxCount = boxCount;
        this.waterCount = watercount;
    }

    // 걸음 수를 증가시키고 100 걸음마다 상자를 추가
    public void addSteps(int steps) {
        int previousWalking = walking;
        walking += steps;
        int newBoxes = (walking / 100) - (previousWalking / 100);
        boxCount += newBoxes;
    }
}
