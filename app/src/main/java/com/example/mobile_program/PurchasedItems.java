package com.example.mobile_program;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PurchasedItems")
public class PurchasedItems {
    @PrimaryKey(autoGenerate = true)
    public int No;

    @ColumnInfo(name = "ID")
    public int id;

    @ColumnInfo(name = "ItemName")
    public String itemName; // 구매한 아이템 이름
}
