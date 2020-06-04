package com.example.eventbuzz.POJO;

public class EventPojo {


        private String mName;
        private String mStartDate;
        private String mEndDate;
        private String mImageUrl;


        public EventPojo(String name, String startDate, String endDate, String imageUrl) {
            mName = name;
            mStartDate = startDate;
            mEndDate = endDate;
            mImageUrl = imageUrl;
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

