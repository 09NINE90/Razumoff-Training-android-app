package ru.razumoff.razo.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razo.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserEntity?>

}