package com.example.eventbuzz.ArchitectureComponents;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.eventbuzz.POJO.FavouritePojo;

import java.util.List;

public class FavouriteRepository {
    private FavouriteDao mFavouriteDao;
    private LiveData<List<FavouritePojo>> mAllFavourites;

    public FavouriteRepository(Application application) {
        FavouriteDatabase favouriteDatabase = FavouriteDatabase.getInstance(application);
        mFavouriteDao = favouriteDatabase.favouriteDao();
        mAllFavourites = mFavouriteDao.getAllFavourite();
    }

    public void insert(FavouritePojo favouriteDetails) {
        new insertAsyncTask(mFavouriteDao).execute(favouriteDetails);
    }

    public void delete(FavouritePojo favouriteDetails) {
        new deleteAsyncTask(mFavouriteDao).execute(favouriteDetails);
    }

    public LiveData<List<FavouritePojo>> getAllFavourites() {
        return mAllFavourites;
    }



    private static class insertAsyncTask extends AsyncTask<FavouritePojo, Void, Void> {
        FavouriteDao cFavouriteDao;

        public insertAsyncTask(FavouriteDao favouriteDao) {
            cFavouriteDao = favouriteDao;
        }

        @Override
        protected Void doInBackground(FavouritePojo... favouriteDetails) {
            cFavouriteDao.insert(favouriteDetails[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<FavouritePojo, Void, Void> {
        FavouriteDao cFavouriteDao;

        public deleteAsyncTask(FavouriteDao favouriteDao) {
            cFavouriteDao = favouriteDao;
        }

        @Override
        protected Void doInBackground(FavouritePojo... favouriteDetails) {
            cFavouriteDao.delete(favouriteDetails[0]);
            return null;
        }
    }


}

