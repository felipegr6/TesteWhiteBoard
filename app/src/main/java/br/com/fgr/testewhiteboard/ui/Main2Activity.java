package br.com.fgr.testewhiteboard.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import br.com.fgr.testewhiteboard.R;

public class Main2Activity extends AppCompatActivity implements DatePickerFragment.OnDateCallback,
        TimePickerFragment.OnTimeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

    }

    public void showTimePickerDialog(View v) {

        DialogFragment newFragment = TimePickerFragment.newInstance(0);
        newFragment.show(getFragmentManager(), "timePicker");

    }

    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = DatePickerFragment.newInstance(0);
        newFragment.show(getFragmentManager(), "datePicker");

    }

    @Override
    public void onDate(int id, int day, int month, int year) {
        Log.w("onDate", id + " " + day + " " + month + " " + year);
    }

    @Override
    public void onTime(int id, int hour, int minute) {
        Log.w("onTime", id + " " + hour + " " + minute);
    }

}