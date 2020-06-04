package com.example.eventbuzz.ArchitectureComponents;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eventbuzz.POJO.FavouritePojo;

import java.util.List;

public class FavouriteViewModel extends AndroidViewModel {

    private FavouriteRepository mFavouriteRepository;
    private LiveData<List<FavouritePojo>> mAllFavourites;

    public FavouriteViewModel(Application application) {
        super(application);
        mFavouriteRepository = new FavouriteRepository(application);
        mAllFavourites = mFavouriteRepository.getAllFavourites();
    }

    public void insert(FavouritePojo favouriteDetails) {
        mFavouriteRepository.insert(favouriteDetails);
    }

    public void delete(FavouritePojo favouriteDetails) {
        mFavouriteRepository.delete(favouriteDetails);
    }

    public LiveData<List<FavouritePojo>> getAllFavourites() {
        return mAllFavourites;
    }

}
