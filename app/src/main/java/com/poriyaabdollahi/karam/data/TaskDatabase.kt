package com.poriyaabdollahi.karam.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.poriyaabdollahi.karam.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO


    class Callback @Inject constructor(private val database:Provider<TaskDatabase>,@ApplicationScope private val applicationScope:CoroutineScope) :RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //db operation
            //this will happen after build method on AppModule
                val dao =  database.get().taskDao()
            applicationScope.launch {
                dao.insert(Task("کار های خانه رو انجام بدم"))
                dao.insert(Task("حموم برم"))
                dao.insert(Task("ریاضی بخوانم",completed = true))
                dao.insert(Task("ماشین رو بشورم",true))
            }
        }
    }

}