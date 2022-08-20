package com.example.zeroerror.data.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.zeroerror.data.model.Inspect
import kotlinx.coroutines.flow.Flow

@Dao
interface InspectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspectItem(inspectItem: Inspect)

    @Query("SELECT * from `inspect`")
    fun getInspectItem(): LiveData<Inspect>

    @Query("SELECT tracking_id from `inspect`")
    fun getTrackingId(): LiveData<String>

    @Query("SELECT total_count from `inspect`")
    fun getTotalCount(): LiveData<Int>

    @Query("SELECT check_count from `inspect`")
    suspend fun getCheckCount(): Int

    @Delete
    suspend fun deleteInspectItem(inspectItem: Inspect)

    @Update
    suspend fun updateCheckCount(inspectItem: Inspect)
}