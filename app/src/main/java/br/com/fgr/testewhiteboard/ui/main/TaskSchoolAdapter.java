package br.com.fgr.testewhiteboard.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import br.com.fgr.testewhiteboard.R;
import br.com.fgr.testewhiteboard.model.TaskSchool;

public class TaskSchoolAdapter extends RecyclerView.Adapter<TaskSchoolAdapter.ViewHolder> {

    private List<TaskSchool> taskList;
    private OnItemClick onItemClick;

    public TaskSchoolAdapter(List<TaskSchool> taskList, OnItemClick onItemClick) {

        this.taskList = taskList;
        this.onItemClick = onItemClick;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TaskSchool task = taskList.get(position);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        DateTime dt = new DateTime(task.getDate());

        holder.getLblName().setText(task.getName());
        holder.getLblDiscipline().setText(task.getDiscipline().getName());
        holder.getLblDate().setText(dtf.print(dt));

        if (!task.isGradeZero()) {

            holder.getLblGrade().setVisibility(View.VISIBLE);
            holder.getLblGrade().setText(task.getGrade());

        } else
            holder.getLblGrade().setVisibility(View.GONE);

        holder.getItemView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onItemClick.onClick(task);
            }

        });

    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblName;
        private final TextView lblDiscipline;
        private final TextView lblGrade;
        private final TextView lblDate;
        private final View itemView;

        public ViewHolder(View itemView) {

            super(itemView);

            lblName = (TextView) itemView.findViewById(R.id.lbl_name);
            lblDiscipline = (TextView) itemView.findViewById(R.id.lbl_discipline);
            lblGrade = (TextView) itemView.findViewById(R.id.lbl_grade);
            lblDate = (TextView) itemView.findViewById(R.id.lbl_date);
            this.itemView = itemView;

        }

        public TextView getLblName() {
            return lblName;
        }

        public TextView getLblDiscipline() {
            return lblDiscipline;
        }

        public TextView getLblGrade() {
            return lblGrade;
        }

        public TextView getLblDate() {
            return lblDate;
        }

        public View getItemView() {
            return itemView;
        }

    }

    public interface OnItemClick {

        void onClick(TaskSchool task);

    }

}