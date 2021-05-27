package com.poriyaabdollahi.karam.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Query("SELECT * from task_table")
    fun getAllTask() : Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update()
    suspend fun update(task: Task)

    @Delete()
    suspend fun delete(Task: Task)


}