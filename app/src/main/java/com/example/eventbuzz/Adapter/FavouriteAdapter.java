package com.example.eventbuzz.Adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eventbuzz.POJO.FavouritePojo;
import com.example.eventbuzz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.favouriteViewHolder> {

    private List<FavouritePojo> list = new ArrayList<>();
    private final Context mContext;

    public FavouriteAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public favouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_holder, parent, false);
        return new favouriteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull favouriteViewHolder holder, final int position) {
        holder.concertName.setText(list.get(position).getName());
        holder.startDate.setText(list.get(position).getStartDate());
        holder.endDate.setText(list.get(position).getEndDate());
        Picasso.get().load(list.get(position).getImageUrl()).into(holder.imageView);

        holder.concertName.setContentDescription(list.get(position).getName());
        holder.startDate.setContentDescription(list.get(position).getStartDate());
        holder.endDate.setContentDescription(list.get(position).getEndDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String concertUrl = list.get(position).getContextUrl();
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
        return list.size();
    }


    public void updateEventList(List<FavouritePojo> eventPojoArrayList) {
        list = eventPojoArrayList;
        notifyDataSetChanged();
    }

    public FavouritePojo getItemAt(int position) {
        return list.get(position);
    }

    public static class favouriteViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView concertName;
        final TextView startDate;
        final TextView endDate;
        final FloatingActionButton fab;

        @SuppressLint("RestrictedApi")
        public favouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            concertName = itemView.findViewById(R.id.concertTitle);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            fab = itemView.findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
        }

    }
}
