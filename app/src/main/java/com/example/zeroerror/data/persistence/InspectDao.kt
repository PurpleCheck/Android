package com.example.zeroerror.data.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.zeroerror.data.model.Inspect

@Dao
interface InspectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInspectItem(inspectItem: Inspect)

    @Query("SELECT trackingId from Inspect")
    fun getTrackingId(): String

    @Query("SELECT totalCount from Inspect")
    fun getTotalCount(): Int

    @Update
    fun updateCheckCount(checkCount: Int)
}