package com.example.smartclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class MyDialogue extends AppCompatDialogFragment {

    private CheckBox[] days;
    private String mDays;
    private MyDialogueListener listener;
    private int mPos;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (MyDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement my dialogue listener");
        }

    }

    public MyDialogue(String mDays, int pos) {
        this.mDays = mDays;
        this.mPos = pos;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialogue, null);

        builder.setView(view)
                .setTitle("Days to repeat in")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newList = "";
                        for (int i = 0; i < 7; i++) {
                            if (days[i].isChecked())
                                newList += "" + (i+1);
                        }
                        listener.applyDays(newList, mPos);
                    }
                });

        days = new CheckBox[7];
        for (int i = 1; i <= 7; i++) {
            String id = "day" + i;
            int resID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
            days[i-1] = view.findViewById(resID);
            if (mDays.contains("" + i)) {
                System.out.println(i);
                days[i - 1].setChecked(true);
            }
        }

        return builder.create();
    }

    public interface MyDialogueListener {
        void applyDays(String newDays, int pos);
    }
}
