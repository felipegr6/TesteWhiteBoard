package br.com.fgr.testewhiteboard.ui.done;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.testewhiteboard.R;
import br.com.fgr.testewhiteboard.model.TaskSchool;
import br.com.fgr.testewhiteboard.ui.main.TaskSchoolAdapter;
import br.com.fgr.testewhiteboard.ui.task.TaskSchoolActivity;

public class DoneActivity extends AppCompatActivity implements DoneActionsMVP.RequiredViewOperations {

    private DoneActionsMVP.PresenterOperations presenter;

    private TextView lblNullList;
    private RecyclerView listTasks;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<TaskSchool> tasks;
    private TaskSchoolAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        presenter = new DonePresenter(this);
        lblNullList = (TextView) findViewById(R.id.lbl_null_list);
        listTasks = (RecyclerView) findViewById(R.id.list_tasks);
        mLayoutManager = new LinearLayoutManager(this);
        tasks = new ArrayList<>();

        adapter = new TaskSchoolAdapter(tasks, new TaskSchoolAdapter.OnItemClick() {

            @Override
            public void onClick(TaskSchool task) {

                Intent intent = new Intent(DoneActivity.this, TaskSchoolActivity.class);

                intent.putExtra("task", task);
                startActivity(intent);

            }

        });

        listTasks.setLayoutManager(mLayoutManager);
        listTasks.setHasFixedSize(true);
        listTasks.setItemAnimator(new DefaultItemAnimator());
        listTasks.setAdapter(adapter);

    }

    @Override
    protected void onResume() {

        super.onResume();

        presenter.retrieveDoneTasks();

    }

    @Override
    public void populateDoneTasks(List<TaskSchool> tasks) {

        this.tasks.clear();
        this.tasks.addAll(tasks);

        adapter.notifyDataSetChanged();

        if (tasks.isEmpty()) {

            listTasks.setVisibility(View.GONE);
            lblNullList.setVisibility(View.VISIBLE);

        } else {

            listTasks.setVisibility(View.VISIBLE);
            lblNullList.setVisibility(View.GONE);

        }

    }

    @Override
    public void goToMainActivity() {

    }

    @Override
    public void onError(String message) {

    }

}