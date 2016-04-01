package br.com.fgr.testewhiteboard;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private OnTimeListener callback;
    private int id;

    private static final String ARG_ID = "argId";

    public static TimePickerFragment newInstance(int id) {

        TimePickerFragment fragment = new TimePickerFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);

        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            id = getArguments().getInt(ARG_ID);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        callback.onTime(id, hourOfDay, minute);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnTimeListener)
            callback = (OnTimeListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnTimeListener");

    }

    @Override
    public void onDetach() {

        super.onDetach();
        callback = null;

    }

    public interface OnTimeListener {

        void onTime(int id, int hour, int minute);

    }

}