package br.com.fgr.testewhiteboard.ui.task;

import android.Manifest;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import br.com.fgr.testewhiteboard.R;
import br.com.fgr.testewhiteboard.model.DateTimeHelper;
import br.com.fgr.testewhiteboard.model.TaskSchool;
import br.com.fgr.testewhiteboard.ui.datetime.DatePickerFragment;
import br.com.fgr.testewhiteboard.ui.datetime.TimePickerFragment;

public class TaskSchoolActivity extends AppCompatActivity implements DatePickerFragment.OnDateCallback,
        TimePickerFragment.OnTimeListener, TaskActionsMVP.RequiredViewOperations {

    private final int REQUEST_CAMERA = 17;
    private final int SELECT_FILE = 18;

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

    private TaskSchool taskSchool;
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

        chkIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    txtGrade.setVisibility(View.VISIBLE);
                else {

                    txtGrade.setText("0.00");
                    txtGrade.setVisibility(View.GONE);

                }

            }

        });

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

        if (getIntent() != null) {

            taskSchool = (TaskSchool) getIntent().getSerializableExtra("task");

            if (taskSchool != null) {

                txtName.setText(taskSchool.getName());

                DateTimeFormatter dtf1 = DateTimeFormat.forPattern("dd/MM/yyyy");
                DateTimeFormatter dtf2 = DateTimeFormat.forPattern("HH:mm");
                DateTime d = new DateTime(taskSchool.getDate());

                txtDate.setText(dtf1.print(d));
                txtHour.setText(dtf2.print(d));
                txtGrade.setText(taskSchool.getGrade());
                chkIsDone.setChecked(taskSchool.isDone());

            } else {

                DateTime dt = DateTime.now();

                txtDate.setText(DateTimeHelper.dateFormatted(dt.getDayOfMonth(),
                        dt.getMonthOfYear(), dt.getYear()));
                txtHour.setText(DateTimeHelper.timeFormatted(dt.getHourOfDay(),
                        dt.getMinuteOfHour()));

            }

        }

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
        txtName.requestFocus();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 15:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    save();
                else
                    Snackbar.make(findViewById(R.id.container), "Algo de Errado.",
                            Snackbar.LENGTH_SHORT).show();
                break;

        }

    }

    public void saveChanges(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 15);
        else
            save();

    }

    private void save() {

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

        if (verifyFields(taskName, taskDiscipline, taskGrade))
            presenter.addTask(this, taskSchool == null ? "0" : taskSchool.getId(), taskName,
                    taskDiscipline, taskDate.toDate(), taskGrade, isDone);
        else
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
        return taskGrade.isEmpty() ? 0.0 : Double.parseDouble(taskGrade.replace(",", "."));
    }

    @Override
    public void populateDisciplines(List<String> disciplines) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, disciplines);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnDisciplines.setAdapter(adapter);

        if (taskSchool != null)
            spnDisciplines.setSelection(adapter.getPosition(taskSchool.getDiscipline().getName()));

    }

    @Override
    public void populateImages(List<String> images) {
//        x
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

        txtDiscipline.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;

                try {

                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                image.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {

                presenter.addImage(taskSchool.getId(), data.getData().toString());
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
//                image.setImageBitmap(bm);

            }

        }

    }

    private void selectImage() {

        final CharSequence[] items = {"Tirar Foto", "Escolha da Galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(items[0])) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals(items[1])) {

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Selecionar Arquivo"),
                            SELECT_FILE);

                } else if (items[item].equals(items[2]))
                    dialog.dismiss();

            }

        });

        builder.show();

    }

}