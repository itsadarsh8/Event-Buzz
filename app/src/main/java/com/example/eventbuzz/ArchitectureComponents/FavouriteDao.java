package com.example.eventbuzz.ArchitectureComponents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.eventbuzz.POJO.FavouritePojo;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Insert
    void insert(FavouritePojo favouritePojo);

    @Delete
    void delete(FavouritePojo favouritePojo);

    @Query("SELECT * FROM favourite_table ORDER BY mId ASC")
    LiveData<List<FavouritePojo>> getAllFavourite();
}
