package com.poriyaabdollahi.karam.di

import android.app.Application
import androidx.room.Room
import com.poriyaabdollahi.karam.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app:Application,callback:TaskDatabase.Callback)=
     Room.databaseBuilder(app,TaskDatabase::class.java,"task_database")
         .fallbackToDestructiveMigration()
         .addCallback(callback)
         .build()

    @Provides

    fun provideTaskDao(db:TaskDatabase) = db.taskDao()
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}
@Retention
@Qualifier
annotation class ApplicationScope