package com.example.eventbuzz.POJO;

@SuppressWarnings("ALL")
public class EventPojo {


    private final String mName;
    private final String mStartDate;
    private final String mEndDate;
    private final String mImageUrl;
    private final String mConcertUrl;


    public EventPojo(String name, String startDate, String endDate, String imageUrl, String concertUrl) {
        mName = name;
        mStartDate = startDate;
        mEndDate = endDate;
        mImageUrl = imageUrl;
        mConcertUrl = concertUrl;
    }

    public String getConcertUrl() {
        return mConcertUrl;
    }

    public String getConcertName() {
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

