package com.poriyaabdollahi.karam.data

import android.content.Context
import android.util.Log

import androidx.datastore.preferences.*

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
private  const val  TAG = "PreferenceManager"
enum class SortOrder {BY_NAME,BY_DATE}
data class  FilterPreferences(val sortOrder : SortOrder,val hideCompleted : Boolean)

@Singleton
class PreferencesManager @Inject  constructor(@ApplicationContext context : Context){
//    private val Context.dataStore by preferencesDataStore("PreferenceManager")

    private val datastore =  context.createDataStore("user_preferences")
            val preferenceFlow = datastore.data
        .catch { execption ->
            if (execption is IOException) {
                Log.e(TAG,"error reading preferences",execption)
                emit(emptyPreferences())

            }else {
                throw execption
            }

        }
        .map {
            preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER]?:SortOrder.BY_DATE.name
            )
            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED]?: false
            FilterPreferences(sortOrder,hideCompleted)

        }

    suspend fun updateSortOrder(sortOrder:SortOrder){
        datastore.edit {
            //
                preferences->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }

    }
    suspend fun  updateHideCompleted(hideCompleted:Boolean){
        datastore.edit {    preferences->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object  PreferencesKeys{
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }
}