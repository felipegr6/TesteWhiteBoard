package br.com.fgr.testewhiteboard.ui.datetime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private OnDateCallback callback;
    private int id;

    private static final String ARG_ID = "argId";

    public static DatePickerFragment newInstance(int id) {

        DatePickerFragment fragment = new DatePickerFragment();

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

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        callback.onDate(id, dayOfMonth, monthOfYear + 1, year);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnDateCallback)
            callback = (OnDateCallback) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnDateCallback");

    }

    @Override
    public void onDetach() {

        super.onDetach();
        callback = null;

    }

    public interface OnDateCallback {

        void onDate(int id, int day, int month, int year);

    }

}