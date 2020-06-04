package com.example.eventbuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eventbuzz.Adapter.RecyclerViewAdapter;
import com.example.eventbuzz.POJO.EventPojo;
import com.example.eventbuzz.POJO.FavouritePojo;
import com.example.eventbuzz.Utility.JsonUtil;
import com.example.eventbuzz.Utility.NetworkUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String LOG_Error=MainActivity.class.getSimpleName();
    private MyAsyncTask mAsyncTask;
    private  RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        mAsyncTask = new MyAsyncTask();

        if(getNetworkStatus()==true) {
            mAsyncTask.execute(getResources().getString(R.string.US_Link));
        }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemClicked=item.getItemId();
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
        return super.onOptionsItemSelected(item);
    }

    private class MyAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
          //  Log.i(LOG_Error, NetworkUtil.fetchJsonString(strings[0]));
            return  NetworkUtil.fetchJsonString(strings[0]);
        }

        @Override
        protected void onPostExecute(String json) {
           ArrayList<EventPojo> eventPojoArrayList = JsonUtil.extractFeaturesFromJson(json);
           recyclerViewAdapter.updateEventList(eventPojoArrayList);
            super.onPostExecute(json);
        }
    }
}