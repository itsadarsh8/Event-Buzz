package com.example.eventbuzz.Utility;

import android.util.Log;

import com.example.eventbuzz.POJO.EventPojo;
import com.example.eventbuzz.POJO.FavouritePojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtil {

    private static String LOG_Error=JsonUtil.class.getSimpleName();

    //call by class name only
    private JsonUtil(){}

    public static ArrayList<EventPojo> extractFeaturesFromJson(String json){
        ArrayList<EventPojo> eventPojoArrayList =new ArrayList<>();

        if(json==null){
            Log.e(LOG_Error,"ExtractFeaturesFromJson- input json is null");
            return null;
        }
        else{
            try {
                JSONObject jsonBaseObject= new JSONObject(json);
                JSONObject jsonSecondBaseArray=jsonBaseObject.getJSONObject("_embedded");
                JSONArray jsonBaseArray=jsonSecondBaseArray.getJSONArray("events");

                for(int i=0;i<jsonBaseArray.length();i++){
                    JSONObject jsonObject=jsonBaseArray.getJSONObject(i);

                    //get Name of Concert
                    String concertName=jsonObject.getString("name");

                    //get concert URL
                    String concertUrl=jsonObject.getString("url");

                    //get Start and End date of concert tickets
                    JSONObject salesObject=jsonObject.getJSONObject("sales");
                    JSONObject publicObject=salesObject.getJSONObject("public");
                    String startDate=publicObject.getString("startDateTime");
                    String endDate=publicObject.getString("endDateTime");
                    String[] splitStartDate=startDate.split("T");
                    String[] splitEndDate=endDate.split("T");
                    startDate=splitStartDate[0];
                    endDate=splitEndDate[0];

                    //get Image URL
                    JSONArray imageArray=jsonObject.getJSONArray("images");
                    JSONObject imageObject=imageArray.getJSONObject(0);
                    String imageUrl=imageObject.getString("url");

                    //Creating Object
                    EventPojo eventPojo =new EventPojo(concertName,startDate,endDate,imageUrl);

                    //Creating Object URL
                    eventPojoArrayList.add(eventPojo);



                }
                return eventPojoArrayList;
            } catch (JSONException e) {
                Log.e(LOG_Error,"ExtractFeaturesFromJson- "+e);
            }
        }

        return null;
    }

}
