package com.shabelnikd.rickandmorty.data.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shabelnikd.rickandmorty.data.datasource.local.FavoriteCharacterDao
import com.shabelnikd.rickandmorty.domain.models.characters.FavoriteCharacterEntity

@Database(
    entities = [FavoriteCharacterEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}
