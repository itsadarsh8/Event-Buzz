package com.example.eventbuzz.ArchitectureComponents;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.eventbuzz.POJO.FavouritePojo;


@androidx.room.Database(entities = FavouritePojo.class, version =1 )
public abstract class FavouriteDatabase extends RoomDatabase {

    private static FavouriteDatabase instance;
    public abstract FavouriteDao favouriteDao();

    public static synchronized FavouriteDatabase getInstance(Context context) {
        //It can only be created once(for the first time)
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , FavouriteDatabase.class
                    , "favourite_database")
                    .fallbackToDestructiveMigration()  //To prevent crash while incrementing version.
                    .build();


        }

        return instance;
    }

    public static RoomDatabase.Callback PopulateDb = new RoomDatabase.Callback() {

        //Called when database is created for the first time
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopulateDbAsyncTask(instance).execute();
            super.onCreate(db);
        }
    };


    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavouriteDao favouriteDao;

        public PopulateDbAsyncTask(FavouriteDatabase db) {
            this.favouriteDao = db.favouriteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favouriteDao.insert(new FavouritePojo("Title 1", "2019-12-08", "2019-12-07", "url1","url4"));
            favouriteDao.insert(new FavouritePojo("Title 2", "2019-12-09", "2019-12-08", "url2","url5"));
            favouriteDao.insert(new FavouritePojo("Title 3", "2019-12-07", "2019-12-09", "url3","url6"));

            return null;
        }
    }
}
