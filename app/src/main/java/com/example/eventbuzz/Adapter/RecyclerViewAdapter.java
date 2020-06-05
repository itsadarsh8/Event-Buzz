package com.example.eventbuzz.Adapter;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbuzz.ArchitectureComponents.FavouriteViewModel;
import com.example.eventbuzz.POJO.EventPojo;
import com.example.eventbuzz.POJO.FavouritePojo;
import com.example.eventbuzz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    ArrayList<EventPojo> mEventPojoArrayList = new ArrayList<>();
    FavouriteViewModel favouriteViewModel;
    final Activity mActivity;
    final Context mContext;
    final Application mApplication;

    public RecyclerViewAdapter(Activity activity, Context context, Application application) {
        mActivity = activity;
        mContext = context;
        mApplication = application;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_holder, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.concertName.setText(mEventPojoArrayList.get(position).getConcertName());
        holder.startDate.setText(mEventPojoArrayList.get(position).getStartDate());
        holder.endDate.setText(mEventPojoArrayList.get(position).getEndDate());
        Picasso.get().load(mEventPojoArrayList.get(position).getImageUrl()).into(holder.imageView);

        holder.concertName.setContentDescription(mEventPojoArrayList.get(position).getConcertName());
        holder.startDate.setContentDescription(mEventPojoArrayList.get(position).getStartDate());
        holder.endDate.setContentDescription(mEventPojoArrayList.get(position).getEndDate());

        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouritePojo favouritePojo = new FavouritePojo(
                        mEventPojoArrayList.get(position).getConcertName(),
                        mEventPojoArrayList.get(position).getStartDate(),
                        mEventPojoArrayList.get(position).getEndDate(),
                        mEventPojoArrayList.get(position).getImageUrl(),
                        mEventPojoArrayList.get(position).getConcertUrl());

                favouriteViewModel = new FavouriteViewModel(mApplication);
                favouriteViewModel.insert(favouritePojo);

                Toast.makeText(mActivity.getApplicationContext(), mContext.getResources().getString(R.string.saved_fav), Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String concertUrl = mEventPojoArrayList.get(position).getConcertUrl();
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(concertUrl));
                try {
                    mContext.startActivity(webIntent);
                } catch (ActivityNotFoundException ex) {
                    mContext.startActivity(webIntent);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return mEventPojoArrayList.size();
    }

    public void updateEventList(ArrayList<EventPojo> eventPojoArrayList) {
        mEventPojoArrayList = eventPojoArrayList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView concertName;
        final TextView startDate;
        final TextView endDate;
        final FloatingActionButton fab;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            concertName = itemView.findViewById(R.id.concertTitle);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            fab = itemView.findViewById(R.id.fab);

        }
    }
}
