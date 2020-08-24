package com.example.smartclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private ArrayList<String> mAlarmList;

    public AlarmAdapter(ArrayList<String> list) {
        this.mAlarmList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.alarm.setText(mAlarmList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView alarm;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarm = itemView.findViewById(R.id.alarm_time);
            delete = itemView.findViewById(R.id.delete_alarm);
        }
    }
}
