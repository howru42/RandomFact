package com.temp.randomfact.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.temp.randomfact.data.model.FactInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface FactsDao {
    @Query("SELECT * FROM Facts where language = :language and publishDate != :date and not isRead ORDER BY RANDOM() LIMIT 1")
    fun getFact(language: String, date: String): Flow<FactInfo?>

    @Query("SELECT * FROM Facts where language = :language and publishDate = :date LIMIT 1")
    fun getTodayFact(language: String, date: String): Flow<FactInfo?>

    @Query("SELECT * FROM Facts where id = :id")
    fun getFactBaseId(id: String): FactInfo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFact(factInfo: FactInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReadStatus(factInfo: FactInfo)

}
