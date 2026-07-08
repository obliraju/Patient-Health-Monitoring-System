package com.example.patienthealthmonitoring.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.patienthealthmonitoring.data.local.entity.StaffEntity

@Dao
interface StaffDao {
    @Query(
        """
        SELECT * FROM staff
        WHERE LOWER(staff_id) = LOWER(:identifier)
           OR LOWER(name) = LOWER(:identifier)
        LIMIT 1
        """
    )
    suspend fun getStaffByIdentifier(identifier: String): StaffEntity?

    @Query("SELECT COUNT(*) FROM staff WHERE LOWER(staff_id) = LOWER(:staffId)")
    suspend fun countByStaffId(staffId: String): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStaff(staff: StaffEntity)
}
