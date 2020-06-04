package com.example.eventbuzz.POJO;

public class EventPojo {


        private String mName;
        private String mStartDate;
        private String mEndDate;
        private String mImageUrl;
        private String mConcertUrl;


        public EventPojo(String name, String startDate, String endDate, String imageUrl, String concertUrl) {
            mName = name;
            mStartDate = startDate;
            mEndDate = endDate;
            mImageUrl = imageUrl;
            mConcertUrl=concertUrl;
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

