package br.com.fgr.testewhiteboard.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.testewhiteboard.R;
import br.com.fgr.testewhiteboard.model.TaskSchool;
import br.com.fgr.testewhiteboard.ui.done.DoneActivity;
import br.com.fgr.testewhiteboard.ui.task.TaskSchoolActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainActionsMVP.RequiredViewOperations {

    private MainActionsMVP.PresenterOperations presenter;

    private RecyclerView listTasks;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView lblNullList;

    private List<TaskSchool> tasks;
    private TaskSchoolAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lblNullList = (TextView) findViewById(R.id.lbl_null_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, TaskSchoolActivity.class);
                startActivity(intent);

            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listTasks = (RecyclerView) findViewById(R.id.list_tasks);
        mLayoutManager = new LinearLayoutManager(this);
        tasks = new ArrayList<>();

        adapter = new TaskSchoolAdapter(tasks, new TaskSchoolAdapter.OnItemClick() {

            @Override
            public void onClick(TaskSchool task) {

                Intent intent = new Intent(MainActivity.this, TaskSchoolActivity.class);

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

        presenter.retrieveTasks();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_done) {
            // Handle the camera action
            intent = new Intent(MainActivity.this, DoneActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void populateDisciplines(List<TaskSchool> tasks) {

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
    public void onError(String message) {
        Snackbar.make(findViewById(R.id.coordinator), message, Snackbar.LENGTH_SHORT).show();
    }

}