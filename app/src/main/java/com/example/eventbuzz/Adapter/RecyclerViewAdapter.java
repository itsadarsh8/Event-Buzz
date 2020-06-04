package com.example.eventbuzz.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbuzz.POJO.EventPojo;
import com.example.eventbuzz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    ArrayList<EventPojo> mEventPojoArrayList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_holder, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.concertName.setText(mEventPojoArrayList.get(position).getConcertName());
        holder.startDate.setText(mEventPojoArrayList.get(position).getStartDate());
        holder.endDate.setText(mEventPojoArrayList.get(position).getEndDate());
        Picasso.get().load(mEventPojoArrayList.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mEventPojoArrayList.size();
    }

    public void updateEventList(ArrayList<EventPojo> eventPojoArrayList){
        mEventPojoArrayList=eventPojoArrayList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView concertName;
        TextView startDate;
        TextView endDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            concertName=itemView.findViewById(R.id.concertTitle);
            startDate=itemView.findViewById(R.id.startDate);
            endDate=itemView.findViewById(R.id.endDate);

        }
    }
}
