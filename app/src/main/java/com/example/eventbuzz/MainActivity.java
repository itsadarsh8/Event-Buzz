package com.example.eventbuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
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
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.eventbuzz.Adapter.FavouriteAdapter;
import com.example.eventbuzz.Adapter.RecyclerViewAdapter;
import com.example.eventbuzz.ArchitectureComponents.FavouriteViewModel;
import com.example.eventbuzz.POJO.EventPojo;
import com.example.eventbuzz.POJO.FavouritePojo;
import com.example.eventbuzz.Utility.JsonUtil;
import com.example.eventbuzz.Utility.NetworkUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static String LOG_VALUE=MainActivity.class.getSimpleName();
    public static String widgetText="YO";
    private MyAsyncTask mAsyncTask;
    private  RecyclerViewAdapter recyclerViewAdapter;
    private FavouriteAdapter favouriteAdapter;
    private  RecyclerView recyclerView;
    private  FavouriteViewModel favouriteViewModel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar=findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this,this,getApplication());
        favouriteAdapter= new FavouriteAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        mAsyncTask = new MyAsyncTask();
        favouriteViewModel=new FavouriteViewModel(this.getApplication());

        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getMenuResponse(item);
                return true;
            }
        });

        if(getNetworkStatus()==true) {
            mAsyncTask.execute(getResources().getString(R.string.US_Link));
        }

        AdView adView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private boolean getNetworkStatus(){
        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NI = CM.getActiveNetworkInfo();
        return NI != null && NI.isConnected();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void getMenuResponse( MenuItem item) {

        int itemClicked=item.getItemId();

        if(itemClicked==R.id.favourite){
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            recyclerView.setAdapter(favouriteAdapter);
            favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
            favouriteViewModel.getAllFavourites().observe(this, new Observer<List<FavouritePojo>>() {
                @Override
                public void onChanged(List<FavouritePojo> favouritePojos) {
                    favouriteAdapter.updateEventList(favouritePojos);
                    Log.i(LOG_VALUE,"RecyclerView Updated");
                }
            });

            deleteItemOnSwipe();
        }

        if(getNetworkStatus()==true){
            if(itemClicked==R.id.canada){
                mAsyncTask=new MyAsyncTask();
                mAsyncTask.execute(getResources().getString(R.string.Canada_Link));
            }

            else if(itemClicked==R.id.germany){
                mAsyncTask=new MyAsyncTask();
                mAsyncTask.execute(getResources().getString(R.string.Germany_Link));
            }

            else if(itemClicked==R.id.us){
                mAsyncTask=new MyAsyncTask();
                mAsyncTask.execute(getResources().getString(R.string.US_Link));
            }
        }

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
                Toast.makeText(MainActivity.this, "removed from favourites", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private class MyAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            return  NetworkUtil.fetchJsonString(strings[0]);
        }

        @Override
        protected void onPostExecute(String json) {

           ArrayList<EventPojo> eventPojoArrayList = JsonUtil.extractFeaturesFromJson(json);
           recyclerViewAdapter.updateEventList(eventPojoArrayList);
           recyclerView.setAdapter(recyclerViewAdapter);
           Log.i(LOG_VALUE,"RecyclerView Updated");
            super.onPostExecute(json);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        widgetText="";
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllFavourites().observe(this, new Observer<List<FavouritePojo>>() {
            @Override
            public void onChanged(List<FavouritePojo> favouriteDetails) {
                for(int i=0;i<favouriteDetails.size();i++){
                   widgetText+=(i+1)+favouriteDetails.get(i).getName()+"\n";
                }
            }
        });

        Intent intent = new Intent(MainActivity.this, FavShowWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), FavShowWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}