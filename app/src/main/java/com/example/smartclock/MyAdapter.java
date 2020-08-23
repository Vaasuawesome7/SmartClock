package com.example.smartclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<String> mLapTimes;

    public MyAdapter(ArrayList<String> times) {
        this.mLapTimes = times;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lap_time_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = "Lap " + (position + 1) + ": " + mLapTimes.get(position);
        holder.lap.setText(text);
    }

    @Override
    public int getItemCount() {
        return mLapTimes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView lap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lap = itemView.findViewById(R.id.lap_item);
        }
    }
}
