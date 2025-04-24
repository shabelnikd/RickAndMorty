package com.shabelnikd.rickandmorty.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shabelnikd.rickandmorty.domain.models.characters.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteCharacterEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteCharacterEntity)

    @Query("DELETE FROM favorite_characters WHERE characterId = :characterId")
    suspend fun deleteFavoriteById(characterId: Int)

    @Query("SELECT * FROM favorite_characters WHERE characterId = :characterId LIMIT 1")
    fun getFavoriteById(characterId: Int): Flow<FavoriteCharacterEntity?>

    @Query("SELECT * FROM favorite_characters")
    fun getAllFavorites(): Flow<List<FavoriteCharacterEntity>>

    @Query("SELECT characterId FROM favorite_characters")
    fun getAllFavoriteIds(): Flow<List<Int>>
}