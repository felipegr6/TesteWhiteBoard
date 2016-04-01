package br.com.fgr.testewhiteboard.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.testewhiteboard.R;
import br.com.fgr.testewhiteboard.model.DateTimeHelper;

public class AddTaskSchoolFragment extends DialogFragment {

    public AddTaskSchoolFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_task, container);
        Spinner spinner = (Spinner) view.findViewById(R.id.spn_discipline);
        TextView txtDate = (TextView) view.findViewById(R.id.txt_date);
        TextView txtHour = (TextView) view.findViewById(R.id.txt_hour);

        List<String> disciplines = new ArrayList<>();
        DateTime dt = DateTime.now();

        txtDate.setText(DateTimeHelper.dateFormatted(dt.getDayOfMonth(), dt.getMonthOfYear(), dt.getYear()));
        txtHour.setText(DateTimeHelper.timeFormatted(dt.getHourOfDay(), dt.getMinuteOfHour()));
        disciplines.add("1");
        disciplines.add("2");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, disciplines);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        getDialog().setTitle(R.string.title_add_task);

        return view;

    }

}
