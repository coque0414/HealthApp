package com.example.mobile_program;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PurchasedItemsDao {
    @Insert
    void insertPurchasedItem(PurchasedItems item);

    @Query("SELECT * FROM PurchasedItems WHERE id = :id AND itemName = :itemName")
    PurchasedItems getPurchasedItem(int uid, String itemName);

    @Query("DELETE FROM PurchasedItems WHERE id = :id AND itemName = :itemName")
    void deletePurchasedItem(int uid, String itemName);
}