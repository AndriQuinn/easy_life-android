package com.example.easy_life.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val THEME_MODE = stringPreferencesKey("theme_mode")
val userFontSize = intPreferencesKey("font_size")

suspend fun setFontSize(context: Context, size:Int) {
    context.dataStore.edit { prefs ->
        prefs[userFontSize] = size
    }
}

fun getfontSize(context: Context): Flow<Int> {
    return context.dataStore.data.map { prefs ->
        prefs[userFontSize] ?: 14
    }
}

suspend fun setTheme(context: Context, mode: String) {
    context.dataStore.edit { prefs ->
        prefs[THEME_MODE] = mode
    }
}

fun getTheme(context: Context): Flow<String> {
    return context.dataStore.data.map { prefs ->
        prefs[THEME_MODE] ?: "system"
    }
}

val Context.dataStore by preferencesDataStore(name = "user_prefs")
object PrefKeys {
    val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
}
fun isFirstLaunch(context: Context): Flow<Boolean> {
    return context.dataStore.data.map { prefs ->
        prefs[PrefKeys.FIRST_LAUNCH] ?: true
    }
}
suspend fun setNotFirstLaunch(context: Context) {
    context.dataStore.edit { prefs ->
        prefs[PrefKeys.FIRST_LAUNCH] = false
    }
}