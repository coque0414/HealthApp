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
                childColumns = "uid",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "uid")}
)
public class WalkingRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int uid;
    public String datetime;

    @ColumnInfo(name = "Walking")
    public int walking;
}
