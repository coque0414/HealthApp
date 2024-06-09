package com.example.mobile_program;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Walking_Record",
        foreignKeys = @ForeignKey(
                entity = USER_ENTITY.class,
                parentColumns = "ID",
                childColumns = "id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = {"id", "datetime"}, unique = true)}
)
public class WalkingRecord {
    @PrimaryKey(autoGenerate = false)
    public int id;
    public String datetime;

    @ColumnInfo(name = "Walking")
    public int walking;
    @ColumnInfo(name = "BoxCount")
    public int boxCount = 0; // 얻은 상자 수

    // 걸음 수를 증가시키고 100 걸음마다 상자를 추가
    public void addSteps(int steps) {
        int previousWalking = walking;
        walking += steps;
        int newBoxes = (walking / 100) - (previousWalking / 100);
        boxCount += newBoxes;
    }
}
