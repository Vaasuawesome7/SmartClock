package com.example.smartclock;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private ArrayList<String> mAlarmList;
    private ArrayList<Boolean> mSwitchStates;

    public interface OnAlarmListener{
        void onAlarmClick(int pos);
    }

    public AlarmAdapter(ArrayList<String> list, ArrayList<Boolean> switchStates) {
        this.mAlarmList = list;
        this.mSwitchStates = switchStates;
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
        holder.alarmOnOff.setChecked(mSwitchStates.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView alarm;
        Button delete;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch alarmOnOff;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarm = itemView.findViewById(R.id.alarm_time);
            delete = itemView.findViewById(R.id.delete_alarm);
            alarmOnOff = itemView.findViewById(R.id.alarm_on_off);
        }
    }

}
