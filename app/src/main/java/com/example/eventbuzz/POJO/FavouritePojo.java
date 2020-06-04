package com.example.eventbuzz.POJO;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class FavouritePojo {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String mName;
    private String mStartDate;
    private String mEndDate;
    private String mImageUrl;
    private String mContextUrl;

    public FavouritePojo( String name, String startDate, String endDate, String imageUrl, String contextUrl) {
        mName = name;
        mStartDate = startDate;
        mEndDate = endDate;
        mImageUrl = imageUrl;
        mContextUrl=contextUrl;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public String getContextUrl() {
        return mContextUrl;
    }

    public String getName() {
        return mName;
    }
    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
