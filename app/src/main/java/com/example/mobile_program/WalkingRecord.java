package com.example.mobile_program;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Walking_Record",
        foreignKeys = @ForeignKey(
                entity = USER_ENTITY.class,
                parentColumns = "uid",
                childColumns = "uid",
                onDelete = ForeignKey.CASCADE
        )
)
public class WalkingRecord {
    public int uid;
    public String datetime;

    @ColumnInfo(name = "Walking")
    public int walking;
}
