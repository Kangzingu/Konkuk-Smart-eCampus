package kksy.konkuk_smart_ecampus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.ViewHolder> {

    private List<Item> data;
    private Context context;
    private AttendanceContentsAdapter adapter;

    public AttendanceListAdapter(Context context, List<Item> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_attendance_round, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListAdapter.ViewHolder holder, int position) {
        Item item = data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.attendanceWeek.setText(item.attendance_week + "주차");

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AttendanceContentsAdapter(item.attendance_contents);
        viewHolder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Item{
        public int attendance_week;
        public List<AttendanceContentsAdapter.Item> attendance_contents;

        public Item(int attendance_week) {
            this.attendance_week = attendance_week;
        }

        public Item(int attendance_week, List<AttendanceContentsAdapter.Item> attendance_contents) {
            this.attendance_week = attendance_week;
            this.attendance_contents = attendance_contents;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView attendanceWeek;
        public RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            attendanceWeek = (TextView) itemView.findViewById(R.id.textViewAttendanceWeek);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.listViewAttendanceContents);
        }
    }
}
