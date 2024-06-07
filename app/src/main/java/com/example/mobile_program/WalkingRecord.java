package com.example.mobile_program;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Walking_Record",
        foreignKeys = @ForeignKey(
                entity = USER_ENTITY.class,
                parentColumns = "uid",
                childColumns = "id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "id")}
)
public class WalkingRecord {
    @PrimaryKey(autoGenerate = true)
    public int No;
    public int id;
    public String datetime;

    @ColumnInfo(name = "Walking")
    public int walking;
}
