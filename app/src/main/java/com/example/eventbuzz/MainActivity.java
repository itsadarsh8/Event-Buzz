package com.example.eventbuzz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.eventbuzz.Adapter.RecyclerViewAdapter;
import com.example.eventbuzz.POJO.EventPojo;
import com.example.eventbuzz.Utility.JsonUtil;
import com.example.eventbuzz.Utility.NetworkUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String LOG_Error=MainActivity.class.getSimpleName();
    private USAsyncTask mUSAsyncTask;
    private  RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter =new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        mUSAsyncTask=new USAsyncTask();
        mUSAsyncTask.execute(getResources().getString(R.string.US_Link));
    }

    private class USAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.i(LOG_Error, NetworkUtil.fetchJsonString(strings[0]));
            return  NetworkUtil.fetchJsonString(strings[0]);
        }

        @Override
        protected void onPostExecute(String json) {
           ArrayList<EventPojo> eventPojoArrayList= JsonUtil.extractFeaturesFromJson(json);
           recyclerViewAdapter.updateEventList(eventPojoArrayList);
            super.onPostExecute(json);
        }
    }
}