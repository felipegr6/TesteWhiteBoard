package br.com.fgr.testewhiteboard.ui;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import br.com.fgr.testewhiteboard.R;
import br.com.fgr.testewhiteboard.model.DateTimeHelper;

public class TaskSchoolActivity extends AppCompatActivity implements DatePickerFragment.OnDateCallback,
        TimePickerFragment.OnTimeListener, TaskActionsMVP.RequiredViewOperations {

    private final String ARG_TASK_NAME = "taskName";
    private final String ARG_TASK_DATE = "taskDate";
    private final String ARG_TASK_HOUR = "taskHour";
    private final String ARG_TASK_GRADE = "taskGrade";

    private EditText txtName;
    private Spinner spnDisciplines;
    private EditText txtDate;
    private EditText txtHour;
    private EditText txtGrade;
    private CheckBox chkIsDone;

    private TaskActionsMVP.PresenterOperations presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_school);

        txtName = (EditText) findViewById(R.id.txt_work_name);
        spnDisciplines = (Spinner) findViewById(R.id.spn_discipline);
        txtDate = (EditText) findViewById(R.id.txt_date);
        txtHour = (EditText) findViewById(R.id.txt_hour);
        txtGrade = (EditText) findViewById(R.id.txt_grade);
        chkIsDone = (CheckBox) findViewById(R.id.chk_is_done);

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    DialogFragment newFragment = DatePickerFragment.newInstance(0);
                    newFragment.show(getFragmentManager(), "datePicker");

                }

            }

        });

        txtHour.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    DialogFragment newFragment = TimePickerFragment.newInstance(0);
                    newFragment.show(getFragmentManager(), "timePicker");

                }

            }

        });

        DateTime dt = DateTime.now();

        txtDate.setText(DateTimeHelper.dateFormatted(dt.getDayOfMonth(), dt.getMonthOfYear(), dt.getYear()));
        txtHour.setText(DateTimeHelper.timeFormatted(dt.getHourOfDay(), dt.getMinuteOfHour()));

        presenter = new TaskPresenter(this);

        presenter.retrieveDisciplines();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(ARG_TASK_NAME, txtName.getText().toString());
        outState.putString(ARG_TASK_DATE, txtDate.getText().toString());
        outState.putString(ARG_TASK_HOUR, txtHour.getText().toString());
        outState.putString(ARG_TASK_GRADE, txtGrade.getText().toString());

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        txtName.setText(savedInstanceState.getString(ARG_TASK_NAME));
        txtDate.setText(savedInstanceState.getString(ARG_TASK_DATE));
        txtHour.setText(savedInstanceState.getString(ARG_TASK_HOUR));
        txtGrade.setText(savedInstanceState.getString(ARG_TASK_GRADE));

    }

    @Override
    public void onDate(int id, int day, int month, int year) {

        txtDate.setText(DateTimeHelper.dateFormatted(day, month, year));
        txtHour.requestFocus();

    }

    @Override
    public void onTime(int id, int hour, int minute) {

        txtHour.setText(DateTimeHelper.timeFormatted(hour, minute));
        txtGrade.requestFocus();

    }

    public void saveChanges(View v) {

        String taskName = txtName.getText().toString();
        String taskDiscipline = spnDisciplines.getSelectedItem() != null ?
                spnDisciplines.getSelectedItem().toString() :
                "";
        double taskGrade = getGradeNumber(txtGrade.getText().toString());
        String strDate = txtDate.getText().toString();
        String strHour = txtHour.getText().toString();
        boolean isDone = chkIsDone.isChecked();
        DateTime taskDate = DateTime.now();

        try {

            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
            taskDate = dtf.parseDateTime(String.format("%s %s", strDate, strHour));

        } catch (Exception e) {
            Log.e("parseDate", e.getMessage() + "");
        }

        if (verifyFields(taskName, taskDiscipline, taskGrade)) {
            presenter.addTask(taskName, taskDiscipline, taskDate.toDate(), taskGrade, isDone);
        } else
            Snackbar.make(findViewById(R.id.container), "Algo de Errado.",
                    Snackbar.LENGTH_SHORT).show();

    }

    private boolean verifyFields(String taskName, String taskDiscipline, double taskGrade) {
        return !taskName.isEmpty() &&
                !taskDiscipline.isEmpty() &&
                verifyGrade(taskGrade);
    }

    private boolean verifyGrade(double grade) {
        return grade >= 0.0 && grade <= 10.0;
    }

    private double getGradeNumber(String taskGrade) {
        return taskGrade.isEmpty() ? 0.0 : Double.parseDouble(taskGrade);
    }

    @Override
    public void populateDisciplines(List<String> disciplines) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, disciplines);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnDisciplines.setAdapter(adapter);

    }

    @Override
    public void goToMainActivity() {
        finish();
    }

    @Override
    public void onError(String message) {
        Snackbar.make(findViewById(R.id.container), message,
                Snackbar.LENGTH_SHORT).show();
    }

    public void addDiscipline(View view) {

        final EditText txtDiscipline = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(txtDiscipline);
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String disc = txtDiscipline.getText().toString();

                if (!disc.isEmpty())
                    presenter.addDiscipline(txtDiscipline.getText().toString());

                dialog.dismiss();

            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder.setTitle("Adicionar Disciplina");

        builder.create().show();

    }

}