package ru.razumoff.razo.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razo.database.entities.UserMeasurementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMeasurementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: UserMeasurementEntity)

    @Query("SELECT * FROM user_measurements WHERE userId = :userId ORDER BY dateTime DESC")
    fun getMeasurements(userId: String): Flow<List<UserMeasurementEntity>>

    @Query("SELECT * FROM user_measurements WHERE userId = :userId AND type = :type ORDER BY dateTime DESC")
    fun getMeasurementsByType(userId: String, type: String): Flow<List<UserMeasurementEntity>>

    @Query("SELECT * FROM user_measurements WHERE userId = :userId AND type = :type ORDER BY dateTime DESC LIMIT 1")
    suspend fun getLatestMeasurement(userId: String, type: String): UserMeasurementEntity?

    @Query("SELECT * FROM user_measurements WHERE id = :id AND userId = :userId")
    suspend fun getMeasurementById(id: String, userId: String): UserMeasurementEntity?

    @Query("DELETE FROM user_measurements WHERE id = :id AND userId = :userId")
    suspend fun deleteMeasurement(id: String, userId: String)

    @Query("DELETE FROM user_measurements WHERE userId = :userId")
    suspend fun deleteAllMeasurements(userId: String)
}