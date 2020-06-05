package com.example.eventbuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.eventbuzz.Adapter.FavouriteAdapter;
import com.example.eventbuzz.Adapter.RecyclerViewAdapter;
import com.example.eventbuzz.ArchitectureComponents.FavouriteViewModel;
import com.example.eventbuzz.POJO.EventPojo;
import com.example.eventbuzz.POJO.FavouritePojo;
import com.example.eventbuzz.Utility.JsonUtil;
import com.example.eventbuzz.Utility.NetworkUtil;
import com.example.eventbuzz.Widget.FavShowWidget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_VALUE = MainActivity.class.getSimpleName();
    public static String widgetText;
    private MyAsyncTask mAsyncTask;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FavouriteAdapter favouriteAdapter;
    private static RecyclerView recyclerView;
    private FavouriteViewModel favouriteViewModel;
    private FirebaseAnalytics mFirebaseAnalytics;
    private int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this, this, getApplication());
        favouriteAdapter = new FavouriteAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        mAsyncTask = new MyAsyncTask();
        favouriteViewModel = new FavouriteViewModel(this.getApplication());

        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getMenuResponse(item);
                return true;
            }
        });


        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (savedInstanceState != null) {
            flag = savedInstanceState.getInt("CURRENT_ADAPTER");
        }
        switch (flag) {
            case 1:
                if (getNetworkStatus()) {
                    setUSAdapter();
                    break;
                }
            case 2:
                if (getNetworkStatus()) {
                    setGermanyAdapter();
                    break;
                }
            case 3:
                if (getNetworkStatus()) {
                    setCanadaAdapter();
                    break;
                }
            case 4: {
                setFavouriteAdapter();
                break;
            }

            default:
                setFavouriteAdapter();
        }

        recyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

    }

    private boolean getNetworkStatus() {
        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NI = CM.getActiveNetworkInfo();
        return NI != null && NI.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void getMenuResponse(MenuItem item) {

        int itemClicked = item.getItemId();

        if (itemClicked == R.id.favourite) {
            setFavouriteAdapter();

        }

        if (getNetworkStatus()) {
            if (itemClicked == R.id.canada) {
                setCanadaAdapter();
            } else if (itemClicked == R.id.germany) {
                setGermanyAdapter();
            } else if (itemClicked == R.id.us) {
                setUSAdapter();
            }
        }

    }

    private void setUSAdapter() {
        mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute(getResources().getString(R.string.US_Link));
        flag = 1;

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "US");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void setGermanyAdapter() {
        mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute(getResources().getString(R.string.Germany_Link));
        flag = 2;

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Germany");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void setCanadaAdapter() {
        mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute(getResources().getString(R.string.Canada_Link));
        flag = 3;

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Canada");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void setFavouriteAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.setAdapter(favouriteAdapter);
        flag = 4;
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllFavourites().observe(this, new Observer<List<FavouritePojo>>() {
            @Override
            public void onChanged(List<FavouritePojo> favouritePojos) {
                favouriteAdapter.updateEventList(favouritePojos);
                Log.i(LOG_VALUE, "RecyclerView Updated");
            }
        });

        deleteItemOnSwipe();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Favourite");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void deleteItemOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                favouriteViewModel.delete(favouriteAdapter.getItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, getString(R.string.removed_fav), Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return NetworkUtil.fetchJsonString(strings[0]);
        }

        @Override
        protected void onPostExecute(String json) {

            ArrayList<EventPojo> eventPojoArrayList = JsonUtil.extractFeaturesFromJson(json);
            recyclerViewAdapter.updateEventList(eventPojoArrayList);
            recyclerView.setAdapter(recyclerViewAdapter);

            Log.i(LOG_VALUE, "RecyclerView Updated");
            super.onPostExecute(json);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        widgetText = "";
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllFavourites().observe(this, new Observer<List<FavouritePojo>>() {
            @Override
            public void onChanged(List<FavouritePojo> favouriteDetails) {
                for (int i = 0; i < favouriteDetails.size(); i++) {
                    widgetText += (i + 1) + favouriteDetails.get(i).getName() + "\n";
                }
            }
        });

        Intent intent = new Intent(MainActivity.this, FavShowWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), FavShowWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        recyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("CURRENT_ADAPTER", flag);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        flag = savedInstanceState.getInt("CURRENT_ADAPTER");
    }
}